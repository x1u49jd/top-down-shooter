import java.awt.Graphics;
import java.awt.Color;

public class Bullet {
    double x, y;
    double velX, velY;
    double speed = 30;
    int width = 8;
    int height = 8;
    
    public Bullet(double startX, double startY, double targetX, double targetY) {
        x = startX;
        y = startY;

        double dx = targetX - startX; // horizontal distance from start to target
        double dy = targetY - startY; // vertical distance from start to target

        // distance from bullet start to target (Pythagoras: √(dx² + dy²))
        double length = Math.sqrt(dx * dx + dy * dy);

        // if bullet and target are exactly on top of each other, length would be 0
        // dividing by 0 would crash the program, so we fix it
        if (length == 0) {length = 1;};

        // dx / length and dy /length normalise vector, makes it length 1, this gives direction
        // multiplying the vector by speed scales the vector, how fast the bullet moves
        // velX and VelY tell you how much the bullet moves each frame in x and y
        velX = (dx / length) * speed;
        velY = (dy / length) * speed;
    }

    // bullet moves along the direction toward the target by (velX, velY)
    public void update() {
        x += velX;
        y += velY;
    }
    
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillOval((int) x,(int) y, width, height);
    }
}
