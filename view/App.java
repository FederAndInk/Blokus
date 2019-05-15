
package view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import controller.Game;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.APlayer;
import model.Board;
import model.Coord;
import model.Move;
import model.Piece;
import model.PieceTransform;
import model.PlayerType;

/**
 * App
 */
public class App extends Application implements Observer {
  Group root;
  double squareSize = 0;
  GridPane boardGame;
  double boardGameWidth;
  double boardGameHeight;
  double pieceListWidth;
  double pieceListHeight;
  GridPane pieceList;
  Vector<Pane> panVect = new Vector<>();
  Game game;
  double mouseX = 0;
  double mouseY = 0;
  final double widthPercentBoard = 0.7;
  final double heightPercentBoard = 0.9;
  double borderSize = BorderWidths.DEFAULT.getLeft();
  int num = 0;

  public Boolean isInBord(double mx, double my) {
    double width = squareSize * game.getBoard().SIZE.x;
    double height = squareSize * game.getBoard().SIZE.y;
    double x = (boardGameWidth * widthPercentBoard - width) / 2;
    return (mx < (x + width) && my < (boardGame.getLayoutY() + height) && mx > (x) && my > (boardGame.getLayoutY()));
  }

  final StatusTimer timer = new StatusTimer() {
    @Override
    public void handle(long now) {
      // System.out.println(mouseX + " " + mouseY);
      timer.movingPiece.setSizeSquare(squareSize);
      if (timer.movingPiece != null && !isInBord(mouseX, mouseY)) {
        timer.movingPiece.toFront();
        timer.movingPiece.setLayoutX(mouseX);
        timer.movingPiece.setLayoutY(mouseY);
      }
    }
  };

  public void setActive(int num) {
    for (int i = 0; i < panVect.size(); i++) {
      if (i != num) {
        panVect.get(i)
            .setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
      } else {
        panVect.get(i).setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
      }
    }
  }

  @Override
  public void init() throws Exception {
    super.init();
    game = new Game();
    game.setApp(this);
    game.addPlayer(PlayerType.USER);
    game.addPlayer(PlayerType.USER);
    game.addPlayer(PlayerType.USER);
    game.addPlayer(PlayerType.USER);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("blokus");
    GridPane mainGrid = new GridPane();
    pieceList = new GridPane();
    GridPane gridlayoutMenu = new GridPane();
    GridPane menuGrid = new GridPane();

    boardGame = new GridPane();

    root = new Group();

    root.autoSizeChildrenProperty();

    pieceList.setGridLinesVisible(true);
    mainGrid.setGridLinesVisible(true);
    gridlayoutMenu.setGridLinesVisible(true);
    menuGrid.setGridLinesVisible(true);
    boardGame.setGridLinesVisible(true);

    root.getChildren().add(mainGrid);

    Scene sc = new Scene(root);
    primaryStage.setScene(sc);
    primaryStage.show();

    primaryStage.setMinHeight(720);
    primaryStage.setMinWidth(800);

    ColumnConstraints collumnSize = new ColumnConstraints();
    collumnSize.setPercentWidth(100);
    RowConstraints rowSize = new RowConstraints();
    rowSize.setPercentHeight(100);

    // ----------------------------------- button menu
    ColumnConstraints menuButtonSize = new ColumnConstraints();
    menuButtonSize.setPercentWidth(100.0 / 6.0);
    Button quit = new Button("quit");
    quit.setMaxWidth(Double.MAX_VALUE);
    quit.setMaxHeight(Double.MAX_VALUE);
    Vector<ColumnConstraints> cc2 = new Vector<>();
    cc2.add(menuButtonSize);
    menuGrid.add(quit, 0, 0);
    for (int i = 1; i < 6; i++) {
      Button b = new Button("other");
      b.setMaxWidth(Double.MAX_VALUE);
      b.setMaxHeight(Double.MAX_VALUE);
      cc2.add(menuButtonSize);
      menuGrid.add(b, i, 0);
    }
    menuGrid.getColumnConstraints().setAll(cc2);
    menuGrid.getRowConstraints().setAll(rowSize);

    // ----------------------------------- player menu
    RowConstraints pieceSize = new RowConstraints();
    pieceSize.setPercentHeight(100.0 / game.getNbPlayers());
    Vector<RowConstraints> cc = new Vector<>();
    for (int i = 0; i < game.getNbPlayers(); i++) {
      cc.add(pieceSize);
      Pane f = new Pane();
      panVect.add(f);
      f.setMaxWidth(Double.MAX_VALUE);
      f.setMaxHeight(Double.MAX_VALUE);
      // f.setBackground(new Background(new BackgroundFill(Color.web("#" + "ffff00"),
      // CornerRadii.EMPTY, Insets.EMPTY)));
      f.setBorder((new Border(
          new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))));
      pieceList.add(panVect.get(i), 0, i);
    }
    pieceList.getRowConstraints().setAll(cc);
    pieceList.getColumnConstraints().setAll(collumnSize);
    // -----------------------------------
    RowConstraints boardConstraint = new RowConstraints();
    boardConstraint.setPercentHeight(90);
    RowConstraints menuSize = new RowConstraints();
    menuSize.setPercentHeight(10);
    gridlayoutMenu.add(menuGrid, 0, 0);
    gridlayoutMenu.add(boardGame, 0, 1);
    boardGame.setAlignment(Pos.CENTER);
    gridlayoutMenu.getRowConstraints().setAll(menuSize, boardConstraint);
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

    drawPieces(primaryStage.getWidth() - pieceListWidth, pieceListHeight, pieceListWidth, sc);
    // -----------------------------------
    primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
      boardGameWidth = (double) primaryStage.getWidth();
      updateBoardSize(boardGameWidth, boardGameHeight);
    });
    primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
      boardGameHeight = (double) primaryStage.getHeight();
      updateBoardSize(boardGameWidth, boardGameHeight);
    });
    pieceList.widthProperty().addListener((observable, oldValue, newValue) -> {
      pieceListWidth = (double) newValue;
      drawPieces(primaryStage.getWidth() - pieceListWidth, pieceListHeight, pieceListWidth, sc);
    });
    pieceList.heightProperty().addListener((observable, oldValue, newValue) -> {
      pieceListHeight = (double) newValue;
      drawPieces(primaryStage.getWidth() - pieceListWidth, pieceListHeight, pieceListWidth, sc);
    });
    // ----------------------------------- game board
    int boardSize = game.getBoard().SIZE.x;

    ColumnConstraints colc = new ColumnConstraints();
    colc.setPercentWidth(100.0 / boardSize);
    RowConstraints rowc = new RowConstraints();
    rowc.setPercentHeight(100.0 / boardSize);
    Vector<ColumnConstraints> colv = new Vector<>();
    Vector<RowConstraints> rowv = new Vector<>();
    for (int i = 0; i < boardSize; i++) {
      colv.add(colc);
    }
    for (int i = 0; i < boardSize; i++) {
      rowv.add(rowc);
    }
    for (int i = 0; i < boardSize; i++) {
      for (int j = 0; j < boardSize; j++) {
        Pane pane = new Pane();
        final int col = i;
        final int row = j;
        pane.setOnMouseEntered(e -> {
          System.out.printf("Mouse entered cell [%d, %d]%n", col, row);
          System.out.println(pane.getLayoutX() + " " + pane.getLayoutY());
          // if (timer.movingPiece != null &&
          // game.getBoard().canAdd(timer.movingPiece.piece, new Coord(col, row),
          // game.getPlayers().get(timer.movingPiece.playerNumber).getColor())) {
          if (timer.movingPiece != null) {
            timer.movingPiece.setLayoutX(pane.getLayoutX() + boardGame.getLayoutX());
            timer.movingPiece.setLayoutY(pane.getLayoutY() + boardGame.getLayoutY());
          }
        });
        pane.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent t) {
            System.out.printf("Mouse clicked cell [%d, %d]%n", col, row);
            System.out.println(pane.getLayoutX() + " " + pane.getLayoutY());
            if (timer.movingPiece != null) {
              timer.movingPiece.setLayoutX(pane.getLayoutX() + boardGame.getLayoutX());
              timer.movingPiece.setLayoutY(pane.getLayoutY() + boardGame.getLayoutY());
            }
            if (timer.isRunning() && game.getBoard().canAdd(timer.movingPiece.piece, new Coord(col, row),
                game.getPlayers().get(timer.movingPiece.playerNumber).getColor())) {
              timer.stop();
              game.nextPlayer();
              System.out.println(game.getNbPlayers());
              num = (num + 1) % game.getNbPlayers();
              setActive(num);
              System.out.println(num);
            }
          }
        });
        boardGame.add(pane, col, row);
      }
    }
    setActive(0);
    boardGame.getColumnConstraints().setAll(colv);
    boardGame.getRowConstraints().setAll(rowv);
    // quit.setOnMouseClicked((e) -> {
    // quit.setLayoutX(MouseInfo.getPointerInfo().getLocation().x);
    // quit.setLayoutY(MouseInfo.getPointerInfo().getLocation().y);
    // });
    // Button test = new Button("test");
    // test.setLayoutX(250);
    // test.setLayoutY(250);
    // can.widthProperty().bind(primaryStage.widthProperty());
    // can.heightProperty().bind(primaryStage.heightProperty());
    // root.getChildren().add(can);

    // can.draw();
    // mainGrid.toFront();
    // test.setOnMouseClicked(new EventHandler<MouseEvent>() {
    // @Override
    // public void handle(MouseEvent mouseEvent) {
    // System.out.println("test");
    // }
    // });

    sc.setOnMouseMoved(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent event) {
        mouseX = event.getSceneX();
        mouseY = event.getSceneY();
      }

    });

    // sc.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
    // @Override
    // public void handle(MouseEvent mouseEvent) {
    // if (timer.isRunning()) {
    // timer.stop();
    // } else {
    // timer.start();
    // }
    // }
    // });

  }

  public void drawPieces(Double x, Double y, Double width, Scene sc) {
    System.out.println(root.getChildren().size());
    root.getChildren().remove(1, root.getChildren().size());
    for (int i = 0; i < game.getNbPlayers(); i++) {
      Double currenty = y / game.getNbPlayers() * i + borderSize + 5;
      Double currentx = x + borderSize;
      Double height = y / game.getNbPlayers();
      ArrayList<Piece> pieces = game.getPlayers().get(i).getPieces();

      System.out.println(width + " " + height);

      double pieceSize = width / 34.0;
      // double pieceSize = Math.min(width / 34.0,height/);

      int maxNbRow = 0;

      for (int j = 0; j < pieces.size(); j++) {
        PieceView p = new PieceView(pieces.get(j), game, pieceSize, i);
        if (p.nbRow > maxNbRow) {
          maxNbRow = p.nbRow;
        }
        currentx = currentx + p.pieceMarginW;
        if ((currentx + pieceSize * p.nbCol) > (width + x)) {
          currentx = x + borderSize + p.pieceMarginW;
          currenty = currenty + p.pieceMarginH + (maxNbRow) * pieceSize;
        }
        p.setSizeSquare(pieceSize);
        p.setLayoutX(currentx);
        p.setLayoutY(currenty);
        currentx = currentx + pieceSize * p.nbCol;
        root.getChildren().add(p);
        p.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent t) {
            System.out.println(game.getCurPlayer().getColor() == game.getPlayers().get(p.playerNumber).getColor());
            if (game.getCurPlayer().getColor() == game.getPlayers().get(p.playerNumber).getColor()) {

              p.setMouseTransparent(true);
              sc.setOnKeyPressed(e -> {
                System.out.println("awfafwfaffwfaw");
                if (e.getCode() == KeyCode.LEFT) {
                  // p.piece.apply(PieceTransform.LEFT);
                  p.piece.left();
                } else if (e.getCode() == KeyCode.UP) {
                  // p.piece.apply(PieceTransform.UP);
                  p.piece.revertX();
                } else if (e.getCode() == KeyCode.RIGHT) {
                  // p.piece.apply(PieceTransform.RIGHT);
                  p.piece.right();
                } else if (e.getCode() == KeyCode.DOWN) {
                  // p.piece.apply(PieceTransform.DOWN);
                  p.piece.revertY();
                }
                p.clearPiece();
                p.drawPiece();

              });
              // if (timer.isRunning()) {
              // timer.stop();
              // } else {
              timer.setMovingPiece(p);
              timer.start();
              // }
            }
          }
        });
      }
    }
  }

  public void updateBoardSize(double wwidth, double wheight) {
    int boardSize = game.getBoard().SIZE.x;

    double width = (double) wwidth * widthPercentBoard;
    double height = (double) wheight * heightPercentBoard;

    squareSize = (double) Math.min(width, height) / ((double) boardSize + 1);

    ColumnConstraints col = new ColumnConstraints(squareSize);
    RowConstraints row = new RowConstraints(squareSize);
    double restWidth = wwidth * 0.3;
    ColumnConstraints col2 = new ColumnConstraints(restWidth);
    Vector<ColumnConstraints> colv = new Vector<>();
    Vector<RowConstraints> rowv = new Vector<>();
    for (int i = 0; i < boardSize; i++) {
      colv.add(col);
    }
    for (int i = 0; i < boardSize; i++) {
      rowv.add(row);
    }
    boardGame.getColumnConstraints().setAll(colv);
    boardGame.getRowConstraints().setAll(rowv);
    pieceList.getColumnConstraints().setAll(col2);
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