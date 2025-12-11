package LLD.tictactoe.observer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import LLD.tictactoe.Game;

public class Scoreboard implements GameObserver{

    public final Map<String,Integer> scores;

    public Scoreboard()
    {
        this.scores=new ConcurrentHashMap<>();
    }

    public void update(Game game)
    {

        if(game.getWinner()!=null)
        {
            String winnerName=game.getWinner().getName();
            scores.putIfAbsent(winnerName, 0);
            scores.put(winnerName, scores.get(winnerName)+1);
             System.out.printf("[Scoreboard] %s wins! Their new score is %d.%n", winnerName, scores.get(winnerName));
        }
        
    }
     public void printScores() {
        System.out.println("\n--- Overall Scoreboard ---");
        if (scores.isEmpty()) {
            System.out.println("No games with a winner have been played yet.");
            return;
        }
        scores.forEach((playerName, score) ->
                System.out.printf("Player: %-10s | Wins: %d%n", playerName, score)
        );
        System.out.println("--------------------------\n");
    }

    

}
