import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Graphics;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;

public class Game implements KeyListener{
    JFrame window;
    JPanel panel;

    int windowWidth = 800, windowHeight = 600;

    boolean upPressed = false;
    boolean downPressed = false;
    boolean leftPressed = false;
    boolean rightPressed = false;

    Player player;
    ArrayList<Enemy> enemies;

    int wave = 1;
    int enemiesPerWave = 3;

    ArrayList<Item> items;

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

    public void spawnWave() {
        enemies.clear();

        for (int i = 0; i < enemiesPerWave; i++) {
            int spawnX = (int)(Math.random() * panel.getWidth());
            int spawnY = (int)(Math.random() * panel.getHeight());

            enemies.add(new Enemy(spawnX, spawnY));
        }
    }

    public void updatePlayer() {
        // update player position based on keys pressed
        player.move(upPressed, downPressed, leftPressed, rightPressed);
    }

    public void updateEnemies() {
        for (Enemy e : enemies) {
            e.update(player);
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

    public void gameLoop() {
        while (true) {
            updatePlayer();
            updateEnemies();
            checkWaveClear();
            
            // update player bullets
            player.updateBullets(panel.getWidth(), panel.getHeight(), enemies);

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
        items.add(new Item(420, 420, ItemType.MEDKIT));
        items.add(new Item(470, 470, ItemType.MAGAZINE));
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