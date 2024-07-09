package com.example.deepseagame.Utilities;

import android.util.Log;

import com.example.deepseagame.Models.Player;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DataManager {
    private ArrayList<Player> playerList = new ArrayList<>();
    private static DataManager instance = null;
    public static final String PLAYER_LIST ="PLAYER_LIST";

    public DataManager() {
        this.playerList = loadFromSharedPreferences();
        if(playerList == null) {
            this.playerList = new ArrayList<Player>();
        }
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public static DataManager getInstance() {
        if (instance == null)
            instance = new DataManager();
        return instance;
    }

    public DataManager setPlayerList(ArrayList<Player> playerList) {
        this.playerList = playerList;
        return this;
    }

    public void addPlayer(Player player) {
        this.playerList.add(player);
        sortByScore();
        if (this.playerList.size() > 10) {
            this.playerList = new ArrayList<>(this.playerList.subList(0, 10));
        }
        saveToSharedPreferences();
    }

    private void saveToSharedPreferences() {
        Gson gson = new Gson();
        String playerListJson = gson.toJson(this.playerList);
        MSP.getInstance().saveString(PLAYER_LIST, playerListJson);
    }

    public ArrayList<Player> loadFromSharedPreferences(){
        String playersJson = MSP.getInstance().readString(PLAYER_LIST, "");
        Log.d("rrr", playersJson);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Player>>(){}.getType();
        return gson.fromJson(playersJson, type);
    }

    @Override
    public String toString() {
        return "PlayerList{" +
                "playerArrayList=" + playerList +
                '}';
    }

    public void sortByScore() {
        // Sort the players list based on their scores
        Collections.sort(playerList, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                // Sort in descending order (highest score first)
                return Integer.compare(p2.getScore(), p1.getScore());
            }
        });
    }
}
