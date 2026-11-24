package com.justinhollis;

import java.util.concurrent.ThreadLocalRandom;

public class ReactionTimeGame implements BrainGame {
    private long reactionTime;
    private long startedAt;
    private boolean complete;

    @Override
    public String getGameName() {
        return "Reaction Time";
    }

    @Override
    public void startGame() {
        if (startedAt != 0L || complete) {
            throw new IllegalStateException("This game has already been started.");
        }
        System.out.println("Wait for the signal...");
        try {
            Thread.sleep(ThreadLocalRandom.current().nextLong(1500L, 3501L));
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Reaction game was interrupted.", exception);
        }
        startedAt = System.nanoTime();
        System.out.println("Press Enter now!");
    }

    public void recordResponse() {
        if (startedAt == 0L || complete) {
            throw new IllegalStateException("The game is not waiting for a response.");
        }
        reactionTime = (System.nanoTime() - startedAt) / 1_000_000L;
        complete = true;
    }

    @Override
    public boolean isGameComplete() {
        return complete;
    }

    @Override
    public GameResult getResult() {
        if (!complete) {
            throw new IllegalStateException("The game is not complete.");
        }
        return new GameResult(getGameName(), reactionTime, "ms", reactionTime);
    }
}
