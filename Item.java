import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Item {

    enum ItemType {
        MEDKIT,
        MAGAZINE
    }

    private int x,y;
    private int width = 20;
    private int height = 20;
    private ItemType type;

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

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public ItemType getType() {
        return type;
    }
}
