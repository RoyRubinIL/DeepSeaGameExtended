package com.example.deepseagame;

public class GameManager {

    private int lives;

    public GameManager(int initialLives) {
        if (initialLives > 0 && initialLives <= 4) {
            lives = initialLives;
        } else {
            lives = 3; // Default to 3 lives if the provided initialLives is out of range
        }
    }

    public void decrementLives() {
        if (lives > 0) {
            lives--;
        }
    }

    public void resetLives() {
        lives = 3;
    }

    public int getLives() {
        return lives;
    }
}
