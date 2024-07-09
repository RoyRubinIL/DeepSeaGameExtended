package com.example.deepseagame.Views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.deepseagame.R;
import com.example.deepseagame.Utilities.DataManager;
import com.example.deepseagame.Models.Player;
import com.google.gson.Gson;

import java.util.List;

public class StartActivity extends AppCompatActivity {

    private AppCompatImageButton slow_btn;
    private AppCompatImageButton fast_btn;
    private AppCompatImageButton sensor_btn;
    private AppCompatImageButton leaderboard_btn;

    private final String SLOW = "slow";
    private final String FAST = "fast";
    private boolean sensor_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        findViews();
        sensor_mode = false;
        setupButtons();
    }

    private void setupButtons() {
        slow_btn.setOnClickListener((view) -> startMainActivity(SLOW));
        fast_btn.setOnClickListener((view) -> startMainActivity(FAST));
        sensor_btn.setOnClickListener((view) -> toggleSensorMode());
        leaderboard_btn.setOnClickListener((view) -> startLeaderboardActivity());
    }

    private void startMainActivity(String speed_mode) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("speed_mode", speed_mode);
        intent.putExtra("sensor_mode", sensor_mode);
        startActivity(intent);
        finish();
    }

    private void startLeaderboardActivity() {
        Intent intent = new Intent(this, LeaderboardActivity.class);
        intent.putExtra("fromMainActivity", false);  // Add this line to ensure no player data is passed

        // Get the player list from DataManager and convert it to JSON
        List<Player> playerList = DataManager.getInstance().getPlayerList();
        String playerListJson = new Gson().toJson(playerList);
        intent.putExtra("playerListJson", playerListJson);

        startActivity(intent);
        finish();
    }

    private void toggleSensorMode() {
        sensor_mode = !sensor_mode;
        Toast.makeText(this, sensor_mode ? "Sensor mode is ON" : "Sensor mode is OFF", Toast.LENGTH_SHORT).show();
    }

    private void findViews() {
        slow_btn = findViewById(R.id.slow_button);
        fast_btn = findViewById(R.id.fast_button);
        sensor_btn = findViewById(R.id.sensor_button);
        leaderboard_btn = findViewById(R.id.scoreboard_button);
    }
}
