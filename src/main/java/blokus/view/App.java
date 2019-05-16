
package blokus.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import blokus.controller.Game;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;
import blokus.model.APlayer;
import blokus.model.BigPieceChooser;
import blokus.model.Board;
import blokus.model.Computer;
import blokus.model.Coord;
import blokus.model.Piece;
import blokus.model.Player;
import blokus.model.PlayerType;
import blokus.model.RandomPieceAI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * App
 */
public class App extends Application implements IApp {
  Group root;
  Stage primaryStage;
  Scene sc;
  double squareSize = 0;
  IntelligentGridPane boardGame;
  double boardGameWidth;
  double boardGameHeight;
  double pieceListWidth;
  double pieceListHeight;
  IntelligentGridPane pieceList;
  ArrayList<Pane> panVect = new ArrayList<>();
  ArrayList<Button> buttonArray = new ArrayList<>();
  Game game;
  double mouseX = 0;
  double mouseY = 0;
  final double widthPercentBoard = 0.7;
  final double heightPercentBoard = 0.9;
  double borderSize = BorderWidths.DEFAULT.getLeft();
  ArrayList<ArrayList<Piece>> poolPlayer;

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

  public void setActive(Color col) {
    for (int i = 0; i < panVect.size(); i++) {
      if (i != game.getCurPlayerNo()) {
        panVect.get(i)
            .setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
      } else {
        panVect.get(i).setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
      }
    }
  }

  private void newGame() {
    ArrayList<PlayerType> listPType = new ArrayList<>();
    if (game != null) {
      System.out.println("---------------------------------");
      for (APlayer var : game.getPlayers()) {
        if (var instanceof Player) {
          listPType.add(PlayerType.USER);
        } else if (var instanceof RandomPieceAI) {
          listPType.add(PlayerType.RANDOM_PIECE);
        } else if (var instanceof Computer) {
          listPType.add(PlayerType.RANDOM_PLAY);

        }
      }
    }
    game = new Game();
    game.setApp(this);
    game.addPlayer(PlayerType.USER);
    game.addPlayer(PlayerType.RANDOM_PIECE);
    if (primaryStage != null) {
      poolPlayer.clear();
      for (int i = 0; i < game.getNbPlayers(); i++) {
        System.out.println(game.getPlayers().get(i).getPieces().size());
        poolPlayer.add(game.getPlayers().get(i).getPieces());
      }
      drawPieces(primaryStage.getWidth() - pieceListWidth, pieceListHeight, pieceListWidth, sc);
      redrawBoard();
    }
  }

  @Override
  public void init() throws Exception {
    super.init();
    newGame();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    this.primaryStage = primaryStage;
    primaryStage.setTitle("blokus");
    IntelligentGridPane mainGrid = new IntelligentGridPane();
    pieceList = new IntelligentGridPane();
    IntelligentGridPane gridlayoutMenu = new IntelligentGridPane();
    IntelligentGridPane unredoMenu = new IntelligentGridPane();
    IntelligentGridPane menuGrid = new IntelligentGridPane();

    boardGame = new IntelligentGridPane();

    root = new Group();
    StatusTimer permanentTimer = new StatusTimer() {
      @Override
      public void handle(long now) {
        game.refresh();
      }
    };
    permanentTimer.start();

    root.autoSizeChildrenProperty();

    // pieceList.setGridLinesVisible(true);
    mainGrid.setGridLinesVisible(true);
    gridlayoutMenu.setGridLinesVisible(true);
    menuGrid.setGridLinesVisible(true);
    // boardGame.setGridLinesVisible(true);

    root.getChildren().add(mainGrid);

    Scene sc = new Scene(root);
    this.sc = sc;
    primaryStage.setScene(sc);
    primaryStage.show();

    primaryStage.setMinHeight(720);
    primaryStage.setMinWidth(800);

    ColumnConstraints collumnSize = new ColumnConstraints();
    collumnSize.setPercentWidth(100);
    RowConstraints rowSize = new RowConstraints();
    rowSize.setPercentHeight(100);

    poolPlayer = new ArrayList<>();
    for (int i = 0; i < game.getNbPlayers(); i++) {
      poolPlayer.add(game.getPlayers().get(i).getPieces());
    }

    // ----------------------------------- button menu
    ColumnConstraints menuButtonSize = new ColumnConstraints();
    menuButtonSize.setPercentWidth(100.0 / 3.0);
    Button quit = new Button("Quit");
    Button newGame = new Button("New Game");
    Button options = new Button("Options");
    Button redo = new Button("Undo");
    Button undo = new Button("Redo");
    Button Hints = new Button("Hints");
    newGame.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        newGame();
      }
    });
    quit.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        Platform.exit();
      }
    });
    buttonArray.add(quit);
    buttonArray.add(newGame);
    buttonArray.add(options);
    buttonArray.add(redo);
    buttonArray.add(undo);
    buttonArray.add(Hints);
    ArrayList<ColumnConstraints> cc2 = new ArrayList<>();
    ArrayList<ColumnConstraints> cc3 = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      Button b = buttonArray.get(i);
      b.setMaxWidth(Double.MAX_VALUE);
      b.setMaxHeight(Double.MAX_VALUE);
      cc2.add(menuButtonSize);
      menuGrid.add(b, i, 0);
    }
    for (int i = 3; i < 6; i++) {
      Button b = buttonArray.get(i);
      b.setMaxWidth(Double.MAX_VALUE);
      b.setMaxHeight(Double.MAX_VALUE);
      cc3.add(menuButtonSize);
      unredoMenu.add(b, i - 3, 0);
    }
    menuGrid.getColumnConstraints().setAll(cc2);
    menuGrid.getRowConstraints().setAll(rowSize);
    unredoMenu.getColumnConstraints().setAll(cc3);
    unredoMenu.getRowConstraints().setAll(rowSize);
    // ----------------------------------- player menu
    RowConstraints pieceSize = new RowConstraints();
    pieceSize.setPercentHeight(100.0 / game.getNbPlayers());
    ArrayList<RowConstraints> cc = new ArrayList<>();
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
    menuSize.setPercentHeight(5);
    RowConstraints unreSize = new RowConstraints();
    unreSize.setPercentHeight(5);
    gridlayoutMenu.add(menuGrid, 0, 0);
    gridlayoutMenu.add(boardGame, 0, 1);
    gridlayoutMenu.add(unredoMenu, 0, 2);
    boardGame.setAlignment(Pos.CENTER);
    gridlayoutMenu.getRowConstraints().setAll(menuSize, boardConstraint, unreSize);
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
    pieceList.addEventFilter(MouseEvent.MOUSE_PRESSED, (t) -> {
      if (timer.movingPiece != null) {
        timer.cancelMove();
      }
    });
    // ----------------------------------- game board
    int boardSize = game.getBoard().SIZE.x;

    ColumnConstraints colc = new ColumnConstraints();
    colc.setPercentWidth(100.0 / boardSize);
    RowConstraints rowc = new RowConstraints();
    rowc.setPercentHeight(100.0 / boardSize);
    ArrayList<ColumnConstraints> colv = new ArrayList<>();
    ArrayList<RowConstraints> rowv = new ArrayList<>();
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
          // System.out.printf("Mouse entered cell [%d, %d]%n", col, row);
          // System.out.println(pane.getLayoutX() + " " + pane.getLayoutY());
          // if (timer.movingPiece != null &&
          // game.getBoard().canAdd(timer.movingPiece.piece, new Coord(col, row),
          // game.getPlayers().get(timer.movingPiece.playerNumber).getColor())) {
          if (timer.movingPiece != null) {
            timer.movingPiece.setLayoutX(pane.getLayoutX() + boardGame.getLayoutX());
            timer.movingPiece.setLayoutY(pane.getLayoutY() + boardGame.getLayoutY());
          }
        });
        pane.addEventFilter(MouseEvent.MOUSE_PRESSED, (t) -> {
          if (t.getButton() == MouseButton.PRIMARY) {
            System.out.printf("Mouse clicked cell [%d, %d]%n", col, row);
            System.out.println(pane.getLayoutX() + " " + pane.getLayoutY());
            if (timer.movingPiece != null) {
              timer.movingPiece.setLayoutX(pane.getLayoutX() + boardGame.getLayoutX());
              timer.movingPiece.setLayoutY(pane.getLayoutY() + boardGame.getLayoutY());
            }
            Coord pos = new Coord(col, row);
            if (timer.isRunning()
                && game.getBoard().canAdd(timer.movingPiece.piece, pos, game.getCurPlayer().getColor())) {
              game.inputPlay(timer.movingPiece.piece, pos);
              timer.stop();
              // root.getChildren().remove(timer.movingPiece);
              // drawPieces(primaryStage.getWidth() - pieceListWidth, pieceListHeight,
              // pieceListWidth, sc);
            }
          }
        });
        boardGame.add(pane, col, row);
        pane.setBorder((new Border(
            new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))));
        System.out.println(IntelligentGridPane.getColumnIndex(pane));
        System.out.println(IntelligentGridPane.getRowIndex(pane));
      }
    }
    setActive(game.getCurPlayer().getColor());
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

    sc.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent t) {
        if (timer.isRunning()) {
          if (t.getButton() == MouseButton.SECONDARY) {
            timer.cancelMove();
          }
        }
      }
    });

  }

  public void drawPieces(Double x, Double y, Double width, Scene sc) {
    System.out.println(root.getChildren().size());
    root.getChildren().remove(1, root.getChildren().size());
    for (int i = 0; i < game.getNbPlayers(); i++) {
      Double currenty = y / game.getNbPlayers() * i + borderSize + 5;
      Double currentx = x + borderSize;
      Double height = y / game.getNbPlayers();

      System.out.println(width + " " + height);

      double pieceSize = width / 34.0;
      // double pieceSize = Math.min(width / 34.0,height/);

      int maxNbRow = 0;

      for (int j = 0; j < poolPlayer.get(i).size(); j++) {
        PieceView p = new PieceView(poolPlayer.get(i).get(j), game, pieceSize, i);
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
            if (t.getButton() == MouseButton.PRIMARY) {
              System.out.println(game.getCurPlayer().getColor() == game.getPlayers().get(p.playerNumber).getColor());
              if (game.getCurPlayer().getColor() == game.getPlayers().get(p.playerNumber).getColor()) {

                p.setMouseTransparent(true);
                sc.setOnKeyPressed(e -> {
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
    ArrayList<ColumnConstraints> colv = new ArrayList<>();
    ArrayList<RowConstraints> rowv = new ArrayList<>();
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

  public Pane get(int x, int y) {
    Pane res = null;
    for (int i = 0; i < boardGame.getChildren().size(); i++) {
      if (boardGame.getChildren().get(i) instanceof Pane) {
        Pane tempPane = (Pane) boardGame.getChildren().get(i);
        if ((IntelligentGridPane.getColumnIndex(tempPane) == x) && (IntelligentGridPane.getRowIndex(tempPane) == y)) {
          res = tempPane;
        }
      }
    }
    return res;
  }

  public void redrawBoard() {
    for (int i = 0; i < game.getBoard().SIZE.x; i++) {
      for (int j = 0; j < game.getBoard().SIZE.y; j++) {
        Pane pane = (Pane) get(i, j);
        pane.setBackground(
            new Background(new BackgroundFill(game.getBoard().get(i, j), CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setBorder((new Border(
            new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))));
        ;
      }
    }
  }

  public PieceView getPieceView(Piece p) {
    PieceView res = null;
    for (int i = 0; i < root.getChildren().size(); i++) {
      if (root.getChildren().get(i) instanceof PieceView) {
        PieceView possibleRes = (PieceView) root.getChildren().get(i);
        if (possibleRes.piece == p) {
          res = possibleRes;
        }
      }
    }
    return res;
  }

  @Override
  public void update(APlayer oldPlayer, Piece playedPiece) {
    System.out.println("Model changed");
    setActive(game.getCurPlayer().getColor());
    poolPlayer = new ArrayList<>();
    poolPlayer.clear();
    for (int i = 0; i < game.getNbPlayers(); i++) {
      System.out.println(game.getPlayers().get(i).getPieces().size());
      poolPlayer.add(game.getPlayers().get(i).getPieces());
    }
    root.getChildren().remove(getPieceView(playedPiece));
    drawPieces(primaryStage.getWidth() - pieceListWidth, pieceListHeight, pieceListWidth, sc);
    redrawBoard();
    System.out.println(game.getBoard());
    for (APlayer p : game.getPlayers()) {
      System.out.println("Nb piece " + Board.getColorName(p.getColor()) + ": " + p.getPieces().size());
    }
    if (game.isEndOfGame()) {
      displayEOG();
      for (Entry<Color, Integer> c : game.getScore().entrySet()) {
        System.out.println(Board.getColorName(c.getKey()) + ": " + c.getValue());
      }
      // try {
      // Thread.sleep(7000);
      // } catch (InterruptedException e) {
      // e.printStackTrace();
      // }
      // newGame();
    }
  }

  private void displayEOG() {
    APlayer winner = game.getWinnerPlayer();
    Label secondLabel = new Label("le joueur " + Board.getColorName(winner.getColor()) + " est meilleur");

    IntelligentGridPane secondaryLayout = new IntelligentGridPane();
    IntelligentGridPane buttonPane = new IntelligentGridPane();
    IntelligentGridPane LabelPane = new IntelligentGridPane();

    Button quit = new Button("quit");
    Button newGame = new Button("new game");

    quit.setMaxWidth(Double.MAX_VALUE);
    quit.setMaxHeight(Double.MAX_VALUE);
    newGame.setMaxWidth(Double.MAX_VALUE);
    newGame.setMaxHeight(Double.MAX_VALUE);

    Scene secondScene = new Scene(secondaryLayout, 230, 100);
    Stage newWindow = new Stage();
    newGame.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        newWindow.close();
        newGame();
      }
    });
    quit.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        Platform.exit();
      }
    });

    newWindow.setTitle("Second Stage");
    newWindow.setScene(secondScene);

    newWindow.setX(primaryStage.getX() + 200);
    newWindow.setY(primaryStage.getY() + 100);

    newWindow.show();
    RowConstraints rc = new RowConstraints();
    rc.setPercentHeight(50);
    ColumnConstraints lc = new ColumnConstraints();
    lc.setPercentWidth(100);
    ColumnConstraints lc2 = new ColumnConstraints();
    lc2.setPercentWidth(30);
    ColumnConstraints lc3 = new ColumnConstraints();
    lc3.setPercentWidth(20);
    secondaryLayout.getRowConstraints().addAll(rc, rc);
    secondaryLayout.getColumnConstraints().addAll(lc);
    secondaryLayout.add(LabelPane, 0, 0);
    secondaryLayout.add(buttonPane, 0, 1);
    LabelPane.add(secondLabel, 0, 0);
    buttonPane.add(quit, 1, 0);
    buttonPane.add(newGame, 2, 0);
    buttonPane.getColumnConstraints().addAll(lc3, lc2, lc2, lc3);
  }
}