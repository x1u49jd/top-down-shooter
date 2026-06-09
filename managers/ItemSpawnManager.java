package managers;

import entities.Item;
import entities.ItemType;

import java.util.Random;

public class ItemSpawnManager {
    private static final int ITEM_SIZE = Item.getSize();
    private static final int SPAWN_PADDING = 50;
    private static final Random RANDOM = new Random();
    private static final int MAX_ITEMS_ON_SCREEN = 4;
    private static final long ITEMS_SPAWN_DELAY = 1000;
    private long lastItemSpawnTime = 0;
   

    private void spawnItem(ItemManager itemManager, int panelWidth, int panelHeight) {
        // counts how many of each item there already are on the screen
        int medkitsOnScreen = itemManager.countType(ItemType.MEDKIT);
        int magazinesOnScreen = itemManager.countType(ItemType.MAGAZINE);

        // if the amount exceeds the maximum don't proceed
        if (itemManager.getSize() >= MAX_ITEMS_ON_SCREEN) return;

        // calculate spawn boundaries, keeping items away from screen edges
        int minX = SPAWN_PADDING; // left
        int maxX = panelWidth - ITEM_SIZE - SPAWN_PADDING; // right
        int minY = SPAWN_PADDING; // top
        int maxY = panelHeight - ITEM_SIZE - SPAWN_PADDING; //bottom
      
        // pick random position within the safe spawn boundaries
        int spawnX = RANDOM.nextInt(maxX - minX + 1) + minX;
        int spawnY = RANDOM.nextInt(maxY - minY + 1) + minY; 

        ItemType type;
        
        // ensures atleast one Medkit and one Magazine are spawned on the screen at first
        if (medkitsOnScreen == 0 && magazinesOnScreen == 0) {
            type = RANDOM.nextBoolean() ? ItemType.MAGAZINE : ItemType.MEDKIT;
        }
        else if (medkitsOnScreen == 0) {
            type = ItemType.MEDKIT;
        }
        else if (magazinesOnScreen == 0) {
            type = ItemType.MAGAZINE;
        }
        else {
            type = RANDOM.nextBoolean() ? ItemType.MAGAZINE : ItemType.MEDKIT;
        }

        itemManager.addItem(new Item(spawnX, spawnY, type));
    }


    public void update(ItemManager itemManager, int width, int height) {
        if (itemManager.getSize() < MAX_ITEMS_ON_SCREEN && System.currentTimeMillis() - lastItemSpawnTime > ITEMS_SPAWN_DELAY) {
            spawnItem(itemManager, width, height);
            lastItemSpawnTime = System.currentTimeMillis();
        }
    }

    public void resetItemSpawnTime() {
        lastItemSpawnTime = System.currentTimeMillis();
    }
}
