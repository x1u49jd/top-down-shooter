public class WaveManager {

    int startWave = 1;
    int startEnemiesPerWave = 3;
    int wave = 1;
    int enemiesPerWave = 3;

    void spawnWave(EnemyManager enemyManager, int panelWidth, int panelHeight) {
        enemyManager.clearEnemies();
        int margin = 50; // how far the enemy spawns outside the window
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
            enemyManager.addEnemy(new Enemy(spawnX, spawnY));
        }
    }

    void spawnNextWave(EnemyManager enemyManager, int panelWidth, int panelHeight) {
        wave++;
        enemiesPerWave += 2;
        Sound.play("audio/PowerUp1.wav");
        spawnWave(enemyManager, panelWidth, panelHeight);
    }

    void resetWave() {
        wave = startWave;
        enemiesPerWave = startEnemiesPerWave;
    }
}
