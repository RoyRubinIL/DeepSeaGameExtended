package com.example.deepseagame.Views;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deepseagame.Interfaces.MoveCallback;
import com.example.deepseagame.Logic.GameManager;
import com.example.deepseagame.R;
import com.example.deepseagame.Utilities.MoveDetector;
import com.google.android.material.button.MaterialButton;

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
    private boolean shouldGenerateGameObject = true;
    private String speedMode;
    private boolean sensorMode;
    private MoveDetector movementDetector;

    private final Runnable gameRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (gameManager.checkCollision()) {
                    Log.d(TAG, "Collision detected!");
                    onCollision();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speedMode = getIntent().getStringExtra("speed_mode");
        sensorMode = getIntent().getBooleanExtra("sensor_mode", false);

        gameManager = new GameManager(3);
        gameObjectsViews = new ImageView[gameManager.getNumRows()][gameManager.getNumLanes()];
        fishViews = new ImageView[gameManager.getNumLanes()];

        initializeViews();
        initializeButtons();
        updateLivesUI();
        enableMovement();
    }

    private void initializeButtons() {
        btnLeft = findViewById(R.id.btn_left);
        btnRight = findViewById(R.id.btn_right);

        if (sensorMode) {
            btnLeft.setVisibility(View.GONE);
            btnRight.setVisibility(View.GONE);
        }
    }

    private void enableMovement() {
        if (!sensorMode) {
            btnLeft.setOnClickListener(v -> moveFish(LEFT));
            btnRight.setOnClickListener(v -> moveFish(RIGHT));
        } else {
            movementDetector = new MoveDetector(this, new MoveCallback() {
                @Override
                public void moveX(String directionX) {
                    if (directionX.equals("Right")) {
                        moveFish(RIGHT);
                    } else {
                        moveFish(LEFT);
                    }
                }
            });
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
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Game stopping");
        gameHandler.removeCallbacksAndMessages(null);
    }

    private void initializeViews() {
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
            Toast.makeText(this, "You lose! Start Over", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "No lives left. Resetting game.");
            resetGame();
        }
    }

    private void updateLivesUI() {
        for (int i = 1; i <= gameManager.getMaxLives(); i++) {
            int resId = getResources().getIdentifier("IMG_heart" + i, "id", getPackageName());
            findViewById(resId).setVisibility(gameManager.getLives() >= i ? View.VISIBLE : View.GONE);
        }
    }

    private void resetGame() {
        gameManager.resetLives();
        updateLivesUI();
        gameManager.initializeGameMatrix();
        Log.d(TAG, "Game reset. Lives: " + gameManager.getLives());
    }
}
