import java.awt.Graphics;
import java.awt.Color;

public class Player {
    int x,y;
    int speed = 8;
    int health = 50;
    int maxHealth = 50;

    public Player(int startX, int startY) {
        x = startX;
        y = startY;
    }

    public void move(boolean up, boolean down, boolean left, boolean right) {
        if (up) {y -= speed;};
        if (down) {y += speed;};
        if (left) {x -= speed;};
        if (right) {x += speed;};
    };

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, 40, 40);

        // ---- UI HEALTH BAR ----
        int barWidth = 40; // same width as player
        int barHeight = 8;

        // grey background
        g.setColor(Color.GRAY);
        g.fillRect(x, y - 12, barWidth, barHeight);

        // health scaled properly
        g.setColor(Color.GREEN);
        int currentWidth = (int)((health / (double)maxHealth) * barWidth);
        g.fillRect(x, y - 12, currentWidth, barHeight);



    }

    // called when enemy hits player, player takes damage
    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0) {
            health = 0;
            System.out.println("Player is dead!");
        }
    }
}