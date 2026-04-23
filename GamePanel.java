import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class GamePanel extends JPanel {
    
    private Player player;
    private ItemManager itemManager;
    private EnemyManager enemyManager;
    private WaveManager waveManager;
    private Game.GameState gameState;


    public GamePanel(Player player, ItemManager itemManager, EnemyManager enemyManager, WaveManager waveManager) {
        this.player = player;
        this.itemManager = itemManager;
        this.enemyManager = enemyManager;
        this.waveManager = waveManager;
    }

    public void setGameState(Game.GameState gameState) {
        this.gameState = gameState;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void drawMenu(Graphics g) {
        String titleText = "Top Down Shooter"; 
        Font titleFont = new Font("Arial", Font.BOLD, 80);
        g.setFont(titleFont);
        int titleWidth = g.getFontMetrics().stringWidth(titleText);
        int titlex = (getWidth() - titleWidth) / 2;
        int titley = getHeight() / 2 ;
        g.drawString(titleText, titlex, titley);

        String startText = "Press ENTER to start"; 
        Font starFont = new Font("Arial", Font.BOLD, 35);
        g.setFont(starFont);
        int startWidth = g.getFontMetrics().stringWidth(startText);
        int startx = (getWidth() - startWidth) / 2;
        int starty = getHeight() / 2 + 60;
        g.drawString(startText, startx, starty);
    }

    public void drawPlaying(Graphics g) {
        super.paintComponent(g); // clears the panel
        for (Item i : itemManager.getItems()) {
            i.draw(g);
        }
        for (Enemy e : enemyManager.getEnemies()) {
            e.draw(g);
        }
        player.draw(g);
        g.drawString("Wave: " + waveManager.getWave(), getWidth() - 120, 40);
    }

    public void drawPause(Graphics g) {
        // dark transparent overlay
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());

        String pauseText = "Pause";
        String unpauseText = "Press P to resume";

        Font pauseFont = new Font("Arial", Font.BOLD, 80);
        Font unpauseFont = new Font("Arial", Font.BOLD, 35);

        // PAUSE
        g.setColor(Color.WHITE);
        g.setFont(pauseFont);

        // get width of text
        int pauseWidth = g.getFontMetrics().stringWidth(pauseText);

        // calculate center position for text
        int pausex = (getWidth() - pauseWidth) / 2;
        int pausey = getHeight() / 2;

        g.drawString(pauseText, pausex, pausey);

        // UNPAUSE

        g.setFont(unpauseFont);
        int unpauseWidth = g.getFontMetrics().stringWidth(unpauseText);

        // calculate center position for text
        int unpausex = (getWidth() - unpauseWidth) / 2;
        int unpausey = getHeight() / 2 + 60;

        g.drawString(unpauseText, unpausex, unpausey);
    }

    public void drawGameOver(Graphics g) {
        // dark transparent overlay
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());

        String gameOverText = "Game Over";
        String scoreText = "Score: " + player.getScore();
        String restartText = "Press R to Restart";

        Font gameOverFont = new Font("Arial", Font.BOLD, 80);
        Font scoreFont = new Font("Arial", Font.BOLD, 35);
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

        // SCORE

        g.setFont(scoreFont);
        int scoreWidth = g.getFontMetrics().stringWidth(scoreText);

        // calculate center position for text
        int scorex = (getWidth() - scoreWidth) / 2;
        int scorey = getHeight() / 2 + 60;

        g.drawString(scoreText, scorex, scorey);


        // RESTART

        g.setFont(restartFont);
        int restartWidth = g.getFontMetrics().stringWidth(restartText);

        // calculate center position for text
        int restartx = (getWidth() - restartWidth) / 2;
        int restarty = getHeight() / 2 + 160;

        g.drawString(restartText, restartx, restarty);
    }

    @Override
    protected void paintComponent(Graphics g){
        switch (gameState) {
            case MENU:
                drawMenu(g);
                break;
            case PLAYING:
                drawPlaying(g);
                break;
            case PAUSE:
                drawPause(g);
                break;
            case GAME_OVER:
                drawGameOver(g);
                break;
        }
    }
}
