
package blokus.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import blokus.controller.Game;
import blokus.model.APlayer;
import blokus.model.Board;
import blokus.model.Coord;
import blokus.model.GameType;
import blokus.model.Move;
import blokus.model.Piece;
import blokus.model.PlayStyle;
import blokus.model.PlayerType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * App
 */
public class App extends Application implements IApp {
  Group root;
  int mouseXSquare;
  int mouseYSquare;
  Stage primaryStage;
  Scene sc;
  double squareSize = 0;
  IntelligentGridPane boardGame;
  double boardGameWidth;
  double boardGameHeight;
  double pieceListWidth;
  double pieceListHeight;
  IntelligentGridPane pieceList = new IntelligentGridPane();
  ArrayList<Pane> panVect = new ArrayList<>();
  ArrayList<Button> buttonArray = new ArrayList<>();
  Game game;
  Slider hints;

  Button redo;
  Button undo;

  double mouseX = 0;
  double mouseY = 0;
  final double widthPercentBoard = 0.7;
  final double heightPercentBoard = 0.9;
  double borderSize = BorderWidths.DEFAULT.getLeft();
  ArrayList<ArrayList<Piece>> poolPlayer;
  ArrayList<Pair<PlayerType, PlayStyle>> listPType = new ArrayList<>();
  HashMap<Color, Pair<Color, Color>> colorView;
  IntelligentGridPane menuGrid;
  double boardgameX;
  double boardgameY;
  FloatControl gainControl;

  public Boolean isInBord(double mx, double my) {
    double width = squareSize * (double) game.getBoard().getSize();
    double height = squareSize * (double) game.getBoard().getSize();
    double x = boardgameX;
    double y = boardgameY;
    return (mx <= (x + width) && my <= (y + height) && mx >= (x) && my >= (y));
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

  public void setActive() {
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
    game = new Game();
    clearPieceList();
    game.setApp(this);
    System.out.println(listPType.size());
    for (int i = 0; i < listPType.size(); i++) {
      System.out.println("ajout player " + listPType.get(i).getKey() + " " + listPType.get(i).getValue());
      game.addPlayer(listPType.get(i).getKey(), listPType.get(i).getValue());
    }
    // TODO: init with options
    if (game.getNbPlayers() == 2) {
      game.init(GameType.DUO);
    } else {
      game.init(GameType.BLOKUS);
    }
    colorView.put(game.getPlayers().get(0).getColor(), new Pair<>(Color.web("#" + "1879c9"), Color.web("#" + "5494c9")));
    colorView.put(game.getPlayers().get(1).getColor(), new Pair<>(Color.web("#" + "f2e126"), Color.web("#" + "fcf174")));
    if (game.getPlayers().size() > 2) {
      colorView.put(game.getPlayers().get(2).getColor(),
          new Pair<>(Color.web("#" + "fc1942"), Color.web("#" + "fc6480")));
      colorView.put(game.getPlayers().get(3).getColor(),
          new Pair<>(Color.web("#" + "22c157"), Color.web("#" + "75f49f")));
    }
    if (primaryStage != null) {
      poolPlayer.clear();
      for (int i = 0; i < game.getNbPlayers(); i++) {
        poolPlayer.add(game.getPlayers().get(i).getPieces());
      }
      redrawPieceList();
      // cleanBoard();
      drawPieces(primaryStage.getWidth() - pieceListWidth, pieceListHeight, pieceListWidth, sc);
      updateBoardSize(boardGameWidth, boardGameHeight);
      redrawBoard();
      setActive();
      setPossibleCorner(game.getCurPlayer().getColor());
    }
  }

  @Override
  public void init() throws Exception {
    super.init();
    listPType.add(new Pair<PlayerType, PlayStyle>(PlayerType.USER, null));
    listPType.add(new Pair<PlayerType, PlayStyle>(PlayerType.USER, null));

    colorView = new HashMap<>();
    newGame();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    this.primaryStage = primaryStage;
    primaryStage.setTitle("blokus");
    IntelligentGridPane mainGrid = new IntelligentGridPane();
    IntelligentGridPane gridlayoutMenu = new IntelligentGridPane();
    IntelligentGridPane unredoMenu = new IntelligentGridPane();
    menuGrid = new IntelligentGridPane();
    boardGame = new IntelligentGridPane();

    int depth = 70; // Setting the uniform variable for the glow width and height

    root = new Group();
    StatusTimer permanentTimer = new StatusTimer() {
      @Override
      public void handle(long now) {
        game.refresh();
      }
    };
    boardGame.setBackground(new Background(new BackgroundFill(Color.web("#f2f2f2"), CornerRadii.EMPTY, Insets.EMPTY)));
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

    primaryStage.setMinHeight(800);
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
    redo = new Button("Redo");
    undo = new Button("Undo");
    hints = new Slider(0, 4, 3);
    redo.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        game.redo();
        updateUndoRedoButtons();
      }
    });
    undo.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        game.undo();
        updateUndoRedoButtons();
      }
    });

    updateUndoRedoButtons();

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
    // options.setOnAction(new EventHandler<ActionEvent>() {
    // @Override
    // public void handle(ActionEvent e) {
    // if (game.getNbPlayers() == 2) {
    // listPType.add(PlayerType.RANDOM_PIECE);
    // listPType.add(PlayerType.RANDOM_PIECE);
    // } else {
    // listPType.remove(3);
    // listPType.remove(2);
    // }

    // newGame();

    // }

    // });
    options.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        displayOption();
      }
    });
    buttonArray.add(quit);
    buttonArray.add(newGame);
    buttonArray.add(options);
    buttonArray.add(undo);
    buttonArray.add(redo);

    ArrayList<ColumnConstraints> cc2 = new ArrayList<>();
    ArrayList<ColumnConstraints> cc3 = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      Button b = buttonArray.get(i);
      b.setMaxWidth(Double.MAX_VALUE);
      b.setMaxHeight(Double.MAX_VALUE);
      cc2.add(menuButtonSize);
      menuGrid.add(b, i, 0);
    }
    for (int i = 3; i < buttonArray.size(); i++) {
      Button b = buttonArray.get(i);
      b.setMaxWidth(Double.MAX_VALUE);
      b.setMaxHeight(Double.MAX_VALUE);
      cc3.add(menuButtonSize);
      unredoMenu.add(b, i - 3, 0);
    }
    hints.setMaxWidth(Double.MAX_VALUE);
    hints.setMaxHeight(Double.MAX_VALUE);
    hints.setShowTickMarks(true);
    hints.setShowTickLabels(true);
    hints.setSnapToTicks(true);
    hints.setMinorTickCount(1);
    hints.setMajorTickUnit(1);
    hints.setBlockIncrement(1.0);
    hints.setTooltip(new Tooltip("Niveau d'aide\n" + //
        "1. Aucune aide\n" + //
        "2. Les coins jouables sont surlignés\n" + //
        "3. Affiche où la pièce peut être jouée\n" + //
        "4. Grise les pièces non jouables"));
    hints.valueProperty().addListener((obs, oldval, newVal) -> {
      hints.setValue(Math.round(newVal.doubleValue()));
      cleanBoard();
      redrawBoard();
      setPossibleCorner(game.getCurPlayer().getColor());
      drawPieces(primaryStage.getWidth() - pieceListWidth, pieceListHeight, pieceListWidth, sc);
    });

    String musicFile = "katyusha-8-bit.wav";
    try {
      Clip clip = AudioSystem.getClip();
      AudioInputStream inputStream = AudioSystem.getAudioInputStream(ClassLoader.getSystemResourceAsStream(musicFile));
      clip.open(inputStream);
      // clip.start();
      clip.loop(Clip.LOOP_CONTINUOUSLY);
      gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(-20.0f); // Reduce volume by 10 decibels.
    } catch (Exception e) {
      e.printStackTrace();
    }

    // -----------------------------------------
    hints.setOnMouseReleased(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent e) {

      }
    });
    hints.focusedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        unredoMenu.requestFocus(); // Delegate the focus to container
      }
    });
    root.requestFocus();
    unredoMenu.add(hints, 2, 0);
    // Slider slider = new Slider();
    // slider.setMin(0);
    // slider.setMax(100);
    // slider.setValue(40);
    // slider.setShowTickLabels(true);
    // slider.setShowTickMarks(true);
    // slider.setMajorTickUnit(50);
    // slider.setMinorTickCount(5);
    // slider.setBlockIncrement(10);
    // slider.setSnapToTicks(true);
    // unredoMenu.add(slider, 2, 0);
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
      redrawBoard();
      setPossibleCorner(game.getCurPlayer().getColor());
    });
    primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
      boardGameHeight = (double) primaryStage.getHeight();
      updateBoardSize(boardGameWidth, boardGameHeight);
      redrawBoard();
      setPossibleCorner(game.getCurPlayer().getColor());
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
        cleanBoard();
        redrawBoard();
        setPossibleCorner(game.getCurPlayer().getColor());
        drawPieces(primaryStage.getWidth() - pieceListWidth, pieceListHeight, pieceListWidth, sc);
      }
    });
    // ----------------------------------- game board
    int boardSize = game.getBoard().getSize();

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
        if (i == 0 && j == 0) {

          pane.layoutXProperty().addListener((obs, oldVal, newVal) -> {
            boardgameX = (double) newVal;
          });
          pane.layoutYProperty().addListener((obs, oldVal, newVal) -> {
            boardgameY = (double) newVal + (boardGameHeight - boardGameHeight * heightPercentBoard) / 2.0;
          });
        }
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
            mouseXSquare = col;
            mouseYSquare = row;
            Coord pos = new Coord(col, row);
            if (game.getBoard().canAdd(timer.movingPiece.piece, pos, game.getCurPlayer().getColor())
                && hints.getValue() >= 1) {
              timer.movingPiece.setColor(colorView.get(game.getCurPlayer().getColor()).getValue());
            } else {
              timer.movingPiece.setColor(colorView.get(game.getCurPlayer().getColor()).getKey());
            }
          }
        });
        pane.addEventFilter(MouseEvent.MOUSE_PRESSED, (t) -> {
          if (t.getButton() == MouseButton.PRIMARY) {
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
      }
    }
    setActive();
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
            cleanBoard();
            redrawBoard();
            setPossibleCorner(game.getCurPlayer().getColor());
            drawPieces(primaryStage.getWidth() - pieceListWidth, pieceListHeight, pieceListWidth, sc);
          }
        }
      }
    });
    hints.setValue(4.0);
  }

  private void updateUndoRedoButtons() {
    undo.setDisable(!game.canUndo());
    redo.setDisable(!game.canRedo());
  }

  public void removeNodeByRowColumnIndex(final int row, final int column, IntelligentGridPane gridPane) {

    ObservableList<Node> childrens = gridPane.getChildren();
    for (Node node : childrens) {
      if (node instanceof Pane && IntelligentGridPane.getRowIndex(node) == row
          && IntelligentGridPane.getColumnIndex(node) == column) {
        gridPane.getChildren().remove((Pane) node);
        break;
      }
    }
  }

  private void clearPieceList() {
    for (int i = 0; i < game.getNbPlayers() && pieceList != null; i++) {
      // Pane f = new Pane();
      // f.setMaxWidth(Double.MAX_VALUE);
      // f.setMaxHeight(Double.MAX_VALUE);
      // pieceList.add(f, 0, i);
      removeNodeByRowColumnIndex(i, 0, pieceList);
    }
    // pieceList.getChildren().remove(0, game.getNbPlayers());
  }

  private void redrawPieceList() {
    panVect.clear();
    RowConstraints pieceSize = new RowConstraints();
    // ColumnConstraints collumnSize = new ColumnConstraints();
    // collumnSize.setPercentWidth(100);
    pieceSize.setPercentHeight(100.0 / game.getNbPlayers());
    ArrayList<RowConstraints> cc = new ArrayList<>();
    for (int i = 0; i < game.getNbPlayers(); i++) {
      cc.add(pieceSize);
      Pane f = new Pane();
      panVect.add(f);
      f.setMaxWidth(Double.MAX_VALUE);
      f.setMaxHeight(Double.MAX_VALUE);
      // f.setBackground(
      // new Background(new BackgroundFill(game.getPlayers().get(i).getColor(),
      // CornerRadii.EMPTY, Insets.EMPTY)));
      f.setBorder((new Border(
          new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))));
      pieceList.add(panVect.get(i), 0, i);
    }
    pieceList.getRowConstraints().setAll(cc);
    // pieceList.getColumnConstraints().setAll(collumnSize);
  }

  public void drawPieces(Double x, Double y, Double width, Scene sc) {
    root.getChildren().remove(1, root.getChildren().size());
    for (int i = 0; i < game.getNbPlayers(); i++) {
      Double currenty = y / game.getNbPlayers() * i + borderSize + 5;
      Double currentx = x + borderSize;
      Double height = y / game.getNbPlayers();

      double pieceSize = width / 34.0;
      // double pieceSize = Math.min(width / 34.0,height/);

      int maxNbRow = 0;
      ArrayList<Move> placements = game.getPlayers().get(i).whereToPlayAll(game);
      for (int j = 0; j < poolPlayer.get(i).size(); j++) {
        PieceView p = new PieceView(poolPlayer.get(i).get(j), game, pieceSize, game.getPlayers().get(i),
            colorView.get(game.getPlayers().get(i).getColor()).getKey());
        if (hints.getValue() >= 4 && p.player == game.getCurPlayer()) {
          p.setActive(placements.stream().anyMatch((pl) -> {
            return pl.getPiece().equals(p.piece);
          }));
        } else {
          p.setActive(true);
        }
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
            if (t.getButton() == MouseButton.PRIMARY && p.active) {
              if (game.getCurPlayer() == p.player) {
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
                  setPossible(p.piece);
                  if (timer.isRunning()) {
                    Coord pos = new Coord(mouseXSquare, mouseYSquare);
                    if (game.getBoard().canAdd(timer.movingPiece.piece, pos, game.getCurPlayer().getColor())
                        && hints.getValue() >= 1) {
                      timer.movingPiece.setColor(colorView.get(game.getCurPlayer().getColor()).getValue());
                    } else {
                      timer.movingPiece.setColor(colorView.get(game.getCurPlayer().getColor()).getKey());
                    }
                  }

                });
                // if (timer.isRunning()) {
                // timer.stop();
                // } else {
                if (timer.movingPiece != null) {
                  timer.cancelMove();
                  cleanBoard();
                  redrawBoard();
                  drawPieces(primaryStage.getWidth() - pieceListWidth, pieceListHeight, pieceListWidth, sc);
                  // setPossibleCorner(game.getCurPlayer().getColor());
                }

                timer.setMovingPiece(p);
                setPossible(p.piece);
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
    int boardSize = game.getBoard().getSize();

    // for (int i = 0; i < boardGame.getChildren().size(); i++) {
    // boardGame.getChildren().remove(boardGame.getChildren().get(i));
    // }
    boardGame.getChildren().clear();

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
    for (int i = 0; i < boardSize; i++) {
      for (int j = 0; j < boardSize; j++) {
        if (get(i, j) == null) {
          Pane pane = new Pane();
          if (i == 0 && j == 0) {
            pane.layoutXProperty().addListener((obs, oldVal, newVal) -> {
              boardgameX = (double) newVal;
            });
            pane.layoutYProperty().addListener((obs, oldVal, newVal) -> {
              boardgameY = (double) newVal + (boardGameHeight - boardGameHeight * heightPercentBoard) / 2.0;
            });
          }
          pane.setBorder((new Border(
              new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))));
          final int colo = i;
          final int rowo = j;
          pane.setOnMouseEntered(e -> {
            if (timer.movingPiece != null) {
              timer.movingPiece.setLayoutX(pane.getLayoutX() + boardGame.getLayoutX());
              timer.movingPiece.setLayoutY(pane.getLayoutY() + boardGame.getLayoutY());
              mouseXSquare = colo;
              mouseYSquare = rowo;
              Coord pos = new Coord(colo, rowo);
              if (game.getBoard().canAdd(timer.movingPiece.piece, pos, game.getCurPlayer().getColor())
                  && hints.getValue() >= 1) {
                timer.movingPiece.setColor(colorView.get(game.getCurPlayer().getColor()).getValue());
              } else {
                timer.movingPiece.setColor(colorView.get(game.getCurPlayer().getColor()).getKey());
              }
            }
          });
          pane.addEventFilter(MouseEvent.MOUSE_PRESSED, (t) -> {
            if (t.getButton() == MouseButton.PRIMARY) {
              System.out.printf("Mouse clicked cell [%d, %d]%n", colo, rowo);
              if (timer.movingPiece != null) {
                timer.movingPiece.setLayoutX(pane.getLayoutX() + boardGame.getLayoutX());
                timer.movingPiece.setLayoutY(pane.getLayoutY() + boardGame.getLayoutY());
              }
              Coord pos = new Coord(colo, rowo);
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
          boardGame.add(pane, i, j);
        }
      }
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

  public void cleanBoard() {
    for (int i = 0; i < game.getBoard().getSize(); i++) {
      for (int j = 0; j < game.getBoard().getSize(); j++) {
        Pane pane = (Pane) get(i, j);
        if (game.getBoard().get(i, j) != null) {

          pane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
          pane.setBorder((new Border(
              new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))));
          ;
        }
      }
    }
  }

  public void setPossible(Piece p) {
    if (hints.getValue() >= 3) {
      cleanBoard();
      redrawBoard();
      ArrayList<Move> truc = game.getCurPlayer().whereToPlay(p, game);
      for (Move pl : truc) {
        if (pl.getTrans() == p.mapState()) {
          for (Coord pPart : p.getShape()) {
            get(pl.getPos().x + pPart.x, pl.getPos().y + pPart.y).setBackground(
                new Background(new BackgroundFill(new Color(0, 0.5, 0, 0.3), CornerRadii.EMPTY, Insets.EMPTY)));
          }
        }
      }
    }
  }

  public void setPossibleCorner(Color c) {
    if (hints.getValue() >= 2) {
      Set<Coord> truc = game.getBoard().getAccCorners(c);
      for (Coord var : truc) {
        get(var.x, var.y).setBackground(new Background(
            new BackgroundFill(new Color(0, 0.5, 0, 0.3).darker().darker(), CornerRadii.EMPTY, Insets.EMPTY)));
      }
    }
  }

  public void glowPieces() {
    HashMap<Color, ArrayList<Piece>> pieces = game.getBoard().getPieces();
    int depth = (int) Math.floor(squareSize) / 2; // Setting the uniform variable for the glow width and height
    for (Node child : boardGame.getChildren()) {
      if (child instanceof Pane) {
        Pane p = (Pane) child;
        p.setEffect(null);
        p.setMouseTransparent(false);
      }
    }
    for (APlayer player : game.getPlayers()) {
      if (game.getBoard().hasPlayed(player.getColor())) {
        Piece lastPiece = pieces.get(player.getColor()).get(pieces.get(player.getColor()).size() - 1);
        for (Coord var : lastPiece.getShape()) {
          DropShadow borderGlow = new DropShadow();
          borderGlow.setOffsetY(0f);
          borderGlow.setOffsetX(0f);
          borderGlow.setColor(colorView.get(player.getColor()).getKey());
          borderGlow.setWidth(depth);
          borderGlow.setHeight(depth);
          borderGlow.setSpread(0.7);
          get(var.x, var.y).setEffect(borderGlow);
          for (int k = 0; k < buttonArray.size(); k++) {
            buttonArray.get(k).toFront();
          }
          menuGrid.toFront();
        }
      }
    }

  }

  public void redrawBoard() {
    for (int i = 0; i < game.getBoard().getSize(); i++) {
      for (int j = 0; j < game.getBoard().getSize(); j++) {
        Pane pane = (Pane) get(i, j);
        if (game.getBoard().get(i, j) != null) {
          pane.setBackground(new Background(
              new BackgroundFill(colorView.get(game.getBoard().get(i, j)).getKey(), CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
          pane.setBackground(
              new Background(new BackgroundFill((game.getBoard().get(i, j)), CornerRadii.EMPTY, Insets.EMPTY)));
        }
        pane.setBorder((new Border(
            new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))));
      }
    }
    glowPieces();
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
    setActive();
    poolPlayer = new ArrayList<>();
    poolPlayer.clear();
    for (int i = 0; i < game.getNbPlayers(); i++) {
      poolPlayer.add(game.getPlayers().get(i).getPieces());
    }
    root.getChildren().remove(getPieceView(playedPiece));
    drawPieces(primaryStage.getWidth() - pieceListWidth, pieceListHeight, pieceListWidth, sc);
    redrawBoard();
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
    setPossibleCorner(game.getCurPlayer().getColor());
    updateUndoRedoButtons();
  }

  private void displayOption() {
    Stage stage = new Stage();
    stage.setTitle("options");
    TabPane tabpane = new TabPane();
    Tab tabplayers = new Tab("players");
    Tab tabgameopt = new Tab("options du jeu");
    RadioButton twoplayers = new RadioButton("2 joueurs");
    RadioButton fourplayers = new RadioButton("4 joueurs");
    ToggleGroup nbPlayers = new ToggleGroup();
    twoplayers.setToggleGroup(nbPlayers);
    fourplayers.setToggleGroup(nbPlayers);
    HBox playerBumberBox = new HBox(twoplayers, fourplayers);
    ComboBox<String> typeBox = new ComboBox<>();
    // TODO: use GameType enum
    typeBox.getItems().addAll("Duo", "Blokus");
    typeBox.getSelectionModel().selectFirst();
    Label typeLabel = new Label("type de jeu : ");
    HBox type = new HBox(typeLabel, typeBox);
    VBox meh = new VBox(playerBumberBox, type);
    for (int i = 0; i < 4; i++) {
      meh.getChildren().add(new PlayerOptPane(game, i));
    }
    nbPlayers.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
      public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
        // if (meh.getChildren().size() > 2) {
        // meh.getChildren().remove(2, meh.getChildren().size());
        // }
        for (int i = 4; i < 6; i++) {
          meh.getChildren().get(i).setVisible(fourplayers.isSelected());
        }
      }
    });
    if (listPType.size() == 2) {
      twoplayers.setSelected(true);
    } else {
      fourplayers.setSelected(true);
    }
    tabplayers.setContent(meh);
    BorderPane borderPane = new BorderPane();
    Button valider = new Button("valider");
    valider.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        listPType.clear();
        for (int i = 2; i < 6; i++) {
          if (meh.getChildren().get(i).isVisible()) {
            PlayerOptPane currentBox = (PlayerOptPane) meh.getChildren().get(i);
            if (!currentBox.user.isSelected()) {
              System.out.println(PlayerType.RANDOM_PIECE.toString());
              PlayStyle ps = PlayStyle.RAND_PIECE;
              for (int h = 0; h < PlayStyle.values().length; h++) {
                if (PlayStyle.values()[h].toString() == currentBox.typeBox.getValue()) {
                  ps = PlayStyle.values()[h];
                }
              }
              listPType.add(new Pair<>(PlayerType.values()[(int) currentBox.iaLvl.getValue()], ps));
            } else {
              System.out.println(i + " est un player");
              listPType.add(new Pair<>(PlayerType.USER, null));
            }
          }
        }
        stage.close();
        newGame();
      }
    });
    borderPane.setBottom(valider);
    borderPane.setTop(tabpane);
    // ----------------------- game options --------------------------------
    HBox volumeOption = new HBox();
    VBox optionsGameVbox = new VBox();
    Slider volumeSlider = new Slider(-50, 0, -20);
    volumeSlider.valueProperty().addListener((obs, oldval, newVal) -> {
      if (volumeSlider.getValue() > -50) {
        gainControl.setValue((float) volumeSlider.getValue()); // Reduce volume by 10 decibels.
      } else {
        gainControl.setValue(gainControl.getMinimum());
      }
    });
    CheckBox fullscreenBox = new CheckBox("plein ecran");
    volumeOption.getChildren().addAll(new Label("volume de la musique : "), volumeSlider);
    HBox fullscreenHBox = new HBox();
    fullscreenHBox.getChildren().addAll(fullscreenBox);
    optionsGameVbox.getChildren().addAll(fullscreenBox, volumeOption);
    Scene scene = new Scene(borderPane, 600, 500);
    fullscreenBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        primaryStage.setFullScreen(fullscreenBox.isSelected());
        stage.setAlwaysOnTop(true);
      }
    });
    // VBox.setMargin(volumeOption, new Insets(20, 0, 0, 20));
    // VBox.setMargin(fullscreenHBox, new Insets(20, 0, 0, 20));
    // VBox.setMargin(optionsGameVbox, new Insets(20, 0, 0, 20));
    optionsGameVbox.setPadding(new Insets(20, 0, 0, 20));
    optionsGameVbox.setSpacing(20);
    tabgameopt.setContent(optionsGameVbox);
    // ---------------------------------------------------------------------
    tabpane.getTabs().addAll(tabplayers, tabgameopt);
    stage.setScene(scene);
    stage.show();

  }

  private void displayEOG() {
    ArrayList<APlayer> winner = game.getWinner();
    Label secondLabel;
    if (winner.size() > 1) {
      secondLabel = new Label();
      String text = new String("les joueurs ");
      for (int i = 0; i < winner.size(); i++) {
        text = text + Board.getColorName(winner.get(i).getColor());
        if (i < winner.size() - 1) {
          text = text + " et ";
        }
      }
      text = text + " sont meilleurs";
      secondLabel.setText(text);
    } else {
      secondLabel = new Label("le joueur " + Board.getColorName(winner.get(0).getColor()) + " est meilleur");
    }
    ArrayList<Label> scores = new ArrayList<>();
    ArrayList<RowConstraints> rowLabelcs = new ArrayList<>();
    RowConstraints rowLabelc = new RowConstraints();
    rowLabelc.setPercentHeight(100 / (1 + game.getScore().size()));
    for (int i = 0; i < game.getScore().size(); i++) {
      Label tempLabel = new Label("le joueur " + Board.getColorName(game.getPlayers().get(i).getColor()) + " a "
          + game.getScore().get(game.getPlayers().get(i).getColor()));
      tempLabel.setMaxWidth(Double.MAX_VALUE);
      tempLabel.setMaxHeight(Double.MAX_VALUE);
      // tempLabel.setTextAlignment(TextAlignment.CENTER);
      // tempLabel.setContentDisplay(ContentDisplay.TOP);
      // tempLabel.setBackground(new Background(new BackgroundFill(Color.WHITE,
      // CornerRadii.EMPTY, Insets.EMPTY)));
      scores.add(tempLabel);
      rowLabelcs.add(rowLabelc);
    }
    secondLabel.setTextAlignment(TextAlignment.CENTER);

    IntelligentGridPane secondaryLayout = new IntelligentGridPane();
    IntelligentGridPane buttonPane = new IntelligentGridPane();
    IntelligentGridPane LabelPane = new IntelligentGridPane();

    Button quit = new Button("quit");
    Button newGame = new Button("new game");

    quit.setMaxWidth(Double.MAX_VALUE);
    quit.setMaxHeight(Double.MAX_VALUE);
    newGame.setMaxWidth(Double.MAX_VALUE);
    newGame.setMaxHeight(Double.MAX_VALUE);

    Scene secondScene = new Scene(secondaryLayout, 300, 170);
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

    newWindow.setTitle("Fin");
    newWindow.initModality(Modality.APPLICATION_MODAL);
    newWindow.setScene(secondScene);

    newWindow.setAlwaysOnTop(true);

    newWindow.setX(primaryStage.getX() + 200);
    newWindow.setY(primaryStage.getY() + 100);
    newWindow.centerOnScreen();
    newWindow.maximizedProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue)
        primaryStage.setMaximized(false);
    });
    newWindow.setResizable(false);
    // newWindow.widthProperty().addListener((observable, oldValue, newValue) -> {
    // newWindow.setWidth((double) oldValue);
    // });
    // newWindow.heightProperty().addListener((observable, oldValue, newValue) -> {
    // newWindow.setHeight((double) oldValue);
    // });

    newWindow.show();
    RowConstraints rc = new RowConstraints();
    rc.setPercentHeight(70);
    RowConstraints rc4 = new RowConstraints();
    rc4.setPercentHeight(30);
    ColumnConstraints lc = new ColumnConstraints();
    lc.setPercentWidth(100);
    ColumnConstraints lc2 = new ColumnConstraints();
    lc2.setPercentWidth(30);
    ColumnConstraints lc3 = new ColumnConstraints();
    lc3.setPercentWidth(20);
    secondaryLayout.getRowConstraints().addAll(rc, rc4);
    secondaryLayout.getColumnConstraints().addAll(lc);
    LabelPane.getRowConstraints().addAll(rowLabelcs);
    LabelPane.getColumnConstraints().add(lc);
    secondaryLayout.add(LabelPane, 0, 0);
    secondaryLayout.add(buttonPane, 0, 1);
    LabelPane.add(secondLabel, 0, 0);
    for (int i = 0; i < game.getScore().size(); i++) {
      LabelPane.add(scores.get(i), 0, i + 1);
    }
    buttonPane.add(quit, 1, 0);
    buttonPane.add(newGame, 2, 0);
    buttonPane.getColumnConstraints().addAll(lc3, lc2, lc2, lc3);
  }

  @Override
  public void playerPassed(APlayer player) {
    for (int i = 0; i < pieceList.getChildren().size(); i++) {

    }
  }

  @Override
  public void undo(APlayer oldPlayer, Piece removedPiece) {
    setActive();
    drawPieces(primaryStage.getWidth() - pieceListWidth, pieceListHeight, pieceListWidth, sc);
    redrawBoard();
    for (APlayer p : game.getPlayers()) {
      System.out.println("Nb piece " + Board.getColorName(p.getColor()) + ": " + p.getPieces().size());
    }
    setPossibleCorner(game.getCurPlayer().getColor());
    updateUndoRedoButtons();
  }
}