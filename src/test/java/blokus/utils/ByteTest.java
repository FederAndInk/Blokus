package blokus.utils;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * ByteTest
 */
public class ByteTest {

  @Test
  public void byte_test() {
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
    byte b2 = -1;
    System.out.println("-1:");
    Utils.printByte(b2);
    System.out.println("-1 >>> 8:");
    Utils.printByte((byte) (b2 >>> 8));
    System.out.println("-1 >>> 8 & 1:");
    b2 = (byte) ((b2 >>> 8) & 1);
    Utils.printByte(b2);
    assertEquals(b2, 1);
    
    System.out.println("2 >>> 8 & 1:");
    b2 = 2;
    b2 = (byte) ((b2 >>> 8) & 1);
  }
}