import java.util.ArrayList;

public class EnemyManager {
    private ArrayList<Enemy> enemies = new ArrayList<>();

    public void update(Player player) {
        for (Enemy e : enemies) {
            e.update(player, enemies);
        }
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void addEnemy (Enemy e) {
        enemies.add(e);
    }

    public void removeEnemy (Enemy e) {
        enemies.remove(e);
    }

    public void clearEnemies() {
        enemies.clear();
    }

    public boolean areAllDead() {
        for (Enemy e : enemies) { 
            if (e.alive) return false; }
        return true;
    }
}
