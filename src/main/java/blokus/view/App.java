
package blokus.view;

import blokus.controller.Game;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * App
 */
public class App extends Application implements IApp {

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
  public void update() {
  }
}
