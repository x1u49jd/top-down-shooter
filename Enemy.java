import java.awt.Color;
import java.awt.Graphics;

public class Enemy {
    int x,y;
    int speed = 2;

    public Enemy(int startX, int startY) {
        x = startX;
        y = startY;
    }

    public void update(int playerX, int playerY) {
        if (x < playerX) {x += speed;};
        if (x > playerX) {x -= speed;};
        if (y < playerY) {y += speed;};
        if (y > playerY) {y -= speed;};
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, 40, 40);
    }
}
