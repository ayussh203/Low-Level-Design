## Snake and Ladder – Low Level Design

---

### 1. Problem Statement

- **Goal**: Model and implement the classic **Snake and Ladder** board game as a console-based, object-oriented Java program.
- **Key Requirements**:
  - Multiple players (2 or more).
  - A board with:
    - **Snakes**: moving a player **down** when they land on the snake’s head.
    - **Ladders**: moving a player **up** when they land at the ladder base.
  - Dice-based movement:
    - Each turn, a player rolls a dice and moves forward.
    - Optional rule: a roll of **6** gives an **extra turn** (implemented).
  - Winning condition:
    - First player to land **exactly** on the last cell (e.g. 100) wins.
  - Turn management:
    - Players take turns in a fixed cyclic order until someone wins.

This implementation focuses on **clean separation of concerns**, **testability**, and **extensibility** using a few simple patterns.

---

### 2. Design Overview

- **Core Components**:
  - **`Game`**:
    - Orchestrates gameplay (turns, dice rolls, winner detection).
    - Owns the `Board`, `Dice`, and player queue.
    - Exposes a high-level API: `play()`.
    - Constructed via a **Builder pattern** (`Game.Builder`).
  - **`Board`**:
    - Represents the game board with a fixed **size** and a mapping of **special cells** (snakes and ladders).
    - Given a position, can compute the **final position** (after applying any snake/ladder).
  - **`BoardEntity` hierarchy**:
    - Abstract representation of board elements with a `start` and `end`.
    - Concrete types:
      - `Snake`
      - `Ladder`
  - **`Player`**:
    - Holds the player’s `name` and current `position` on the board.
  - **`Dice`**:
    - Encapsulates random number generation between `minValue` and `maxValue`.
  - **`GameStatus` enum**:
    - Lifecycle of the game: `NOT_STARTED`, `RUNNING`, `FINISHED`.
  - **`SnakeAndLadderDemo`**:
    - Example usage / composition root:
      - Creates snakes, ladders, players, dice.
      - Builds and runs a game.

---

### 3. Class-by-Class Low-Level Design

#### 3.1 `BoardEntity` (Abstract Base)

```java
public abstract class BoardEntity {
    private final int start;
    private final int end;
    // getters...
}
```

- **Responsibility**:
  - Common abstraction for any entity that occupies a segment on the board.
  - Examples: `Snake`, `Ladder`.
- **Fields**:
  - **`start`**: the cell where the entity is triggered.
  - **`end`**: the cell where the player will end up if they land on `start`.
- **Why abstraction?**:
  - Both snakes and ladders share identical structural data (`start`, `end`).
  - Behavior differences are purely **validation logic** (up vs down).
  - Enables the `Board` to treat all entities uniformly as `BoardEntity`.

**Design pattern**:  
- This is a simple use of **inheritance** and the **Template Method idea** (base holds structure, subclasses constrain invariants).

---

#### 3.2 `Snake` and `Ladder`

```java
public class Snake extends BoardEntity {
    public Snake(int start, int end) {
        super(start, end);
        if (start <= end) {
            throw new IllegalArgumentException("Invalid Snake positions: start should be greater than end.");
        }
    }
}
```

```java
public class Ladder extends BoardEntity {
    public Ladder(int start, int end) {
        super(start, end);
        if (start >= end) {
            throw new IllegalArgumentException("Invalid Ladder positions: start should be less than end.");
        }
    }
}
```

- **Responsibilities**:
  - `Snake`: models a downward move (start > end).
  - `Ladder`: models an upward move (start < end).
- **Invariants enforced in constructors**:
  - `Snake`: `start > end`.
  - `Ladder`: `start < end`.
- **Why is this useful?**
  - Catches configuration errors **early** (at object creation).
  - Keeps `Board` free from special-case checks for snake vs ladder semantics.
  - If you add more entity types in future (e.g., `Boost`, `Trap`), you simply extend `BoardEntity`.

**Tradeoff**:
- Simple inheritance hierarchy (only 2 types) – easy to understand.
- Slight overhead of extra classes vs an enum + if/else, but much better **OO modeling** and **extensibility**.

---

#### 3.3 `Board`

```java
public class Board {
    private final int size;
    private final Map<Integer, Integer> SnakeAndLadder;

    public Board(int size, List<BoardEntity> entities) {
        this.size = size;
        SnakeAndLadder = new HashMap<>();
        for (BoardEntity entity : entities) {
            SnakeAndLadder.put(entity.getStart(), entity.getEnd());
        }
    }

    public int getSize() { ... }

    public int getFinalPosition(int position) {
        return SnakeAndLadder.getOrDefault(position, position);
    }
}
```

- **Responsibilities**:
  - Represents the playing surface with cells from `1` to `size` (e.g. 1 to 100).
  - Stores all snakes and ladders in a **single map**:
    - Key: start position.
    - Value: end position.
  - Answers the question: “If a player lands on cell X, where do they finally end up?”

- **Key methods**:
  - **`getSize()`**: used by `Game` to validate moves and winning condition.
  - **`getFinalPosition(int position)`**:
    - If the given position is the `start` of a snake or ladder, returns the corresponding `end`.
    - Otherwise returns the original position.

- **Why `Map<Integer, Integer>` instead of holding `BoardEntity` directly?**
  - For gameplay, only the mapping from start to end is needed.
  - Primitive mapping reduces indirection during heavy use.
  - The validation logic (snake vs ladder) is done in constructors, so by the time we get here, data is consistent.

**Complexity**:
- `getFinalPosition`: **O(1)** average-time lookup in the HashMap.

**Tradeoffs**:
- Simpler runtime logic vs richer modelling (we no longer know in `Board` whether a jump is a snake or a ladder—only that a jump exists).
  - The game infers snake vs ladder by comparing `finalPosition` vs `nextPosition`.

---

#### 3.4 `Player`

```java
public class Player {
    private final String name;
    private int position;
    // getters and setter...
}
```

- **Responsibilities**:
  - Holds **identity** (`name`) and **state** (`position`) of a player.
- **Fields**:
  - `name`: immutable string.
  - `position`: mutable; updated every turn.

- **Why this design?**
  - Keeps player-related data small and focused.
  - No coupling to game rules—`Game` owns rules, `Player` just owns state relevant to rules.

---

#### 3.5 `Dice`

```java
public class Dice {
    private final int minValue;
    private final int maxValue;

    public Dice(int minValue, int maxValue) { ... }

    public int roll() {
        return (int)(Math.random() * (maxValue - minValue + 1)) + minValue;
    }
}
```

- **Responsibilities**:
  - Encapsulate random number generation.
  - Allow configurable range (e.g., 1–6, 1–12).

- **Why not just use `Random` directly inside `Game`?**
  - **Single Responsibility**: `Game` focuses on orchestrating rules, not on how randomness is generated.
  - **Testability**: you can later inject a deterministic dice (e.g., stub or mock) for predictable tests.
  - **Extensibility**: if you add rules like “roll 2 dice” or special dice types, you extend/replace `Dice` without touching `Game`.

---

#### 3.6 `GameStatus` enum

```java
public enum GameStatus {
    NOT_STARTED,
    RUNNING,
    FINISHED;
}
```

- **Responsibilities**:
  - Represents the **lifecycle state** of a game.
- **Usage in `Game`**:
  - Initial state: `NOT_STARTED`.
  - When `play()` begins: `RUNNING`.
  - When a winner is found: `FINISHED`.

**Why an enum?**
- Type-safe representation of fixed finite states.
- Avoids using bare strings/ints which are error-prone and less readable.

---

#### 3.7 `Game`

```java
public class Game {
    private final Board board;
    private final Queue<Player> players;
    private final Dice dice;
    private GameStatus gameStatus;
    private Player winner;

    private Game(Builder builder) {
        this.board = builder.board;
        this.players = builder.players;
        this.dice = builder.dice;
        this.gameStatus = GameStatus.NOT_STARTED;
    }
    // play(), takeTurn(), Builder...
}
```

##### 3.7.1 Fields and Responsibilities

- **`board`**:
  - Game arena; used to check size and compute snake/ladder jumps.
- **`players`** (`Queue<Player>`):
  - Holds players in their turn order.
  - `poll()` + `add()` implements a **round-robin turn mechanism**.
- **`dice`**:
  - Supplies random moves each turn.
- **`gameStatus`**:
  - Tracks current state; controls the main loop.
- **`winner`**:
  - Set when someone reaches the final cell.

##### 3.7.2 `play()` Game Loop

```java
public void play() {
    if (players.size() < 2) {
        System.out.println("Cannot start game. At least 2 players are required.");
        return;
    }
    this.gameStatus = GameStatus.RUNNING;
    while (gameStatus == GameStatus.RUNNING) {
        Player currPlayer = players.poll();
        takeTurn(currPlayer);
        if (gameStatus == GameStatus.RUNNING) {
            players.add(currPlayer);
        }
    }
    System.out.println("Game Over!");
    if (winner != null) {
        System.out.printf("The winner is %s!\n", winner.getName());
    }
}
```

- **Flow**:
  1. Validate minimum players (>= 2).
  2. Set status to `RUNNING`.
  3. Main loop:
     - Dequeue current player.
     - Let them take a turn.
     - If the game is still running (no winner yet), put them back to the end of the queue.
  4. Once status becomes `FINISHED`, exit loop.
  5. Announce the winner, if any.

- **Patterns used**:
  - **Round-robin scheduling** using a `Queue`:
    - Elegant and simple turn rotation without indexing.

##### 3.7.3 `takeTurn(Player player)`

```java
private void takeTurn(Player player) {
    int roll = dice.roll();
    System.out.printf("\n%s's turn. Rolled a %d.\n", player.getName(), roll);

    int currentPosition = player.getPosition();
    int nextPosition = currentPosition + roll;

    if (nextPosition > board.getSize()) {
        System.out.printf("Oops, %s needs to land exactly on %d. Turn skipped.\n",
                          player.getName(), board.getSize());
        return;
    }

    if (nextPosition == board.getSize()) {
        player.setPosition(nextPosition);
        this.winner = player;
        this.gameStatus = GameStatus.FINISHED;
        System.out.printf("Congratulations %s! You've won the game!\n", player.getName());
        return;
    }

    int finalPosition = board.getFinalPosition(nextPosition);
    if (finalPosition > nextPosition) {
        System.out.printf("Yay! %s climbed a ladder from %d to %d.\n",
                          player.getName(), nextPosition, finalPosition);
    } else if (finalPosition < nextPosition) {
        System.out.printf("Oh no! %s got bitten by a snake from %d to %d.\n",
                          player.getName(), nextPosition, finalPosition);
    } else {
        System.out.printf("%s moved to %d.\n", player.getName(), finalPosition);
    }

    player.setPosition(finalPosition);

    if (roll == 6) {
        System.out.printf("%s rolled a 6 and gets an extra turn!\n", player.getName());
        takeTurn(player);
    }
}
```

- **Rules encoded**:
  - **Exact landing rule**:
    - If `nextPosition > board.getSize()`: the player overshoots → turn is skipped, position unchanged.
  - **Winning rule**:
    - If `nextPosition == board.getSize()`:
      - Player moved to that position, set as `winner`, game status set to `FINISHED`.
  - **Snake/Ladder application**:
    - Compute `finalPosition = board.getFinalPosition(nextPosition)`.
    - Compare `finalPosition` vs `nextPosition` to detect:
      - Ladder: `finalPosition > nextPosition`.
      - Snake: `finalPosition < nextPosition`.
      - Normal move: equal.
  - **Extra turn rule**:
    - If dice roll is `6`, recursively call `takeTurn(player)` to grant another move.

**Tradeoffs**:
- **Recursion for extra turns**:
  - Very readable for a small game.
  - Theoretically, many consecutive 6s could deepen recursion, but practically unlikely in this simple console app.
  - Could be refactored into a loop if stack-depth is a concern.

---

#### 3.8 `Game.Builder` (Builder Pattern)

```java
public static class Builder {
    private Board board;
    private Queue<Player> players;
    private Dice dice;

    public Builder setBoard(int size, List<BoardEntity> boardEntities) {
        this.board = new Board(size, boardEntities);
        return this;
    }

    public Builder setPlayers(List<String> names) {
        this.players = new LinkedList<>();
        for (String name : names) {
            players.add(new Player(name));
        }
        return this;
    }

    public Builder setDice(Dice dice) {
        this.dice = dice;
        return this;
    }

    public Game build() {
        if (board == null || players == null || dice == null) {
            throw new IllegalStateException("Board, Players, and Dice must be set.");
        }
        return new Game(this);
    }
}
```

- **Pattern**: Classic **Builder Pattern**.
- **Why Builder?**
  - `Game` requires multiple dependencies: `Board`, `Queue<Player>`, and `Dice`.
  - Using a telescoping constructor with many parameters would be error-prone and less readable.
  - Builder enables a **fluent**, self-documenting construction:

```java
Game game = new Game.Builder()
        .setBoard(100, boardEntities)
        .setPlayers(players)
        .setDice(new Dice(1, 6))
        .build();
```

- **Advantages**:
  - Order independence of configuration calls.
  - Easier to add more configuration options later without breaking existing code.
  - Centralized validation in `build()` ensures object integrity.

---

#### 3.9 `SnakeAndLadderDemo`

```java
public class SnakeAndLadderDemo {
    public static void main(String[] args) {
        List<BoardEntity> boardEntities = List.of(
                new Snake(17, 7), new Snake(54, 34),
                new Snake(62, 19), new Snake(98, 79),
                new Ladder(3, 38), new Ladder(24, 33),
                new Ladder(42, 93), new Ladder(72, 84)
        );

        List<String> players = Arrays.asList("Alice", "Bob", "Charlie");

        Game game = new Game.Builder()
                .setBoard(100, boardEntities)
                .setPlayers(players)
                .setDice(new Dice(1, 6))
                .build();

        game.play();
    }
}
```

- Demonstrates:
  - How to configure snakes and ladders.
  - How to build the game via the builder.
  - How to start the game by calling `play()`.

**Separation of concerns**:
- Keeps the creation/wiring code separate from the core game logic.
- In a real application, this could be replaced by a DI framework or UI/controller layer.

---

### 4. Patterns Used & How They Help

- **Builder Pattern (`Game.Builder`)**:
  - Solves the problem of complex object construction with multiple mandatory components.
  - Makes the code **readable** and **less error-prone**.

- **Inheritance / Polymorphism (`BoardEntity`, `Snake`, `Ladder`)**:
  - Models domain entities in a **natural OO way**.
  - Encapsulates validation in subclasses, keeping `Board` simple.
  - Easy to extend with new entity types without modifying board logic.

- **Enum for State (`GameStatus`)**:
  - Provides a type-safe, self-documenting way to handle game lifecycle.

- **Queue-based Turn Management (`Queue<Player>`)**:
  - Natural representation of **round-robin** player turns.
  - Cleaner than managing indices and wrap-around logic manually.

---

### 5. Design Tradeoffs and Discussion

- **Simplicity vs Flexibility**:
  - The current design is intentionally straightforward:
    - Single dice, single board, fixed rules.
  - It’s easy to read and suitable for interviews or teaching LLD.
  - For more complex variants (multiple boards, power-ups, different dice), you’d introduce more abstractions.

- **BoardEntity Storage as Map of Jumps**:
  - Storing only `(start -> end)` inside `Board` is efficient and simple.
  - Tradeoff: we lose direct knowledge of whether a given jump is a snake or ladder at the `Board` level, and re-derive it in `Game` using comparisons.

- **Recursion in `takeTurn` for extra rolls**:
  - Very readable and short.
  - Depth is limited by practical dice behavior, so acceptable here.
  - In a production environment or different language/runtime, you might prefer an iterative approach.

- **Thread Safety**:
  - This implementation is **not** designed for multi-threaded play:
    - All data structures are normal (non-concurrent) collections.
    - `Game` is meant to be used on a single thread (typical for console/UI games).
  - For concurrency (e.g., multiple remote players), you would:
    - Protect state with synchronization or locks.
    - Or design a message-driven / event-driven architecture.

- **I/O Coupling**:
  - `Game` directly prints to `System.out`.
  - This is fine for a demo, but couples logic to a specific I/O mechanism.
  - For higher testability:
    - You might inject a logger or observer to receive game events instead of printing directly.

---

### 6. Extensions and Future Enhancements

- **Configurable Rules**:
  - Pluggable rule objects (strategy pattern) for:
    - Extra-turn rules.
    - Win conditions.
    - Special tiles (e.g., lose a turn, move backwards).

- **Multiple Dice / Weighted Dice**:
  - Replace `Dice` with an interface (e.g., `RollStrategy`) for flexible probability distributions.

- **Game Observers / Event System**:
  - Introduce an observer pattern so UI or logging layers can subscribe to events:
    - Turn started, dice rolled, snake/ladder hit, winner declared.

- **Persistence / Replay**:
  - Add the ability to record moves (event sourcing) for replaying or analyzing the game.

---

### 7. Summary

- The Snake and Ladder implementation is structured around:
  - A **game orchestrator** (`Game`) with a clear `play()` loop.
  - A **board model** (`Board`) that encapsulates snakes and ladders via `BoardEntity` subclasses.
  - Simple, focused models: `Player`, `Dice`, `GameStatus`.
  - Usage of the **Builder pattern** for clean game construction and a **queue** for intuitive turn handling.
- The design balances **clarity**, **correctness**, and **extensibility**, making it ideal as a low-level design reference or interview solution.


