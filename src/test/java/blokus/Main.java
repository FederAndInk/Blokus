
package blokus;

import org.testng.annotations.Test;

import blokus.controller.GameTest;
import blokus.model.PlayerType;

/**
 * Main
 */
public class Main {

  @Test
  void caca() {

  }

  public static void main(String[] args) {
    GameTest gt = new GameTest();
    gt.game_test(PlayerType.MCAI, PlayerType.RANDOM_PIECE);
    System.out.println("*****************************************************************************");
    gt.results();
  }
}