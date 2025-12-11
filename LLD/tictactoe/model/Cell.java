package LLD.tictactoe.model;

import LLD.tictactoe.enums.Symbol;

public class Cell {
 private final Symbol symbol;
    public Cell() {
        this.symbol = Symbol.EMPTY;
    }
    public Symbol getSymbol() {
        return symbol;
    }
    public Symbol setSymbol(Symbol symbol) {
        return symbol;
    }   
}
