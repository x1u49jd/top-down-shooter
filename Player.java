import java.awt.Graphics;
import java.awt.Color;

public class Player {
    int x,y;
    int speed = 8;
    int health = 10;

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

        // draws healthbar above player
        g.setColor(Color.GREEN);
        g.fillRect(x, y - 12, health * 4, 8);
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