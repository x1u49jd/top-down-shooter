import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class GamePanel extends JPanel{
    
    Player player;
    ArrayList<Item> items;
    ArrayList<Enemy> enemies;
    WaveManager waveManager;
    Game.GameState gameState;


    public GamePanel(Player player, ArrayList<Item> items, ArrayList<Enemy> enemies, WaveManager waveManager) {
        this.player = player;
        this.items = items;
        this.enemies = enemies;
        this.waveManager = waveManager;

    }

    public void setGameState(Game.GameState gameState) {
        this.gameState = gameState;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

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
        g.drawString("Wave: " + waveManager.wave, getWidth() - 120, 40);

        if (gameState == Game.GameState.GAME_OVER) {

            // dark transparent overlay
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, getWidth(), getHeight());

            String gameOverText = "Game Over";
            String restartText = "Press R to Restart";

            Font gameOverFont = new Font("Arial", Font.BOLD, 80);
            Font restartFont = new Font("Arial", Font.BOLD, 35);

            // GAME OVER
            g.setColor(Color.WHITE);
            g.setFont(gameOverFont);

            // get width of text
            int gameOverWidth = g.getFontMetrics().stringWidth(gameOverText);

            // calculate center position for text
            int gameOverx = (getWidth() - gameOverWidth) / 2;
            int gameOvery = getHeight() / 2;

            g.drawString(gameOverText, gameOverx, gameOvery);

            // RESTART

            g.setFont(restartFont);
            int restartWidth = g.getFontMetrics().stringWidth(restartText);

            // calculate center position for text
            int restartx = (getWidth() - restartWidth) / 2;
            int restarty = getHeight() / 2 + 60;

            g.drawString(restartText, restartx, restarty);
        }
    }
}
