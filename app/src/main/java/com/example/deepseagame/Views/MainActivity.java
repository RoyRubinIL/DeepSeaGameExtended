package com.example.deepseagame.Views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;

import com.example.deepseagame.Interfaces.MoveCallback;
import com.example.deepseagame.Logic.GameManager;
import com.example.deepseagame.Models.Player;
import com.example.deepseagame.R;
import com.example.deepseagame.Utilities.BackgroundSound;
import com.example.deepseagame.Utilities.DataManager;
import com.example.deepseagame.Utilities.MoveDetector;
import com.example.deepseagame.Utilities.MyLocationManager;
import com.example.deepseagame.Utilities.SoundPlayer;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int DELAY_MILLIS_SLOW = 1000;
    private static final int DELAY_MILLIS_FAST = 500;
    private static final int RIGHT = 1;
    private static final int LEFT = 0;

    private GameManager gameManager;
    private ImageView[][] gameObjectsViews;
    private ImageView[] fishViews;
    private final Handler gameHandler = new Handler();
    private MaterialButton btnLeft;
    private MaterialButton btnRight;
    private MaterialButton main_submit;
    private boolean shouldGenerateGameObject = true;
    private String speedMode;
    private boolean sensorMode;
    private MoveDetector movementDetector;
    private MyLocationManager myLocationManager;
    private CardView cardview_gameover;
    private MaterialTextView score;
    private MaterialTextView score_lbl;
    private AppCompatEditText main_player_name;
    private SoundPlayer soundPlayer;
    private boolean isGameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );

        setContentView(R.layout.activity_main);
        speedMode = getIntent().getStringExtra("speed_mode");
        sensorMode = getIntent().getBooleanExtra("sensor_mode", false);

        gameManager = new GameManager(3);
        gameObjectsViews = new ImageView[gameManager.getNumRows()][gameManager.getNumLanes()];
        fishViews = new ImageView[gameManager.getNumLanes()];

        initializeViews();
        showEndGameCard(false);
        initializeButtons();
        updateLivesUI();
        enableMovement();

        // Initialize MyLocationManager and get user location
        myLocationManager = new MyLocationManager(this);
        myLocationManager.findUserLocation(); // Ensure location is being fetched

        soundPlayer = new SoundPlayer(this);

    }

    private void initializeButtons() {
        btnLeft = findViewById(R.id.btn_left);
        btnRight = findViewById(R.id.btn_right);
        main_submit = findViewById(R.id.main_submit);
        main_submit.setOnClickListener(v -> startLeaderboardActivity());

        if (sensorMode) {
            btnLeft.setVisibility(View.GONE);
            btnRight.setVisibility(View.GONE);
        }
    }

    private void enableMovement() {
        if (!sensorMode) {
            btnLeft.setVisibility(View.VISIBLE);
            btnRight.setVisibility(View.VISIBLE);
            btnLeft.setOnClickListener(v -> moveFish(LEFT));
            btnRight.setOnClickListener(v -> moveFish(RIGHT));
        } else {
            btnLeft.setVisibility(View.GONE);
            btnRight.setVisibility(View.GONE);
            movementDetector = new MoveDetector(this, new MoveCallback() {
                @Override
                public void moveX(String dirX) {
                    if (dirX.equals("Right")) {
                        moveFish(RIGHT);
                    } else {
                        moveFish(LEFT);
                    }
                }

                @Override
                public void moveY(String dirY) {
                    // No vertical movement in this game
                }
            });
            movementDetector.start();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Game starting or restarting");
        startGameLoop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: Game restarting");
        startGameLoop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Game stopping");
        if (sensorMode)
            movementDetector.stop();
        BackgroundSound.getInstance().pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Game resuming");
        if (sensorMode && movementDetector != null) {
            movementDetector.start();
        }
        BackgroundSound.getInstance().playMusic();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Game stopping");
        gameHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Cleaning up resources");
        if (sensorMode && movementDetector != null) {
            movementDetector.stop();
        }
        gameHandler.removeCallbacksAndMessages(null);
    }

    private void initializeViews() {
        score = findViewById(R.id.main_score);
        score_lbl = findViewById(R.id.main_score_lbl);
        main_player_name = findViewById(R.id.main_player_name);
        cardview_gameover = findViewById(R.id.cardview_gameover);

        for (int i = 0; i < gameManager.getNumRows() - 1; i++) {
            for (int j = 0; j < gameManager.getNumLanes(); j++) {
                String jellyfishId = "object_" + i + j;
                @SuppressLint("DiscouragedApi") int resId = getResources().getIdentifier(jellyfishId, "id", getPackageName());
                gameObjectsViews[i][j] = findViewById(resId);
                if (gameObjectsViews[i][j] == null) {
                    Log.e(TAG, "GameObjectsViews ImageView is null at " + i + ", " + j + " with ID: " + jellyfishId);
                }
            }
        }

        for (int i = 0; i < gameManager.getNumLanes(); i++) {
            String fishId = "fish_9" + i;
            @SuppressLint("DiscouragedApi") int resId = getResources().getIdentifier(fishId, "id", getPackageName());
            fishViews[i] = findViewById(resId);
            if (fishViews[i] == null) {
                Log.e(TAG, "Fish ImageView is null at lane " + i + " with ID: " + fishId);
            }
        }
    }

    private void startGameLoop() {
        gameHandler.removeCallbacks(gameRunnable);
        gameHandler.post(gameRunnable);
    }

    private final Runnable gameRunnable = new Runnable() {
        @Override
        public void run() {
            if (isGameOver)
                return;
            try {
                if (gameManager.checkCollision()) {
                    Log.d(TAG, "Collision detected!");
                    onCollision();
                    makeElectricSound();
                }
                if (gameManager.checkWormCollision()) {
                    gameManager.setScore(gameManager.getScore() + 10);
                    score_lbl.setText(String.valueOf(gameManager.getScore()));
                    makeBiteSound();
                }
                gameManager.updateGameMatrix();
                updateUI();
                if (shouldGenerateGameObject) {
                    gameManager.generateGameObjects();
                }
                shouldGenerateGameObject = !shouldGenerateGameObject;
            } catch (Exception e) {
                Log.e(TAG, "Error in game loop", e);
            }
            int delay = speedMode.equals("slow") ? DELAY_MILLIS_SLOW : DELAY_MILLIS_FAST;
            gameHandler.postDelayed(this, delay);
        }
    };

    private void updateUI() {
        for (int i = 0; i < gameManager.getNumRows() - 1; i++) {
            for (int j = 0; j < gameManager.getNumLanes(); j++) {
                int value = gameManager.getValueInMatrix(i, j);
                if (value == GameManager.JELLYFISH) {
                    gameObjectsViews[i][j].setImageResource(R.drawable.ic_jellyfish);
                } else if (value == GameManager.WORM) {
                    gameObjectsViews[i][j].setImageResource(R.drawable.ic_worm);
                } else {
                    gameObjectsViews[i][j].setImageResource(0);
                }
            }
        }

        for (int i = 0; i < gameManager.getNumLanes(); i++) {
            fishViews[i].setVisibility(i == gameManager.getFishLane() ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void moveFish(int direction) {
        if (direction == RIGHT) {
            gameManager.moveFishRight();
        } else {
            gameManager.moveFishLeft();
        }
        updateUI();
    }

    private void onCollision() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(500);
        }
        Toast.makeText(this, "Crash!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Collision! Lives remaining: " + gameManager.getLives());
        gameManager.decrementLives();
        updateLivesUI();

        if (gameManager.getLives() <= 0) {
            gameOver();
        }
    }

    private void updateLivesUI() {
        for (int i = 1; i <= gameManager.getMaxLives(); i++) {
            int resId = getResources().getIdentifier("IMG_heart" + i, "id", getPackageName());
            findViewById(resId).setVisibility(gameManager.getLives() >= i ? View.VISIBLE : View.GONE);
        }
    }

    private void gameOver() {
        isGameOver = true;
        gameHandler.removeCallbacks(gameRunnable);
        showEndGameCard(true);
        score.setText("Final Score: " + gameManager.getScore());
    }

    private void showEndGameCard(boolean isVisible) {
        cardview_gameover.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
    }

    private boolean savePlayer() {
        if (main_player_name.length() == 0) {
            Toast.makeText(this, "Please Add Name", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Player player = new Player()
                    .setName(main_player_name.getText().toString())
                    .setScore(gameManager.getScore())
                    .setLat(myLocationManager.getUserLat())
                    .setLng(myLocationManager.getUserLon());

            Log.d("rrr", "MainActivity- lat: " + myLocationManager.getUserLat() + " lng: " + myLocationManager.getUserLon());
            DataManager.getInstance().addPlayer(player);
            DataManager.getInstance().sortByScore();
            return true;
        }
    }

    private Intent savePlayerListToJson() {
        Gson gson = new Gson();
        String playerListJson = gson.toJson(DataManager.getInstance().getPlayerList());
        Intent intent = new Intent(this, LeaderboardActivity.class);
        intent.putExtra("playerListJson", playerListJson);
        return intent;
    }

    private void startLeaderboardActivity() {
        if (savePlayer()) {
            Intent intent = savePlayerListToJson();
            intent.putExtra("fromMainActivity", true);  // Add this line
            Log.d("rrr", DataManager.getInstance().toString());

            startActivity(intent);
            finish();
        }
    }

    private void makeElectricSound() {
        soundPlayer.playSound(R.raw.electric);
    }

    private void makeBiteSound() {
        soundPlayer.playSound(R.raw.eat);
    }


}
