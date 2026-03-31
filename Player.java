import java.awt.Graphics;
import java.awt.Rectangle;
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

    int maxAmmo = 16;
    int currentAmmo = 16;
    int magazines = 3;

    int score = 0;

    long lastWalkSound = 0;
    long walkDelay = 200;

    boolean reloading = false;
    boolean readyToShoot = true;
    long reloadTime = 500;
    long reloadStartTime = 0;


    public Player(int startX, int startY) {
        x = startX;
        y = startY;
    }

    public void move(boolean up, boolean down, boolean left, boolean right) {

        boolean moving = up || down || left || right;

        if (up) {y -= speed;};
        if (down) {y += speed;};
        if (left) {x -= speed;};
        if (right) {x += speed;};

        if (moving && System.currentTimeMillis() - lastWalkSound > walkDelay) {
            Sound.play("audio/Random171.wav");
            lastWalkSound = System.currentTimeMillis();
        }

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

        // draw score
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 20, 40);

        // draw ammo
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Ammo: " + currentAmmo + " / " + maxAmmo + " Mags: " + magazines, 20, 80);

    }

    // called when enemy hits player, player takes damage
    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0) {
            health = 0;
            Sound.play("audio/Random369.wav");
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
        Sound.play("audio/Hit7.wav");
    }

    public void shoot(double targetX, double targetY) {
        if (readyToShoot) {
            if (currentAmmo > 0) {
                // spawn bullet at player's center
                double startX = x + (width / 2);
                double startY = y + (height / 2);

                bullets.add(new Bullet(startX, startY, targetX, targetY));
                currentAmmo--;
                Sound.play("audio/Shoot104.wav");
            }
            if (currentAmmo == 0){
                reload();
            }
            if (currentAmmo == 0 && magazines == 0) {
                Sound.play("audio/Random463.wav");
            }
        }
    }

    public void reload() {
        if (reloading) return;

        else if (magazines > 0) {
            readyToShoot = false;
            reloading = true;
            Sound.play("audio/Random494.wav");
            reloadStartTime = System.currentTimeMillis();
        }
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
                if (e.alive && b.getBounds().intersects(e.getBounds())) {
                    System.out.println("Bullet touched enemy");
                    bullets.remove(i);

                    // if the enemy died, add a score
                    if (e.takeDamage(1, this.x, this.y)) {
                        score++;
                    }

                    break; // bullet was removed, skip remaining enemy checks
                }
            }
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void collectItem (Item item) {
        if (item.type == ItemType.MEDKIT) {
            // heal 1 point, but don't go over maxHealth
            health = Math.min(health + 1, maxHealth);
        }
        if (item.type == ItemType.MAGAZINE) {
            // heal 1 point, but don't go over maxHealth
            magazines++;
        }
        Sound.play("audio/Random60.wav");
    }
}