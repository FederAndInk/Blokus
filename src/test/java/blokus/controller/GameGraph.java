
package blokus.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.testng.annotations.Test;

import blokus.model.APlayer;
import blokus.model.GameType;
import blokus.model.Move;
import blokus.model.PColor;
import blokus.model.Piece;
import blokus.model.PlayStyle;
import blokus.model.PlayerType;
import blokus.view.IApp;

/**
 * GameGraph
 */
public class GameGraph implements IApp {
  int turn;
  Game g;
  PrintStream ps;

  void stat_game(PlayerType p1, PlayStyle pc1, PlayerType p2, PlayStyle pc2, PrintStream ps) {
    this.ps = ps;
    g = new Game();
    g.setApp(this);
    turn = 0;
    g.addPlayer(p1, pc1);
    g.addPlayer(p2, pc2);
    g.init(GameType.DUO);

    update(null, null);
    do {
      g.refresh();
    } while (!g.isEndOfGame());
    System.out.println("scores:");
    for (Entry<PColor, Integer> sc : g.getScore().entrySet()) {
      System.out.println(g.getPlayer(sc.getKey()) + ": " + sc.getValue());
    }
  }

  @Override
  public void update(APlayer oldPlayer, Piece playedPiece) {
    ps.println("turn no " + turn++);
    ps.println(g.getBoard());
    ArrayList<Move> placements = g.getCurPlayer().whereToPlayAllFlat(g);
    ps.println("nb placements: " + placements.size());
    int nbPiece = 0;
    int[] placementsNb = new int[g.getNbPieces()];
    for (Move p : placements) {
      if (placementsNb[p.getPiece().no] == 0) {
        nbPiece++;
      }
      placementsNb[p.getPiece().no]++;
    }
    ps.println("nb pieces that can be played: " + nbPiece);
    for (int i = 0; i < placementsNb.length; ++i) {
      ps.println("piece no " + i + " nb placements: " + placementsNb[i]);
    }
    // ps.println("chance to win: " + Computer.monteCarlo(300, g,
    // g.getCurPlayer()));
  }

  @Override
  public void playerPassed(APlayer player) {

  }

  @Test
  public void game_graph() {
    stat_game(PlayerType.RANDOM_PIECE, PlayStyle.RAND_BIG_PIECE, PlayerType.RANDOM_PIECE, PlayStyle.RAND_PIECE,
        System.out);
  }

  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("args: nb_game folder");
      System.exit(1);
    }
    GameGraph gg = new GameGraph();
    for (int i = 0; i < Integer.parseInt(args[0]); i++) {
      File f = new File(args[1], "g" + i);
      try {
        gg.stat_game(PlayerType.RANDOM_PIECE, PlayStyle.RAND_PIECE, PlayerType.RANDOM_PIECE, PlayStyle.RAND_PIECE,
            new PrintStream(f));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void undo(APlayer oldPlayer, Piece removedPiece) {
  }
}