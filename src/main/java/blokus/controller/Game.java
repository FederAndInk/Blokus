package blokus.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import blokus.model.APlayer;
import blokus.model.Board;
import blokus.model.CenterPieceChooser;
import blokus.model.Computer;
import blokus.model.Config;
import blokus.model.Coord;
import blokus.model.MCAI;
import blokus.model.Move;
import blokus.model.Piece;
import blokus.model.PieceChooser;
import blokus.model.PieceReader;
import blokus.model.Player;
import blokus.model.PlayerType;
import blokus.model.RandBigPieceChooser;
import blokus.model.RandPieceChooser;
import blokus.model.RandomPieceAI;
import blokus.view.IApp;
import javafx.scene.paint.Color;

/**
 * Class Game
 */
public class Game {

  //
  // Fields
  //

  private ArrayList<APlayer> players = new ArrayList<>();
  private static ArrayList<Piece> pieces = new ArrayList<>();

  private Board board;

  private APlayer curPlayer;
  private IApp app;
  private boolean gameOver = false;
  private boolean output = true;

  //
  // Constructors
  //
  public Game() {
    Config.i().logger().info("Active threads: " + Thread.activeCount());

    if (pieces.isEmpty()) {
      PieceReader pr = new PieceReader(Config.loadRsc("pieces"));
      Piece p;
      while ((p = pr.nextPiece()) != null) {
        pieces.add(p);
      }
      Config.i().logger().info("read " + pieces.size() + " pieces");
    }
  };

  public Game(Game g) {
    this.app = null;
    this.output = g.output;
    this.board = new Board(g.board);
    for (APlayer player : g.players) {
      players.add(player.copy());
    }
    this.curPlayer = players.get(g.getCurPlayerNo());
  }

  //
  // Methods
  //

  public void init(int boardSize) {
    board = new Board(boardSize);
  }

  public void addPlayer(PlayerType pt) {
    switch (pt) {
    case USER:
      addPlayer(pt, null);
      break;
    case AI:
      addPlayer(pt, new RandBigPieceChooser());
      break;
    case MCAI:
      addPlayer(pt, new CenterPieceChooser());
      break;
    case RANDOM_PIECE:
    case RANDOM_PLAY:
      addPlayer(pt, new RandPieceChooser());
      break;
    }
  }

  public void addPlayer(PlayerType pt, PieceChooser pieceChooser) {
    Color c = Board.getColor((byte) players.size());
    switch (pt) {
    case USER:
      players.add(new Player(c, pieces));
      break;
    case AI:
      players.add(new Computer(c, pieces, pieceChooser));
      break;
    case MCAI:
      players.add(new MCAI(c, pieces, pieceChooser));
      break;
    case RANDOM_PIECE:
      players.add(new RandomPieceAI(c, pieces, pieceChooser));
      break;
    case RANDOM_PLAY:

    }

    if (players.size() == 1) {
      curPlayer = players.get(0);
      out(curPlayer + " turn");
    }
  }

  /**
  */
  private void nextPlayer() {
    if (!isEndOfGame()) {
      curPlayer = nextPlayer(curPlayer);
      out(getCurPlayer() + " turn");
      while (getCurPlayer().hasPassed() || getCurPlayer().whereToPlayAll(this).isEmpty()) {
        if (getCurPlayer().hasPassed()) {
          out(getCurPlayer() + " HAS passed");
        } else {
          out(getCurPlayer() + " passed");
        }
        if (getCurPlayer().getPieces().isEmpty()) {
          out("no more pieces");
        } else {
          out("no more space to play");
          out(getCurPlayer().getPieces().size() + " pieces remaining");
        }
        if (app != null) {
          app.playerPassed(getCurPlayer());
        }
        curPlayer = nextPlayer(curPlayer);
      }
    } else {
      out("Game is over");
      for (APlayer p : players) {
        if (p.getPieces().isEmpty()) {
          out("no more pieces");
        } else if (p.whereToPlayAll(this).isEmpty()) {
          out("no more space to play");
          out(p.getPieces().size() + " pieces remaining");
        } else {
          throw new IllegalStateException(p + " can play but the game is over nani ?!!");
        }
      }
    }
  }

  public APlayer nextPlayer(APlayer p) {
    return players.get((players.indexOf(p) + 1) % players.size());
  }

  private void play(Move m) {
    m.doMove();
    nextPlayer();
    if (app != null) {
      app.update(m.getPlayer(), m.getPiece());
    }
    // SEE: save the move
  }

  /**
   * the player (user) input a move
   */
  public Move inputPlay(Piece p, Coord pos) {
    Move m = new Move(getCurPlayer(), p, this, pos, p.getState());
    play(m);
    return m;
  }

  /**
   * the player (user) input a move
   */
  public Move inputPlay(Move m) {
    m.changeGame(this);
    if (m.getPlayer() != getCurPlayer()) {
      throw new IllegalStateException(
          "Move is not from current player (" + getCurPlayer() + ") but from " + m.getPlayer());
    }
    play(m);
    return m;
  }

  public boolean isEndOfGame() {
    if (!gameOver) {
      for (APlayer p : players) {
        if (p.canPlay(board)) {
          return false;
        }
      }
    }
    gameOver = true;
    return true;
  }

  /**
   * called when a Move has been undone</br>
   * 
   * mainly called by {@link Move#undoMove()}
   */
  public void undoDone() {
    gameOver = false;
  }

  public HashMap<Color, Integer> getScore() {
    // case where one of the players get either +20 points or +15 points
    HashMap<Color, Integer> score = board.numOfEachColor();
    for (APlayer p : players) {
      score.putIfAbsent(p.getColor(), 0);
      if (p.getPieces().isEmpty()) {
        ArrayList<Piece> pieces = board.getPlayed(p.getColor());
        Piece lastPiece = pieces.get(pieces.size() - 1);
        Integer res = score.get(p.getColor());
        if (lastPiece.getShape().size() == 1) {
          score.put(p.getColor(), res + 20);
        } else {
          score.put(p.getColor(), res + 15);
        }
      }
    }
    return score;
  }

  public ArrayList<APlayer> getWinner() {
    HashMap<Color, Integer> scores = getScore();
    ArrayList<APlayer> ret = new ArrayList<>();
    int max = 0;
    for (int i : scores.values()) {
      if (i > max) {
        max = i;
      }
    }
    for (Entry<Color, Integer> e : scores.entrySet()) {
      if (e.getValue() == max) {
        ret.add(players.get(Board.getColorId(e.getKey())));
      }
    }
    return ret;
  }

  /**
   * function to call in main loop</br>
   * to update the model:</br>
   * </br>
   * - AI computation when AI turn
   */
  public void refresh() {
    if (!isEndOfGame()) {
      long bTime = System.currentTimeMillis();
      Move m = getCurPlayer().completeMove(this);
      if (m != null && m.isValid()) {
        out(Board.getColorName(getCurPlayer().getColor()) + " took " + (System.currentTimeMillis() - bTime) / 60_000.0
            + "min to complete move");
        play(m);
      } else {
        if (m != null) {
          out("move was invalid !");
          out(m);
          ArrayList<Move> wtp = getCurPlayer().whereToPlayAll(this);
          out(getCurPlayer() + " can play one of " + wtp.size() + " possible moves");
          out(wtp);
        }
      }
    }
  }

  private void out(Object o) {
    if (output) {
      System.out.println(o);
    }
  }

  public void setOutput(boolean o) {
    output = o;
  }

  public Game copy() {
    return new Game(this);
  }

  //
  // Accessor methods
  //
  /**
   * @return the board
   */
  public Board getBoard() {
    return board;
  }

  /**
   * @return the curPlayer
   */
  public APlayer getCurPlayer() {
    return curPlayer;
  }

  /**
   * @param app the app to set
   */
  public void setApp(IApp app) {
    this.app = app;
  }

  public ArrayList<APlayer> getPlayers() {
    return this.players;
  }

  public int getNbPlayers() {
    return players.size();
  }

  /**
   * player index from 0 to 3 BEGIN at index 0
   */
  public int getCurPlayerNo() {
    return getPlayerNo(getCurPlayer());
  }

  public int getPlayerNo(APlayer p) {
    return players.indexOf(p);
  }

  public int getPlayerNo(Color c) {
    return Board.getColorId(c);
  }

  public APlayer getPlayer(Color c) {
    return players.get(getPlayerNo(c));
  }

  public int getNbPieces() {
    return pieces.size();
  }
}
