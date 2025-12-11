package LLD.tictactoe.state;

import LLD.tictactoe.Game;
import LLD.tictactoe.enums.GameStatus;
import LLD.tictactoe.enums.Symbol;
import LLD.tictactoe.exception.InvalidMoveException;
import LLD.tictactoe.model.Player;

public class InProgressState implements GameState {

    public void handleMove(Game game,Player player,int row,int col) {
        // Implementation for handling a move in the InProgress state
         if (game.getCurrentPlayer() != player) {
            throw new InvalidMoveException("Not your turn!");
        }

        // Place the piece on the board
        game.getBoard().placeSymbol(row, col, player.getSymbol());

        // Check for a winner or a draw
        if (game.checkWinner(player)) {
            game.setWinner(player);
            game.setStatus(player.getSymbol() == Symbol.X ? GameStatus.WINNER_X : GameStatus.WINNER_O);
            game.setState(new WinnerState());
        } else if (game.getBoard().isFull()) {
            game.setStatus(GameStatus.DRAW);
            game.setState(new DrawState());
        } else {
            // If the game is still in progress, switch players
            game.switchPlayer();
        }
    
    }
    
}
