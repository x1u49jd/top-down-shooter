import java.awt.Color;
import java.awt.Graphics;

public class WalkerEnemy extends Enemy {

    public WalkerEnemy(int startX, int startY) {
        super(startX, startY);
    }

    @Override
    public void draw(Graphics g) {
         if (!isAlive()) { return;}

        g.setColor(Color.RED);
        g.fillRect(getX(), getY(), getWidth(), getHeight());

        if (getHealth() < getMaxHealth()) {
            // ---- UI HEALTH BAR ----
            int barWidth = 40; // same width as player
            int barHeight = 8;

            // grey background
            g.setColor(Color.GRAY);
            g.fillRect(getX(), getY() - 15, barWidth, barHeight);

            // health scaled properly
            g.setColor(Color.RED);
            int currentWidth = (int)((getHealth() / (double)getMaxHealth()) * barWidth);
            g.fillRect(getX(), getY() - 15, currentWidth, barHeight);
        }
    }
}
