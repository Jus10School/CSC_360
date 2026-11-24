package com.justinhollis;

import java.util.List;

public interface ResultRepository {
    void saveResult(GameResult result);

    List<GameResult> getAllResults();
}
