package LLD.SnakeAndLadder.models;

public class Snake extends BoardEntity 
{
    public Snake(int start, int end) {
        super(start, end);
        if(start <= end) {
            throw new IllegalArgumentException("Invalid Snake positions: start should be greater than end.");
        }
    }
    
}
