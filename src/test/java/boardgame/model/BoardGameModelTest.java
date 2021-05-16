package boardgame.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;


public class BoardGameModelTest {
    
    BoardGameModel baseBoard;
    
    public BoardGameModelTest() {

    }
    
   
    @BeforeEach
    public void setUp() {
        baseBoard = new BoardGameModel();
    }
    
    @Test
    void testTwoArgConstructor_shouldThrowException() {
        
        assertThrows(IllegalArgumentException.class, () -> new BoardGameModel(null,Square.PLAYER1));
        assertThrows(IllegalArgumentException.class, () -> new BoardGameModel(new String[]{
                "0000",
                "1122",
                "2211",
                "0000"},Square.NONE));
        assertThrows(IllegalArgumentException.class, () -> new BoardGameModel(new String[] {
                "1111",
                "2222"}, Square.PLAYER1));
        assertThrows(IllegalArgumentException.class, () -> new BoardGameModel(new String[] {
                "1111",
                "2222",
                "0000",
                "000"}, Square.PLAYER1));
        assertThrows(IllegalArgumentException.class, () -> new BoardGameModel(new String[] {
                "1110",
                "2220",
                "0001",
                "0000"}, Square.PLAYER1));
        
    }

    @Test
    public void testGetCurrentPlayer() {
        assertEquals(Square.PLAYER1, baseBoard.getCurrentPlayer());
        baseBoard.move(new BoardPosition(0, 0), new BoardPosition(0, 1));
        assertEquals(Square.PLAYER2, baseBoard.getCurrentPlayer());
    }

    @Test
    public void testSquareProperty() {
        assertEquals(Square.PLAYER1, baseBoard.getSquare(0, 0));
        assertEquals(Square.PLAYER2, baseBoard.getSquare(0, 3));
        assertEquals(Square.NONE, baseBoard.getSquare(1, 0));
    }


    @Test
    public void testGetSquare() {
        assertEquals(Square.PLAYER1, baseBoard.getSquare(0, 0));
        assertEquals(Square.PLAYER2, baseBoard.getSquare(0, 3));
        assertEquals(Square.NONE, baseBoard.getSquare(1, 0));
    }

    
    @Test
    public void testLegalMove() {
        BoardGameModel testBoard = new BoardGameModel(
                new String[]{"0000",
                             "1122",
                             "2211",
                             "0000"},
                             Square.PLAYER1);
        
        //wrong player
        assertEquals(false, baseBoard.legalMove(new BoardPosition(0, 3), new BoardPosition(0, 2)));
        //no movement not allowed
        assertEquals(false, baseBoard.legalMove(new BoardPosition(0, 0), new BoardPosition(0, 0)));
        //moving right
        assertEquals(true, baseBoard.legalMove(new BoardPosition(0, 0), new BoardPosition(0, 2)));
        assertEquals(false, baseBoard.legalMove(new BoardPosition(0, 0), new BoardPosition(0, 3)));
        //moving left
        assertEquals(true, baseBoard.legalMove(new BoardPosition(1, 1), new BoardPosition(1, 0)));
        assertEquals(false, baseBoard.legalMove(new BoardPosition(2, 2), new BoardPosition(2, 1)));
        //moving down
        assertEquals(true, baseBoard.legalMove(new BoardPosition(0, 0), new BoardPosition(2, 0)));
        assertEquals(false, baseBoard.legalMove(new BoardPosition(0, 0), new BoardPosition(3, 0)));
        //moving up
        assertEquals(true, baseBoard.legalMove(new BoardPosition(3, 3), new BoardPosition(2, 3)));
        assertEquals(false, baseBoard.legalMove(new BoardPosition(2, 2), new BoardPosition(0, 2)));
        //moving up and right
        assertEquals(true, testBoard.legalMove(new BoardPosition(1, 1), new BoardPosition(0, 2)));
        assertEquals(false, testBoard.legalMove(new BoardPosition(2, 2), new BoardPosition(1, 3)));
        //moving up and left
        assertEquals(true, testBoard.legalMove(new BoardPosition(1, 1), new BoardPosition(0, 0)));
        assertEquals(false, testBoard.legalMove(new BoardPosition(2, 2), new BoardPosition(1, 1)));
        //moving down and right
        assertEquals(true, testBoard.legalMove(new BoardPosition(2, 2), new BoardPosition(3, 3)));
        assertEquals(false, testBoard.legalMove(new BoardPosition(1, 0), new BoardPosition(3, 2)));
        //moving down and left
        assertEquals(true, testBoard.legalMove(new BoardPosition(2, 3), new BoardPosition(3, 2)));
        assertEquals(false, testBoard.legalMove(new BoardPosition(1, 1), new BoardPosition(2, 0)));
        //incorrect movement direction
        assertEquals(false, baseBoard.legalMove(new BoardPosition(0, 0), new BoardPosition(2, 1)));
    }

    @Test
    public void testMove() {
        baseBoard.move(new BoardPosition(0, 0), new BoardPosition(0, 2));
        assertEquals(Square.PLAYER1, baseBoard.getSquare(0, 2));
        assertEquals(Square.NONE, baseBoard.getSquare(0, 0));
        assertEquals(Square.PLAYER2, baseBoard.getCurrentPlayer());
        baseBoard.move(new BoardPosition(3, 0), new BoardPosition(0, 0));
        assertEquals(Square.NONE, baseBoard.getSquare(3, 0));
        assertEquals(Square.PLAYER2, baseBoard.getSquare(0, 0));
        assertEquals(Square.PLAYER1, baseBoard.getCurrentPlayer());
    }

    @Test
    public void testIsEndState2x2() {
        BoardGameModel testBoard1 = new BoardGameModel(
                new String[]{"0000",
                             "1122",
                             "0022",
                             "1100"},
                             Square.PLAYER1);
        
        BoardGameModel testBoard2 = new BoardGameModel(
                new String[]{"1111",
                             "0000",
                             "0002",
                             "0222"},
                             Square.PLAYER1);
        
        BoardGameModel testBoard3 = new BoardGameModel(
                new String[]{"0000",
                             "2211",
                             "0010",
                             "2201"},
                             Square.PLAYER2);
        
        assertEquals(true, testBoard1.isEndState2x2());
        assertEquals(false, testBoard2.isEndState2x2());
        assertEquals(false, testBoard3.isEndState2x2());
    }

    @Test
    public void testIsEndStateCorners() {
        BoardGameModel testBoard1 = new BoardGameModel(
                new String[]{"2002",
                             "1100",
                             "0010",
                             "2102"},
                             Square.PLAYER1);
        
        BoardGameModel testBoard2 = new BoardGameModel(
                new String[]{"1111",
                             "0000",
                             "0002",
                             "0222"},
                             Square.PLAYER1);
        
        
        assertEquals(true, testBoard1.isEndStateCorners());
        assertEquals(false, testBoard2.isEndStateCorners());
    }

    @Test
    public void testIsEndStateCol() {
        BoardGameModel testBoard1 = new BoardGameModel(
                new String[]{"0002",
                             "1102",
                             "0002",
                             "1102"},
                             Square.PLAYER1);
        
        BoardGameModel testBoard2 = new BoardGameModel(
                new String[]{"0001",
                             "0001",
                             "0112",
                             "0222"},
                             Square.PLAYER2);
        
        BoardGameModel testBoard3 = new BoardGameModel(
                new String[]{"0000",
                             "2211",
                             "0010",
                             "2201"},
                             Square.PLAYER2);
        
        assertEquals(true, testBoard1.isEndStateCol());
        assertEquals(false, testBoard2.isEndStateCol());
        assertEquals(false, testBoard3.isEndStateCol());
    }

    @Test
    public void testIsEndStateRow() {
        BoardGameModel testBoard1 = new BoardGameModel(
                new String[]{"2222",
                             "1100",
                             "0000",
                             "1100"},
                             Square.PLAYER1);
        
        BoardGameModel testBoard2 = new BoardGameModel(
                new String[]{"0001",
                             "0001",
                             "1102",
                             "0222"},
                             Square.PLAYER2);
        
        BoardGameModel testBoard3 = new BoardGameModel(
                new String[]{"0000",
                             "2211",
                             "0010",
                             "2201"},
                             Square.PLAYER2);
        
        assertEquals(true, testBoard1.isEndStateRow());
        assertEquals(false, testBoard2.isEndStateRow());
        assertEquals(false, testBoard3.isEndStateRow());
    }

    @Test
    public void testIsEndStateCornered() {
        BoardGameModel testBoard1 = new BoardGameModel(
                new String[]{"0221",
                             "1022",
                             "0000",
                             "1100"},
                             Square.PLAYER1);
        
        BoardGameModel testBoard2 = new BoardGameModel(
                new String[]{"0200",
                             "1022",
                             "1100",
                             "2100"},
                             Square.PLAYER2);
        
        BoardGameModel testBoard3 = new BoardGameModel(
                new String[]{"1221",
                             "2200",
                             "0000",
                             "1100"},
                             Square.PLAYER1);
        
        BoardGameModel testBoard4 = new BoardGameModel(
                new String[]{"0221",
                             "0020",
                             "0011",
                             "0012"},
                             Square.PLAYER2);
        
        BoardGameModel testBoard5 = new BoardGameModel(
                new String[]{"0221",
                             "1022",
                             "0000",
                             "1100"},
                             Square.PLAYER2);
        
        assertEquals(true, testBoard1.isEndStateCornered());
        assertEquals(true, testBoard2.isEndStateCornered());
        assertEquals(true, testBoard3.isEndStateCornered());
        assertEquals(true, testBoard4.isEndStateCornered());
        assertEquals(false, testBoard5.isEndStateCornered());
    }

    @Test
    public void testGetWinner() {
        BoardGameModel testBoard1 = new BoardGameModel(
                new String[]{"0221",
                             "1022",
                             "0000",
                             "1100"},
                             Square.PLAYER1);
        
        BoardGameModel testBoard2 = new BoardGameModel(
                new String[]{"0021",
                             "1020",
                             "0020",
                             "1120"},
                             Square.PLAYER1);
        
        assertEquals(Square.PLAYER1, testBoard1.getWinner());
        assertEquals(Square.PLAYER2, testBoard2.getWinner());
        assertEquals(Square.NONE, baseBoard.getWinner());
    }

    @Test
    public void testToString() {
        String initialBoard = "1 0 0 2 \n"
                + "0 1 2 0 \n"
                + "0 2 1 0 \n"
                + "2 0 0 1 \n";
        
        assertEquals(initialBoard, baseBoard.toString());
    }

}
