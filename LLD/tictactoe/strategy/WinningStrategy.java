package LLD.tictactoe.strategy;

import LLD.tictactoe.model.Board;
import LLD.tictactoe.model.Player;

public interface WinningStrategy {
    boolean checkWinner(Board board,Player player);
}
