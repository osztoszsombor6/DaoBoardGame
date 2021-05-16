package boardgame.model;

/**
 * Class representing a position on a 4 squares wide and 4 squares wide gridded board.
 */
public class BoardPosition {
    /**
    * Index of the row, starting from zero.
    */
    public int xPos;
    
    /**
    * Index of the column, starting from zero.
    */
    public int yPos;
    
    /**
     * Creates a {@code BoardPosition} object that is initialized with the given coordinates.
     *
     * @param x the index of the row, starting from zero
     * @param y the index of the column, starting from zero
     * @throws IllegalArgumentException if the coordinates do not represent a valid board position.
     */
    public BoardPosition(int x, int y){
        if(x < 0 || x > 3 || y < 0 || y > 3){
            throw new IllegalArgumentException();
        }
        xPos = x;
        yPos = y;
    }
}
