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
    
    public void gameLoop() {
        while (true) {
            // update player position based on keys pressed
            player.move(upPressed, downPressed, leftPressed, rightPressed);
            
            for (Enemy e : enemies) {
                e.update(player);
                
                if (!e.alive && System.currentTimeMillis() >= e.respawnTime) {
                    int spawnX = (int)(Math.random() * panel.getWidth());
                    int spawnY = (int)(Math.random() * panel.getHeight());

                    e.x = spawnX;
                    e.y = spawnY;
                    e.health = e.maxHealth;
                    e.alive = true;
                }
            }

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

    public Game() {
        window = new JFrame();
    
        window.setSize(windowWidth, windowHeight);
        window.setTitle("Top Down Shooter");

        // tells Java that when X is clicked, end the program
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        // centers the window on screen
        window.setLocationRelativeTo(null);

        player = new Player(400,400);

        enemies = new ArrayList<>();

        enemies.add(new Enemy(100, 100));
        enemies.add(new Enemy(700, 100));
        enemies.add(new Enemy(100, 500));
        enemies.add(new Enemy(700, 500));

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g); // clears the panel
                player.draw(g);
                for (Enemy e : enemies) {
                    e.draw(g);
                }
            }

        };

        panel.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                player.shoot(e.getX(), e.getY());
            }
        });

        window.add(panel);
        window.addKeyListener(this);
        window.setVisible(true);

        new Thread(this::gameLoop).start();
    }

    public static void main(String[] args) {
        new Game();
    }
}