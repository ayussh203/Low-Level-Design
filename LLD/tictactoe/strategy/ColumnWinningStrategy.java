package LLD.tictactoe.strategy;

import LLD.tictactoe.model.Board;
import LLD.tictactoe.model.Player;

public class ColumnWinningStrategy implements WinningStrategy {

    public boolean checkWinner(Board board,Player player)
    {
        for(int col=0;col<board.getSize();col++)
        {
            boolean win=true;
            for(int row=0;row<board.getSize();row++)
            {
                if(board.getCell(row, col).getSymbol()!=player.getSymbol())
                {
                    win=false;
                    break;
                }
            }
             if(win)
        {
            return true;
        }
        }
        return false;
       
    }
    
}
