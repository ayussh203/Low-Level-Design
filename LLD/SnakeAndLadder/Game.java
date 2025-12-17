package LLD.SnakeAndLadder;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import LLD.SnakeAndLadder.enums.GameStatus;
import LLD.SnakeAndLadder.models.Board;
import LLD.SnakeAndLadder.models.BoardEntity;
import LLD.SnakeAndLadder.models.Dice;
import LLD.SnakeAndLadder.models.Player;

public class Game {
    private final Board board;
    private final Queue<Player> players;
    private final Dice dice;
    private GameStatus gameStatus;
    private Player winner;

    private Game(Builder builder) {
        this.board = builder.board;
        this.players = builder.players;
        this.dice = builder.dice;
        this.gameStatus = GameStatus.NOT_STARTED;
    }
           
    public void play()
    {
        if(players.size()<2)
        {
                        System.out.println("Cannot start game. At least 2 players are required.");
                return;
        }
        this.gameStatus=GameStatus.RUNNING;
        while(gameStatus==GameStatus.RUNNING)
        {
            Player currPlayer=players.poll();
            takeTurn(currPlayer);
            if(gameStatus==GameStatus.RUNNING)
            {
                players.add(currPlayer);
            }


        }
        System.out.println("Game Over!");
                if (winner != null) {
            System.out.printf("The winner is %s!\n", winner.getName());
                }


    }
    private void takeTurn(Player player)
    {
        int roll = dice.roll();
                System.out.printf("\n%s's turn. Rolled a %d.\n", player.getName(), roll);

                int currentPosition=player.getPosition();
                int nextPosition=currentPosition+roll;
                        if (nextPosition > board.getSize()) {
            System.out.printf("Oops, %s needs to land exactly on %d. Turn skipped.\n", player.getName(), board.getSize());
            return;
                        }
                        if(nextPosition==board.getSize())
                        {
                            player.setPosition(nextPosition);
                            this.winner=player;
                            this.gameStatus=GameStatus.FINISHED;
                            System.out.printf("Congratulations %s! You've won the game!\n", player.getName());
                            return;
                        }
                        int finalPosition=board.getFinalPosition(nextPosition);
                        if(finalPosition>nextPosition)
                        {
                            System.out.printf("Yay! %s climbed a ladder from %d to %d.\n", player.getName(), nextPosition, finalPosition);
                        }
                        else if(finalPosition<nextPosition)
                        {
                            System.out.printf("Oh no! %s got bitten by a snake from %d to %d.\n", player.getName(), nextPosition, finalPosition);
                        }
                        else
                        {
                            System.out.printf("%s moved to %d.\n", player.getName(), finalPosition);
                        }
                        player.setPosition(finalPosition);
                        if(roll==6)
                        {
                            System.out.printf("%s rolled a 6 and gets an extra turn!\n", player.getName());
                            takeTurn(player);
                        }
    }
    public static class Builder {
        private Board board;
        private Queue<Player> players;
        private Dice dice;

        public Builder setBoard(int size, List<BoardEntity> boardEntities) {
            this.board = new Board(size, boardEntities);
            return this;
        }

        public Builder setPlayers(List<String> names) {
            this.players = new LinkedList<>();
            for (String name : names) {
                players.add(new Player(name));
            }
            return this;
        }

        public Builder setDice(Dice dice) {
            this.dice = dice;
            return this;
        }

        public Game build() {
            if (board == null || players == null || dice == null) {
                throw new IllegalStateException("Board, Players, and Dice must be set.");
            }
            return new Game(this);
        }
    }
}