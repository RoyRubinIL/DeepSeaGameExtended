package com.example.deepseagame.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deepseagame.Fragments.LeaderboardFragment;
import com.example.deepseagame.Fragments.MapFragment;
import com.example.deepseagame.Models.Player;
import com.example.deepseagame.R;
import com.example.deepseagame.Utilities.BackgroundSound;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private static final String EXTRA_FROM_MAIN_ACTIVITY = "fromMainActivity";

    private FrameLayout board_list;
    private FrameLayout board_map;
    private MapFragment mapFragment;
    private LeaderboardFragment leaderboardFragment;
    private ImageButton return_button;

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

        setContentView(R.layout.activity_leaderboard); // Make sure this layout contains the return_button
        findViews();
        setupFragments();

        setupReturnButton();
    }

    private void setupFragments() {
        leaderboardFragment = new LeaderboardFragment();
        leaderboardFragment.setCallBackConnectLocationToPlayer((lat, lon) -> mapFragment.moveToLocation(lat, lon));

        mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.board_list, leaderboardFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.board_map, mapFragment).commit();

        // Ensure fragments are committed before attempting to set player list
        getSupportFragmentManager().executePendingTransactions();

        boolean fromMainActivity = getIntent().getBooleanExtra(EXTRA_FROM_MAIN_ACTIVITY, false);
        List<Player> playerList = getPlayerListData();
        if (playerList != null && !playerList.isEmpty()) {
            leaderboardFragment.setPlayerList(playerList);
        }
    }

    private List<Player> getPlayerListData() {
        String playerListJson = getIntent().getStringExtra("playerListJson");
        if (playerListJson == null || playerListJson.isEmpty()) {
            return Collections.emptyList();
        }
        List<Player> playerList = new Gson().fromJson(playerListJson, new TypeToken<List<Player>>(){}.getType());
        return playerList;
    }

    private void findViews() {
        board_list = findViewById(R.id.board_list);
        board_map = findViewById(R.id.board_map);
        return_button = findViewById(R.id.return_button); // Ensure this ID matches the one in your layout file
    }

    private void setupReturnButton() {
        if (return_button != null) {
            return_button.setOnClickListener(v -> returnToStartActivity());
        }
    }

    private void returnToStartActivity() {
        startActivity(new Intent(this, StartActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BackgroundSound.getInstance().pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BackgroundSound.getInstance().playMusic();
    }
}
