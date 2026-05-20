import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public abstract class Enemy {
    private static final int WIDTH = 40, HEIGHT = 40;
    private int x,y;
    private int speed = 2;
    private int health = 3, maxHealth = 3;
    private double knockbackStrength = 10;
    private boolean alive = true;

    private double knockbackX, knockbackY;
    private int staggerDuration = 0;

    public Enemy(int startX, int startY) {
        x = startX;
        y = startY;
    }

    public void update(Player player, ArrayList<Enemy> enemies) {

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
    }

    // draw the square that represents the enemy
    public abstract void draw(Graphics g);

    public boolean takeDamage(int amount, int sourceX, int sourceY) {

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
    
    public void collectItem(Item item) {
        if (item.getType() == Item.ItemType.MEDKIT) {
            // heal 1 point, but don't go over maxHealth
            health = Math.min(health + 1, maxHealth);
        }
        Sound.play("audio/Random60.wav");
    }


    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public boolean isAlive(){
        return alive;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }
}
