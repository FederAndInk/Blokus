
package blokus.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.testng.annotations.Test;

import blokus.model.APlayer;
import blokus.model.Board;
import blokus.model.Piece;
import blokus.model.PieceChooser;
import blokus.model.Placement;
import blokus.model.PlayerType;
import blokus.model.RandPieceChooser;
import blokus.view.IApp;
import javafx.scene.paint.Color;

/**
 * GameGraph
 */
public class GameGraph implements IApp {
  int turn;
  Game g;
  PrintStream ps;

  void stat_game(PlayerType p1, PieceChooser pc1, PlayerType p2, PieceChooser pc2, PrintStream ps) {
    this.ps = ps;
    g = new Game();
    g.setApp(this);
    turn = 0;
    g.addPlayer(p1, pc1);
    g.addPlayer(p2, pc2);
    g.init(14);

    update(null, null);
    do {
      g.refresh();
    } while (!g.isEndOfGame());
    System.out.println("scores:");
    for (Entry<Color, Integer> sc : g.getScore().entrySet()) {
      System.out.println(g.getPlayers().get(Board.getColorId(sc.getKey()) - 1) + ": " + sc.getValue());
    }
  }

  @Override
  public void update(APlayer oldPlayer, Piece playedPiece) {
    ps.println("turn no " + turn++);
    ps.println(g.getBoard());
    ArrayList<Placement> placements = g.getCurPlayer().whereToPlayAll(g.getBoard());
    ps.println("nb placements: " + placements.size());
    int nbPiece = 0;
    int[] placementsNb = new int[g.getNbPieces()];
    for (Placement p : placements) {
      if (placementsNb[p.piece.no] == 0) {
        nbPiece++;
      }
      placementsNb[p.piece.no]++;
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

  // @Test
  public void game_graph() {
    stat_game(PlayerType.RANDOM_PIECE, new RandPieceChooser(), PlayerType.RANDOM_PIECE, new RandPieceChooser(),
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
        gg.stat_game(PlayerType.RANDOM_PIECE, new RandPieceChooser(), PlayerType.RANDOM_PIECE, new RandPieceChooser(),
            new PrintStream(f));
      } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
}