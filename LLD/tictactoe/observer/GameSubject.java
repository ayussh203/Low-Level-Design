package LLD.tictactoe.observer;

import java.util.ArrayList;
import java.util.List;

public class GameSubject {
    public final List<GameObserver> observers =new ArrayList<>();

    public void addObserver(GameObserver observer){
        observers.add(observer);
    }
    public void removeObserver(GameObserver observer){
        observers.remove(observer);
    }
    public void notifyObservers(LLD.tictactoe.Game game){
        for(GameObserver observer:observers){
            observer.update(game);
        }
    }
}
