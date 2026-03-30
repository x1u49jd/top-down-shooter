import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Font;
import java.awt.Graphics;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.Random;

public class Game implements KeyListener {

    enum GameState {
        PLAYING,
        GAME_OVER
    }

    GameState gameState = GameState.PLAYING;

    JFrame window;
    JPanel panel;

    int windowWidth = 1440, windowHeight = 900;

    boolean upPressed = false;
    boolean downPressed = false;
    boolean leftPressed = false;
    boolean rightPressed = false;

    Player player;
    ArrayList<Enemy> enemies;

    int wave = 1;
    int enemiesPerWave = 3;

    ArrayList<Item> items;

    int maxItemsOnScreen = 4;
    long itemsSpawnDelay = 1000;
    long lastItemSpawnTime = 0;

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
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
    
    @Override
    public void keyTyped(KeyEvent e) {}

    public void checkGameOver() {
        if (player.health <= 0) {
            gameState = GameState.GAME_OVER;
        }
    }

    public void spawnWave() {
        enemies.clear();

        for (int i = 0; i < enemiesPerWave; i++) {
            int spawnX = (int)(Math.random() * panel.getWidth());
            int spawnY = (int)(Math.random() * panel.getHeight());

            enemies.add(new Enemy(spawnX, spawnY));
        }
    }

    public void spawnItem() {
        // tracks how many of each item there already are on the screen
        int medkitsOnScreen = 0;
        int magazinesOnScreen = 0;

        for (Item i : items) {
            if (i.type == ItemType.MEDKIT) {medkitsOnScreen++;}
            else if (i.type == ItemType.MAGAZINE) {magazinesOnScreen++;};
        }

        // if the amount exceeds the maximum don't proceed
        if (items.size() >= maxItemsOnScreen) return;

        Random rand = new Random();
        
        int spawnX = (int)(Math.random() * panel.getWidth() - 20);
        int spawnY = (int)(Math.random() * panel.getHeight() - 20);

        ItemType type;
        
        // ensures atleast one Medkit and one Magazine are spawned on the screen at first
        if (medkitsOnScreen == 0 && magazinesOnScreen == 0) {
            type = rand.nextBoolean() ? ItemType.MAGAZINE : ItemType.MEDKIT;
        }
        else if (medkitsOnScreen == 0) {
            type = ItemType.MEDKIT;
        }
        else if (magazinesOnScreen == 0) {
            type = ItemType.MAGAZINE;
        }
        else {
            type = rand.nextBoolean() ? ItemType.MAGAZINE : ItemType.MEDKIT;
        }

        items.add(new Item(spawnX, spawnY, type));
    }

    public void updatePlayer() {
        // update player position based on keys pressed
        player.move(upPressed, downPressed, leftPressed, rightPressed);
    }

    public void updateEnemies() {
        for (Enemy e : enemies) {
            e.update(player, enemies);
    }
    }
    
    public void checkWaveClear() {
        boolean allEnemiesDead = true;

            for (Enemy e : enemies) {
                if (e.alive) {
                    allEnemiesDead = false;
                    break;
                }
            }

            if (allEnemiesDead) {
                wave++;
                enemiesPerWave += 2;
                Sound.play("audio/PowerUp1.wav");
                spawnWave();
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

            if (gameState == GameState.PLAYING) {
                updatePlayer();
                updateEnemies();
                checkWaveClear();
                checkItemPickup();
                checkGameOver();

                // update player bullets
                player.updateBullets(panel.getWidth(), panel.getHeight(), enemies);

                if (player.reloading) {
                    if (System.currentTimeMillis() - player.reloadStartTime >= player.reloadTime) {
                        player.currentAmmo = player.maxAmmo;
                        player.magazines--;
                        player.reloading = false;
                        player.readyToShoot = true;
                    }
                }

                if (items.size() < maxItemsOnScreen && System.currentTimeMillis() - lastItemSpawnTime > itemsSpawnDelay) {
                    spawnItem();
                    lastItemSpawnTime = System.currentTimeMillis();
                }

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

    public void createPanel() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g); // clears the panel
                for (Item i : items) {
                    i.draw(g);
                }
                for (Enemy e : enemies) {
                    e.draw(g);
                }
                player.draw(g);
                g.drawString("Wave: " + wave, panel.getWidth() - 120, 40);

                if (gameState == GameState.GAME_OVER) {
                    String text = "Game Over";
                    Font font = new Font("Arial", Font.BOLD, 80);
                    g.setFont(font);

                    // get width of text
                    int textWidth = g.getFontMetrics().stringWidth(text);

                    // calculate center position for text
                    int x = (panel.getWidth() - textWidth) / 2;
                    int y = panel.getHeight() / 2;

                    g.drawString(text, x, y);
                }
            }
        };
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
        createPanel();
        setupInput();

        window.add(panel);
        window.setVisible(true);

        // added after panel added so that layout is calculated and ready to use by spawnWave()
        spawnWave();

        new Thread(this::gameLoop).start();
    }

    public static void main(String[] args) {
        new Game();
    }
}