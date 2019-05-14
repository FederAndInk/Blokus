package blokus.model;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

/**
 * DirectionTest
 */
public class DirectionTest {
  
  @Test
  public void direction_test() {
    // Test t = new Test("DirectionTest");
    Direction d = Direction.UP;
    assertTrue(d.right() == Direction.RIGHT, "UP.right()");
    assertTrue(d.right().right() == Direction.DOWN, "UP.right().right()");
    assertTrue(d.right().right().right() == Direction.LEFT, "UP.right().right().right()");
    assertTrue(d.right().right().right().right() == Direction.UP, "UP.right().right().right().right()");

    assertTrue(d.left() == Direction.LEFT, "UP.left()");
    assertTrue(d.left().left() == Direction.DOWN, "UP.left().left()");
    assertTrue(d.left().left().left() == Direction.RIGHT, "UP.left().left().left()");
    assertTrue(d.left().left().left().left() == Direction.UP, "UP.left().left().left().left()");
    // t.end();
  }
}