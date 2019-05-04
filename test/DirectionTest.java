package test;

import model.Direction;
import test.tools.Test;

/**
 * DirectionTest
 */
public class DirectionTest {
  Test t = new Test("DirectionTest");

  public DirectionTest() {
    Direction d = Direction.UP;
    t.test(d.right() == Direction.RIGHT, "UP.right()");
    t.test(d.right().right() == Direction.DOWN, "UP.right().right()");
    t.test(d.right().right().right() == Direction.LEFT, "UP.right().right().right()");
    t.test(d.right().right().right().right() == Direction.UP, "UP.right().right().right().right()");

    t.test(d.left() == Direction.LEFT, "UP.left()");
    t.test(d.left().left() == Direction.DOWN, "UP.left().left()");
    t.test(d.left().left().left() == Direction.RIGHT, "UP.left().left().left()");
    t.test(d.left().left().left().left() == Direction.UP, "UP.left().left().left().left()");
    t.end();
  }

  public static void main(String[] args) {
    new DirectionTest();
  }
}