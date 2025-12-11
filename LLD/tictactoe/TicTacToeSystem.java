package LLD.tictactoe;

import LLD.tictactoe.exception.InvalidMoveException;
import LLD.tictactoe.model.Player;
import LLD.tictactoe.observer.Scoreboard;

public class TicTacToeSystem {
    private static volatile TicTacToeSystem instance;
    private Game game;
    private final Scoreboard scoreboard;

    private TicTacToeSystem() {
        this.scoreboard = new Scoreboard();
    }
    public static synchronized TicTacToeSystem getInstance() {
        if (instance == null) {
            instance = new TicTacToeSystem();
        }
        return instance;
    }


    public void createGame(Player player1, Player player2)
    {
        this.game=new Game(player1, player2);
        this.game.addObserver(scoreboard);
         System.out.printf("Game started between %s (X) and %s (O).%n", player1.getName(), player2.getName());



    }

    public void makeMove(Player player,int row,int col)
    {
        if(this.game==null)
        {
            System.out.println("No active game. Please create a game first.");
            return;
        }
        try{
             System.out.printf("%s plays at (%d, %d)%n", player.getName(), row, col);
            game.makeMove(player, row, col);
            printBoard();
            System.out.println("Game Status: " + game.getStatus());
            if (game.getWinner() != null) {
                System.out.println("Winner: " + game.getWinner().getName());
            }
           
        }
        catch (InvalidMoveException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
     public void printBoard() {
        game.getBoard().printBoard();
    }

}
