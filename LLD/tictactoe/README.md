# Tic Tac Toe – Low Level Design

This module implements a console-based Tic Tac Toe using classic OO patterns to keep responsibilities clear, make the game extensible, and demonstrate common LLD techniques.

## Key Design Goals
- Separate game orchestration from UI/demo.
- Encapsulate board state and moves.
- Support pluggable win-detection logic.
- Track game lifecycle (in progress, win, draw) with explicit states.
- Notify observers (e.g., scoreboard) when a game finishes.

## Core Classes and Responsibilities
- `TicTacToeDemo`: Sample runner that creates players, starts three games, and demonstrates win/draw flows.
- `TicTacToeSystem` (Singleton):
  - One shared facade per process (`getInstance`).
  - Creates a new `Game`, attaches observers, routes `makeMove` calls, prints board/status.
- `Game`:
  - Owns `Board`, `Player` instances, current player pointer, winner, status, and active `GameState`.
  - Delegates move handling to the current `GameState`.
  - Uses `WinningStrategy` list to detect victories; notifies observers when the status changes to a terminal state.
- Model:
  - `Board`: 2D grid of `Cell`, move count, bounds and occupancy validation, pretty print.
  - `Cell`: Holds a `Symbol` (`X`, `O`, or `EMPTY`). Note: `setSymbol` currently returns the input rather than mutating (a potential fix area).
  - `Player`: Name + `Symbol`.
- Enums:
  - `Symbol`: X/O/EMPTY with printable char.
  - `GameStatus`: IN_PROGRESS, DRAW, WINNER_X, WINNER_O.
- Exception:
  - `InvalidMoveException`: Thrown for out-of-turn play, out-of-bounds, or occupied cells.

## Patterns Used
- **Singleton**: `TicTacToeSystem` ensures one orchestrator/scoreboard holder.
- **State**: `GameState` interface with concrete `InProgressState`, `WinnerState`, `DrawState`:
  - `InProgressState.handleMove`: validates turn, places symbol, checks win/draw, transitions to `WinnerState`/`DrawState`, switches player otherwise.
  - Terminal states throw `InvalidMoveException` to block further moves.
- **Strategy**: `WinningStrategy` interface with `RowWinningStrategy`, `ColumnWinningStrategy`, `DiagnolWinningStrategy` injected as a list in `Game` constructor. Each strategy inspects `Board` to decide a win; ORed together for final verdict.
- **Observer**: `GameSubject` maintains observers; `Scoreboard` implements `GameObserver` and is attached to each new `Game`. When `GameStatus` transitions away from `IN_PROGRESS`, `notifyObservers` is invoked and the scoreboard increments the winner’s tally.

## Game Flow
1. Demo creates players and calls `TicTacToeSystem.createGame(p1, p2)`.
2. System builds a new `Game`, attaches `Scoreboard`, prints a start message.
3. Each `makeMove(player, row, col)` is delegated to `GameState.handleMove`.
4. `Board.placeSymbol` validates bounds/occupancy and records the symbol.
5. `Game.checkWinner` applies each `WinningStrategy`; on win, `winner` is set and status updated. On full board with no win, status becomes DRAW. Otherwise `switchPlayer` advances the turn.
6. Status changes to WINNER_* or DRAW trigger observer notification; `Scoreboard.update` increments the winner’s count and prints it.

## Relationships at a Glance
- `TicTacToeDemo` → `TicTacToeSystem` (facade/singleton).
- `TicTacToeSystem` → `Game` (composition per match) and → `Scoreboard` (shared observer).
- `Game` → `Board`, `Player`(x2), `GameState`, `List<WinningStrategy>`, `GameSubject` (observer base).
- `Board` → `Cell`[][]; `Cell` → `Symbol`.
- `GameState` implementations drive `Game` transitions; `WinningStrategy` implementations read `Board`.
- `Scoreboard` observes `Game` terminations; stores win counts in a thread-safe map.

## Notable Behaviors / Edge Cases
- Turn enforcement: `InProgressState` throws if the wrong player acts.
- Move validation: out-of-bounds or occupied cells throw `InvalidMoveException`.
- Terminal states reject further moves via exceptions.
- Board printing shows a 3x3 grid with separators.
- Demo uses a fixed sequence of moves to produce X win, O win, then draw.

## Extensibility Ideas
- Support variable board sizes by parameterizing `Game`/`Board` and adjusting strategies.
- Add new `WinningStrategy` implementations (e.g., custom patterns).
- Improve `Cell` mutability (`setSymbol` should mutate the cell).
- Add AI or different UIs; observer can feed GUIs or logs instead of console.
- Persist or reset `Scoreboard` between sessions.

## Running the Demo
Compile and run `TicTacToeDemo` (package `LLD.tictactoe`). The console will show three games and the scoreboard output after wins.

