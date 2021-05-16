package boardgame.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

/**
 * Class representing a possible state of a Dao match. 
 */
public class BoardGameModel {

    /**
    * The width and height of the board, given in number of squares.
    */
    public static int BOARD_SIZE = 4;

    /**
    * An array representing the current state of the board.
    */
    private ReadOnlyObjectWrapper<Square>[][] board = new ReadOnlyObjectWrapper[BOARD_SIZE][BOARD_SIZE];
    
    /**
    * The player who is next to make a move in the current state.
    */
    private Square currentPlayer;
    
    /**
     * {@return the player who can make a move in the state}
     */
    public Square getCurrentPlayer(){
        return this.currentPlayer;
    }

    /**
     * Creates a {@code BoardGameModel} object representing the initial state of the match.
     */
    public BoardGameModel() {
        
        this(new String[]
            {"1002",
             "0120",
             "0210",
             "2001"},
             Square.PLAYER1);
        
    }
    
    /**
     * Creates a {@code BoardGameModel} object that is initialised with the given array and first player.
     * @param boardrep an array of size 4&#xd7;4 representing the initial configuration of the tray
     * @param player the player who can make the next step
     * @throws IllegalArgumentException if the array does not represent a valid configuration of the tray
     * @throws IllegalArgumentException if the given player is invalid
     */
    public BoardGameModel(String[] boardrep, Square player){
        
        if(player == null || player == Square.NONE){
            throw new IllegalArgumentException();
        }
        if (!isValidTray(boardrep)) {
            throw new IllegalArgumentException();
        }
        
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Square element =
                    switch (boardrep[i].charAt(j)){
                        case '1' -> Square.PLAYER1;
                        case '2' -> Square.PLAYER2;
                        default -> Square.NONE;
                };
                board[i][j] = new ReadOnlyObjectWrapper<Square>(element);
            }
        }
        currentPlayer = player;
    }
    
     /**
      * Checks whether the given representation is a valid board state.
      * @param boardrep an array containing four Strings with four characters. Should contain four '1' characters
      * and four '2' characters representing the disks of the players. The other characters represent empty spaces.
      * {@return {@code true} if the given array is a valid board representation, {@code false} otherwise}
      */
    public boolean isValidTray(String[] boardrep){
        
        if(boardrep == null || boardrep.length != BOARD_SIZE){
            return false;
        }
        
        for(int i = 0; i < BOARD_SIZE; ++i){
            if (boardrep[i].length() != BOARD_SIZE){
                return false;
            }
        }
        
        int player1Count = 0;
        int player2Count = 0;
        
        for(int i = 0; i < BOARD_SIZE; ++i){
            for(int j = 0; j < BOARD_SIZE; ++j){
                if(boardrep[i].charAt(j) == '1'){
                    ++player1Count;
                } else if(boardrep[i].charAt(j) == '2'){
                    ++player2Count;
                }
            }
        }
        
        if(player1Count == 4 && player2Count == 4){
            return true;
        }
        return false;
    }

    public ReadOnlyObjectProperty<Square> squareProperty(int i, int j) {
        return board[i][j].getReadOnlyProperty();
    }

    /**
     * Gets the state of the square at the given coordinates.  
     * @param i the row index of the square
     * @param j the column index of the square
     * {@return the state of the square with the given coordinates}
     */
    public Square getSquare(int i, int j) {
        return board[i][j].get();
    }
    
    /**
     * Checks if a given disk can be moved to a given spot.
     * @param from the {@code BoardPosition} a disk may be moved from.
     * @param to the {@code BoardPosition} a disk may be moved to.
     * {@return {@code true} if moving a disk from the first position to the second position is a legal move, {@code false} otherwise.}
     */
    public boolean legalMove(BoardPosition from, BoardPosition to) {
        
        int fromX = from.xPos;
        int fromY = from.yPos;
        int toX = to.xPos;
        int toY = to.yPos;
        
        if(board[fromX][fromY].get() != currentPlayer){
            return false;
        }

        if(fromX == toX && fromY == toY){
            return false;
        }
        if (fromY == toY ) {
            if(fromX < toX){
                for(int i = fromX + 1; i <= toX; ++i){
                    if(getSquare(i, toY) != Square.NONE){
                        return false;
                    }
                }
                return true;
            } else {
                for(int i = fromX - 1; i >= toX; --i){
                    if(getSquare(i, toY) != Square.NONE){
                        return false;
                    }
                }
                return true;
            }
        }
        if (fromX == toX){
            if(fromY < toY){
                for(int i = fromY + 1; i <= toY; ++i){
                    if(getSquare(toX, i) != Square.NONE){
                        return false;
                    }
                }
                return true;
            } else {
                for(int i = fromY - 1; i >= toY; --i){
                    if(getSquare(toX, i) != Square.NONE){
                        return false;
                    }
                }
                return true;
            }
        }
        if(toX-fromX == toY-fromY){
            if(toX > fromX){
                for(int i = fromX + 1; i <= toX; ++i){
                    if(getSquare(i, fromY + (i-fromX)) != Square.NONE){
                        return false;
                    }
                }
                return true;
            } else {
                for(int i = fromX - 1; i >= toX; --i){
                    if(getSquare(i, fromY + (i-fromX)) != Square.NONE){
                        return false;
                    }
                }
                return true;
            }
        }
        if(toX-fromX == -(toY-fromY)){
            if(toX > fromX){
                for(int i = fromX + 1; i <= toX; ++i){
                    if(getSquare(i, fromY - (i-fromX)) != Square.NONE){
                        return false;
                    }
                }
                return true;
            } else {
                for(int i = fromX - 1; i >= toX; --i){
                    if(getSquare(i, fromY - (i-fromX)) != Square.NONE){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    /**
     * Moves the disk from the first position to the second position.
     * @param from the {@code BoardPosition} a disk is  moved from.
     * @param to the {@code BoardPosition} a disk is moved to.
     */
    public void move(BoardPosition from, BoardPosition to){
        
        int fromX = from.xPos;
        int fromY = from.yPos;
        int toX = to.xPos;
        int toY = to.yPos;
        
        board[toX][toY].set(board[fromX][fromY].get());
        board[fromX][fromY].set(Square.NONE);
        if(currentPlayer == Square.PLAYER1){
            currentPlayer = Square.PLAYER2;
        } else {
            currentPlayer = Square.PLAYER1;
        }
    }
    
    /**
     * {@return {@code true} if a player has won the game by placing their disks in a 2&#xd7;2 area {@code false} otherwise}
     */
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
    
    /**
     * {@return {@code true} if a player has won the game by placing their disks in
     * the four corners of the board {@code false} otherwise}
     */
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
    
    /**
     * {@return {@code true} if a player has won the game by placing their disks in a column {@code false} otherwise}
     */
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
    
    /**
     * {@return {@code true} if a player has won the game by placing their disks in a row {@code false} otherwise}
     */
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
    
    /**
     * {@return {@code true} if a player has lost the game by placing their disks 
     * around a disk in the corner belonging to the other player {@code false} otherwise}
     */
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
    
    /**
     * {@return the winner of the game, or {@code Square.NONE} if the game has not ended}
     */
    public Square getWinner(){
        if(!isEndState2x2() &&
                !isEndStateCol() &&
                !isEndStateRow() &&
                !isEndStateCornered() &&
                !isEndStateCorners()){
            return Square.NONE;
        }
            
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
    
    /**
     * {@return a {@code String} representation of the board}
     */
    @Override
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
