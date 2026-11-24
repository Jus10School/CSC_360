package com.justinhollis;

public interface BrainGame {
    String getGameName();

    void startGame();

    boolean isGameComplete();

    GameResult getResult();
}
