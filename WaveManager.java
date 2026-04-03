import java.util.ArrayList;

public class WaveManager {
    int wave = 1;
    int startWave = 1;
    int enemiesPerWave = 3;
    int startEnemiesPerWave = 3;

    void spawnWave(ArrayList<Enemy> enemies, int panelWidth, int panelHeight) {
        enemies.clear();
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
            enemies.add(new Enemy(spawnX, spawnY));
        }
    }

    void checkWaveClear(ArrayList<Enemy> enemies, int panelWidth, int panelHeight) {
        boolean allEnemiesDead = true;

            for (Enemy e : enemies) {
                if (e.alive) {
                    allEnemiesDead = false;
                    break;
                }
            }

            if (allEnemiesDead) {
                wave++;
                enemiesPerWave += 2;
                Sound.play("audio/PowerUp1.wav");
                spawnWave(enemies, panelWidth, panelHeight);
            }
    }

    void resetWave() {
        wave = startWave;
        enemiesPerWave = startEnemiesPerWave;
    }
}
