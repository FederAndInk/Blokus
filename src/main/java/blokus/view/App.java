
package blokus.view;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import java.util.Optional;

import blokus.controller.Game;
import blokus.model.APlayer;
import blokus.model.Config;
import blokus.model.Coord;
import blokus.model.GameType;
import blokus.model.Move;
import blokus.model.PColor;
import blokus.model.Piece;
import blokus.model.PlayStyle;
import blokus.model.PlayerType;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * App
 */
public class App extends Application implements IApp {
  private Group root;
  private int mouseXSquare;
  private int mouseYSquare;
  private Stage primaryStage;
  private int nbTurn;
  private Scene sc;
  private double squareSize = 0;
  private IntelligentGridPane boardGame;
  private double boardGameWidth;
  private double boardGameHeight;
  private double pieceListWidth;
  private double pieceListHeight;
  private IntelligentGridPane pieceList = new IntelligentGridPane();
  private ArrayList<Pane> panVect = new ArrayList<>();
  private ArrayList<Button> buttonArray = new ArrayList<>();
  private Game game;
  private Slider hints;

  private Button redo;
  private Button undo;
  private Button save;
  private Button load;
  private Button stop;

  private double mouseX = 0;
  private double mouseY = 0;
  private final double widthPercentBoard = 0.7;
  private final double heightPercentBoard = 0.9;
  private double borderSize = BorderWidths.DEFAULT.getLeft();
  private ArrayList<ArrayList<Piece>> poolPlayer;
  private ArrayList<Pair<PlayerType, PlayStyle>> listPType = new ArrayList<>();
  private IntelligentGridPane menuGrid;
  private double boardgameX;
  private double boardgameY;
  private Music music;

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
        // System.out.println(timer.movingPiece.getLayoutX());
        // System.out.println(timer.movingPiece.getLayoutY());
      } else {
        // System.out.println(timer.movingPiece != null);
        // System.out.println(!isInBord(mouseX, mouseY));
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

  public void newGame() {
    game = new Game();
    clearPieceList();
    game.setApp(this);
    System.out.println(listPType.size());
    for (int i = 0; i < listPType.size(); i++) {
      System.out.println("ajout player " + listPType.get(i).getKey() + " " + listPType.get(i).getValue());
      game.addPlayer(listPType.get(i).getKey(), listPType.get(i).getValue());
    }
    if (game.getNbPlayers() == 2) {
      if (Config.i().get("typeGame").equals("Duo")) {
        game.init(GameType.DUO);
        System.out.println("duo");
      } else {
        game.init(GameType.BLOKUS);
        System.out.println("pas duo");
      }
    } else {
      game.init(GameType.BLOKUS);
      System.out.println("pas duo");
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
      setPossibleCorner();
      showBeginPoint();

    }
  }

  public void changeAllPlayers(ArrayList<Pair<PlayerType, PlayStyle>> listPType) {
    System.out.println(listPType);
    for (int i = 0; i < game.getNbPlayers(); i++) {
      game.changePlayer(i, listPType.get(i).getKey(), listPType.get(i).getValue());
    }
    System.out.println("change for:");
    for (APlayer p : game.getPlayers()) {
      System.out.println(p);
    }
    System.out.println("cur Player: " + game.getCurPlayer());
    if (timer.isRunning()) {
      timer.cancelMove(sc);
    }
    update(null, null);
  }

  @Override
  public void init() throws Exception {
    super.init();
    for (int i = 0; i < Config.i().geti("nb_player"); i++) {
      String iaLvl = Config.i().get("player" + i);
      String iaStyle = Config.i().get("player" + i + "_style");
      listPType.add(new Pair<>(PlayerType.valueOf(iaLvl), PlayStyle.valueOf(iaStyle)));
    }
    newGame();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    this.primaryStage = primaryStage;
    primaryStage.setTitle("Blokus");
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

    mainGrid.setGridLinesVisible(true);
    gridlayoutMenu.setGridLinesVisible(true);
    menuGrid.setGridLinesVisible(true);

    root.getChildren().add(mainGrid);

    Scene sc = new Scene(root);
    this.sc = sc;
    primaryStage.setScene(sc);
    primaryStage.show();

    primaryStage.setMinHeight(800);
    primaryStage.setMinWidth(900);

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
    menuButtonSize.setPercentWidth(100.0 / 5.0);
    ColumnConstraints menuButtonSize2 = new ColumnConstraints();
    menuButtonSize2.setPercentWidth(100.0 / 4.0);
    Button quit = new Button("Quitter");
    Button newGame = new Button("Nouvelle partie");
    Button options = new Button("Options et aide");
    undo = new Button("Defaire");
    redo = new Button("Refaire");
    save = new Button("sauvegarder");
    save.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("ou sauvegarder");
        File selectedFile = fileChooser.showSaveDialog(primaryStage);
        if (selectedFile != null) {
          System.out.println("want save: " + selectedFile);
          game.save(selectedFile.toString());
        }
      }
    });
    load = new Button("charger");

    load.setOnAction(e -> {
      FileChooser fileChooser = new FileChooser();
      fileChooser.setTitle("ou sauvegarder");
      File selectedFile = fileChooser.showOpenDialog(primaryStage);
      if (selectedFile != null) {
        System.out.println("want save: " + selectedFile);
        game = Game.load(selectedFile.toString());
        game.setApp(this);
        System.out.println("want to load : " + selectedFile);
      }
      poolPlayer.clear();
      for (int i = 0; i < game.getNbPlayers(); i++) {
        poolPlayer.add(game.getPlayers().get(i).getPieces());
      }
      redrawPieceList();
      cleanBoard();
      drawPieces(primaryStage.getWidth() - pieceListWidth, pieceListHeight, pieceListWidth, sc);
      updateBoardSize(boardGameWidth, boardGameHeight);
      redrawBoard();
      setActive();
      setPossibleCorner();
      showBeginPoint();

    });
    stop = new Button("Pause");
    stop.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        if (game.isAiPlaying()) {
          stop.setText("Reprendre");
        } else {
          stop.setText("Pause");
        }
        game.setAiPlay(!game.isAiPlaying());
      }
    });
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
        stop.setText("Reprendre");
        game.setAiPlay(false);
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
        // Platform.exit();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("voulez vous quitter");
        alert.setHeaderText("Look, a Confirmation Dialog with Custom Actions");
        alert.setContentText("Choose your option.");

        ButtonType buttonTypeOne = new ButtonType("sauvegarder");
        ButtonType buttonTypeTwo = new ButtonType("sauvegarder et quitter");
        ButtonType buttonTypeCancel = new ButtonType("annuler", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {

        } else if (result.get() == buttonTypeTwo) {
        } else {
        }
      }
    });
    App app = this;
    options.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent e) {
        Options opt = new Options();
        opt.displayOption(listPType, app, game, music, primaryStage);
      }
    });
    buttonArray.add(newGame);
    buttonArray.add(save);
    buttonArray.add(load);
    buttonArray.add(options);
    buttonArray.add(quit);
    buttonArray.add(undo);
    buttonArray.add(redo);
    buttonArray.add(stop);

    ArrayList<ColumnConstraints> cc2 = new ArrayList<>();
    ArrayList<ColumnConstraints> cc3 = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      Button b = buttonArray.get(i);
      b.setMaxWidth(Double.MAX_VALUE);
      b.setMaxHeight(Double.MAX_VALUE);
      cc2.add(menuButtonSize);
      menuGrid.add(b, i, 0);
    }
    for (int i = 5; i < buttonArray.size(); i++) {
      Button b = buttonArray.get(i);
      b.setMaxWidth(Double.MAX_VALUE);
      b.setMaxHeight(Double.MAX_VALUE);
      cc3.add(menuButtonSize2);
      unredoMenu.add(b, i - 5, 0);
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
      setPossibleCorner();
      showBeginPoint();

      drawPieces(primaryStage.getWidth() - pieceListWidth, pieceListHeight, pieceListWidth, sc);
    });

    music = new Music();

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
    unredoMenu.add(hints, buttonArray.size() - 5, 0);
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
      f.setBorder(generateBorder());
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
      setPossibleCorner();
      showBeginPoint();

    });
    primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
      boardGameHeight = (double) primaryStage.getHeight();
      updateBoardSize(boardGameWidth, boardGameHeight);
      redrawBoard();
      setPossibleCorner();
      showBeginPoint();

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
        timer.cancelMove(sc);
        cleanBoard();
        redrawBoard();
        setPossibleCorner();
        showBeginPoint();

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
          if (timer.movingPiece != null) {
            timer.movingPiece.setLayoutX(pane.getLayoutX() + boardGame.getLayoutX());
            timer.movingPiece.setLayoutY(pane.getLayoutY() + boardGame.getLayoutY());
            mouseXSquare = col;
            mouseYSquare = row;
            Coord pos = new Coord(col, row);
            if (game.getBoard().canAdd(timer.movingPiece.getPiece(), pos, game.getCurPlayer().getColor())
                && hints.getValue() >= 1) {
              timer.movingPiece.setColor(game.getCurPlayer().getColor().secondaryColor());
            } else {
              timer.movingPiece.setColor(game.getCurPlayer().getColor().primaryColor());
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
                && game.getBoard().canAdd(timer.movingPiece.getPiece(), pos, game.getCurPlayer().getColor())) {
              game.inputPlay(timer.movingPiece.getPiece(), pos);
              timer.stop();
            }
          }
        });
        boardGame.add(pane, col, row);
        pane.setBorder(generateBorder());
      }
    }
    setActive();
    boardGame.getColumnConstraints().setAll(colv);
    boardGame.getRowConstraints().setAll(rowv);

    sc.setOnMouseMoved(new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent event) {
        mouseX = event.getSceneX();
        mouseY = event.getSceneY();
      }

    });
    sc.setOnScroll((ScrollEvent ev) -> {
      if (timer.movingPiece != null) {

        if (ev.getDeltaY() < 0) {
          timer.movingPiece.getPiece().left();
        } else {
          timer.movingPiece.getPiece().right();
        }
        timer.movingPiece.clearPiece();
        timer.movingPiece.drawPiece();
        setPossible(timer.movingPiece.getPiece());
        showBeginPoint();
        if (timer.isRunning()) {
          Coord pos = new Coord(mouseXSquare, mouseYSquare);
          if (game.getBoard().canAdd(timer.movingPiece.getPiece(), pos, game.getCurPlayer().getColor())
              && hints.getValue() >= 1) {
            timer.movingPiece.setColor(game.getCurPlayer().getColor().secondaryColor());
          } else {
            timer.movingPiece.setColor(game.getCurPlayer().getColor().primaryColor());
          }
        }
      }
    });
    sc.addEventFilter(MouseEvent.MOUSE_PRESSED, (ev) -> {
      if (ev.getButton() == MouseButton.SECONDARY && timer.movingPiece != null) {
        timer.movingPiece.getPiece().revertY();
        timer.movingPiece.clearPiece();
        timer.movingPiece.drawPiece();
        setPossible(timer.movingPiece.getPiece());
        showBeginPoint();
        if (timer.isRunning()) {
          Coord pos = new Coord(mouseXSquare, mouseYSquare);
          if (game.getBoard().canAdd(timer.movingPiece.getPiece(), pos, game.getCurPlayer().getColor())
              && hints.getValue() >= 1) {
            timer.movingPiece.setColor(game.getCurPlayer().getColor().secondaryColor());
          } else {
            timer.movingPiece.setColor(game.getCurPlayer().getColor().primaryColor());
          }
        }
      } else {
        System.out.println("eubvibevibesu");
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
      removeNodeByRowColumnIndex(i, 0, pieceList);
    }
  }

  private void redrawPieceList() {
    panVect.clear();
    RowConstraints pieceSize = new RowConstraints();
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
      f.setBorder(generateBorder());
      pieceList.add(panVect.get(i), 0, i);
    }
    pieceList.getRowConstraints().setAll(cc);
  }

  public void drawPieces(Double x, Double y, Double width, Scene sc) {
    root.getChildren().remove(1, root.getChildren().size());
    for (int i = 0; i < game.getNbPlayers(); i++) {
      Double currenty = y / game.getNbPlayers() * i + borderSize + 5;
      Double currentx = x + borderSize;
      Double height = y / game.getNbPlayers();

      double pieceSize = width / 34.0;

      int maxNbRow = 0;
      ArrayList<Move> placements = game.getPlayers().get(i).whereToPlayAll(game);
      for (int j = 0; j < poolPlayer.get(i).size(); j++) {
        PieceView p = new PieceView(poolPlayer.get(i).get(j), game, pieceSize, game.getPlayers().get(i),
            game.getPlayers().get(i).getColor().primaryColor());
        if (hints.getValue() >= 4 && p.getPlayer() == game.getCurPlayer()) {
          p.setActive(placements.stream().anyMatch((pl) -> {
            return pl.getPiece().equals(p.getPiece());
          }));
        } else {
          p.setActive(true);
        }
        if (p.getNbRow() > maxNbRow) {
          maxNbRow = p.getNbRow();
        }
        currentx = currentx + p.pieceMarginW;
        if ((currentx + pieceSize * p.getNbCol()) > (width + x)) {
          currentx = x + borderSize + p.pieceMarginW;
          currenty = currenty + p.pieceMarginH + (maxNbRow) * pieceSize;
        }
        p.setSizeSquare(pieceSize);
        p.setLayoutX(currentx);
        p.setLayoutY(currenty);
        currentx = currentx + pieceSize * p.getNbCol();
        root.getChildren().add(p);
        p.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent t) {
            if (t.getButton() == MouseButton.PRIMARY && p.getActive()) {
              if (!timer.isRunning()) {
                if (game.getCurPlayer() == p.getPlayer()) {
                  p.setMouseTransparent(true);
                  timer.setMovingPiece(p);
                  setPossible(p.getPiece());
                  showBeginPoint();
                  timer.start();
                }
              } else {
                timer.cancelMove(sc);
                cleanBoard();
                redrawBoard();
                drawPieces(primaryStage.getWidth() - pieceListWidth, pieceListHeight, pieceListWidth, sc);
              }

            }
          }
        });
      }
    }

  }

  public void updateBoardSize(double wwidth, double wheight) {
    int boardSize = game.getBoard().getSize();

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
          pane.setBorder(generateBorder());
          final int colo = i;
          final int rowo = j;
          pane.setOnMouseEntered(e -> {
            if (timer.movingPiece != null) {
              timer.movingPiece.setLayoutX(pane.getLayoutX() + boardGame.getLayoutX());
              timer.movingPiece.setLayoutY(pane.getLayoutY() + boardGame.getLayoutY());
              mouseXSquare = colo;
              mouseYSquare = rowo;
              Coord pos = new Coord(colo, rowo);
              if (game.getBoard().canAdd(timer.movingPiece.getPiece(), pos, game.getCurPlayer().getColor())
                  && hints.getValue() >= 1) {
                timer.movingPiece.setColor(game.getCurPlayer().getColor().secondaryColor());
              } else {
                timer.movingPiece.setColor(game.getCurPlayer().getColor().primaryColor());
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
                  && game.getBoard().canAdd(timer.movingPiece.getPiece(), pos, game.getCurPlayer().getColor())) {
                game.inputPlay(timer.movingPiece.getPiece(), pos);
                timer.stop();
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
        if (game.getBoard().get(i, j).isColor()) {
          pane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
          pane.setBorder(generateBorder());
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
            if (get(pl.getPos().x + pPart.x, pl.getPos().y + pPart.y) != null) {
              get(pl.getPos().x + pPart.x, pl.getPos().y + pPart.y).setBackground(
                  new Background(new BackgroundFill(new Color(0, 0.5, 0, 0.3), CornerRadii.EMPTY, Insets.EMPTY)));
            }
          }
        }
      }
    }
  }

  public void setPossibleCorner() {
    PColor c = game.getCurPlayer().getColor();
    if (hints.getValue() >= 2) {
      Set<Coord> truc = game.getBoard().getAccCorners(c);
      for (Coord var : truc) {
        get(var.x, var.y).setBackground(new Background(
            new BackgroundFill(new Color(0, 0.5, 0, 0.3).darker().darker(), CornerRadii.EMPTY, Insets.EMPTY)));
      }
    }
  }

  public void glowPieces() {
    HashMap<PColor, ArrayList<Piece>> pieces = game.getBoard().getPieces();
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
          borderGlow.setColor(player.getColor().primaryColor());
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
        PColor col = game.getBoard().get(i, j);
        if (col.isColor()) {
          pane.setBackground(new Background(new BackgroundFill(col.primaryColor(), CornerRadii.EMPTY, Insets.EMPTY)));
          pane.setBorder(generateBoardBorder(col.primaryColor().brighter(), col.primaryColor().darker()));
        } else {
          pane.setBackground(new Background(new BackgroundFill((col.primaryColor()), CornerRadii.EMPTY, Insets.EMPTY)));
          pane.setBorder(generateBoardBorder(Color.web("#3d393b"), Color.web("#656163")));
        }

      }
    }
    glowPieces();
  }

  private void showBeginPoint() {
    ArrayList<Coord> coords = game.getBoard().generateFirstCorners();
    int k = 0;
    for (Coord coord : coords) {
      if (!game.getBoard().get(coord).isColor()) {
        BackgroundImage myBI = new BackgroundImage(
            new Image("circle" + k + ".png", squareSize - 3.7, squareSize - 3.7, true, true),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, false));
        get(coord.x, coord.y).setBackground(new Background(myBI));
      }
      k++;
    }
  }

  private Border generateBorder(Color topL, Color bottomR, BorderWidths bw) {
    BorderStroke bs = new BorderStroke(topL, bottomR, bottomR, topL, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
        BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, bw, Insets.EMPTY);
    Border b = new Border(bs);
    return b;
  }

  private Border generateBoardBorder(Color topL, Color bottomR) {
    return generateBorder(topL, bottomR, new BorderWidths(1.5, 1.8, 1.8, 1.5));
  }

  private Border generateBorder(Color c) {
    return generateBorder(c, c, null);
  }

  // FIXME apply border for the board only ?
  private Border generateBorder() {
    return generateBorder(Color.BLACK);
  }

  public PieceView getPieceView(Piece p) {
    PieceView res = null;
    for (int i = 0; i < root.getChildren().size(); i++) {
      if (root.getChildren().get(i) instanceof PieceView) {
        PieceView possibleRes = (PieceView) root.getChildren().get(i);
        if (possibleRes.getPiece() == p) {
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
      System.out.println("Nb piece " + p.getColor().getName() + ": " + p.getPieces().size());
    }
    if (game.isEndOfGame()) {
      EndOfGame end = new EndOfGame();
      end.displayEOG(game, primaryStage, this);
      for (Entry<PColor, Integer> c : game.getScore().entrySet()) {
        // System.out.println(Board.getColorName(c.getKey()) + ": " + c.getValue());
      }
    }
    setPossibleCorner();
    updateUndoRedoButtons();
    showBeginPoint();
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
      System.out.println("Nb piece " + p.getColor().getName() + ": " + p.getPieces().size());
    }
    setPossibleCorner();
    updateUndoRedoButtons();
    showBeginPoint();

  }
}