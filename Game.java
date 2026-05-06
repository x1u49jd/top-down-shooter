import javax.swing.JFrame;

public class Game {

    enum GameState {
        MENU,
        PLAYING,
        PAUSE,
        GAME_OVER
    }

    private static int INITIAL_WINDOW_WIDTH = 1440, INITIAL_WINDOW_HEIGHT = 900;
    
    private GameState gameState = GameState.MENU;

    private JFrame window;

    private Player player;

    private WaveManager waveManager;
    private ItemManager itemManager;
    private ItemSpawnManager itemSpawnManager;
    private EnemyManager enemyManager;
    private GamePanel panel;
    private InputHandler input;

    private boolean waitingForNextWave = false;
    private long nextWaveCountdownStartTime = 0;
    private static final long NEXT_WAVE_COUNTDOWN_DURATION = 4000;

    private void backMenu() {
        gameState = GameState.MENU;
        waitingForNextWave = false;
        panel.setWaveCountdownText("");
        panel.setGameState(gameState);
        panel.repaint();
        Sound.play("audio/Blip.wav");
    }

    private void pauseGame() {
        gameState = GameState.PAUSE;
        input.clearAllInput();
        panel.setGameState(gameState);
        panel.repaint();
    }

    private void unpauseGame() {
        gameState = GameState.PLAYING;
        input.clearAllInput();
    }

    private void restartGame() {
        gameState = GameState.PLAYING;
        input.clearAllInput();
        waveManager.resetWave();

        waitingForNextWave = false;
        panel.setWaveCountdownText("");

        player = new Player(400, 400);
        panel.setPlayer(player);
        itemManager.clearItems();
        itemSpawnManager.resetItemSpawnTime();

        waveManager.spawnWave(enemyManager, panel.getWidth(), panel.getHeight());

        input.reset();

        // keep panel state in sync before repaint so GAME_OVER UI is not drawn with a reset score
        panel.setGameState(gameState);

        panel.repaint();

        Sound.play("audio/Blip12.wav");
    }

    private void checkGameOver() {
        if (player.getHealth() <= 0) {
            gameState = GameState.GAME_OVER;
            waitingForNextWave = false;
            panel.setWaveCountdownText("");
            input.clearAllInput();
        }
    }

    private void startNextWaveCountDown() {
        waitingForNextWave = true;
        nextWaveCountdownStartTime = System.currentTimeMillis();
    }

    private void updateNextWaveCountDown() {
        long elapsed = System.currentTimeMillis() - nextWaveCountdownStartTime;

        if (elapsed < 1000) {
            panel.setWaveCountdownText("3");
        }
        else if (elapsed < 2000) {
            panel.setWaveCountdownText("2");
        }
        else if (elapsed < 3000) {
            panel.setWaveCountdownText("1");
        }
        else if (elapsed < NEXT_WAVE_COUNTDOWN_DURATION) {
            panel.setWaveCountdownText("START");
        }
        else {
            waitingForNextWave = false;
            panel.setWaveCountdownText("");
            waveManager.spawnNextWave(enemyManager, panel.getWidth(), panel.getHeight());
        }
    }

    private void gameLoop() {
        while (true) {
            if (gameState == GameState.MENU) {
                panel.setGameState(gameState);
                if (input.isStartRequested()) {
                    restartGame();
                }
            }   

            if (gameState == GameState.PLAYING) {

                if (input.isMousePressed()) {
                    player.shoot(input.getMouseX(), input.getMouseY());
                    input.resetMouse();
                }

                // update player's position based on keys pressed, bullets, reload
                player.update(input.isUpPressed(), input.isDownPressed(), input.isLeftPressed(), input.isRightPressed(), panel.getWidth(), panel.getHeight(), enemyManager.getEnemies());
                enemyManager.update(player);
                itemManager.checkItemPickup(player);
                checkGameOver();
                itemSpawnManager.update(itemManager ,panel.getWidth(), panel.getHeight());

                // start the countdown and spawn enemies
                if (waitingForNextWave) {
                    updateNextWaveCountDown();
                }
                else if (enemyManager.areAllDead()) {
                    startNextWaveCountDown();
                }

                panel.setGameState(gameState);
                panel.repaint(); // redraw after moving
            }
            if (gameState == Game.GameState.PLAYING && input.isPaused()) {
                    pauseGame();
                }
            if (gameState == Game.GameState.PAUSE) {
                if (!input.isPaused()){
                    unpauseGame();
                }
                if (input.isEscapeRequested()){
                    backMenu();
                }
            }
            if (gameState == Game.GameState.GAME_OVER){
                if (input.isRestartRequested()) {
                    restartGame();
                }
                if (input.isEscapeRequested()) {
                    backMenu();
                }
            }

            // wait 16ms (60fps)
            try {
                Thread.sleep(16);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void createWindow() {
        window = new JFrame();
        window.setSize(INITIAL_WINDOW_WIDTH, INITIAL_WINDOW_HEIGHT);
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
        input = new InputHandler();
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