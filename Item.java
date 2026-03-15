import java.awt.Color;
import java.awt.Graphics;

public class Item {
    int x,y;
    int width = 20;
    int height = 20;
    ItemType type;

    public Item(int startX, int startY, ItemType type) {
        this.x = startX;
        this.y = startY;
        this.type = type;
    }

    public void draw(Graphics g) {
        if (type == ItemType.MEDKIT) {
            g.setColor(Color.PINK);
        }
        if (type == ItemType.MAGAZINE) {
            g.setColor(Color.DARK_GRAY);
        }

        g.fillRect(x, y, width, height);
    }
}
