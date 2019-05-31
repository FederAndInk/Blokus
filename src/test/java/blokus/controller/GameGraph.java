
package blokus.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import blokus.model.APlayer;
import blokus.model.GameType;
import blokus.model.Move;
import blokus.model.PColor;
import blokus.model.Piece;
import blokus.model.PlayStyle;
import blokus.model.PlayerType;
import blokus.utils.Pair;
import blokus.view.IApp;

/**
 * GameGraph
 */
public class GameGraph implements IApp {
  private int turn;
  private Game g;
  private PrintStream ps;
  private PrintStream psExtra;
  private int nbPlayer;
  private int nbPlayerPassed = 0;

  private ArrayList<Pair<PlayerType, PlayStyle>> players = new ArrayList<>();
  private GameType gt;

  private long bTime;

  public GameGraph() {
    this(GameType.DUO);
  }

  public GameGraph(GameType gt) {
    this.gt = gt;
    switch (gt) {
    case DUO:
      nbPlayer = 2;
      break;
    case BLOKUS:
      nbPlayer = 4;
      break;
    }
  }

  public void infoDisp(PrintStream info) {
    int i = 0;
    for (Pair<PlayerType, PlayStyle> player : players) {
      info.println("Player " + PColor.get((byte) i) + ": " + player);
      i++;
    }
  }

  public void addPlayer(PlayerType p1, PlayStyle pc1) {
    players.add(new Pair<>(p1, pc1));
  }

  Game stat_game(PrintStream ps, PrintStream psExtra) {
    this.ps = ps;
    this.psExtra = psExtra;
    g = new Game();
    for (Pair<PlayerType, PlayStyle> player : players) {
      g.addPlayer(player.getFirst(), player.getSecond());
    }
    turn = 0;
    nbPlayerPassed = 0;
    nbPlayer = g.getNbPlayers();

    g.setApp(this);
    g.init(gt);

    update(null, null);
    do {
      bTime = System.currentTimeMillis();
      g.refresh();
    } while (!g.isEndOfGame());

    System.out.println("turn: " + turn);
    System.out.println("nb turn remaining: " + (getNbTurn() - turn));
    for (int i = turn; i <= getNbTurn(); i++) {
      emptyTurn(ps, psExtra);
    }

    HashMap<PColor, Integer> scores = g.getScore();
    for (int k = 0; k < g.getNbPlayers(); ++k) {
      ps.print(";" + scores.get(PColor.values()[k]));
    }

    Game gTmp = g;
    g = null;
    return gTmp;
  }

  @Override
  public void update(APlayer oldPlayer, Piece playedPiece) {
    // no, t0_nbPlay, t0_nbPiece, t0_nbAccCorners, t0_noPiece, ..., score_bleu,

    // for the last play
    if (playedPiece != null) {
      ps.print(";" + (System.currentTimeMillis() - bTime));
      ps.print(";" + playedPiece.no);
    }
    for (int i = 0; i < nbPlayerPassed; i++) {
      emptyTurn(ps, psExtra);
    }
    nbPlayerPassed = 0;

    System.out.println(g.getBoard());

    ArrayList<Move> placements = g.getCurPlayer().whereToPlayAll(g);
    if (placements.size() != 0) {
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

      // no more piece to play (there will be no ms nor noPiece)
      for (int i = 0; i < placementsNb.length; ++i) {
        psExtra.print(";" + placementsNb[i]);
      }
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

  // @Test
  public void game_graph() {
    addPlayer(PlayerType.RANDOM_PIECE, PlayStyle.RAND_BIG_PIECE);
    addPlayer(PlayerType.RANDOM_PIECE, PlayStyle.RAND_PIECE);
    stat_game(System.out, System.out);
    infoDisp(System.out);
  }

  private int getNbTurn() {
    return Game.getNbPieces() * nbPlayer;
  }

  private String generateHead() {
    String ret = "gNo";
    int noTurn = 0;
    for (int k = 0; k < nbPlayer; ++k) {
      for (int i = 0; i < Game.getNbPieces(); i++) {
        ret += ";t" + noTurn + "_nbPlay" + ";t" + noTurn + "_nbPiece" + ";t" + noTurn + "_nbAccCorners" + ";t" + noTurn
            + "_ms" + ";t" + noTurn + "_noPiece";
        ++noTurn;
      }
    }
    for (int i = 0; i < nbPlayer; ++i) {
      ret += ";score_" + PColor.values()[i].getName();
    }

    return ret;
  }

  private String generateExtraHead() {
    String ret = "gNo";
    for (int i = 0; i < getNbTurn(); i++) {
      for (int j = 0; j < Game.getNbPieces(); j++) {
        ret += ";t" + i + "_piece_" + j + "_nbPlay";
      }
    }
    return ret;
  }

  private static void emptyTurn(PrintStream ps, PrintStream psExtra) {
    ps.print(";;;;;");
    for (int j = 0; j < Game.getNbPieces(); j++) {
      psExtra.print(";");
    }
  }

  public static void main(String[] args) throws FileNotFoundException {
    if (args.length != 2) {
      System.out.println("args: nb_game folder");
      System.exit(1);
    }
    File mainFolder = new File(args[1]);
    File gamesFolder = new File(mainFolder, "games");
    File stats = new File(mainFolder, "stats.csv");
    File statsExtra = new File(mainFolder, "statsExtra.csv");
    File info = new File(mainFolder, "info");
    gamesFolder.mkdirs();
    // no, t0_nbPlay, t0_nbPiece, t0_nbAccCorners, t0_noPiece, ..., score_bleu,
    // score_...
    PrintStream ps = new PrintStream(stats);
    PrintStream psExtra = new PrintStream(statsExtra);
    PrintStream psInfo = new PrintStream(info);

    GameGraph gg = new GameGraph(GameType.DUO);
    gg.addPlayer(PlayerType.RANDOM_PIECE, PlayStyle.RAND_PIECE);
    gg.addPlayer(PlayerType.RANDOM_PIECE, PlayStyle.TWO_HEURISTICS);

    ps.println(gg.generateHead());
    psExtra.println(gg.generateExtraHead());
    gg.infoDisp(psInfo);

    for (int i = 0; i < Integer.parseInt(args[0]); i++) {
      ps.print(i);
      psExtra.print(i);
      Game g = gg.stat_game(ps, psExtra);
      ps.println();
      psExtra.println();
      File game = new File(gamesFolder, "g" + i);
      g.save(game.toString());
    }
    ps.close();
    psExtra.close();
    psInfo.close();
  }

  @Override
  public void undo(APlayer oldPlayer, Piece removedPiece) {
  }
}