package com.example.deepseagame.Views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deepseagame.Fragments.LeaderboardFragment;
import com.example.deepseagame.Fragments.MapFragment;
import com.example.deepseagame.Models.Player;
import com.example.deepseagame.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textview.MaterialTextView;

public class LeaderboardActivity extends AppCompatActivity {

    //private MaterialTextView score_LBL_text;
    private FrameLayout board_list;
    private FrameLayout board_map;
    private MapFragment mapFragment;
    private LeaderboardFragment listFragment;
    private ImageButton return_button;
    public final double DEFAULT_LAT = 31.771959;
    public final double DEFAULT_LON = 35.217018;
    private Player currentPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        findViews();
        mapFragment = new MapFragment();
        listFragment = new LeaderboardFragment();
        getIntents();
//        listFragment.updateData(currentPlayer);
//        listFragment.save();
//        listFragment.getAdapter().setPlayerCallback((player, position) -> mapFragment.focusOnLocation(new LatLng(player.getLat(), player.getLon())));
//        getSupportFragmentManager().beginTransaction().add(R.id.SCORE_FL_list, listFragment).commit();
//        getSupportFragmentManager().beginTransaction().add(R.id.SCORE_FL_map, mapFragment).commit();
        return_button.setOnClickListener((view) -> {
            returnToStartActivity();
        });;
    }

    private void getIntents() {
//        int currentScore = getIntent().getIntExtra("score", 0);
//        double lat = getIntent().getDoubleExtra("lat", DEFAULT_LAT);
//        double lon = getIntent().getDoubleExtra("lon", DEFAULT_LON);
//      currentPlayer = new Player().setScore(currentScore).setLat(lat).setLon(lon);
    }

    private void findViews() {
        board_list = findViewById(R.id.board_list);
        board_map = findViewById(R.id.board_map);
        return_button = findViewById(R.id.return_button);
    }

    private void returnToStartActivity(){
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}