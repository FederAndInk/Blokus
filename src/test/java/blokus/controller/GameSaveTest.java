package blokus.controller;

import blokus.model.GameType;
import blokus.model.PlayerType;

/**
 * GameSaveTest
 */
public class GameSaveTest {

  void game_save_test() {
    Game g = new Game();
    g.addPlayer(PlayerType.RANDOM_PIECE);
    g.addPlayer(PlayerType.RANDOM_PIECE);
    g.init(GameType.DUO);
    g.refresh();
    g.refresh();
    g.refresh();
    System.out.println(g.getBoard());
    g.save("test");
    Game g2 = Game.load("test");
    System.out.println(g2.getBoard());
  }

  public static void main(String[] args) {
    GameSaveTest gst = new GameSaveTest();
    gst.game_save_test();
  }
}