package LLD.tictactoe.state;

import LLD.tictactoe.Game;
import LLD.tictactoe.exception.InvalidMoveException;
import LLD.tictactoe.model.Player;

public class WinnerState implements GameState {

    public void handleMove(Game game,Player player,int row,int col) {
        // Implementation for handling a move in the Winner state
          throw new InvalidMoveException("Game is already over. " + game.getWinner().getName() + " has won.");
    }
    
}
