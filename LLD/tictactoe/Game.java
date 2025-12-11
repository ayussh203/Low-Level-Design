package LLD.tictactoe;

import java.util.List;

import LLD.tictactoe.enums.GameStatus;
import LLD.tictactoe.model.Board;
import LLD.tictactoe.model.Player;
import LLD.tictactoe.observer.GameSubject;
import LLD.tictactoe.state.GameState;
import LLD.tictactoe.state.InProgressState;
import LLD.tictactoe.strategy.ColumnWinningStrategy;
import LLD.tictactoe.strategy.DiagnolWinningStrategy;
import LLD.tictactoe.strategy.RowWinningStrategy;
import LLD.tictactoe.strategy.WinningStrategy;

public class Game extends GameSubject {

    private final Board board;
    private final Player player1;
    private final Player player2;
    private Player currentPlayer;
    private Player winner;
    private GameState gameState;
    private GameStatus gameStatus;
    private List<WinningStrategy> winningStrategies;

    public Game( Player player1, Player player2) {
        this.board = new Board(3);
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
        this.winningStrategies = winningStrategies;
        this.gameStatus = GameStatus.IN_PROGRESS;
    this.gameState=new InProgressState();
     this.winningStrategies = List.of(
                new RowWinningStrategy(),
                new ColumnWinningStrategy(),
                new DiagnolWinningStrategy()
        );
    }
 public void makeMove(Player player, int row, int col) {
        gameState.handleMove(this, player, row, col);
    }

    public boolean checkWinner(Player player) {
        for (WinningStrategy strategy : winningStrategies) {
            if (strategy.checkWinner(board, player)) {
                return true;
            }
        }
        return false;
    }

    public void switchPlayer() {
        this.currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    public Board getBoard() { return board; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public Player getWinner() { return winner; }
    public void setWinner(Player winner) { this.winner = winner; }
    public GameStatus getStatus() { return gameStatus; }
    public void setState(GameState state) { this.gameState = state; }
    public void setStatus(GameStatus status) {
        this.gameStatus = status;
        // Notify observers when the status changes to a finished state
        if (status != GameStatus.IN_PROGRESS) {
            notifyObservers(Game.this);
        }
    }
}

    
    
    
    
    

