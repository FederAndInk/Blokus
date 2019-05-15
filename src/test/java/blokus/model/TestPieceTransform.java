package blokus.model;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * TestPieceTransform
 */
public class TestPieceTransform {

    @Test
    public void test_left() {
        assertEquals(PieceTransform.UP.left(), PieceTransform.LEFT);
        assertEquals(PieceTransform.UP.left().left(), PieceTransform.DOWN);
        assertEquals(PieceTransform.UP.left().left().left(), PieceTransform.RIGHT);
        assertEquals(PieceTransform.UP.left().left().left().left(), PieceTransform.UP);

        assertEquals(PieceTransform.UP_REVERTED.left(), PieceTransform.LEFT_REVERTED);
        assertEquals(PieceTransform.UP_REVERTED.left().left(), PieceTransform.DOWN_REVERTED);
        assertEquals(PieceTransform.UP_REVERTED.left().left().left(), PieceTransform.RIGHT_REVERTED);
        assertEquals(PieceTransform.UP_REVERTED.left().left().left().left(), PieceTransform.UP_REVERTED);

        assertNotEquals(PieceTransform.UP.left(), PieceTransform.RIGHT);
    }

    @Test
    public void test_right() {
        assertEquals(PieceTransform.UP.right(), PieceTransform.RIGHT);
        assertEquals(PieceTransform.UP.right().right(), PieceTransform.DOWN);
        assertEquals(PieceTransform.UP.right().right().right(), PieceTransform.LEFT);
        assertEquals(PieceTransform.UP.right().right().right().right(), PieceTransform.UP);

        assertEquals(PieceTransform.UP_REVERTED.right(), PieceTransform.RIGHT_REVERTED);
        assertEquals(PieceTransform.UP_REVERTED.right().right(), PieceTransform.DOWN_REVERTED);
        assertEquals(PieceTransform.UP_REVERTED.right().right().right(), PieceTransform.LEFT_REVERTED);
        assertEquals(PieceTransform.UP_REVERTED.right().right().right().right(), PieceTransform.UP_REVERTED);

        assertNotEquals(PieceTransform.UP.right(), PieceTransform.LEFT);
    }

}