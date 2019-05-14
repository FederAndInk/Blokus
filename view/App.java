
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
import model.Piece;
import model.PlayerType;

/**
 * App
 */
public class App extends Application implements Observer {
  ResizableCanvas can;
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
  final double pieceMarginW = 20;
  final double pieceMarginH = 10;
  final StatusTimer timer = new StatusTimer() {
    @Override
    public void handle(long now) {
      // System.out.println(mouseX + " " + mouseY);
      if (timer.movingPiece != null) {
        // ColumnConstraints col = new ColumnConstraints(squareSize);
        // RowConstraints row = new RowConstraints(squareSize);

        // Vector<ColumnConstraints> colv = new Vector<>();
        // Vector<RowConstraints> rowv = new Vector<>();
        // for (int i = 0; i < timer.movingPiece.impl_getColumnCount(); i++) {
        // rowv.add(row);
        // }
        // for (int i = 0; i < timer.movingPiece.impl_getRowCount(); i++) {
        // colv.add(col);
        // }
        // timer.movingPiece.getColumnConstraints().addAll(colv);
        // timer.movingPiece.getRowConstraints().addAll(rowv);
        timer.movingPiece.setLayoutX(mouseX);
        timer.movingPiece.setLayoutY(mouseY);
      }
    }
  };;

  class ResizableCanvas extends Canvas {

    public ResizableCanvas() {
      // Redraw canvas when size changes.
      widthProperty().addListener(evt -> draw());
      heightProperty().addListener(evt -> draw());
    }

    private void draw() {
      double width = getWidth();
      double height = getHeight();

      GraphicsContext gc = getGraphicsContext2D();
      gc.clearRect(0, 0, width, height);

      gc.setStroke(Color.RED);
      gc.strokeLine(0, 0, width, height);
      gc.strokeLine(0, height, width, 0);
    }

    @Override
    public boolean isResizable() {
      return true;
    }

    @Override
    public double prefWidth(double height) {
      return getWidth();
    }

    @Override
    public double prefHeight(double width) {
      return getHeight();
    }
  }

  @Override
  public void init() throws Exception {
    super.init();
    game = new Game();
    game.setApp(this);
    game.addPlayer(PlayerType.USER);
    game.addPlayer(PlayerType.USER);
  }

  class StatusTimer extends AnimationTimer {

    private boolean running;
    private GridPane movingPiece = null;

    public void setMovingPiece(GridPane movingPiece) {
      this.movingPiece = movingPiece;
    }

    public void clearMovingPiece() {
      this.movingPiece = null;
    }

    @Override
    public void start() {
      super.start();
      running = true;
    }

    @Override
    public void stop() {
      super.stop();
      running = false;
    }

    public boolean isRunning() {
      return running;
    }

    @Override
    public void handle(long now) {

    }

  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    can = new ResizableCanvas();
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

    primaryStage.setMinHeight(700);
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
        });
        pane.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent t) {
            System.out.printf("Mouse clicked cell [%d, %d]%n", col, row);
          }
        });
        boardGame.add(pane, col, row);
      }
    }
    boardGame.getColumnConstraints().setAll(colv);
    boardGame.getRowConstraints().setAll(rowv);
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
    drawPieces(primaryStage.getWidth() - pieceListWidth, pieceListHeight, pieceListWidth);
    // -----------------------------------
    primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
      System.out.println("width = " + primaryStage.getWidth() + " height = " + primaryStage.getHeight());
      System.out.println("width = " + primaryStage.getWidth() * 0.90 + " height = " + primaryStage.getHeight() * 0.70);
      boardGameWidth = (double) primaryStage.getWidth();
      updateBoardSize(boardGameWidth, boardGameHeight);
    });
    primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
      System.out.println("width = " + primaryStage.getWidth() + " height = " + primaryStage.getHeight());
      System.out.println("width = " + primaryStage.getWidth() * 0.90 + " height = " + primaryStage.getHeight() * 0.70);
      boardGameHeight = (double) primaryStage.getHeight();
      updateBoardSize(boardGameWidth, boardGameHeight);
    });
    pieceList.widthProperty().addListener((observable, oldValue, newValue) -> {
      System.out.println("jehvsevbiawubvcio3cn");
      pieceListWidth = (double) newValue;
      drawPieces(primaryStage.getWidth() - pieceListWidth, pieceListHeight, pieceListWidth);
    });
    pieceList.heightProperty().addListener((observable, oldValue, newValue) -> {
      pieceListHeight = (double) newValue;
      drawPieces(primaryStage.getWidth() - pieceListWidth, pieceListHeight, pieceListWidth);
    });
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

  public void drawPieces(Double x, Double y, Double width) {
    System.out.println(root.getChildren().size());
    root.getChildren().remove(1, root.getChildren().size());
    for (int i = 0; i < game.getNbPlayers(); i++) {
      Double currenty = y / game.getNbPlayers() * i + borderSize;
      Double currentx = x + borderSize;
      Double height = y / game.getNbPlayers();
      ArrayList<Piece> pieces = game.getPlayers().get(i).getPieces();

      int pieceSize = 10;

      ColumnConstraints col = new ColumnConstraints(pieceSize);
      RowConstraints row = new RowConstraints(pieceSize);

      for (int j = 0; j < pieces.size(); j++) {
        int nbRow = 0;
        Vector<ColumnConstraints> colv = new Vector<>();
        Vector<RowConstraints> rowv = new Vector<>();
        HashSet<Coord> shape = pieces.get(j).getShape();
        GridPane grid = new GridPane();
        // System.out.println("la taille de la piece est : " + shape.size());
        for (Coord var : shape) {
          Pane p = new Pane();
          p.setMaxWidth(Double.MAX_VALUE);
          p.setMaxHeight(Double.MAX_VALUE);
          p.setBackground(
              new Background(new BackgroundFill(game.getPlayers().get(i).getColor(), CornerRadii.EMPTY, Insets.EMPTY)));
          System.out.println(var.x + " " + var.y);
          grid.add(p, var.x, var.y);
          // colv.add(col);
          // rowv.add(row);
          p.setBorder((new Border(
              new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))));
        }
        grid.setBorder((new Border(
            new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))));
        int nbCol = grid.impl_getColumnCount();
        if (grid.impl_getRowCount() > nbRow) {
          nbRow = grid.impl_getRowCount();
        }
        for (int l = 0; l < nbRow; l++) {
          rowv.add(row);
        }
        for (int l = 0; l < nbCol; l++) {
          colv.add(col);
        }
        System.out.println("nbow = " + nbRow + " nbcol = " + nbCol + " " + colv.size() + " " + rowv.size());
        grid.getColumnConstraints().setAll(colv);
        grid.getRowConstraints().setAll(rowv);
        currentx = currentx + pieceMarginW;
        if ((currentx + pieceSize * nbCol) > (width + x)) {
          currentx = x + borderSize + pieceMarginW;
          currenty = currenty + pieceMarginH + nbRow * pieceSize;
        }
        grid.setLayoutX(currentx);
        grid.setLayoutY(currenty);
        currentx = currentx + pieceSize * nbCol;
        grid.toFront();
        root.getChildren().add(grid);
        grid.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent t) {
            if (timer.isRunning()) {
              timer.stop();
              timer.clearMovingPiece();
            } else {
              timer.setMovingPiece(grid);
              timer.start();
            }
          }
        });
      }
      // System.out.println(i + "eme pane player = " + x + " " + y /
      // game.getNbPlayers() * i);
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
    // if (condition) {
    //
    // }
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