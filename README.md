## Deep Sea Game - Extended Version

### Overview
The extended version of Deep Sea Game adds new features to enhance the gameplay experience, including new controls, a wider play area, sounds, player records, and a map feature.

### New Features
- **App Icon**: A new custom icon for the game.
- **Tilt Controls**: Move the fish by tilting the device left or right.
- **Wider Play Area**: The game now features a five-lane road.
- **Sounds**: Background music and sound effects.
- **Player Records and Scores**: Keep track of player scores.
- **Menu Screen**: Select sensor or button controls and access the records screen.
- **Records Table Screen**: 
  - Displays the top ten highest scores since the game was installed.
  - Shows a map of locations where the scores were recorded.
  - Clicking on a record updates the map location display accordingly.
- **Speed Modes**: Two modes for speed - slow and fast.

### Installation
1. Clone the repository:
    ```sh
    git clone https://github.com/RoyRubinIL/DeepSeaGame.git
    ```
2. Open the project in Android Studio.
3. Build and run the project on an Android device or emulator.

### Gameplay
- **Controls**:
  - Use the left and right buttons or tilt the device to move the fish.
- **Objective**:
  - Avoid jellyfish appearing randomly in the lanes.
  - The game ends when all three lives are lost.
- **Features**:
  - Visual and tactile feedback for collisions and lives remaining.
  - Background music and sound effects enhance the gameplay experience.
- **Speed Modes**:
  - Select between slow and fast modes to adjust the game pace.

### Records and Map
- **Records Table**:
  - Displays the top ten scores.
  - Click on a score to view the location on the map where the score was recorded.
- **Map**:
  - Shows the locations of top scores.
  - Updates dynamically based on the selected score.

### Permissions
The extended version requires the following permissions:
- `android.permission.VIBRATE`
- `android.permission.ACCESS_NETWORK_STATE`
- `android.permission.ACCESS_FINE_LOCATION`
- `android.permission.ACCESS_COARSE_LOCATION`
- `android.permission.FOREGROUND_SERVICE`
- `android.permission.INTERNET`

### Video Demo
Check out the gameplay video: [Deep Sea Game Extended Demo](#)

---

### Code Structure
The project consists of various components including adapters, fragments, and utilities to manage the game logic, player records, and map integration.

#### Adapters
- **PlayerAdapter**: Manages the display of player records in a RecyclerView.

#### Fragments
- **LeaderboardFragment**: Displays the top scores in a RecyclerView.
- **MapFragment**: Integrates Google Maps to display locations of top scores.

#### Utilities
- **BackgroundSound**: Manages background music.
- **DataManager**: Handles player data storage and retrieval.
- **MoveDetector**: Detects device tilt for tilt controls.
- **MyLocationManager**: Manages user location services.
- **SoundPlayer**: Plays sound effects.
