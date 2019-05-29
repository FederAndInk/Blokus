
package blokus.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
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
  PrintStream psExtra;
  static int nbPlayer = 2;
  int nbPlayerPassed = 0;

  void stat_game(PlayerType p1, PlayStyle pc1, PlayerType p2, PlayStyle pc2, PrintStream ps, PrintStream psExtra) {
    this.ps = ps;
    this.psExtra = psExtra;
    g = new Game();
    g.setApp(this);
    turn = 0;
    nbPlayerPassed = 0;
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
    System.out.println("turn: " + turn);
    System.out.println("nb turn remaining: " + (getNbTurn() - turn));
    for (int i = turn; i < getNbTurn(); i++) {
      emptyTurn(ps, psExtra);
    }
  }

  @Override
  public void update(APlayer oldPlayer, Piece playedPiece) {
    // no, t0_nbPlay, t0_nbPiece, t0_nbAccCorners, t0_noPiece, ..., score_bleu,

    // for the last play
    if (playedPiece != null) {
      ps.print(";" + playedPiece.no);
    }
    for (int i = 0; i < nbPlayerPassed; i++) {
      emptyTurn(ps, psExtra);
    }
    nbPlayerPassed = 0;

    System.out.println(g.getBoard());

    ArrayList<Move> placements = g.getCurPlayer().whereToPlayAll(g);
    ps.print(";" + placements.size());
    int nbPiece = 0;
    int[] placementsNb = new int[Game.getNbPieces()];
    for (Move p : placements) {
      if (placementsNb[p.getPiece().no] == 0) {
        nbPiece++;
      }
      placementsNb[p.getPiece().no]++;
    }
    ps.print(";" + nbPiece);
    ps.print(";" + g.getBoard().getAccCorners(g.getCurPlayer().getColor()).size());
    if (placements.size() == 0) {
      ps.print(";");
    }
    for (int i = 0; i < placementsNb.length; ++i) {
      psExtra.print(";" + placementsNb[i]);
    }
    // ps.println("chance to win: " + Computer.monteCarlo(300, g,
    // g.getCurPlayer()));
    turn++;
  }

  @Override
  public void playerPassed(APlayer player) {
    nbPlayerPassed++;
    turn++;
  }

  @Test
  public void game_graph() {
    stat_game(PlayerType.RANDOM_PIECE, PlayStyle.RAND_BIG_PIECE, PlayerType.RANDOM_PIECE, PlayStyle.RAND_PIECE,
        System.out, System.out);
  }

  private static int getNbTurn() {
    return Game.getNbPieces() * nbPlayer;
  }

  private static String generateHead() {
    String ret = "gNo";
    int noTurn = 0;
    for (int k = 0; k < nbPlayer; ++k) {
      for (int i = 0; i < Game.getNbPieces(); i++) {
        ret += ";t" + noTurn + "_nbPlay" + ";t" + noTurn + "_nbPiece" + ";t" + noTurn + "_nbAccCorners" + ";t" + noTurn
            + "_noPiece";
        ++noTurn;
      }
    }
    for (int i = 0; i < nbPlayer; ++i) {
      ret += ";score_" + PColor.values()[i].getName();
    }

    return ret;
  }

  private static String generateExtraHead() {
    String ret = "gNo";
      for (int i = 0; i < getNbTurn(); i++) {
        for (int j = 0; j < Game.getNbPieces(); j++) {
          ret += ";t" + i + "_piece_" + j + "_nbPlay";
      }
    }
    return ret;
  }

  private static void emptyTurn(PrintStream ps, PrintStream psExtra) {
    ps.print(";;;;");
    for (int j = 0; j < Game.getNbPieces(); j++) {
      psExtra.print(";");
    }
  }

  public static void main(String[] args) throws FileNotFoundException {
    if (args.length != 2) {
      System.out.println("args: nb_game folder");
      System.exit(1);
    }
    GameGraph gg = new GameGraph();
    File gamesFolder = new File(args[1], "games");
    File stats = new File(args[1], "stats.csv");
    File statsExtra = new File(args[1], "statsExtra.csv");
    gamesFolder.mkdirs();
    // no, t0_nbPlay, t0_nbPiece, t0_nbAccCorners, t0_noPiece, ..., score_bleu,
    // score_...
    PrintStream ps = new PrintStream(stats);
    PrintStream psExtra = new PrintStream(statsExtra);

    ps.println(generateHead());
    psExtra.println(generateExtraHead());

    for (int i = 0; i < Integer.parseInt(args[0]); i++) {
      ps.print(i);
      psExtra.print(i);
      gg.stat_game(PlayerType.RANDOM_PIECE, PlayStyle.RAND_PIECE, PlayerType.RANDOM_PIECE, PlayStyle.RAND_PIECE, ps,
          psExtra);
      HashMap<PColor, Integer> scores = gg.g.getScore();
      for (int k = 0; k < nbPlayer; ++k) {
        ps.print(";" + scores.get(PColor.values()[k]));
      }
      ps.println();
      psExtra.println();
      File game = new File(gamesFolder, "g" + i);
      gg.g.save(game.toString());
    }
    ps.close();
    psExtra.close();
  }

  @Override
  public void undo(APlayer oldPlayer, Piece removedPiece) {
  }
}