package LLD.SnakeAndLadder.models;

public class Ladder extends BoardEntity 
{
    public Ladder(int start, int end) {
        super(start, end);
        if(start >= end) {
            throw new IllegalArgumentException("Invalid Ladder positions: start should be less than end.");
        }
    }
    
}
