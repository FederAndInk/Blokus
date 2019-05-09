package controller;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import model.APlayer;
import model.Board;
import model.Computer;
import model.Config;
import model.Piece;
import model.PieceReader;
import model.Player;
import view.App;
import java.util.HashMap;

/**
 * Class Game
 */
public class Game {

  //
  // Fields
  //

  private ArrayList<APlayer> players = new ArrayList<>();
  private ArrayList<Piece> pieces = new ArrayList<>();

  private Board board;

  private APlayer curPlayer;
  private App app;

  //
  // Constructors
  //
  public Game() {
    Config.i();

    board = new Board();
    PieceReader pr = new PieceReader(Config.loadRsc("pieces"));
    Piece p;
    while ((p = pr.nextPiece()) != null) {
      pieces.add(p);
    }
    Config.i().logger().info("read " + pieces.size() + " pieces");
  };

  //
  // Methods
  //

  public void addPlayer(boolean computer) {
    Color c = Board.colors.get(players.size());
    if (computer) {
      players.add(new Computer(c, pieces));
    } else {
      players.add(new Player(c, pieces));
    }
    if (players.size() == 1) {
      curPlayer = players.get(0);
    }
  }

  /**
  */
  public void nextPlayer() {
    curPlayer = players.get((players.indexOf(curPlayer) + 1) % players.size());
  }

  public boolean isEndOfGame() {
    for (APlayer p : players) {
      if (!p.hasToPass(board)) {
        return false;
      }
    }
    return true;
  }

  public HashMap<Color, Integer> getScore() {
    // case where one of the players get either +20 points or +15 points
    HashMap<Color, Integer> score = board.numOfEachColor();
    for (APlayer p : players) {
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
  public void setApp(App app) {
    this.app = app;
  }

  //
  // Other methods
  //

}
