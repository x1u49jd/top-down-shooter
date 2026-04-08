import javax.swing.JFrame;
import java.util.ArrayList;

public class Game {

    enum GameState {
        PLAYING,
        GAME_OVER
    }

    GameState gameState = GameState.PLAYING;

    JFrame window;

    int initialWindowWidth = 1440, initialWindowHeight = 900;

    volatile boolean restartRequested = false;

    Player player;
    ArrayList<Item> items;

    WaveManager waveManager = new WaveManager();
    ItemSpawnManager itemSpawnManager;
    EnemyManager enemyManager;
    GamePanel panel;
    InputHandler input;

    public void restartGame() {
        gameState = GameState.PLAYING;
        waveManager.resetWave();

        player = new Player(400, 400);
        panel.setPlayer(player);
        input.setPlayer(player);
        items.clear();
        itemSpawnManager.lastItemSpawnTime = System.currentTimeMillis();

        waveManager.spawnWave(enemyManager, panel.getWidth(), panel.getHeight());

        input.upPressed = false;
        input.downPressed = false;
        input.leftPressed = false;
        input.rightPressed = false;

        panel.repaint();

        Sound.play("audio/Blip12.wav");
    }

    public void checkGameOver() {
        if (player.health <= 0) {
            gameState = GameState.GAME_OVER;
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
                player.update(input.upPressed, input.downPressed, input.leftPressed, input.rightPressed, panel.getWidth(), panel.getHeight(), enemyManager.getEnemies());
                enemyManager.update(player);
                checkItemPickup();
                checkGameOver();
                itemSpawnManager.update(panel.getWidth(), panel.getHeight());

                // spawn next wave when all enemies in the current wave are dead
                if (enemyManager.areAllDead()) {
                    waveManager.spawnNextWave(enemyManager, panel.getWidth(), panel.getHeight());
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
            if (gameState == Game.GameState.GAME_OVER && input.restartRequested){
                restartRequested = true;
            }
        }
    }

    public void createWindow() {
        window = new JFrame();
        window.setSize(initialWindowWidth, initialWindowHeight);
        window.setTitle("Top Down Shooter");

        // tells Java that when X is clicked, end the program
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        // centers the window on screen
        window.setLocationRelativeTo(null);
    }

    public void createGameObjects() {
        player = new Player(400,400);
        //enemies = new ArrayList<>();

        items = new ArrayList<>();
    }

    public void setupInput() {
        panel.addMouseListener(input);
        window.addKeyListener(input);
    }

    public Game() {
        createWindow();
        createGameObjects();
        enemyManager = new EnemyManager();
        panel = new GamePanel(player, items, enemyManager.getEnemies(), waveManager);
        input = new InputHandler(player);
        setupInput();
        window.add(panel);
        window.setVisible(true);

        // added after panel added so that layout is calculated and ready to use by spawnWave()
        itemSpawnManager = new ItemSpawnManager(items);
        waveManager.spawnWave(enemyManager, panel.getWidth(), panel.getHeight());

        new Thread(this::gameLoop).start();
    }

    public static void main(String[] args) {
        new Game();
    }
}