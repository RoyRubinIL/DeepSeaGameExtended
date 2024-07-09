package com.example.deepseagame.Logic;

import java.util.Random;

public class GameManager {

    public static final int JELLYFISH = 2;
    public static final int WORM = 3;

    private int lives;
    private int score;
    private int[][] gameMatrix;
    private static final int NUM_LANES = 5;
    private static final int NUM_ROWS = 10;
    private static final int MAX_LIVES = 4;
    private int fishLane = (NUM_LANES / 2);

    public GameManager(int initialLives) {
        if (initialLives > 0 && initialLives <= MAX_LIVES) {
            lives = initialLives;
        } else {
            lives = 3;
        }
        score = 0;
        gameMatrix = new int[NUM_ROWS][NUM_LANES];
        initializeGameMatrix();
    }

    public int getNumLanes() {
        return NUM_LANES;
    }

    public int getNumRows() {
        return NUM_ROWS;
    }

    public int getMaxLives() {
        return MAX_LIVES;
    }

    public void decrementLives() {
        if (lives > 0) {
            lives--;
        }
    }

    public int getFishLane() {
        return fishLane;
    }

    public void initializeGameMatrix() {
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_LANES; j++) {
                gameMatrix[i][j] = 0;
            }
        }
        gameMatrix[NUM_ROWS - 1][fishLane] = 1;
    }

    public void generateGameObjects() {
        Random rand = new Random();
        int lane = rand.nextInt(NUM_LANES);
        int objectType = rand.nextBoolean() ? JELLYFISH : WORM;
        gameMatrix[0][lane] = objectType;
    }

    public void updateGameMatrix() {
        for (int i = NUM_ROWS - 1; i >= 0; i--) {
            for (int j = 0; j < NUM_LANES; j++) {
                if (gameMatrix[i][j] == JELLYFISH || gameMatrix[i][j] == WORM) {
                    if (i < NUM_ROWS - 2) {
                        if (gameMatrix[i + 1][j] == 0) {
                            gameMatrix[i + 1][j] = gameMatrix[i][j];
                            gameMatrix[i][j] = 0;
                        }
                    } else {
                        gameMatrix[i][j] = 0;
                    }
                }
            }
        }
    }

    public int getValueInMatrix(int i, int j) {
        return gameMatrix[i][j];
    }

    public boolean checkCollision() {
        return gameMatrix[NUM_ROWS - 2][fishLane] == JELLYFISH;
    }

    public boolean checkWormCollision(){
        return gameMatrix[NUM_ROWS - 2][fishLane] == WORM;
    }

    public void moveFishLeft() {
        if (fishLane > 0) {
            gameMatrix[NUM_ROWS - 1][fishLane] = 0;
            fishLane--;
            gameMatrix[NUM_ROWS - 1][fishLane] = 1;
        }
    }

    public void moveFishRight() {
        if (fishLane < NUM_LANES - 1) {
            gameMatrix[NUM_ROWS - 1][fishLane] = 0;
            fishLane++;
            gameMatrix[NUM_ROWS - 1][fishLane] = 1;
        }
    }

    public void resetLives() {
        lives = 3;
    }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


}
