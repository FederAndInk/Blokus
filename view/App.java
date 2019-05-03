
package view;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import controller.Game;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.APlayer;
import model.Board;

/**
 * App
 */
public class App extends Application implements Observer {
  Canvas can;
  GridPane boardGame;
  double boardGameWidth;
  double boardGameHeight;
  Game game;

  @Override
  public void init() throws Exception {
    super.init();
    game = new Game(this, 2);

  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    GridPane mainGrid = new GridPane();
    GridPane pieceList = new GridPane();
    GridPane gridlayoutMenu = new GridPane();
    GridPane menuGrid = new GridPane();
    boardGame = new GridPane();
    pieceList.setGridLinesVisible(true);
    mainGrid.setGridLinesVisible(true);
    gridlayoutMenu.setGridLinesVisible(true);
    menuGrid.setGridLinesVisible(true);
    boardGame.setGridLinesVisible(true);
    Scene sc = new Scene(mainGrid);
    primaryStage.setScene(sc);
    primaryStage.show();

    ColumnConstraints collumnSize = new ColumnConstraints();
    collumnSize.setPercentWidth(100);
    RowConstraints rowSize = new RowConstraints();
    rowSize.setPercentHeight(100);

    // -----------------------------------
    ColumnConstraints menuButtonSize = new ColumnConstraints();
    menuButtonSize.setPercentWidth(100.0 / 6.0);
    Vector<ColumnConstraints> cc2 = new Vector<>();
    for (int i = 0; i < 6; i++) {
      cc2.add(menuButtonSize);
      menuGrid.add(new Button("meh"), i, 0);
    }
    menuGrid.getColumnConstraints().setAll(cc2);
    menuGrid.getRowConstraints().setAll(rowSize);
    // -----------------------------------
    ColumnConstraints col = new ColumnConstraints();
    col.setPercentWidth(100.0 / game.getBoard().getSize());
    RowConstraints row = new RowConstraints();
    row.setPercentHeight(100.0 / game.getBoard().getSize());
    Vector<ColumnConstraints> colv = new Vector<>();
    Vector<RowConstraints> rowv = new Vector<>();
    for (int i = 0; i < game.getBoard().getSize(); i++) {
      colv.add(col);
    }
    for (int i = 0; i < game.getBoard().getSize(); i++) {
      rowv.add(row);
    }
    for (int i = 0; i < game.getBoard().getSize(); i++) {
      for (int j = 0; j < game.getBoard().getSize(); j++) {
        boardGame.add(new Label("wallah"), i, j);
      }
    }
    boardGame.getColumnConstraints().setAll(colv);
    boardGame.getRowConstraints().setAll(rowv);
    // -----------------------------------
    RowConstraints pieceSize = new RowConstraints();
    pieceSize.setPercentHeight(100.0 / game.getNbPlayers());
    Vector<RowConstraints> cc = new Vector<>();
    for (int i = 0; i < game.getNbPlayers(); i++) {
      cc.add(pieceSize);
      pieceList.add(new Label("meh"), 0, i);
    }
    pieceList.getRowConstraints().setAll(cc);
    pieceList.getColumnConstraints().setAll(collumnSize);
    // -----------------------------------
    RowConstraints boardSize = new RowConstraints();
    boardSize.setPercentHeight(90);
    RowConstraints menuSize = new RowConstraints();
    menuSize.setPercentHeight(10);
    gridlayoutMenu.add(menuGrid, 0, 0);
    gridlayoutMenu.add(boardGame, 0, 1);
    boardGame.setAlignment(Pos.CENTER);
    gridlayoutMenu.getRowConstraints().setAll(menuSize, boardSize);
    gridlayoutMenu.getColumnConstraints().setAll(collumnSize);
    // ------------------------------------
    ColumnConstraints gridlayoutMenuSize = new ColumnConstraints();
    gridlayoutMenuSize.setPercentWidth(70);
    ColumnConstraints pieceListSize = new ColumnConstraints();
    pieceListSize.setPercentWidth(30);
    mainGrid.add(gridlayoutMenu, 0, 0);
    mainGrid.add(pieceList, 1, 0);
    mainGrid.getColumnConstraints().setAll(gridlayoutMenuSize, pieceListSize);
    mainGrid.getRowConstraints().setAll(rowSize);
    // -----------------------------------
    primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
      System.out.println("width = " + primaryStage.getWidth() + " height = " + primaryStage.getHeight());
      System.out.println("width = " + primaryStage.getWidth() * 0.90 + " height = " + primaryStage.getHeight() * 0.70);
      boardGameWidth = (double) primaryStage.getWidth() * 0.70;
      updateBoardSize(boardGameWidth, boardGameHeight);
    });
    primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
      System.out.println("width = " + primaryStage.getWidth() + " height = " + primaryStage.getHeight());
      System.out.println("width = " + primaryStage.getWidth() * 0.90 + " height = " + primaryStage.getHeight() * 0.70);
      boardGameHeight = (double) primaryStage.getHeight() * 0.90;
      updateBoardSize(boardGameWidth, boardGameHeight);
    });
  }

  public void updateBoardSize(double width, double height) {
    ColumnConstraints col = new ColumnConstraints(
        (double) Math.min(width, height) / ((double) game.getBoard().getSize() + 1));
    RowConstraints row = new RowConstraints(
        (double) Math.min(width, height) / ((double) game.getBoard().getSize() + 1));
    Vector<ColumnConstraints> colv = new Vector<>();
    Vector<RowConstraints> rowv = new Vector<>();
    for (int i = 0; i < game.getBoard().getSize(); i++) {
      colv.add(col);
    }
    for (int i = 0; i < game.getBoard().getSize(); i++) {
      rowv.add(row);
    }
    boardGame.getColumnConstraints().setAll(colv);
    boardGame.getRowConstraints().setAll(rowv);
  }

  @Override
  public void update(Observable o, Object arg) {
    if (o instanceof Board) {
      Board b = (Board) o;
      // board changed

    } else if (o instanceof APlayer) {
      APlayer player = (APlayer) o;
      // player changed (piece removed)
    }
  }
}