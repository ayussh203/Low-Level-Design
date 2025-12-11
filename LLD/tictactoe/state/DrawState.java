package LLD.tictactoe.state;

import LLD.tictactoe.Game;
import LLD.tictactoe.exception.InvalidMoveException;
import LLD.tictactoe.model.Player;

public class DrawState implements GameState {
    public void handleMove(Game game,Player player,int row,int col) {
        // Implementation for h
        // andling a move in the Draw state
      throw new InvalidMoveException("Game is already over. It was a draw.");
    }
    
}
