package com.justinhollis;

import java.util.List;

public class StatisticsService {
    private final ResultRepository resultRepository;

    public StatisticsService(ResultRepository resultRepository) {
        if (resultRepository == null) {
            throw new IllegalArgumentException("Result repository cannot be null.");
        }
        this.resultRepository = resultRepository;
    }

    public double calculateAverageScore(String gameName) {
        List<GameResult> results = resultRepository.getAllResults();
        double total = 0.0;
        int count = 0;
        for (GameResult result : results) {
            if (result.getGameName().equals(gameName)) {
                total += result.getScore();
                count++;
            }
        }
        return count == 0 ? 0.0 : total / count;
    }

    public GameResult getPersonalBest(String gameName) {
        GameResult best = null;
        for (GameResult result : resultRepository.getAllResults()) {
            if (!result.getGameName().equals(gameName)) {
                continue;
            }
            boolean lowerIsBetter = "Reaction Time".equals(gameName);
            if (best == null || (lowerIsBetter
                    ? result.getScore() < best.getScore()
                    : result.getScore() > best.getScore())) {
                best = result;
            }
        }
        return best;
    }

    public int getTotalGamesPlayed() {
        return resultRepository.getAllResults().size();
    }
}
