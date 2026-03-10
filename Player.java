import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;


public class Player {
    int x,y;
    int speed = 6;
    int health = 5, maxHealth = 5;
    int knockbackX, knockbackY;
    int width = 40;
    int height = 40;

    ArrayList<Bullet> bullets = new ArrayList<>();

    int score = 0;

    public Player(int startX, int startY) {
        x = startX;
        y = startY;
    }

    public void move(boolean up, boolean down, boolean left, boolean right) {
        if (up) {y -= speed;};
        if (down) {y += speed;};
        if (left) {x -= speed;};
        if (right) {x += speed;};

        // moves player according to knockback
        x += knockbackX;
        y += knockbackY;

        // slowly reduces knockback for smooth stop
        knockbackX *= 0.9; // friction factor
        knockbackY *= 0.9;
    };

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, width, height);

        // ---- UI HEALTH BAR ----
        int barWidth = 40; // same width as player
        int barHeight = 8;

        // grey background
        g.setColor(Color.GRAY);
        g.fillRect(x, y - 15, barWidth, barHeight);

        // health scaled properly
        g.setColor(Color.GREEN);
        int currentWidth = (int)((health / (double)maxHealth) * barWidth);
        g.fillRect(x, y - 15, currentWidth, barHeight);

        // draw bullets
        for (Bullet b : bullets) {
            b.draw(g);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 20, 40);

    }

    // called when enemy hits player, player takes damage
    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0) {
            health = 0;
            System.out.println("Player is dead!");
        }
    }

    public void applyKnockback(int enemyX, int enemyY, double knockbackStrength) {
        // --- Step 1: Compute vector from enemy to player ---
        // This gives a direction pointing *away* from the enemy
        double dx = x - enemyX; // horizontal difference
        double dy = y - enemyY; // vertical difference

        // --- Step 2: Calculate distance between enemy and player ---
        // Using the Pythagorean theorem: distance = √(dx² + dy²)
        double length = Math.sqrt(dx * dx + dy * dy);

        // --- Step 3: Avoid division by 0 ---
        // If player and enemy are exactly on top of each other, length would be 0
        // Dividing by 0 would crash the program
        if (length == 0) {length = 1;};

        // --- Step 4: Normalise the vector ---
        // We only want the direction, not the distance
        // Divide each component by the length to make the vector have length 1
        dx /= length;
        dy /= length;

        // --- Step 5: Apply knockback velocity ---
        // Multiply unit vector by knockback strength to get the push amount
        // Store it in knockbackX and knockbackY for smooth sliding in move() later on
        knockbackX = (int) (dx * knockbackStrength);
        knockbackY = (int) (dy * knockbackStrength);
    }

    public void shoot(double targetX, double targetY) {
        // spawn bullet at player's center
        double startX = x + (width / 2);
        double startY = y + (height / 2);

        bullets.add(new Bullet(startX, startY, targetX, targetY));
    }

    public void updateBullets (int windowWidth, int windowHeight, ArrayList<Enemy> enemies) {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            
            Bullet b = bullets.get(i);
            b.update();

            // if bullet goes off screen, remove it
            if (b.x < 0 || b.x > windowWidth || b.y < 0 || b.y > windowHeight) {
                bullets.remove(i);
                continue;
            }
            // if bullet touches enemy, remove it
            for (Enemy e : enemies) {
                if (b.x > e.x && b.x < e.x + e.width && b.y > e.y && b.y < e.y + e.height) {

                    bullets.remove(i);

                    // if the enemy died, add a score
                    if (e.takeDamage(1)) {
                        score++;
                    }

                    break; // bullet was removed, skip remaining enemy checks
                }
            }
        }
    }
}