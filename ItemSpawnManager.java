import java.util.Random;

public class ItemSpawnManager {
    
    private int maxItemsOnScreen = 4;
    private long itemsSpawnDelay = 1000;
    private long lastItemSpawnTime = 0;

    private void spawnItem(ItemManager itemManager, int panelWidth, int panelHeight) {
        // counts how many of each item there already are on the screen
        int medkitsOnScreen = itemManager.countType(Item.ItemType.MEDKIT);
        int magazinesOnScreen = itemManager.countType(Item.ItemType.MAGAZINE);

        // if the amount exceeds the maximum don't proceed
        if (itemManager.getSize() >= maxItemsOnScreen) return;

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
