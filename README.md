# Quantum Blocks - Android Game

A 2D falling block puzzle game for Android built with Kotlin and Jetpack Compose.

## Features

- **Classic Gameplay**: T-piece tetromino falling and rotating mechanics
- **Responsive Design**: Adapts to different Android screen sizes
- **Portrait Mode**: Optimized for vertical gameplay
- **Touch Controls**: Intuitive button-based controls
- **Score System**: Points for clearing lines with level progression
- **Game Over Handling**: Restart functionality when pieces reach the top

## Game Controls

- **← (Left Arrow)**: Move piece left
- **→ (Right Arrow)**: Move piece right  
- **↻ (Rotate)**: Rotate piece 90° clockwise
- **↓ (Drop)**: Soft drop (rapid falling)

## Technical Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with ViewModel
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)

## Project Structure

```
app/src/main/java/com/quantumblocks/game/
├── model/           # Data models (GameState, Piece, Position)
├── engine/          # Game logic and mechanics
├── viewmodel/       # ViewModel for state management
├── ui/
│   ├── components/  # Reusable UI components
│   ├── screens/     # Main game screen
│   └── theme/       # App theming
└── MainActivity.kt  # Main activity entry point
```

## Core Components

### Game Models
- `Position`: 2D grid coordinates
- `Piece`: Tetromino with rotation and movement logic
- `GameState`: Complete game state including board, score, and level

### Game Engine
- `GameEngine`: Handles game logic, collision detection, and piece movement
- Automatic falling with speed progression
- Line clearing and scoring system
- Game over detection

### UI Components
- `GameBoard`: Canvas-based game grid rendering
- `ControlPanel`: Touch controls for piece manipulation
- `ScoreDisplay`: Current score and level display
- `GameOverDialog`: End game dialog with restart option

## Building and Running

1. **Prerequisites**:
   - Android Studio Arctic Fox or later
   - Android SDK API 24+
   - Java 8 or later

2. **Build the project**:
   ```bash
   ./gradlew build
   ```

3. **Install on device**:
   ```bash
   ./gradlew installDebug
   ```

4. **Run in Android Studio**:
   - Open the project in Android Studio
   - Connect an Android device or start an emulator
   - Click the "Run" button

## Game Mechanics

### Piece Movement
- Pieces spawn at the top center of the board
- Automatic downward movement at constant speed
- Manual left/right movement with button controls
- Rotation with 90° clockwise turns
- Soft drop for rapid falling

### Line Clearing
- Complete horizontal rows disappear
- Blocks above fall down to fill gaps
- Score increases based on lines cleared and current level
- Level increases every 1000 points

### Game Over
- Occurs when a new piece cannot be placed at the top
- Displays final score and level reached
- Restart option to begin a new game

## Future Enhancements

This is the MVP (Minimum Viable Product) version. Future iterations will include:

1. **Multiple Piece Shapes**: All 7 standard tetrominoes
2. **Material Types**: Different block behaviors (wood, metal, ice)
3. **Quantum Effects**: Phase blocks and entangled pieces
4. **Advanced Physics**: Sliding, bouncing, and instability
5. **Visual Effects**: Animations and particle systems
6. **Audio System**: Sound effects and background music
7. **Special Modes**: Daily challenges and time trials

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing

This is a learning project demonstrating Android game development with modern tools. Feel free to fork and experiment with the code!
