package LLD.tictactoe.state;

import LLD.tictactoe.Game;
import LLD.tictactoe.model.Player;

public interface GameState {
  void handleMove(Game game,Player player,int row,int col);
    
} 
