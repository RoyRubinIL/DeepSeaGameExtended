package com.example.deepseagame.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepseagame.Adapters.PlayerAdapter;
import com.example.deepseagame.Interfaces.LocToPlayerCallback;
import com.example.deepseagame.Interfaces.PlayerCallback;
import com.example.deepseagame.Models.Player;
import com.example.deepseagame.R;

import java.util.List;

public class LeaderboardFragment extends Fragment {

    private LocToPlayerCallback locToPlayerCallback;
    private RecyclerView main_LST_scores;
    private TextView emptyStateTextView;
    private PlayerAdapter playerAdapter;
    private List<Player> playerList;

    public LeaderboardFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        findViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void findViews(View view) {
        main_LST_scores = view.findViewById(R.id.main_LST_scores);
        emptyStateTextView = view.findViewById(R.id.empty_state_text_view);
    }

    private void initViews() {
        if (playerList == null || playerList.isEmpty()) {
            showEmptyState();
        } else {
            hideEmptyState();
            playerAdapter = new PlayerAdapter(this.getContext(), playerList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
            main_LST_scores.setLayoutManager(linearLayoutManager);
            main_LST_scores.setAdapter(playerAdapter);
            playerAdapter.setPlayerCallback(new PlayerCallback() {
                @Override
                public void CardClicked(Player player, int position) {
                    itemClicked(player.getLat(), player.getLng());
                }
            });
        }
    }

    private void showEmptyState() {
        if (emptyStateTextView != null) {
            emptyStateTextView.setVisibility(View.VISIBLE);
        }
        if (main_LST_scores != null) {
            main_LST_scores.setVisibility(View.GONE);
        }
    }

    private void hideEmptyState() {
        if (emptyStateTextView != null) {
            emptyStateTextView.setVisibility(View.GONE);
        }
        if (main_LST_scores != null) {
            main_LST_scores.setVisibility(View.VISIBLE);
        }
    }

    private void itemClicked(double lat, double lon) {
        if (locToPlayerCallback != null) {
            locToPlayerCallback.scoreBoxClicked(lat, lon);
        }
    }

    public void setCallBackConnectLocationToPlayer(LocToPlayerCallback callBackConnectLocationToPlayer) {
        this.locToPlayerCallback = callBackConnectLocationToPlayer;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
        if (playerAdapter != null) {
            playerAdapter.updatePlayerList(this.playerList);
        }
        if (getView() != null) {
            initViews();
        }
    }
}
