
package blokus.view;

import java.util.Observable;
import java.util.Observer;

import blokus.controller.Game;
import blokus.model.APlayer;
import blokus.model.Board;
import blokus.model.Piece;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * App
 */
public class App extends Application implements Observer {

  Game game;

  @Override
  public void init() throws Exception {
    super.init();
    game = new Game();
    game.setApp(this);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

  }

  @Override
  public void update(Observable o, Object arg) {
    if (o instanceof Board) {
      Board b = (Board) o;
      // board changed

    } else if (o instanceof APlayer) {
      APlayer player = (APlayer) o;
      // player changed (piece removed)
      if (arg instanceof Piece) {
        Piece p = (Piece) arg;

      }
    }
  }
}
