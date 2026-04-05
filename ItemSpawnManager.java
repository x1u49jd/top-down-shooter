import java.util.ArrayList;
import java.util.Random;

public class ItemSpawnManager {
    
    ArrayList<Item> items;
    int maxItemsOnScreen = 4;
    long itemsSpawnDelay = 1000;
    long lastItemSpawnTime = 0;

    public ItemSpawnManager(ArrayList<Item> items) {
        this.items = items;
    }

    public void spawnItem(int panelWidth, int panelHeight) {
        // tracks how many of each item there already are on the screen
        int medkitsOnScreen = 0;
        int magazinesOnScreen = 0;

        for (Item i : items) {
            if (i.type == Item.ItemType.MEDKIT) {medkitsOnScreen++;}
            else if (i.type == Item.ItemType.MAGAZINE) {magazinesOnScreen++;};
        }

        // if the amount exceeds the maximum don't proceed
        if (items.size() >= maxItemsOnScreen) return;

        Random rand = new Random();
        
        int spawnX = (int)(Math.random() * panelWidth - 20);
        int spawnY = (int)(Math.random() * panelHeight - 20);

        Item.ItemType type;
        
        // ensures atleast one Medkit and one Magazine are spawned on the screen at first
        if (medkitsOnScreen == 0 && magazinesOnScreen == 0) {
            type = rand.nextBoolean() ? Item.ItemType.MAGAZINE : Item.ItemType.MEDKIT;
        }
        else if (medkitsOnScreen == 0) {
            type = Item.ItemType.MEDKIT;
        }
        else if (magazinesOnScreen == 0) {
            type = Item.ItemType.MAGAZINE;
        }
        else {
            type = rand.nextBoolean() ? Item.ItemType.MAGAZINE : Item.ItemType.MEDKIT;
        }

        items.add(new Item(spawnX, spawnY, type));
    }


    public void update(int width, int height) {
        if (items.size() < maxItemsOnScreen && System.currentTimeMillis() - lastItemSpawnTime > itemsSpawnDelay) {
            spawnItem(width, height);
            lastItemSpawnTime = System.currentTimeMillis();
        }
    }
}
