public class WaveManager {

    private static final int SHOOTER_START_WAVE = 2;
    private int startWave = 1;
    private int startEnemiesPerWave = 3;
    private int wave = 1;
    private int enemiesPerWave = 3;

    public void spawnWave(EnemyManager enemyManager, int panelWidth, int panelHeight) {
        enemyManager.clearEnemies();
        int margin = 50; // how far the enemy spawns outside the window
        int shooterCount = wave - (SHOOTER_START_WAVE - 1);
        
        for (int i = 0; i < enemiesPerWave; i++) {

            int spawnX = 0;
            int spawnY = 0;

            int side = (int)(Math.random() * 4);

            switch(side){
                case 0: // top
                    spawnX = (int)(Math.random() * panelWidth);
                    spawnY = -margin;
                    break;
                case 1: // right
                    spawnX = panelWidth + margin;
                    spawnY = (int)(Math.random() * panelHeight);
                    break;
                case 2: // bottom
                    spawnX = (int)(Math.random() * panelWidth);
                    spawnY = panelHeight + margin;
                    break;
                case 3: // left
                    spawnX = -margin;
                    spawnY = (int)(Math.random() * panelHeight);
                    break;
                }
                
            // add shooter enemies first, then fill the remaining with walker enemies
            if (i < shooterCount) {
                enemyManager.addEnemy(new ShooterEnemy(spawnX, spawnY));
            }
            else {
                enemyManager.addEnemy(new WalkerEnemy(spawnX, spawnY));
            }
        }
    }

    public void spawnNextWave(EnemyManager enemyManager, int panelWidth, int panelHeight) {
        enemiesPerWave += 2;
        spawnWave(enemyManager, panelWidth, panelHeight);
    }

    public void incrementWave() {
        wave++;
    }

    public void resetWave() {
        wave = startWave;
        enemiesPerWave = startEnemiesPerWave;
    }

    public int getWave() {
        return wave;
    }
}
