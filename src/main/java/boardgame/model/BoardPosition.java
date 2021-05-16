package boardgame.model;


public class BoardPosition {
    public int xPos;
    public int yPos;
    
    public BoardPosition(int x, int y){
        if(x < 0 || x > 3 || y < 0 || y > 3){
            throw new IllegalArgumentException();
        }
        xPos = x;
        yPos = y;
    }
}
