package test;

import utils.Utils;

/**
 * ByteTest
 */
public class ByteTest {

  public static void main(String[] args) {
    byte b = 0;
    System.out.println(b);
    b = 1;
    System.out.println(b);
    b <<= 1;
    System.out.println(b);
    b <<= 1;
    System.out.println(b);
    Utils.printByte(b);

    b = 0b01001101;
    System.out.println(b);
    Utils.printByte(b);
    System.out.println("get(1) get(0):");
    System.out.println(Utils.get(b, 1) + "" + Utils.get(b, 0));
    System.out.println("get2(b)...:");
    System.out.println(Utils.get2(b, 3) + " " + Utils.get2(b, 2) + " " + Utils.get2(b, 1) + " " + Utils.get2(b, 0));
    b = Utils.set(b, 1, 1);
    Utils.printByte(b);
    b = Utils.set2(b, 2, 2);
    Utils.printByte(b);

  }
}