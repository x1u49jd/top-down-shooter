import java.awt.Color;
import java.awt.Graphics;

public class Enemy {
    int x,y;
    int speed = 2;
    int health = 3, maxHealth = 3;
    double knockbackStrength = 20;
    int width = 40, height = 40;

    public Enemy(int startX, int startY) {
        x = startX;
        y = startY;
    }

    public void update(Player player) {
        if (x < player.x) {x += speed;};
        if (x > player.x) {x -= speed;};
        if (y < player.y) {y += speed;};
        if (y > player.y) {y -= speed;};

        // checks collision with player, and causes damage to player
        if (player.x < x + 40 && player.x + 40 > x &&
            player.y < y + 40 && player.y + 40 > y) {
                player.takeDamage(1);
                player.applyKnockback(x, y, knockbackStrength);
            }
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, 40, 40);
    }

    public void takeDamage(int amount) {
        health -= 1;
        if (health <= 0) {
            health = 0;
        }
    }
}
