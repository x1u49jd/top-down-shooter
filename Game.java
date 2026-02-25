import javax.swing.JFrame;

public class Game {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        
        window.setSize(800,600);
        window.setTitle("Top Down Shooter");
        // tells Java that when X is clicked, end the program
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        // centers the window on screen
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}