package com.justinhollis;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ResultRepository repository = new FileResultRepository("brain-games-results.tsv");
        GameManager gameManager = new GameManager(repository);
        StatisticsService statistics = new StatisticsService(repository);

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Brain Training Application");
            System.out.println("Choose a game: 1) Reaction time  2) Number memory  3) Sequence memory");
            String choice = scanner.nextLine().trim();

            BrainGame game;
            switch (choice) {
                case "1":
                    game = new ReactionTimeGame();
                    break;
                case "2":
                    game = new NumberMemoryGame();
                    break;
                case "3":
                    game = new SequenceMemoryGame();
                    break;
                default:
                    System.out.println("Invalid game selection.");
                    return;
            }

            gameManager.startGame(game);
            if (game instanceof ReactionTimeGame) {
                scanner.nextLine();
                ((ReactionTimeGame) game).recordResponse();
            } else if (game instanceof NumberMemoryGame) {
                NumberMemoryGame numberGame = (NumberMemoryGame) game;
                while (!numberGame.isGameComplete()) {
                    if (!numberGame.checkAnswer(scanner.nextLine().trim())) {
                        break;
                    }
                    System.out.println("Correct! The next round is harder.");
                    numberGame.startGame();
                }
            } else if (game instanceof SequenceMemoryGame) {
                SequenceMemoryGame sequenceGame = (SequenceMemoryGame) game;
                while (!sequenceGame.isGameComplete()) {
                    if (!sequenceGame.checkSequence(readSequence(scanner))) {
                        break;
                    }
                    System.out.println("Correct! The next round is harder.");
                    sequenceGame.startGame();
                }
            }

            gameManager.endGame();
            GameResult result = game.getResult();
            System.out.printf("Result: %.0f %s%n", result.getScore(), result.getScoreUnit());
            System.out.printf("Average %s score: %.0f %s%n",
                    game.getGameName(),
                    statistics.calculateAverageScore(game.getGameName()),
                    result.getScoreUnit());
            GameResult personalBest = statistics.getPersonalBest(game.getGameName());
            if (personalBest != null) {
                System.out.printf("Personal best: %.0f %s%n",
                        personalBest.getScore(), personalBest.getScoreUnit());
            }
            System.out.printf("Games played: %d%n", statistics.getTotalGamesPlayed());
            System.out.printf("Results in game history: %d%n", gameManager.getGameHistory().size());
        }
    }

    private static List<Integer> readSequence(Scanner scanner) {
        while (true) {
            System.out.print("Enter the full sequence in order, separated by spaces: ");
            String[] values = scanner.nextLine().trim().split("\\s+");
            List<Integer> sequence = new ArrayList<>();
            try {
                for (String value : values) {
                    sequence.add(Integer.parseInt(value));
                }
                return sequence;
            } catch (NumberFormatException exception) {
                System.out.println("Please enter whole numbers separated by spaces.");
            }
        }
    }
}
