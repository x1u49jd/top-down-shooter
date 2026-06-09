package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Item {

    private static final int WIDTH = 20, HEIGHT = 20;
    private int x,y;    
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

        g.fillRect(x, y, WIDTH, HEIGHT);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public ItemType getType() {
        return type;
    }

    public static int getSize() {
        return WIDTH;
    }
}
