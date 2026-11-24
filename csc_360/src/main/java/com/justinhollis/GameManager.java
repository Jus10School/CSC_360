package com.justinhollis;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private BrainGame currentGame;
    private final List<GameResult> gameHistory;
    private final ResultRepository resultRepository;

    public GameManager(ResultRepository resultRepository) {
        if (resultRepository == null) {
            throw new IllegalArgumentException("Result repository cannot be null.");
        }
        this.resultRepository = resultRepository;
        this.gameHistory = new ArrayList<>(resultRepository.getAllResults());
    }

    public void startGame(BrainGame game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null.");
        }
        if (currentGame != null) {
            throw new IllegalStateException("The current game must end before another game starts.");
        }
        currentGame = game;
        try {
            currentGame.startGame();
        } catch (RuntimeException exception) {
            currentGame = null;
            throw exception;
        }
    }

    public void endGame() {
        if (currentGame == null) {
            throw new IllegalStateException("There is no active game.");
        }
        if (!currentGame.isGameComplete()) {
            throw new IllegalStateException("The current game is not complete.");
        }
        GameResult result = currentGame.getResult();
        resultRepository.saveResult(result);
        gameHistory.add(result);
        currentGame = null;
    }

    public List<GameResult> getGameHistory() {
        return new ArrayList<>(gameHistory);
    }
}
