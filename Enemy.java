import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Enemy {
    int x,y;
    int speed = 1;
    int health = 3, maxHealth = 3;
    double knockbackStrength = 10;
    int width = 40, height = 40;
    boolean alive = true;

    public Enemy(int startX, int startY) {
        x = startX;
        y = startY;
    }

    public void update(Player player) {

        if (alive == false) { return;}

        if (x < player.x) {x += speed;};
        if (x > player.x) {x -= speed;};
        if (y < player.y) {y += speed;};
        if (y > player.y) {y -= speed;};

        // checks collision with player, and causes damage to player
        if (getBounds().intersects(player.getBounds())) {
                player.takeDamage(1);
                player.applyKnockback(x, y, knockbackStrength);
            }
    }

    public void draw(Graphics g) {

        if (alive == false) { return;}

        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
    }

    public boolean takeDamage(int amount) {

        // ignore damage to dead enemies
        // and adding extra score when bullet hit an already dead enemy
        if (!alive) { return false; };

        health -= amount;
        Sound.play("audio/Hit9.wav");

        if (health <= 0) {
            health = 0;
            alive = false;
            Sound.play("audio/Random98.wav");
            return true; // enemy died
        }

        return false; // enemy didn't die
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
