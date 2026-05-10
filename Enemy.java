import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Enemy {
    private static final int WIDTH = 40, HEIGHT = 40;
    private int x,y;
    private int speed = 2;
    private int health = 3, maxHealth = 3;
    private double knockbackStrength = 10;
    private boolean alive = true;

    private double knockbackX, knockbackY;
    private int staggerDuration = 0;

    private ArrayList<Bullet> bullets = new ArrayList<>();
    private long lastShotTime = 0;
    private long shootCooldown = 2000; // ms between shots
    private double shootRange = 300;   // only shoot if within this distance

    private boolean sliding = false;
    private long slideStartTime = 0;
    private long slideDuration = 300;
    private double slideVX = 0, slideVY = 0;
    private double slideSpeed = 20;   // less than player
    private long nextSlideTime = 0;
    private long slideCheckInterval = 3000;  // check every 3 seconds
    private double slideChance = 0.4;  // 40% chance to slide when check triggers

    private double stamina = 50, maxStamina = 50;
    private double staminaCost = 20;
    private double staminaRechargeRate = 10; // per second

    public Enemy(int startX, int startY) {
        x = startX;
        y = startY;
        nextSlideTime = System.currentTimeMillis() + (long)(Math.random() * slideCheckInterval);
    }

    public void update(Player player, ArrayList<Enemy> enemies) {

        // update enemy bullets regardless of alive state
        updateBullets(player);

        // recharge stamina
        rechargeStamina();

        // Apply knockback if stagger is active
        if (staggerDuration > 0) {
            x += knockbackX;
            y += knockbackY;

            // optional: slowly reduce knockback for smoothing
            knockbackX *= 0.9;
            knockbackY *= 0.9;

            staggerDuration--;
            return; // enemy is staggered, skip normal movement
        }

        if (alive == false) { return;}

        // Check if it's time to attempt a slide
        if (System.currentTimeMillis() >= nextSlideTime && !sliding && stamina >= staminaCost && Math.random() < slideChance) {
            startSlide(player);
            nextSlideTime = System.currentTimeMillis() + slideCheckInterval;
        }

        // Apply slide movement
        if (sliding) {
            if (System.currentTimeMillis() - slideStartTime >= slideDuration) {
                sliding = false;
            } else {
                x += (int) slideVX;
                y += (int) slideVY;
                slideVX *= 0.85;
                slideVY *= 0.85;
            }
        }

        if (x < player.getX()) {x += speed;};
        if (x > player.getX()) {x -= speed;};
        if (y < player.getY()) {y += speed;};
        if (y > player.getY()) {y -= speed;};

        // checks collision with player, and causes damage to player
        if (getBounds().intersects(player.getBounds())) {
                player.takeDamage(1);
                player.applyKnockback(x, y, knockbackStrength);
        }

        // Seperation Behaviour: prevents enemies overlapping by applying a small repelling force when they get too close
        for (Enemy other: enemies) {
            // don't compare enemy to itself or to a dead enemy, only deal with living enemies
            if (other == this || !other.alive ) continue;

            // calculate the direction from other enemy to this enemy
            double dx = x - other.x;
            double dy = y - other.y;

            // calculate distance between this enemy and other enemy (distance = √(dx² + dy²))
            double distance = Math.sqrt(dx * dx + dy * dy);

            // set minimum allowed distance 
            // width means enemies won't overlap, adding extra increases spacing
            int minDistance = WIDTH + 15;

            // if enemies are too cloase , apply push force
            // distance > 0 prevents division by zero below
            if (distance < minDistance && distance > 0) {

                // normalise the direction vector (make its length = 1)
                // this keeps only one direction, removing distance influence
                dx /= distance;
                dy /= distance;

                // how strong the enemies push each other away (pixels per frame)
                int pushStrength = 2;

                // move this enemy from the other enemy
                x += dx * pushStrength;
                y += dy * pushStrength;
            }
        }

        // shoot at player if in range and cooldown elapsed
        double distToPlayer = Math.sqrt(Math.pow(player.getX() - x, 2) + Math.pow(player.getY() - y, 2));
        if (distToPlayer <= shootRange && System.currentTimeMillis() - lastShotTime >= shootCooldown) {
            double centerX = x + WIDTH / 2.0;
            double centerY = y + HEIGHT / 2.0;
            bullets.add(new Bullet(centerX, centerY, player.getX() + 20, player.getY() + 20));
            Sound.play("audio/Shoot104.wav");
            lastShotTime = System.currentTimeMillis();
        }
    }

    private void rechargeStamina() {
        stamina = Math.min(maxStamina, stamina + staminaRechargeRate / 60.0);
    }

    private void startSlide(Player player) {
        double dx = player.getX() - x;
        double dy = player.getY() - y;
        double len = Math.sqrt(dx * dx + dy * dy);
        if (len == 0) len = 1;
        slideVX = (dx / len) * slideSpeed;
        slideVY = (dy / len) * slideSpeed;
        sliding = true;
        slideStartTime = System.currentTimeMillis();
        stamina -= staminaCost;
    }

    private void updateBullets(Player player) {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            b.update();
            // remove if far off screen
            if (b.getX() < -100 || b.getX() > 2000 || b.getY() < -100 || b.getY() > 2000) {
                bullets.remove(i);
                continue;
            }
            // damage player on hit
            if (player.getHealth() > 0 && b.getBounds().intersects(player.getBounds())) {
                player.takeDamage(1);
                bullets.remove(i);
            }
        }
    }

    public void draw(Graphics g) {

        if (alive == false) { return;}

        g.setColor(Color.RED);
        g.fillRect(x, y, WIDTH, HEIGHT);

        if (health < maxHealth) {
            // ---- UI HEALTH BAR ----
            int barWidth = 40;
            int barHeight = 8;

            // grey background
            g.setColor(Color.GRAY);
            g.fillRect(x, y - 15, barWidth, barHeight);

            // health scaled properly
            g.setColor(Color.RED);
            int currentWidth = (int)((health / (double)maxHealth) * barWidth);
            g.fillRect(x, y - 15, currentWidth, barHeight);
        }

        // ---- UI STAMINA BAR ----
        g.setColor(new Color(80, 80, 80));
        g.fillRect(x - 1, y - 27, 42, 6);
        g.setColor(Color.CYAN);
        int staminaWidth = (int)((stamina / maxStamina) * 40);
        g.fillRect(x, y - 26, staminaWidth, 4);

        // draw enemy bullets
        g.setColor(new Color(255, 140, 0));
        for (Bullet b : bullets) {
            b.draw(g);
        }
    }

    public boolean takeDamage(int amount, int sourceX, int sourceY ){

        // ignore damage to dead enemies
        // and adding extra score when bullet hit an already dead enemy
        if (!alive) { return false; };
        health -= amount;
        System.out.println("Sound played");
        Sound.play("audio/Hit9.wav");

        // calculate direction from source (player/bullet) to enemy
        double dx = x - sourceX;
        double dy = y - sourceY;
        double distance = Math.sqrt(dx*dx + dy*dy);
        if (distance != 0) {
            dx /= distance;
            dy /= distance;
        }

        // apply knockback
        double knockbackStrength = 2;
        knockbackX = dx * knockbackStrength;
        knockbackY = dy * knockbackStrength;
        staggerDuration = 10; // frames to stagger

        if (health <= 0) {
            health = 0;
            alive = false;
            Sound.play("audio/Random98.wav");
            return true; // enemy died
        }

        return false; // enemy didn't die
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public boolean isAlive(){
        return alive;
    }
}
