import java.awt.Color;
import java.awt.Graphics;

public class Enemy {
    int x,y;

    public Enemy(int startX, int startY) {
        x = startX;
        y = startY;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, 40, 40);
    }
}
