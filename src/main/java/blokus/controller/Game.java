package blokus.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Stack;

import blokus.model.APlayer;
import blokus.model.Board;
import blokus.model.Computer;
import blokus.model.Config;
import blokus.model.Coord;
import blokus.model.GameType;
import blokus.model.MCAI;
import blokus.model.Move;
import blokus.model.PColor;
import blokus.model.Piece;
import blokus.model.PieceReader;
import blokus.model.PlayStyle;
import blokus.model.Player;
import blokus.model.PlayerType;
import blokus.model.RandomPieceAI;
import blokus.model.RandomPlayAI;
import blokus.view.IApp;

/**
 * Class Game
 */
public class Game implements Serializable {

  //
  // Fields
  //

  private static final long serialVersionUID = 2358645711649943641L;
  private ArrayList<APlayer> players = new ArrayList<>();
  private static ArrayList<Piece> pieces = new ArrayList<>();

  private Board board;

  private Stack<Move> hist = new Stack<>();
  private Stack<Move> fwHist = new Stack<>();

  private APlayer curPlayer;
  private transient IApp app;
  private boolean gameOver = false;
  private boolean output = true;
  private boolean aiPlay = true;

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
      players.add(player.copy(this));
    }

    for (Move m : g.hist) {
      hist.push(new Move(m));
      hist.peek().changeGame(this);
    }

    for (Move m : g.fwHist) {
      fwHist.push(new Move(m));
      fwHist.peek().changeGame(this);
    }

    this.curPlayer = players.get(g.getCurPlayerNo());
  }

  //
  // Methods
  //

  public void init(GameType gt) {
    board = new Board(gt);
  }

  public void addPlayer(PlayerType pt) {
    switch (pt) {
    case USER:
      addPlayer(pt, null);
      break;
    case AI:
      addPlayer(pt, PlayStyle.RAND_BIG_PIECE);
      break;
    case MCAI:
      addPlayer(pt, PlayStyle.CENTER);
      break;
    case RANDOM_PIECE:
    case RANDOM_PLAY:
      addPlayer(pt, PlayStyle.RAND_PIECE);
      break;
    }
  }

  public void addPlayer(PlayerType pt, PlayStyle ps) {
    PColor c = PColor.get((byte) (players.size()));
    APlayer p = null;
    switch (pt) {
    case USER:
      p = new Player(c, pieces);
      break;
    case AI:
      p = new Computer(c, pieces, ps.create());
      break;
    case MCAI:
      p = new MCAI(c, pieces, ps.create());
      break;
    case RANDOM_PIECE:
      p = new RandomPieceAI(c, pieces, ps.create());
      break;
    case RANDOM_PLAY:
      p = new RandomPlayAI(c, pieces, ps.create());
      break;
    }
    players.add(p);

    if (players.size() == 1) {
      curPlayer = players.get(0);
      out(curPlayer + " turn");
    }
  }

  public void changePlayer(int pNo, PlayerType pt, PlayStyle ps) {
    APlayer p = null;
    switch (pt) {
    case USER:
      p = new Player(players.get(pNo), this);
      break;
    case AI:
      p = new Computer(players.get(pNo), ps.create(), this);
      break;
    case MCAI:
      p = new MCAI(players.get(pNo), ps.create(), this);
      break;
    case RANDOM_PIECE:
      p = new RandomPieceAI(players.get(pNo), ps.create(), this);
      break;
    case RANDOM_PLAY:
      p = new RandomPlayAI(players.get(pNo), ps.create(), this);
      break;
    }
    players.set(pNo, p);
    curPlayer = getPlayer(curPlayer.getColor());
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

  private void previousPlayer() {
    curPlayer = previousPlayer(curPlayer);
    out("rollback to " + getCurPlayer() + " turn");
    while (getCurPlayer().hasPassed() || getCurPlayer().whereToPlayAll(this).isEmpty()) {
      curPlayer = previousPlayer(curPlayer);
      out("rollback to " + getCurPlayer() + " turn");
    }
  }

  public APlayer nextPlayer(APlayer p) {
    return players.get((players.indexOf(p) + 1) % players.size());
  }

  private APlayer previousPlayer(APlayer p) {
    return players.get(((players.indexOf(p) - 1) + players.size()) % players.size());
  }

  private void play(Move m) {
    m.doMove();
    nextPlayer();
    hist.push(m);
    fwHist.clear();
    if (app != null) {
      app.update(m.getPlayer(), m.getPiece());
    }
    // SEE: save the move
  }

  public boolean canUndo() {
    return !hist.isEmpty();
  }

  public boolean canRedo() {
    return !fwHist.isEmpty();
  }

  public void undo() {
    Move m = hist.pop();
    fwHist.push(m);
    m.undoMove();
    previousPlayer();
    if (app != null) {
      app.undo(m.getPlayer(), m.getPiece());
    }
  }

  public void redo() {
    Move m = fwHist.pop();
    hist.push(m);
    m.doMove();
    nextPlayer();
    if (app != null) {
      app.update(m.getPlayer(), m.getPiece());
    }
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

  public HashMap<PColor, Integer> getScore() {
    // case where one of the players get either +20 points or +15 points
    HashMap<PColor, Integer> score = board.numOfEachColor();
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
    HashMap<PColor, Integer> scores = getScore();
    ArrayList<APlayer> ret = new ArrayList<>();
    int max = 0;
    for (int i : scores.values()) {
      if (i > max) {
        max = i;
      }
    }
    for (Entry<PColor, Integer> e : scores.entrySet()) {
      if (e.getValue() == max) {
        ret.add(getPlayer(e.getKey()));
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
    if (aiPlay && !isEndOfGame()) {
      long bTime = System.currentTimeMillis();
      Move m = getCurPlayer().completeMove(this);
      if (m != null && m.isValid()) {
        out(getCurPlayer().getColor().getName() + " took " + (System.currentTimeMillis() - bTime) / 60_000.0
            + "min to complete move");
        play(m);
      } else {
        if (m != null) {
          out("move was invalid !");
          out(m);
          ArrayList<Move> wtp = getCurPlayer().whereToPlayAllFlat(this);
          out(getCurPlayer() + " can play one of " + wtp.size() + " possible moves");
          out(wtp);
        }
      }
    }
  }

  /**
   * @param aiPlay the aiPlay to set
   */
  public void setAiPlay(boolean aiPlay) {
    this.aiPlay = aiPlay;
  }

  /**
   * @return the aiPlay
   */
  public boolean isAiPlaying() {
    return aiPlay;
  }

  public void save(String filename) {
    filename += ".ser";

    try {
      FileOutputStream file = new FileOutputStream(filename);
      ObjectOutputStream out = new ObjectOutputStream(file);
      out.writeObject(this);
      out.close();
      file.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static Game load(String filename) {

    Game game = null;
    try {
      // Reading the object from a file
      FileInputStream file = new FileInputStream(filename);
      ObjectInputStream in = new ObjectInputStream(file);

      // Method for deserialization of object
      game = (Game) in.readObject();

      in.close();
      file.close();

      for (Move m : game.hist) {
        m.changeGame(game);
      }
      for (Move m : game.fwHist) {
        m.changeGame(game);
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    } catch (ClassNotFoundException ex) {
      ex.printStackTrace();
    }

    return game;
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

  public int getPlayerNo(PColor c) {
    return c.getId();
  }

  public APlayer getPlayer(PColor c) {
    return players.get(getPlayerNo(c));
  }

  public int getNbPieces() {
    return pieces.size();
  }
}
