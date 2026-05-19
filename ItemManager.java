import java.util.ArrayList;

public class ItemManager {
    private ArrayList<Item> items = new ArrayList<>();

    public ArrayList<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public void clearItems() {
        items.clear();
    }

    public int getSize() {
        return items.size();
    }

    public int countType(Item.ItemType type) {
        int count = 0;

        for (Item i : items) {
            if (i.getType() == type) {
                count++;
            }
        }
        return count;
    }

    public void checkItemPickup(Player player, EnemyManager enemyManager) {
        for (int i = items.size() - 1; i >= 0; i--) {
            Item item = items.get(i);
            // check player pickup
            if (player.getBounds().intersects(item.getBounds())){
                player.collectItem(item);
                items.remove(i);
                continue;
            }
            // check enemy pickup
            for (Enemy enemy : enemyManager.getEnemies()) {
                if (enemy.getBounds().intersects(item.getBounds())){
                enemy.collectItem(item);
                items.remove(i);
                break; // item already removed, stop checking other enemies
            }
            }
        }
    }
}
