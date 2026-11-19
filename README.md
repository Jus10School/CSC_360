# Brain Training Application

## Project Description

Brain Training Application is a GUI-based cognitive games program. It lets users choose from multiple short brain-training activities, play them through an interactive graphical interface, and review their performance over time.

The application includes the following games:

- **Reaction Time:** Measures how quickly a player responds after a visual prompt.
- **Number Memory:** Displays a number for the player to remember and challenges them to enter it correctly as the difficulty level increases.
- **Sequence Memory:** Presents a sequence of values and asks the player to repeat the sequence in the correct order.

During a game, the application tracks the player's score, score unit, duration, and date played. Results are stored locally and can be used to calculate average scores, personal bests, and the total number of games played.

## Application Architecture

The application is organized around a central `GameManager` that selects and runs the active game. All games implement the `BrainGame` interface, giving them a consistent way to start, detect completion, and return a `GameResult`.

The result system separates result storage from the rest of the application:

- `GameResult` represents the outcome of a game.
- `StatisticsService` reads stored results and calculates performance statistics.
- `ResultRepository` defines the operations for saving and retrieving results.
- `FileResultRepository` provides a file-based implementation for persistent local storage.

This structure allows the GUI and game implementations to remain independent from the underlying result-storage mechanism.

## Planned Features

- Graphical menu for selecting available games
- Interactive game screens with clear instructions and feedback
- Progressive difficulty for number and sequence memory games
- Reaction-time measurement for the response game
- Persistent game history
- Personal-best and average-score displays
- Total games-played tracking
