import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Graphics;

import java.awt.Color;


public class Game {
    JFrame window;
    JPanel panel;

    int playerX = 400;
    int playerY = 400;
    int playerSpeed = 5;

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
    }

    public static void main(String[] args) {
        new Game();
    }
}