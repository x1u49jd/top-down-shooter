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

    private String waveCountdownText = "";

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

    public void setWaveCountdownText(String waveCountdownText) {
        this.waveCountdownText = waveCountdownText;
    }

    public void drawMenu(Graphics g) {
        super.paintComponent(g); // clears the panel

        String titleText = "Top Down Shooter"; 
        Font titleFont = new Font("Arial", Font.BOLD, 80);
        g.setFont(titleFont);
        int titleWidth = g.getFontMetrics().stringWidth(titleText);
        int titleX = (getWidth() - titleWidth) / 2;
        int titleY = getHeight() / 2 ;
        g.drawString(titleText, titleX, titleY);

        String startText = "Press ENTER to start"; 
        Font starFont = new Font("Arial", Font.BOLD, 35);
        g.setFont(starFont);
        int startWidth = g.getFontMetrics().stringWidth(startText);
        int startX = (getWidth() - startWidth) / 2;
        int startY = getHeight() / 2 + 60;
        g.drawString(startText, startX, startY);
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

        if (!waveCountdownText.isEmpty()) {
            g.setColor(new Color(0, 0, 0, 180));
            g.setFont(new Font("Arial", Font.BOLD, 80));

            int countdownWidth = g.getFontMetrics().stringWidth(waveCountdownText);
            int countdownX = (getWidth() - countdownWidth) / 2;
            int countdownY = getHeight() / 2;
            
            g.drawString(waveCountdownText, countdownX, countdownY);
        }
    }

    public void drawPause(Graphics g) {
        // dark transparent overlay
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());

        String pauseText = "Pause";
        String unpauseText = "Press P to resume";
        String backMenuText = "Press ESC to main menu";

        Font pauseFont = new Font("Arial", Font.BOLD, 80);
        Font unpauseFont = new Font("Arial", Font.BOLD, 35);
        Font backMenuFont = new Font("Arial", Font.BOLD, 35);

        // PAUSE
        g.setColor(Color.WHITE);
        g.setFont(pauseFont);

        // get width of text
        int pauseWidth = g.getFontMetrics().stringWidth(pauseText);

        // calculate center position for text
        int pauseX = (getWidth() - pauseWidth) / 2;
        int pauseY = getHeight() / 2;

        g.drawString(pauseText, pauseX, pauseY);

        // UNPAUSE

        g.setFont(unpauseFont);
        int unpauseWidth = g.getFontMetrics().stringWidth(unpauseText);

        // calculate center position for text
        int unpauseX = (getWidth() - unpauseWidth) / 2;
        int unpauseY = getHeight() / 2 + 60;

        g.drawString(unpauseText, unpauseX, unpauseY);

        // MAIN MENU

        g.setFont(backMenuFont);
        int backMenuWidth = g.getFontMetrics().stringWidth(backMenuText);

        // calculate center position for text
        int backMenuX = (getWidth() - backMenuWidth) / 2;
        int backMenuY = getHeight() / 2 + 120;

        g.drawString(backMenuText, backMenuX, backMenuY);
    }

    public void drawGameOver(Graphics g) {
        // dark transparent overlay
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());

        String gameOverText = "Game Over";
        String scoreText = "Score: " + player.getScore();
        String restartText = "Press R to Restart";
        String backMenuText = "Press ESC to Main Menu";

        Font gameOverFont = new Font("Arial", Font.BOLD, 80);
        Font scoreFont = new Font("Arial", Font.BOLD, 35);
        Font restartFont = new Font("Arial", Font.BOLD, 35);
        Font backMenuFont = new Font("Arial", Font.BOLD, 35);

        // GAME OVER
        g.setColor(Color.WHITE);
        g.setFont(gameOverFont);

        // get width of text
        int gameOverWidth = g.getFontMetrics().stringWidth(gameOverText);

        // calculate center position for text
        int gameOverX = (getWidth() - gameOverWidth) / 2;
        int gameOverY = getHeight() / 2;

        g.drawString(gameOverText, gameOverX, gameOverY);

        // SCORE

        g.setFont(scoreFont);
        int scoreWidth = g.getFontMetrics().stringWidth(scoreText);

        // calculate center position for text
        int scoreX = (getWidth() - scoreWidth) / 2;
        int scoreY = getHeight() / 2 + 60;

        g.drawString(scoreText, scoreX, scoreY);


        // RESTART

        g.setFont(restartFont);
        int restartWidth = g.getFontMetrics().stringWidth(restartText);

        // calculate center position for text
        int restartX = (getWidth() - restartWidth) / 2;
        int restartY = getHeight() / 2 + 160;

        g.drawString(restartText, restartX, restartY);

        // MAIN MENU

        g.setFont(backMenuFont);
        int backMenuWidth = g.getFontMetrics().stringWidth(backMenuText);

        // calculate center position for text
        int backMenuX = (getWidth() - backMenuWidth) / 2;
        int backMenuY = getHeight() / 2 + 220;

        g.drawString(backMenuText, backMenuX, backMenuY);
    }

    @Override
    protected void paintComponent(Graphics g) {
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
