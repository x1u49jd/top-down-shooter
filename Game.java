import javax.swing.JFrame;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Game implements KeyListener {

    enum GameState {
        PLAYING,
        GAME_OVER
    }

    GameState gameState = GameState.PLAYING;

    JFrame window;

    int windowWidth = 1440, windowHeight = 900;

    boolean upPressed, downPressed, leftPressed, rightPressed = false;

    volatile boolean restartRequested = false;

    Player player;
    ArrayList<Enemy> enemies;
    ArrayList<Item> items;

    WaveManager waveManager = new WaveManager();
    ItemSpawnManager itemSpawnManager;
    GamePanel panel;

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (gameState == GameState.GAME_OVER && key == KeyEvent.VK_R){
            restartRequested = true;
        }
        if (key == KeyEvent.VK_W) {upPressed = true;};
        if (key == KeyEvent.VK_S) {downPressed = true;};
        if (key == KeyEvent.VK_A) {leftPressed = true;};
        if (key == KeyEvent.VK_D) {rightPressed = true;};
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {upPressed = false;};
        if (key == KeyEvent.VK_S) {downPressed = false;};
        if (key == KeyEvent.VK_A) {leftPressed = false;};
        if (key == KeyEvent.VK_D) {rightPressed = false;};
    }
    
    public void restartGame() {
        gameState = GameState.PLAYING;
        waveManager.resetWave();

        player = new Player(400, 400);
        panel.setPlayer(player);
        items.clear();
        itemSpawnManager.lastItemSpawnTime = System.currentTimeMillis();

        waveManager.spawnWave(enemies, panel.getWidth(), panel.getHeight());

        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;

        panel.repaint();

        Sound.play("audio/Blip12.wav");
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public void checkGameOver() {
        if (player.health <= 0) {
            gameState = GameState.GAME_OVER;
        }
    }

    public void updateEnemies() {
        for (Enemy e : enemies) {
            e.update(player, enemies);
        }
    }

    public void checkItemPickup() {
        for (int i = items.size() - 1; i >= 0; i--) {
            Item item = items.get(i);
            if (player.getBounds().intersects(item.getBounds())){
                player.collectItem(item);
                items.remove(i);
            }
        }
    }

    public void gameLoop() {
        while (true) {
            if (restartRequested) {
                restartRequested = false;
                restartGame();
            }

            if (gameState == GameState.PLAYING) {
                // update player's position based on keys pressed, bullets, reload
                player.update(upPressed, downPressed, leftPressed, rightPressed, windowWidth, windowHeight, enemies);
                updateEnemies();
                checkItemPickup();
                checkGameOver();
                itemSpawnManager.update(panel.getWidth(), panel.getHeight());

                // spawn next wave when all enemies in the current wave are dead
                if (waveManager.isWaveClear(enemies)) {
                    waveManager.spawnNextWave(enemies, windowWidth, windowHeight);
                }

                panel.setGameState(gameState);
                panel.repaint(); // redraw after moving

                // wait 16ms (60fps)
                try {
                    Thread.sleep(16);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void createWindow() {
        window = new JFrame();
        window.setSize(windowWidth, windowHeight);
        window.setTitle("Top Down Shooter");

        // tells Java that when X is clicked, end the program
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        // centers the window on screen
        window.setLocationRelativeTo(null);
    }

    public void createGameObjects() {
        player = new Player(400,400);
        enemies = new ArrayList<>();

        items = new ArrayList<>();
    }

    public void setupInput() {
        panel.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                player.shoot(e.getX(), e.getY());
            }
        });

        window.addKeyListener(this);
    }

    public Game() {
        createWindow();
        createGameObjects();
        panel = new GamePanel(player, items, enemies, waveManager);
        setupInput();
        window.add(panel);
        window.setVisible(true);

        // added after panel added so that layout is calculated and ready to use by spawnWave()
        itemSpawnManager = new ItemSpawnManager(items);
        waveManager.spawnWave(enemies, panel.getWidth(), panel.getHeight());

        new Thread(this::gameLoop).start();
    }

    public static void main(String[] args) {
        new Game();
    }
}