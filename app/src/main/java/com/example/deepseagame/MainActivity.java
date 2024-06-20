package com.example.deepseagame;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int NUM_LANES = 3;
    private static final int NUM_ROWS = 10;
    private static final String TAG = "MainActivity";
    private static final int DELAY_MILLIS = 1000; // Variable for delay duration
    private static final int MAX_LIVES = 4;

    private GameManager gameManager;
    private ImageView[][] jellyfishViews;
    private ImageView[] fishViews;
    private int[][] gameMatrix;
    private int fishLane = 1; // Fish starts in the center lane
    private final Handler gameHandler = new Handler();
    private MaterialButton btnLeft;
    private MaterialButton btnRight;
    private boolean shouldGenerateJellyfish = true; // Initialize the boolean flag

    private final Runnable gameRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (checkCollision()) {
                    Log.d(TAG, "Collision detected!");
                    onCollision();
                }
                updateGameMatrix();
                updateUI();
                if (shouldGenerateJellyfish) {
                    generateJellyfish();
                }
                shouldGenerateJellyfish = !shouldGenerateJellyfish; // Toggle the flag
            } catch (Exception e) {
                Log.e(TAG, "Error in game loop", e);
            }
            gameHandler.postDelayed(this, DELAY_MILLIS); // Repeat every DELAY_MILLIS
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Ensure this layout file name is correct

        gameManager = new GameManager(3);
        updateLivesUI();
        jellyfishViews = new ImageView[NUM_ROWS][NUM_LANES];
        fishViews = new ImageView[NUM_LANES];
        initializeViews();

        gameMatrix = new int[NUM_ROWS][NUM_LANES];
        initializeGameMatrix();

        // Add button click listeners
        btnLeft = findViewById(R.id.btn_left);
        btnRight = findViewById(R.id.btn_right);

        btnLeft.setOnClickListener(v -> moveFishLeft());
        btnRight.setOnClickListener(v -> moveFishRight());

        startGameLoop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Game starting or restarting");
        // Optionally, you can include any necessary setup logic here
        startGameLoop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: Game restarting");
        // Optionally, you can include any necessary setup logic here
        startGameLoop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Game stopping");
        gameHandler.removeCallbacksAndMessages(null); // Stop the game loop
    }

    private void initializeViews() {
        for (int i = 0; i < NUM_ROWS - 1; i++) {
            for (int j = 0; j < NUM_LANES; j++) {
                String jellyfishId = "jellyfish_" + i + j;
                @SuppressLint("DiscouragedApi") int resId = getResources().getIdentifier(jellyfishId, "id", getPackageName());
                jellyfishViews[i][j] = findViewById(resId);
                if (jellyfishViews[i][j] == null) {
                    Log.e(TAG, "Jellyfish ImageView is null at " + i + ", " + j + " with ID: " + jellyfishId);
                }
            }
        }
        for (int i = 0; i < NUM_LANES; i++) {
            String fishId = "fish_9" + i;
            @SuppressLint("DiscouragedApi") int resId = getResources().getIdentifier(fishId, "id", getPackageName());
            fishViews[i] = findViewById(resId);
            if (fishViews[i] == null) {
                Log.e(TAG, "Fish ImageView is null at lane " + i + " with ID: " + fishId);
            }
        }
    }

    private void initializeGameMatrix() {
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_LANES; j++) {
                gameMatrix[i][j] = 0; // Initialize with 0
            }
        }
        gameMatrix[NUM_ROWS - 1][fishLane] = 1;
    }

    private void generateJellyfish() {
        Random rand = new Random();
        int lane = rand.nextInt(NUM_LANES);
        gameMatrix[0][lane] = 2; // 2 represents jellyfish
        jellyfishViews[0][lane].setVisibility(View.VISIBLE);
    }

    private void startGameLoop() {
        gameHandler.removeCallbacks(gameRunnable);
        gameHandler.post(gameRunnable);
    }

    private void updateGameMatrix() {
        for (int i = NUM_ROWS - 1; i >= 0; i--) {
            for (int j = 0; j < NUM_LANES; j++) {
                if (gameMatrix[i][j] == 2) { // Jellyfish present
                    if (i < NUM_ROWS - 2) { // Not the last row
                        if (gameMatrix[i + 1][j] == 0) {
                            gameMatrix[i + 1][j] = 2;
                            gameMatrix[i][j] = 0;
                            jellyfishViews[i + 1][j].setVisibility(View.VISIBLE);
                            jellyfishViews[i][j].setVisibility(View.INVISIBLE);
                        }
                    } else { // Last row
                        gameMatrix[i][j] = 0;
                        jellyfishViews[i][j].setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }

    private boolean checkCollision() {
        return gameMatrix[NUM_ROWS - 2][fishLane] == 2;
    }

    private void onCollision() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(500); // Vibrate for 500ms
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
        for (int i = 1; i <= MAX_LIVES; i++) {
            int resId = getResources().getIdentifier("IMG_heart" + i, "id", getPackageName());
            findViewById(resId).setVisibility(gameManager.getLives() >= i ? View.VISIBLE : View.GONE);
        }
    }

    private void resetGame() {
        gameManager.resetLives();
        updateLivesUI();
        initializeGameMatrix();
        Log.d(TAG, "Game reset. Lives: " + gameManager.getLives());
    }

    private void updateUI() {
        for (int i = 0; i < NUM_ROWS - 1; i++) {
            for (int j = 0; j < NUM_LANES; j++) {
                if (gameMatrix[i][j] == 2) {
                    jellyfishViews[i][j].setImageResource(R.drawable.ic_jellyfish);
                } else {
                    jellyfishViews[i][j].setImageResource(0);
                }
            }
        }

        // Position the fish based on the column (lane)
        for (int i = 0; i < NUM_LANES; i++) {
            if (i == fishLane) {
                fishViews[i].setVisibility(View.VISIBLE);
            } else {
                fishViews[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void moveFishLeft() {
        if (fishLane > 0) {
            gameMatrix[NUM_ROWS - 1][fishLane] = 0;
            fishLane--;
            gameMatrix[NUM_ROWS - 1][fishLane] = 1;
            updateUI();
        }
    }

    private void moveFishRight() {
        if (fishLane < NUM_LANES - 1) {
            gameMatrix[NUM_ROWS - 1][fishLane] = 0;
            fishLane++;
            gameMatrix[NUM_ROWS - 1][fishLane] = 1;
            updateUI();
        }
    }
}
