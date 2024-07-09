package com.example.deepseagame.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deepseagame.Interfaces.PlayerCallback;
import com.example.deepseagame.Models.Player;
import com.example.deepseagame.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private Context context;
    private List<Player> players;
    private PlayerCallback playerCallback;

    public PlayerAdapter(Context context, List<Player> players) {
        this.context = context;
        this.players = players;
    }

    public PlayerAdapter setPlayerCallback(PlayerCallback playerCallback) {
        this.playerCallback = playerCallback;
        return this;
    }

    @NonNull
    @Override
    public PlayerAdapter.PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_score, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerAdapter.PlayerViewHolder holder, int position) {
        Player player = getItem(position);
        holder.player_rank.setText("#" + (position + 1));
        holder.player_name.setText(player.getName());
        holder.player_score.setText(String.valueOf(player.getScore()));
    }

    @Override
    public int getItemCount() {
        return players == null ? 0 : players.size();
    }

    private Player getItem(int position) {
        return players.get(position);
    }

    public void updatePlayerList(List<Player> newPlayers) {
        this.players = newPlayers;
        notifyDataSetChanged();
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {
        private MaterialTextView player_rank;
        private MaterialTextView player_name;
        private MaterialTextView player_score;
        private ShapeableImageView player_location;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            player_rank = itemView.findViewById(R.id.player_rank);
            player_name = itemView.findViewById(R.id.player_name);
            player_score = itemView.findViewById(R.id.player_score);
            player_location = itemView.findViewById(R.id.player_location);
            player_location.setOnClickListener(v -> {
                if (playerCallback != null) {
                    playerCallback.CardClicked(getItem(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }
}
