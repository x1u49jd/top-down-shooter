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
            if (i.type == type) {
                count++;
            }
        }
        return count;
    }

    public void checkItemPickup(Player player) {
        for (int i = items.size() - 1; i >= 0; i--) {
            Item item = items.get(i);
            if (player.getBounds().intersects(item.getBounds())){
                player.collectItem(item);
                items.remove(i);
            }
        }
    }
}
