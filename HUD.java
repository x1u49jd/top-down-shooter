import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class HUD {
    

    private WaveManager waveManager;
    

    public HUD(WaveManager waveManager) {

        this.waveManager = waveManager;
    }

    public void draw(Graphics g, int width, int height, String waveCountdownText, Player player) {
         // draw score count
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + player.getScore(), 20, 40);

        // draw ammo/mags count
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Ammo: " + player.getCurrentAmmo() + " / " + player.getMaxAmmo() + " Mags: " + player.getMagazines(), 20, 80);

        // draw wave count
        g.drawString("Wave: " + waveManager.getWave(), width - 120, 40);

        // draw counter
        if (!waveCountdownText.isEmpty()) {
            g.setFont(new Font("Arial", Font.BOLD, 80));

            int countdownWidth = g.getFontMetrics().stringWidth(waveCountdownText);
            int countdownX = (width - countdownWidth) / 2;
            int countdownY = height / 2;
            
            g.drawString(waveCountdownText, countdownX, countdownY);
        }
    }

  
}
