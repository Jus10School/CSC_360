package com.justinhollis;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class FileResultRepository implements ResultRepository {
    private final String filePath;

    public FileResultRepository(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be empty.");
        }
        this.filePath = filePath;
    }

    @Override
    public void saveResult(GameResult result) {
        if (result == null) {
            throw new IllegalArgumentException("Result cannot be null.");
        }
        Path path = Paths.get(filePath);
        Path parent = path.toAbsolutePath().getParent();
        try {
            if (parent != null) {
                Files.createDirectories(parent);
            }
            String encodedName = Base64.getEncoder()
                    .encodeToString(result.getGameName().getBytes(StandardCharsets.UTF_8));
            String line = String.join("\t",
                    encodedName,
                    Double.toString(result.getScore()),
                    result.getScoreUnit(),
                    Long.toString(result.getDuration()),
                    result.getDatePlayed().toString()) + System.lineSeparator();
            Files.write(path, line.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException exception) {
            throw new UncheckedIOException("Could not save game result to " + filePath, exception);
        }
    }

    @Override
    public List<GameResult> getAllResults() {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }
        List<GameResult> results = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (int i = 0; i < lines.size(); i++) {
                results.add(parseResult(lines.get(i), i + 1));
            }
        } catch (IOException exception) {
            throw new UncheckedIOException("Could not read game results from " + filePath, exception);
        }
        return results;
    }

    private GameResult parseResult(String line, int lineNumber) {
        try {
            String[] fields = line.split("\t", -1);
            if (fields.length != 5) {
                throw new IllegalArgumentException("Expected five tab-separated fields.");
            }
            String gameName = new String(Base64.getDecoder().decode(fields[0]), StandardCharsets.UTF_8);
            double score = Double.parseDouble(fields[1]);
            String scoreUnit = fields[2];
            long duration = Long.parseLong(fields[3]);
            LocalDateTime datePlayed = LocalDateTime.parse(fields[4]);
            return new GameResult(gameName, score, scoreUnit, duration, datePlayed);
        } catch (RuntimeException exception) {
            throw new IllegalStateException(
                    "Invalid game result in " + filePath + " at line " + lineNumber, exception);
        }
    }
}
