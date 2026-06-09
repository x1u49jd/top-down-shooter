package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class ShooterEnemy extends Enemy {
    private static final int SPEED = 2;
    private static final long SHOOT_DELAY = 900;
    private static final double SHOOT_RANGE = 400;

    // === combat ===
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private long lastShotTime = 0;

    public ShooterEnemy(int startX, int startY) {
        super(startX, startY, SPEED);
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
    public void update(Player player, ArrayList<Enemy> enemies, int windowWidth, int windowHeight) {
        // stop moving and shoot when player is within range, otherwise stop shooting and keep chasing player
        if (isPlayerInRange(player)) {
            shootAtPlayer(player);
        }
        else {
            super.update(player, enemies, windowWidth, windowHeight);
        }
        updateBullets(player, windowWidth, windowHeight);
        handlePlayerCollision(player);
    }

    private void shootAtPlayer(Player player) {
        if (!isAlive() || !player.isAlive()) {
            return;
        }
        
        // calculate the distance between the player and enemy
        double enemyCenterX = getX() + getWidth() / 2.0;
        double enemyCenterY = getY() + getHeight() / 2.0;

        double playerCenterX = player.getX() + player.getBounds().width / 2.0;
        double playerCenterY = player.getY() + player.getBounds().height / 2.0;

        if (System.currentTimeMillis() - lastShotTime >= SHOOT_DELAY) {
            bullets.add(new Bullet(enemyCenterX, enemyCenterY, playerCenterX, playerCenterY));
            lastShotTime = System.currentTimeMillis();
        }
    }

    private boolean isPlayerInRange(Player player) {
        // calculate the distance between the player and enemy
        double enemyCenterX = getX() + getWidth() / 2.0;
        double enemyCenterY = getY() + getHeight() / 2.0;

        double playerCenterX = player.getX() + player.getBounds().width / 2.0;
        double playerCenterY = player.getY() + player.getBounds().height / 2.0;

        double dx = playerCenterX - enemyCenterX;
        double dy = playerCenterY - enemyCenterY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        return SHOOT_RANGE >= distance;
    }

    private void updateBullets(Player player, int windowWidth, int windowHeight) {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            b.update();
           
            // if bullet goes off screen, remove it
            if (b.getX() < 0 || b.getX() > windowWidth || b.getY() < 0 || b.getY() > windowHeight) {
                bullets.remove(i);
                continue;
            }
            
            if (player.isAlive() && b.getBounds().intersects(player.getBounds())) {
                System.out.println("Bullet touched player");
                player.takeDamage(1);
                player.applyKnockback(getX(), getY(), 8);
                bullets.remove(i);
            }
        }
    }

}
