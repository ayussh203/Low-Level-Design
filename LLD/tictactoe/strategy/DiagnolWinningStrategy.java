package LLD.tictactoe.strategy;

import LLD.tictactoe.model.Board;
import LLD.tictactoe.model.Player;

public class DiagnolWinningStrategy implements WinningStrategy {

    public
    boolean checkWinner(Board board,Player player)
    {
        boolean leftDiagonalWin=true;
        boolean rightDiagonalWin=true;
        int size=board.getSize();
        for(int i=0;i<size;i++)
        {
            if(board.getCell(i, i).getSymbol()!=player.getSymbol())
            {
                leftDiagonalWin=false;
            }
            if(board.getCell(i, size-i-1).getSymbol()!=player.getSymbol())
            {
                rightDiagonalWin=false;
            }
        }
        return leftDiagonalWin || rightDiagonalWin;
    }
    
}
