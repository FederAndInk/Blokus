package blokus.controller;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import blokus.model.GameType;
import blokus.model.PlayerType;

/**
 * GameSaveTest
 */
public class GameSaveTest {

  @Test
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
    assertTrue(g2.canUndo(), "loading a game with played move should have undo history");
    assertEquals(g2.getCurPlayer().getColor(), g.getCurPlayer().getColor());
    assertTrue(g2.getCurPlayer().getColor() == g.getCurPlayer().getColor());
    assertEquals(g2.getCurPlayer().getPieces().size(), g.getCurPlayer().getPieces().size());
    g2.undo();
    System.out.println(g2.getBoard());
    g2.refresh();
    g2.refresh();
    g2.refresh();
    System.out.println(g2.getBoard());
  }

  public static void main(String[] args) {
    GameSaveTest gst = new GameSaveTest();
    gst.game_save_test();
  }
}