package test.tools;

/**
 * Test
 */
public class Test {
  int nbTest = 0;
  int nbTestPassed = 0;
  String name;

  public Test(String name) {
    this.name = name;
    System.out.println("Begin test " + name);
  }

  public void test(boolean b, String msg) {
    nbTest++;
    if (!b) {
      System.err.println(msg);
      System.err.flush();
    } else {
      nbTestPassed++;
    }
  }

  public void end() {
    System.out.println(name + " results: ");
    System.out.println(nbTestPassed + "/" + nbTest + " tests passed");
    if (nbTest != nbTestPassed) {
      System.exit(1);
    }
    System.out.println("End test " + name);
  }
}