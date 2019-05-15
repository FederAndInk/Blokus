package blokus.model;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * TestDirection
 */
public class TestDirection {

    @Test
    public void test_left() {
        assertEquals(Direction.UP.left(), Direction.LEFT);
        assertEquals(Direction.UP.left().left(), Direction.DOWN);
        assertEquals(Direction.UP.left().left().left(), Direction.RIGHT);
        assertEquals(Direction.UP.left().left().left().left(), Direction.UP);

        assertNotEquals(Direction.UP.left(), Direction.UP);
        assertNotEquals(Direction.UP.left().left(), Direction.UP);
        assertNotEquals(Direction.UP.left().left().left(), Direction.UP);

        assertNotEquals(Direction.RIGHT.left(), Direction.RIGHT);
        assertNotEquals(Direction.RIGHT.left().left(), Direction.RIGHT);
        assertNotEquals(Direction.RIGHT.left().left().left(), Direction.RIGHT);

        assertNotEquals(Direction.DOWN.left(), Direction.DOWN);
        assertNotEquals(Direction.DOWN.left().left(), Direction.DOWN);
        assertNotEquals(Direction.DOWN.left().left().left(), Direction.DOWN);

        assertNotEquals(Direction.LEFT.left(), Direction.LEFT);
        assertNotEquals(Direction.LEFT.left().left(), Direction.LEFT);
        assertNotEquals(Direction.LEFT.left().left().left(), Direction.LEFT);
    }

    @Test
    public void test_right() {
        assertEquals(Direction.UP.right(), Direction.RIGHT);
        assertEquals(Direction.UP.right().right(), Direction.DOWN);
        assertEquals(Direction.UP.right().right().right(), Direction.LEFT);
        assertEquals(Direction.UP.right().right().right().right(), Direction.UP);

        assertNotEquals(Direction.UP.right(), Direction.UP);
        assertNotEquals(Direction.UP.right().right(), Direction.UP);
        assertNotEquals(Direction.UP.right().right().right(), Direction.UP);

        assertNotEquals(Direction.RIGHT.right(), Direction.RIGHT);
        assertNotEquals(Direction.RIGHT.right().right(), Direction.RIGHT);
        assertNotEquals(Direction.RIGHT.right().right().right(), Direction.RIGHT);

        assertNotEquals(Direction.DOWN.right(), Direction.DOWN);
        assertNotEquals(Direction.DOWN.right().right(), Direction.DOWN);
        assertNotEquals(Direction.DOWN.right().right().right(), Direction.DOWN);

        assertNotEquals(Direction.LEFT.right(), Direction.LEFT);
        assertNotEquals(Direction.LEFT.right().right(), Direction.LEFT);
        assertNotEquals(Direction.LEFT.right().right().right(), Direction.LEFT);
    }

    @Test
    public void test_revertX() {
        assertEquals(Direction.UP.revertX(), Direction.DOWN);
        assertEquals(Direction.DOWN.revertX(), Direction.UP);

        assertEquals(Direction.RIGHT.revertX(), Direction.RIGHT);
        assertEquals(Direction.LEFT.revertX(), Direction.LEFT);

        assertNotEquals(Direction.UP.revertX(), Direction.UP);
        assertNotEquals(Direction.DOWN.revertX(), Direction.DOWN);
    }

    @Test
    public void test_revertY() {
        assertEquals(Direction.RIGHT.revertY(), Direction.LEFT);
        assertEquals(Direction.LEFT.revertY(), Direction.RIGHT);

        assertEquals(Direction.UP.revertY(), Direction.UP);
        assertEquals(Direction.DOWN.revertY(), Direction.DOWN);

        assertNotEquals(Direction.RIGHT.revertY(), Direction.RIGHT);
        assertNotEquals(Direction.LEFT.revertY(), Direction.LEFT);
    }
}