package entities;

import main.Sound;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;
import java.util.ArrayList;

public class Player {
    private static final int WIDTH = 40, HEIGHT = 40;
    private static final int SPEED = 6;
    private static final int MAX_HEALTH = 5;
    private static final int MAX_AMMO = 16;
    private static final long WALK_DELAY = 200;
    private static final long RELOAD_TIME = 500;

    // === position and movement === 
    private int x,y;
    private int knockbackX, knockbackY;
    
    // === combat ===
    private int health = MAX_HEALTH;
    private int currentAmmo = MAX_AMMO;
    private int magazines = 3;
    private boolean reloading = false;
    private boolean readyToShoot = true;
    private long reloadStartTime = 0;
    private ArrayList<Bullet> bullets = new ArrayList<>();

    // === audio ===
    private long lastWalkSound = 0;

    // === misc ===
    private int score = 0;

    public Player(int startX, int startY) {
        x = startX;
        y = startY;
    }

    public void update(boolean up, boolean down, boolean left, boolean right, int windowWidth, int windowHeight, ArrayList<Enemy> enemies) {
        move(up, down, left, right, windowWidth, windowHeight);
        updateReload();
        updateBullets(windowWidth, windowHeight, enemies);
    }

    private void move(boolean up, boolean down, boolean left, boolean right, int windowWidth, int windowHeight) {

        boolean moving = up || down || left || right;

        if (up) {y -= SPEED;};
        if (down) {y += SPEED;};
        if (left) {x -= SPEED;};
        if (right) {x += SPEED;};

        if (moving && System.currentTimeMillis() - lastWalkSound > WALK_DELAY) {
            Sound.play("audio/Random171.wav");
            lastWalkSound = System.currentTimeMillis();
        }

        // moves player according to knockback
        x += knockbackX;
        y += knockbackY;

        // slowly reduces knockback for smooth stop
        knockbackX *= 0.9; // friction factor
        knockbackY *= 0.9;

        // unables player to move outside current window
        if (x < 0) {x = 0;}
        if (x + WIDTH > windowWidth) {x = windowWidth - WIDTH;}
        if (y < 0) {y = 0;}
        if (y + HEIGHT > windowHeight) {y = windowHeight - HEIGHT;}

    };

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, WIDTH, HEIGHT);

        // ---- UI HEALTH BAR ----
        int barWidth = 40; // same width as player
        int barHeight = 8;

        // grey background
        g.setColor(Color.GRAY);
        g.fillRect(x, y - 15, barWidth, barHeight);

        // health scaled properly
        g.setColor(Color.GREEN);
        int currentWidth = (int)((health / (double)MAX_HEALTH) * barWidth);
        g.fillRect(x, y - 15, currentWidth, barHeight);

        // draw bullets
        for (Bullet b : bullets) {
            b.draw(g);
        }

        

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
        if (readyToShoot && health > 0) {
            if (currentAmmo > 0) {
                // spawn bullet at player's center
                double startX = x + (WIDTH / 2);
                double startY = y + (HEIGHT / 2);

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

    // starts reload process and records when it began
    private void reload() {
        if (reloading) {
            return;
        }
        else if (magazines > 0) {
            readyToShoot = false;
            reloading = true;
            Sound.play("audio/Random494.wav");
            reloadStartTime = System.currentTimeMillis();
        }
    }

    // completes the reload process once time has passed
    private void updateReload() {
        if (reloading) {
            if (System.currentTimeMillis() - reloadStartTime >= RELOAD_TIME) {
                currentAmmo = MAX_AMMO;
                magazines--;
                reloading = false;
                readyToShoot = true;
            }
        }
    }

    private void updateBullets (int windowWidth, int windowHeight, ArrayList<Enemy> enemies) {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            
            Bullet b = bullets.get(i);
            b.update();

            // if bullet goes off screen, remove it
            if (b.getX() < 0 || b.getX() > windowWidth || b.getY()< 0 || b.getY() > windowHeight) {
                bullets.remove(i);
                continue;
            }
            // if bullet touches enemy, remove it
            for (Enemy e : enemies) {
                if (e.isAlive() && b.getBounds().intersects(e.getBounds())) {
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

    public void collectItem(Item item) {
        if (item.getType() == Item.ItemType.MEDKIT) {
            // heal 1 point, but don't go over MAX_HEALTH
            health = Math.min(health + 1, MAX_HEALTH);
        }
        if (item.getType() == Item.ItemType.MAGAZINE) {
            // heal 1 point, but don't go over MAX_HEALTH
            magazines++;
        }
        Sound.play("audio/Random60.wav");
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getHealth() {
        return health;
    }

    public int getScore() {
        return score;
    }

    public int getCurrentAmmo() {
        return currentAmmo;
    }

    public int getMagazines() {
        return magazines;
    }

    public int getMaxAmmo() {
        return MAX_AMMO;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }
}