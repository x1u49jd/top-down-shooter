package managers;

import entities.Item;

import java.util.Random;

public class ItemSpawnManager {
    
    private static final int ITEM_SIZE = Item.getSize();
    private static final int SPAWN_PADDING = 50;
    private static final Random RANDOM = new Random();
    private int maxItemsOnScreen = 4;
    private long itemsSpawnDelay = 1000;
    private long lastItemSpawnTime = 0;
   

    private void spawnItem(ItemManager itemManager, int panelWidth, int panelHeight) {
        // counts how many of each item there already are on the screen
        int medkitsOnScreen = itemManager.countType(Item.ItemType.MEDKIT);
        int magazinesOnScreen = itemManager.countType(Item.ItemType.MAGAZINE);

        // if the amount exceeds the maximum don't proceed
        if (itemManager.getSize() >= maxItemsOnScreen) return;

        // calculate spawn boundaries, keeping items away from screen edges
        int minX = SPAWN_PADDING; // left
        int maxX = panelWidth - ITEM_SIZE - SPAWN_PADDING; // right
        int minY = SPAWN_PADDING; // top
        int maxY = panelHeight - ITEM_SIZE - SPAWN_PADDING; //bottom
      
        // pick random position within the safe spawn boundaries
        int spawnX = RANDOM.nextInt(maxX - minX + 1) + minX;
        int spawnY = RANDOM.nextInt(maxY - minY + 1) + minY; 

        Item.ItemType type;
        
        // ensures atleast one Medkit and one Magazine are spawned on the screen at first
        if (medkitsOnScreen == 0 && magazinesOnScreen == 0) {
            type = RANDOM.nextBoolean() ? Item.ItemType.MAGAZINE : Item.ItemType.MEDKIT;
        }
        else if (medkitsOnScreen == 0) {
            type = Item.ItemType.MEDKIT;
        }
        else if (magazinesOnScreen == 0) {
            type = Item.ItemType.MAGAZINE;
        }
        else {
            type = RANDOM.nextBoolean() ? Item.ItemType.MAGAZINE : Item.ItemType.MEDKIT;
        }

        itemManager.addItem(new Item(spawnX, spawnY, type));
    }


    public void update(ItemManager itemManager, int width, int height) {
        if (itemManager.getSize() < maxItemsOnScreen && System.currentTimeMillis() - lastItemSpawnTime > itemsSpawnDelay) {
            spawnItem(itemManager, width, height);
            lastItemSpawnTime = System.currentTimeMillis();
        }
    }

    public void resetItemSpawnTime() {
        lastItemSpawnTime = System.currentTimeMillis();
    }
}
