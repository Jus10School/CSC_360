package com.justinhollis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SequenceMemoryGame implements BrainGame {
    private List<Integer> sequence;
    private int level = 1;
    private long startedAt;
    private boolean complete;
    private boolean correct;

    @Override
    public String getGameName() {
        return "Sequence Memory";
    }

    @Override
    public void startGame() {
        if (complete || sequence != null) {
            throw new IllegalStateException("Answer the current round before starting another.");
        }
        sequence = new ArrayList<>();
        for (int i = 0; i < level + 2; i++) {
            sequence.add(ThreadLocalRandom.current().nextInt(10));
        }
        if (startedAt == 0L) {
            startedAt = System.nanoTime();
        }
        System.out.println("Memorize the numbers in order:");
        try {
            for (int number : sequence) {
                System.out.println(number);
                System.out.flush();
                Thread.sleep(1000L);
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Sequence memory game was interrupted.", exception);
        }
        System.out.print("\n".repeat(20));
    }

    public boolean checkSequence(List<Integer> answer) {
        if (sequence == null || complete) {
            throw new IllegalStateException("The game is not accepting an answer.");
        }
        if (answer == null) {
            throw new IllegalArgumentException("Answer cannot be null.");
        }
        correct = sequence.equals(answer);
        if (correct) {
            level++;
            sequence = null;
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
