import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class ShooterEnemy extends Enemy{
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private long lastShotTime = 0;
    private long shootDelay = 900;

    public ShooterEnemy(int startX, int startY) {
        super(startX, startY);
    }

    @Override
    public void draw(Graphics g) {
       if (!isAlive()) { return;}

        g.setColor(Color.ORANGE);
        g.fillRect(getX(), getY(), getWidth(), getHeight());

        // draw bullets
        for (Bullet b : bullets) {
            b.draw(g);
        }

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
    
    @Override
    public void update(Player player, ArrayList<Enemy> enemies) {
        super.update(player, enemies);
        shootAtPlayer(player);
        updateBullets(player);
    }

    private void shootAtPlayer(Player player) {
        if (!isAlive() || !player.isAlive()) {
            return;
        }

        if (System.currentTimeMillis() - lastShotTime >= shootDelay) {
            double startX = getX() + getWidth() / 2.0;
            double startY = getY() + getHeight() / 2.0;

            double targetX = player.getX() + 20;
            double targetY = player.getY() + 20;

            bullets.add(new Bullet(startX, startY, targetX, targetY));
            lastShotTime = System.currentTimeMillis();
        }
    }

    private void updateBullets (Player player) {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            b.update();
            /*
            // if bullet goes off screen, remove it
            if (b.getX() < 0 || b.getX() > windowWidth || b.getY() < 0 || b.getY() > windowHeight) {
                bullets.remove(i);
                continue;
            }
            */

            if (player.isAlive() && b.getBounds().intersects(player.getBounds())) {
                System.out.println("Bullet touched player");
                player.takeDamage(1);
                player.applyKnockback(getX(), getY(), 8);
                bullets.remove(i);
            }
        }
    }

}
