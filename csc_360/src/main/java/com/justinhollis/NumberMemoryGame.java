package com.justinhollis;

import java.util.concurrent.ThreadLocalRandom;

public class NumberMemoryGame implements BrainGame {
    private String number;
    private int level = 1;
    private long startedAt;
    private boolean complete;
    private boolean correct;

    @Override
    public String getGameName() {
        return "Number Memory";
    }

    @Override
    public void startGame() {
        if (complete || (number != null && !number.isEmpty())) {
            throw new IllegalStateException("Answer the current round before starting another.");
        }
        StringBuilder digits = new StringBuilder(level + 2);
        for (int i = 0; i < level + 2; i++) {
            digits.append(ThreadLocalRandom.current().nextInt(10));
        }
        number = digits.toString();
        if (startedAt == 0L) {
            startedAt = System.nanoTime();
        }
        System.out.println("Memorize this number: " + number);
        try {
            Thread.sleep(2500L);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Number memory game was interrupted.", exception);
        }
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.print("Enter the number: ");
    }

    public boolean checkAnswer(String answer) {
        if (number == null || complete) {
            throw new IllegalStateException("The game is not accepting an answer.");
        }
        if (answer == null) {
            throw new IllegalArgumentException("Answer cannot be null.");
        }
        correct = number.equals(answer.trim());
        if (correct) {
            level++;
            number = null;
        } else {
            complete = true;
        }
        return correct;
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
        long score = Math.max(0, level - 1);
        return new GameResult(getGameName(), score, "points", elapsedMillis());
    }

    private long elapsedMillis() {
        return (System.nanoTime() - startedAt) / 1_000_000L;
    }
}
