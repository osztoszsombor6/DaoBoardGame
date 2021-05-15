package boardgame.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

public class BoardGameModel {

    public static int BOARD_SIZE = 4;

    private ReadOnlyObjectWrapper<Square>[][] board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];
    
    private Square currentPlayer = Square.PLAYER1;
    
    public Square getCurrentPlayer(){
        return this.currentPlayer;
    }

    public BoardGameModel() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Square e = Square.NONE;
                if (i == j) {
                    e = Square.PLAYER1;
                } else {
                    if (i == BOARD_SIZE - j - 1) {
                        e = Square.PLAYER2;
                    }
                }
                board[i][j] = new ReadOnlyObjectWrapper<Square>(e);
            }
        }
    }

    public ReadOnlyObjectProperty<Square> squareProperty(int i, int j) {
        return board[i][j].getReadOnlyProperty();
    }

    public Square getSquare(int i, int j) {
        return board[i][j].get();
    }

    public void move(int i, int j) {
        board[i][j].set(                
                switch (board[i][j].get()) {
                    case NONE -> Square.PLAYER1;
                    case PLAYER1 -> Square.PLAYER2;
                    case PLAYER2 -> Square.NONE;
                }
        );
    }
    
    public boolean lephet(int honnanX, int honnanY, int hovaX, int hovaY) {
        
        if (honnanX < 0 || honnanY < 0) {
            return false;
        }
        if(board[honnanX][honnanY].get() != currentPlayer){
            return false;
        }

        if(honnanX == hovaX && honnanY == hovaY){
            return false;
        }
        //System.out.print("  " + honnanX + " " + honnanY + " " + hovaX + " " + hovaY);
        if (honnanY == hovaY ) {
            if(honnanX < hovaX){
                for(int i = honnanX + 1; i <= hovaX; ++i){
                    if(getSquare(i, hovaY) != Square.NONE){
                        return false;
                    }
                }
                return true;
            } else {
                for(int i = honnanX - 1; i >= hovaX; --i){
                    if(getSquare(i, hovaY) != Square.NONE){
                        return false;
                    }
                }
                return true;
            }
        }
        if (honnanX == hovaX){
            if(honnanY < hovaY){
                for(int i = honnanY + 1; i <= hovaY; ++i){
                    if(getSquare(hovaX, i) != Square.NONE){
                        return false;
                    }
                }
                return true;
            } else {
                for(int i = honnanY - 1; i >= hovaY; --i){
                    if(getSquare(hovaX, i) != Square.NONE){
                        return false;
                    }
                }
                return true;
            }
        }
        if(hovaX-honnanX == hovaY-honnanY){
            if(hovaX > honnanX){
                for(int i = honnanX + 1; i <= hovaX; ++i){
                    if(getSquare(i, honnanY + (i-honnanX)) != Square.NONE){
                        return false;
                    }
                }
                return true;
            } else {
                for(int i = honnanX - 1; i >= hovaX; --i){
                    if(getSquare(i, honnanY + (i-honnanX)) != Square.NONE){
                        return false;
                    }
                }
                return true;
            }
        }
        if(hovaX-honnanX == -(hovaY-honnanY)){
            if(hovaX > honnanX){
                for(int i = honnanX + 1; i <= hovaX; ++i){
                    if(getSquare(i, honnanY - (i-honnanX)) != Square.NONE){
                        return false;
                    }
                }
                return true;
            } else {
                for(int i = honnanX - 1; i >= hovaX; --i){
                    if(getSquare(i, honnanY - (i-honnanX)) != Square.NONE){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    public void lep(int honnanX, int honnanY, int hovaX, int hovaY){
        board[hovaX][hovaY].set(board[honnanX][honnanY].get());
        board[honnanX][honnanY].set(Square.NONE);
        if(currentPlayer == Square.PLAYER1){
            currentPlayer = Square.PLAYER2;
        } else {
            currentPlayer = Square.PLAYER1;
        }
    }
    
    public boolean isEndState2x2(){
        Square otherPlayer;
        if(currentPlayer == Square.PLAYER1){
            otherPlayer = Square.PLAYER2;
        } else {
            otherPlayer = Square.PLAYER1;
        }
        for (int i = 0; i < BOARD_SIZE - 1; i++) {
            for (int j = 0; j < BOARD_SIZE -1; j++) {
                if(getSquare(i, j) == otherPlayer){
                    if(getSquare(i+1, j) == otherPlayer
                            && getSquare(i, j+1) == otherPlayer
                            && getSquare(i+1, j+1) == otherPlayer){
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean isEndStateCorners(){
        Square otherPlayer;
        if(currentPlayer == Square.PLAYER1){
            otherPlayer = Square.PLAYER2;
        } else {
            otherPlayer = Square.PLAYER1;
        }
        if(getSquare(0, 0) == otherPlayer
                && getSquare(0, 3) == otherPlayer
                && getSquare(3, 0) == otherPlayer
                && getSquare(3, 3) == otherPlayer){
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isEndStateCol(){
        Square otherPlayer;
        if(currentPlayer == Square.PLAYER1){
            otherPlayer = Square.PLAYER2;
        } else {
            otherPlayer = Square.PLAYER1;
        }
        for(int i = 0; i < BOARD_SIZE; ++i){
            if(getSquare(0, i) == otherPlayer){
                for(int j = 1; j < BOARD_SIZE; ++j){
                    if(getSquare(j, i) != otherPlayer){
                        return false;
                    } 
                }
                return true;
            }
        }
        return false;
    }
    
    public boolean isEndStateRow(){
        Square otherPlayer;
        if(currentPlayer == Square.PLAYER1){
            otherPlayer = Square.PLAYER2;
        } else {
            otherPlayer = Square.PLAYER1;
        }
        for(int i = 0; i < BOARD_SIZE; ++i){
            if(getSquare(i, 0) == otherPlayer){
                for(int j = 1; j < BOARD_SIZE; ++j){
                    if(getSquare(i, j) != otherPlayer){
                        return false;
                    } 
                }
                return true;
            }
        }
        return false;
    }
    
    public boolean isEndStateCornered(){
        Square otherPlayer;
        if(currentPlayer == Square.PLAYER1){
            otherPlayer = Square.PLAYER2;
        } else {
            otherPlayer = Square.PLAYER1;
        }
        
        if(getSquare(0, 0) == currentPlayer){
            if(getSquare(0, 1) == otherPlayer
                    && getSquare(1, 0) == otherPlayer
                    && getSquare(1, 1) == otherPlayer){
                return true;
            }
        }
        if(getSquare(3, 0) == currentPlayer){
            if(getSquare(2, 0) == otherPlayer
                    && getSquare(3, 1) == otherPlayer
                    && getSquare(2, 1) == otherPlayer){
                return true;
            }
        }
        if(getSquare(0, 3) == currentPlayer){
            if(getSquare(0, 2) == otherPlayer
                    && getSquare(1, 3) == otherPlayer
                    && getSquare(1, 2) == otherPlayer){
                return true;
            }
        }
        if(getSquare(3, 3) == currentPlayer){
            if(getSquare(3, 2) == otherPlayer
                    && getSquare(2, 3) == otherPlayer
                    && getSquare(2, 2) == otherPlayer){
                return true;
            }
        }
        return false;
    }
    
    public Square getWinner(){
        Square otherPlayer;
        if(currentPlayer == Square.PLAYER1){
            otherPlayer = Square.PLAYER2;
        } else {
            otherPlayer = Square.PLAYER1;
        }
        
        if(isEndStateCornered()){
            return currentPlayer;
        } else {
            return otherPlayer;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                sb.append(board[i][j].get().ordinal()).append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        var model = new BoardGameModel();
        System.out.println(model);
    }

}
