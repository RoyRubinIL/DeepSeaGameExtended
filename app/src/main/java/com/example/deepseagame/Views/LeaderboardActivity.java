package com.example.deepseagame.Views;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.deepseagame.R;
import com.google.android.material.textview.MaterialTextView;

public class LeaderboardActivity extends AppCompatActivity {

    private MaterialTextView score_LBL_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

//        findViews();
        initViews();
    }

    private void initViews() {
        Intent prev = getIntent();
        score_LBL_text.setText(prev.getExtras().getString("KEY_MESSAGE"));
    }

//    private void findViews() {
//        score_LBL_text = findViewById(R.id.score_LBL_text);
//    }
}