package blokus.view;

import java.util.ArrayList;

import blokus.controller.Game;
import blokus.model.APlayer;
import blokus.model.Board;
import blokus.model.PColor;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * endOfGame
 */
public class EndOfGame extends Stage {

	public EndOfGame() {
		super();
	}

	public void displayEOG(Game game, Stage primaryStage, App app) {
		ArrayList<APlayer> winner = game.getWinner();
		Label secondLabel;
		if (winner.size() > 1) {
			secondLabel = new Label();
			String text = new String("les joueurs ");
			for (int i = 0; i < winner.size(); i++) {
				// text = text + Board.getColorName(winner.get(i).getColor());
				text = text + winner.get(i).getColor().getName();
				if (i < winner.size() - 1) {
					text = text + " et ";
				}
			}
			text = text + " sont meilleurs";
			secondLabel.setText(text);
		} else {
			secondLabel = new Label("le joueur " + winner.get(0).getColor().getName() + " est meilleur");
		}
		ArrayList<Label> scores = new ArrayList<>();
		ArrayList<RowConstraints> rowLabelcs = new ArrayList<>();
		RowConstraints rowLabelc = new RowConstraints();
		rowLabelc.setPercentHeight(100 / (1 + game.getScore().size()));
		for (int i = 0; i < game.getScore().size(); i++) {
			Label tempLabel = new Label("le joueur " + game.getPlayers().get(i).getColor().getName() + " a "
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
		newGame.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				close();
				app.newGame();
			}
		});
		quit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Platform.exit();
			}
		});

		this.setTitle("Fin");
		this.initModality(Modality.APPLICATION_MODAL);
		this.setScene(secondScene);

		this.setAlwaysOnTop(true);

		this.setX(primaryStage.getX() + 200);
		this.setY(primaryStage.getY() + 100);
		this.centerOnScreen();
		this.maximizedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue)
				primaryStage.setMaximized(false);
		});
		this.setResizable(false);
		// newWindow.widthProperty().addListener((observable, oldValue, newValue) -> {
		// newWindow.setWidth((double) oldValue);
		// });
		// newWindow.heightProperty().addListener((observable, oldValue, newValue) -> {
		// newWindow.setHeight((double) oldValue);
		// });

		this.show();
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
}