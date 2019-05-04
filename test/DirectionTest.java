package test;

import model.Direction;

/**
 * DirectionTest
 */
public class DirectionTest {
  int nbTest = 0;
  int nbTestPassed = 0;

  void test(boolean b, String msg) {
    nbTest++;
    if (!b) {
      System.err.println(msg);
    } else {
      nbTestPassed++;
    }
  }

  public DirectionTest() {
    Direction d = Direction.UP;
    test(d.right() == Direction.RIGHT, "UP.right()");
    test(d.right().right() == Direction.DOWN, "UP.right().right()");
    test(d.right().right().right() == Direction.LEFT, "UP.right().right().right()");
    test(d.right().right().right().right() == Direction.UP, "UP.right().right().right().right()");

    test(d.left() == Direction.LEFT, "UP.left()");
    test(d.left().left() == Direction.DOWN, "UP.left().left()");
    test(d.left().left().left() == Direction.RIGHT, "UP.left().left().left()");
    test(d.left().left().left().left() == Direction.UP, "UP.left().left().left().left()");

    System.out.println("DirectionTest results: ");
    System.out.println(nbTestPassed + "/" + nbTest + " tests passed");
    if (nbTest != nbTestPassed) {
      System.exit(1);
    }
  }

  public static void main(String[] args) {
    new DirectionTest();
  }
}