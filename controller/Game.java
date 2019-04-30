package controller;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import model.APlayer;
import model.Board;
import model.Computer;
import model.Player;
import view.App;

/**
 * Class Game
 */
public class Game {

  //
  // Fields
  //

  private ArrayList<APlayer> players = new ArrayList<>();

  private Board board;

  private APlayer curPlayer;
  private App app;

  //
  // Constructors
  //
  public Game(App app) {
    board = new Board();
    players.add(new Player(Color.BLUE));
    players.add(new Player(Color.RED));
    curPlayer = players.get(0);

    this.app = app;
  };

  //
  // Methods
  //

  public void addPlayer(Color c, boolean computer) {
    if (computer) {
      players.add(new Computer(c));
    } else {
      players.add(new Player(c));
    }
  }

  /**
  */
  public void nextPlayer() {
    curPlayer = players.get((players.indexOf(curPlayer) + 1) % players.size());
  }

  //
  // Accessor methods
  //

  //
  // Other methods
  //

}
