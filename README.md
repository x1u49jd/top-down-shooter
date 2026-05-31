# Top Down Shooter

A small Java top-down shooter with GUI built in Swing framework. The player moves around the arena, shoots enemies, survives increasingly larger waves, and collects items for health and ammunition.

## Features

- Menu, playing, pause, and game over screens
- Mouse aiming and shooting
- Enemy waves that grow over time with Walker and Shooter enemy types
- Item spawning for medkits and magazines
- Simple HUD with score, ammo, magazines, and wave number
- Sound effects for movement, combat, pickups, and state changes

## Controls

- `W`, `A`, `S`, `D` to move
- Mouse click to shoot toward the cursor
- `Enter` to start a new run from the menu
- `P` to pause or resume during play
- `R` to restart after game over
- `Esc` to return to the main menu from pause or game over

## Project Structure

- [Game.java](Game.java) - Creates the window, owns the main loop, and switches between menu, play, pause, and game over states.
- [GamePanel.java](GamePanel.java) - Handles all drawing for the menu, game, pause overlay, and game over screen.
- [HUD.java](HUD.java) - Draws score, ammo, magazines, wave number, and wave countdown text.
- [Player.java](Player.java) - Controls player movement, health, knockback, shooting, reloads, bullets, score, and how the player is drawn.
- [Bullet.java](Bullet.java) - Represents bullets fired by the player and how bullets are drawn.
- [Enemy.java](Enemy.java) - Abstract base enemy class for movement, collision, damage, knockback, and shared enemy behavior, including the abstract `draw(...)` method that subclasses implement.
    - [WalkerEnemy.java](WalkerEnemy.java) - Melee/chasing enemy implementation with its own `draw(...)` implementation.
    - [ShooterEnemy.java](ShooterEnemy.java) - Ranged enemy implementation that fires at the player when in range, with its own `draw(...)` implementation (including Shooter bullets).
- [EnemyManager.java](EnemyManager.java) - Stores and updates all enemies currently active.
- [WaveManager.java](WaveManager.java) - Spawns enemy waves, increases enemy count over time, and introduces Shooter enemies from later waves.
- [Item.java](Item.java) - Defines collectible item types, and item rendering.
- [ItemManager.java](ItemManager.java) - Stores items and resolves pickups between the player, enemies, and items.
- [ItemSpawnManager.java](ItemSpawnManager.java) - Spawns items over time while keeping a limited number on screen.
- [InputHandler.java](InputHandler.java) - Listens for keyboard and mouse input.
- [Sound.java](Sound.java) - Plays `.wav` audio files from the `audio/` folder.

## Gameplay Flow

- The game starts in the menu. Press `Enter` to begin a run.
- Enemies spawn from outside the window edges. The first spawned wave contains 5 enemies; each subsequent wave adds 2 more.
- Walker enemies are present from the beginning. Shooter enemies begin appearing from wave 2 onward, with one additional Shooter added each wave.
- Between waves, there is a short pre-delay and then a 3-2-1-START countdown before the next wave spawns.
- The player starts with 16 rounds and 3 spare magazines. Reloading takes 500 ms and triggers automatically when a magazine runs dry.
- Up to 4 items appear on screen at any time. Medkits restore 1 HP (up to a maximum of 5). Magazines add one full reload. Enemies can also pick up items — medkits heal them by 1 HP, and magazines are simply removed from the field without benefiting the enemy.
- Enemies deal 1 damage and apply knockback on contact. Bullets deal 1 damage and briefly stagger enemies.
- Each kill scores 1 point. The final score is shown on the game over screen.
- If the player's HP reaches zero, the game switches to game over. Press `R` to restart or `Esc` to return to the menu.

## Audio

Sound effects are loaded directly from the `audio/` folder using relative paths, so the game should be launched from the project root.

All sounds were generated with [Bfxr](https://www.bfxr.net/), a free 8-bit sound effect tool. They were chosen because they are free, easy to produce, and well-suited to the game. If the game ever moves toward a pixel art visual style in the future, the 8-bit sounds would complement it naturally.

## Notes

- The game currently uses fixed-size window dimensions.
- Because assets are loaded with relative file paths, moving the executable context away from the project root may break audio playback.