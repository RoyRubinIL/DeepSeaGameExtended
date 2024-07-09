package com.example.deepseagame.Models;

public class Player {
    private int score = 0;
    private String name = "";
    private double lat;
    private double lng;


    public int getScore() {
        return score;
    }

    public Player setScore(int score) {
        this.score = score;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public Player setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLng() {
        return lng;
    }

    public Player setLng(double lon) {
        this.lng = lon;
        return this;
    }

    public String getName() {
        return name;
    }

    public Player setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString(){
        return "Player{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }


}
