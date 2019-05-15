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

    @Test
    public void test_revertX() {
        assertEquals(PieceTransform.UP.revertX(), PieceTransform.DOWN_REVERTED);
        assertEquals(PieceTransform.UP.revertX().revertX(), PieceTransform.UP);

        assertEquals(PieceTransform.RIGHT.revertX(), PieceTransform.RIGHT_REVERTED);
        assertEquals(PieceTransform.RIGHT.revertX().revertX(), PieceTransform.RIGHT);

        assertEquals(PieceTransform.DOWN.revertX(), PieceTransform.UP_REVERTED);
        assertEquals(PieceTransform.DOWN.revertX().revertX(), PieceTransform.DOWN);

        assertEquals(PieceTransform.LEFT.revertX(), PieceTransform.LEFT_REVERTED);
        assertEquals(PieceTransform.LEFT.revertX().revertX(), PieceTransform.LEFT);

        assertNotEquals(PieceTransform.UP.revertX(), PieceTransform.UP);
        assertNotEquals(PieceTransform.UP.revertX(), PieceTransform.UP_REVERTED);

        assertNotEquals(PieceTransform.RIGHT.revertX(), PieceTransform.LEFT);
        assertNotEquals(PieceTransform.RIGHT.revertX(), PieceTransform.LEFT_REVERTED);
    }

    @Test
    public void test_revertY() {
        assertEquals(PieceTransform.UP.revertY(), PieceTransform.UP_REVERTED);
        assertEquals(PieceTransform.UP.revertY().revertY(), PieceTransform.UP);

        assertEquals(PieceTransform.RIGHT.revertY(), PieceTransform.LEFT_REVERTED);
        assertEquals(PieceTransform.RIGHT.revertY().revertY(), PieceTransform.RIGHT);

        assertEquals(PieceTransform.DOWN.revertY(), PieceTransform.DOWN_REVERTED);
        assertEquals(PieceTransform.DOWN.revertY().revertY(), PieceTransform.DOWN);

        assertEquals(PieceTransform.LEFT.revertY(), PieceTransform.RIGHT_REVERTED);
        assertEquals(PieceTransform.LEFT.revertY().revertY(), PieceTransform.LEFT);

        assertNotEquals(PieceTransform.UP.revertY(), PieceTransform.DOWN);
        assertNotEquals(PieceTransform.UP.revertY(), PieceTransform.DOWN_REVERTED);

        assertNotEquals(PieceTransform.RIGHT.revertY(), PieceTransform.RIGHT);
        assertNotEquals(PieceTransform.LEFT.revertY(), PieceTransform.LEFT_REVERTED);
    }
}