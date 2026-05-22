import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class ShooterEnemy extends Enemy{
    private static final int FIRE_RANGE = 320;
    private static final int HOLD_DISTANCE = 220;
    private static final int RETREAT_DISTANCE = 120;
    private static final long SHOOT_COOLDOWN = 900;

    private ArrayList<Bullet> bullets = new ArrayList<>();
    private long lastShotTime = 0;

    public ShooterEnemy(int startX, int startY) {
        super(startX, startY);
    }

    @Override
    public void update(Player player, ArrayList<Enemy> enemies, int panelWidth, int panelHeight) {
        if (updateKnockback()) {
            updateBullets(player, panelWidth, panelHeight);
            return;
        }

        if (!isAlive()) { return; }

        double distanceToPlayer = getDistanceTo(player);

        if (distanceToPlayer > HOLD_DISTANCE) {
            moveTowardPlayer(player);
        }
        else if (distanceToPlayer < RETREAT_DISTANCE) {
            moveAwayFromPlayer(player);
        }

        shootAtPlayer(player, distanceToPlayer);
        updateBullets(player, panelWidth, panelHeight);
        handlePlayerCollision(player);
        applySeparation(enemies);
    }

    private void shootAtPlayer(Player player, double distanceToPlayer) {
        long currentTime = System.currentTimeMillis();

        if (distanceToPlayer > FIRE_RANGE) {
            return;
        }

        if (currentTime - lastShotTime < SHOOT_COOLDOWN) {
            return;
        }

        double startX = getX() + (getWidth() / 2.0);
        double startY = getY() + (getHeight() / 2.0);
        double targetX = player.getX() + (player.getBounds().width / 2.0);
        double targetY = player.getY() + (player.getBounds().height / 2.0);

        bullets.add(new Bullet(startX, startY, targetX, targetY));
        lastShotTime = currentTime;
        Sound.play("audio/Shoot104.wav");
    }

    private void updateBullets(Player player, int panelWidth, int panelHeight) {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.update();

            if (bullet.getX() < 0 || bullet.getX() > panelWidth || bullet.getY() < 0 || bullet.getY() > panelHeight) {
                bullets.remove(i);
                continue;
            }

            if (bullet.getBounds().intersects(player.getBounds())) {
                player.takeDamage(1);
                player.applyKnockback(getX(), getY(), 8);
                bullets.remove(i);
            }
        }
    }

    @Override
    public void draw(Graphics g) {
       if (!isAlive()) { return;}

        g.setColor(Color.ORANGE);
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

        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }
    }
    
}
