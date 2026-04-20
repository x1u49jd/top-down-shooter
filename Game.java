import javax.swing.JFrame;

public class Game {

    enum GameState {
        MENU,
        PLAYING,
        GAME_OVER
    }

    private GameState gameState = GameState.MENU;

    private JFrame window;

    private int initialWindowWidth = 1440, initialWindowHeight = 900;

    private boolean startRequested = false;
    private boolean restartRequested = false;

    private Player player;

    private WaveManager waveManager;
    private ItemManager itemManager;
    private ItemSpawnManager itemSpawnManager;
    private EnemyManager enemyManager;
    private GamePanel panel;
    private InputHandler input;

    private void startGame() {
        gameState = GameState.PLAYING;
        startRequested = false;
        Sound.play("audio/Blip.wav");
    }

    private void restartGame() {
        gameState = GameState.PLAYING;
        waveManager.resetWave();

        player = new Player(400, 400);
        panel.setPlayer(player);
        input.setPlayer(player);
        itemManager.clearItems();
        itemSpawnManager.resetItemSpawnTime();

        waveManager.spawnWave(enemyManager, panel.getWidth(), panel.getHeight());

        input.reset();

        panel.repaint();

        Sound.play("audio/Blip12.wav");
    }

    private void checkGameOver() {
        if (player.getHealth() <= 0) {
            gameState = GameState.GAME_OVER;
        }
    }

    private void gameLoop() {
        while (true) {
            if (gameState == GameState.MENU) {
                panel.setGameState(gameState);
                if (input.isStartRequested()) {
                    startRequested = true;
                }
            }

            if (startRequested) {
                startGame();
            }          

            if (gameState == GameState.PLAYING) {
                // update player's position based on keys pressed, bullets, reload
                player.update(input.isUpPressed(), input.isDownPressed(), input.isLeftPressed(), input.isRightPressed(), panel.getWidth(), panel.getHeight(), enemyManager.getEnemies());
                enemyManager.update(player);
                itemManager.checkItemPickup(player);
                checkGameOver();
                itemSpawnManager.update(itemManager ,panel.getWidth(), panel.getHeight());

                // spawn next wave when all enemies in the current wave are dead
                if (enemyManager.areAllDead()) {
                    waveManager.spawnNextWave(enemyManager, panel.getWidth(), panel.getHeight());
                }

                panel.setGameState(gameState);
                panel.repaint(); // redraw after moving

                // wait 16ms (60fps)
                try {
                    Thread.sleep(16);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (gameState == Game.GameState.GAME_OVER && input.isRestartRequested()){
                restartRequested = true;
            }

            if (restartRequested) {
                restartRequested = false;
                restartGame();
            }
        }
    }

    private void createWindow() {
        window = new JFrame();
        window.setSize(initialWindowWidth, initialWindowHeight);
        window.setTitle("Top Down Shooter");

        // tells Java that when X is clicked, end the program
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        // centers the window on screen
        window.setLocationRelativeTo(null);
    }

    private void createGameObjects() {
        player = new Player(400,400);
    }

    private void setupInput() {
        panel.addMouseListener(input);
        window.addKeyListener(input);
    }

    public Game() {
        createWindow();
        createGameObjects();
        enemyManager = new EnemyManager();
        itemManager = new ItemManager();
        waveManager = new WaveManager();
        itemSpawnManager = new ItemSpawnManager();
        panel = new GamePanel(player, itemManager, enemyManager, waveManager);
        input = new InputHandler(player);
        setupInput();
        window.add(panel);
        window.setVisible(true);

        // added after panel added so that layout is calculated and ready to use by spawnWave()
        waveManager.spawnWave(enemyManager, panel.getWidth(), panel.getHeight());

        new Thread(this::gameLoop).start();
    }

    public static void main(String[] args) {
        new Game();
    }
}