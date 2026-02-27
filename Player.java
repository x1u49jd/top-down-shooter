import java.awt.Graphics;
import java.awt.Color;

public class Player {
    int x,y;
    int speed = 8;

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
    }
}