package com.justinhollis;

import java.time.LocalDateTime;

public class GameResult {
    private final String gameName;
    private final double score;
    private final String scoreUnit;
    private final long duration;
    private final LocalDateTime datePlayed;

    public GameResult(String gameName, double score, String scoreUnit, long duration) {
        this(gameName, score, scoreUnit, duration, LocalDateTime.now());
    }

    GameResult(String gameName, double score, String scoreUnit, long duration, LocalDateTime datePlayed) {
        if (gameName == null || gameName.trim().isEmpty()) {
            throw new IllegalArgumentException("Game name cannot be empty.");
        }
        if (scoreUnit == null || scoreUnit.trim().isEmpty()) {
            throw new IllegalArgumentException("Score unit cannot be empty.");
        }
        if (!Double.isFinite(score) || duration < 0L || datePlayed == null) {
            throw new IllegalArgumentException("Score, duration, and date must be valid.");
        }
        this.gameName = gameName;
        this.score = score;
        this.scoreUnit = scoreUnit;
        this.duration = duration;
        this.datePlayed = datePlayed;
    }

    public String getGameName() {
        return gameName;
    }

    public double getScore() {
        return score;
    }

    public String getScoreUnit() {
        return scoreUnit;
    }

    public long getDuration() {
        return duration;
    }

    public LocalDateTime getDatePlayed() {
        return datePlayed;
    }
}
