import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;



public class Game implements KeyListener{
    JFrame window;
    JPanel panel;

    int playerX = 400;
    int playerY = 400;
    int playerSpeed = 5;

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {playerY -= playerSpeed;};
        if (key == KeyEvent.VK_S) {playerY += playerSpeed;};
        if (key == KeyEvent.VK_A) {playerX -= playerSpeed;};
        if (key == KeyEvent.VK_D) {playerX += playerSpeed;};

        panel.repaint(); // redraw after moving
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
    
    public Game() {
        window = new JFrame();
    
        window.setSize(800,600);
        window.setTitle("Top Down Shooter");
        // tells Java that when X is clicked, end the program
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        // centers the window on screen
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g); // clears the panel
                g.setColor(Color.BLUE);
                g.fillRect(playerX, playerY, 40, 40);
            }

        };

        window.add(panel);

        window.addKeyListener(this);
    }

    public static void main(String[] args) {
        new Game();
    }
}