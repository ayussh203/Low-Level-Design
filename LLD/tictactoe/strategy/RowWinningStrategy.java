package LLD.tictactoe.strategy;

import LLD.tictactoe.model.Board;
import LLD.tictactoe.model.Player;

public class RowWinningStrategy  implements WinningStrategy{

    @Override
    public boolean checkWinner(Board board,Player player){
        int size=board.getSize();
        for(int i=0;i<size;i++){
            boolean win=true;
            for(int j=0;j<size;j++){
                if(board.getCell(i,j).getSymbol()!=player.getSymbol()){
                    win=false;
                    break;
                }
            }
            if(win){
                return true;
            }
        }
        return false;
    }
    
}
