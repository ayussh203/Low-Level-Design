package LLD.SnakeAndLadder.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private final int size;
    private final Map<Integer,Integer> SnakeAndLadder;

    public Board(int size, List<BoardEntity> entities ){
        this.size = size;
        SnakeAndLadder = new HashMap<>();
        for(BoardEntity entity: entities)
        {
            SnakeAndLadder.put(entity.getStart(),entity.getEnd());
        }


    }
    public int getSize() {
        return size;
    }
    public int getFinalPosition(int position)
    {
            return SnakeAndLadder.getOrDefault(position,position);
    }

    
}
