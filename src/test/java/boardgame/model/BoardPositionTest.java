package boardgame.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class BoardPositionTest {
    
    public BoardPositionTest() {
    }

    @Test
    public void testConstructor_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new BoardPosition(-1,-1));
        assertThrows(IllegalArgumentException.class, () -> new BoardPosition(0,4));
        assertThrows(IllegalArgumentException.class, () -> new BoardPosition(2,-1));
        assertThrows(IllegalArgumentException.class, () -> new BoardPosition(5,2));
    }
    
}
