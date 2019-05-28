package blokus.view;

import java.util.ArrayList;

import blokus.controller.Game;
import blokus.model.Config;
import blokus.model.PlayStyle;
import blokus.model.PlayerType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * Options
 */
public class Options extends Stage {

	public Options() {
		super();
	}

	public void displayOption(ArrayList<Pair<PlayerType, PlayStyle>> listPType, App app, Game game, Music music,
			Stage primaryStage) {
		this.setTitle("options");
		TabPane tabpane = new TabPane();
		Tab tabplayers = new Tab("players");
		Tab tabgameopt = new Tab("options du jeu");
		RadioButton twoplayers = new RadioButton("2 joueurs");
		RadioButton fourplayers = new RadioButton("4 joueurs");
		ToggleGroup nbPlayers = new ToggleGroup();
		Button valider = new Button("nouvelle partie");
		Button change = new Button("appliquer");
		Button cancel = new Button("annuler");
		ComboBox<String> typeBox = new ComboBox<>();
		typeBox.getItems().addAll("Blokus", "Duo");
		nbPlayers.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				if (twoplayers.isSelected()) {
					if (typeBox.getItems().size() < 2) {
						typeBox.getItems().add("Duo");
					}
				} else {
					typeBox.getItems().remove(1);
				}
				change.setDisable((!Config.i().get(Config.NB_PLAYER)
						.equals(Character.toString(((RadioButton) nbPlayers.getSelectedToggle()).getText().charAt(0))))
						|| (!Config.i().get("typeGame").equals(typeBox.getValue())));
			}
		});
		twoplayers.setToggleGroup(nbPlayers);
		fourplayers.setToggleGroup(nbPlayers);
		HBox playerBumberBox = new HBox(twoplayers, fourplayers);

		typeBox.getSelectionModel().select(Config.i().get("typeGame"));
		typeBox.valueProperty().addListener((ov, t, t1) -> {
			change.setDisable((!Config.i().get(Config.NB_PLAYER)
					.equals(Character.toString(((RadioButton) nbPlayers.getSelectedToggle()).getText().charAt(0))))
					|| (!Config.i().get("typeGame").equals(typeBox.getValue())));
		});
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
		this.initModality(Modality.APPLICATION_MODAL);
		if (Config.i().geti(Config.NB_PLAYER) == 2) {
			twoplayers.setSelected(true);
		} else {
			fourplayers.setSelected(true);
		}
		tabplayers.setContent(meh);
		HBox volumeOption = new HBox();
		VBox optionsGameVbox = new VBox();
		Slider volumeSlider = new Slider(-50, 0, Config.i().getf(Config.VOLUME));
		volumeSlider.valueProperty().addListener((obs, oldval, newVal) -> {
			if (volumeSlider.getValue() > -50) {
				music.setSound((float) volumeSlider.getValue()); // Reduce volume by 10 decibels.
			} else {
				music.mute();
			}
		});
		BorderPane borderPane = new BorderPane();
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
							Config.i().set("player" + (i - 2), PlayerType.values()[(int) currentBox.iaLvl.getValue()].name());
							Config.i().set("player" + (i - 2) + "_style", ps.name());
							listPType.add(new Pair<>(PlayerType.values()[(int) currentBox.iaLvl.getValue()], ps));
						} else {
							Config.i().set("player" + (i - 2), "USER");
							Config.i().set("player" + (i - 2) + "_style", "RAND_PIECE");
							listPType.add(new Pair<>(PlayerType.USER, null));
						}
					}
				}
				Config.i().set("typeGame", typeBox.getValue());
				if (fourplayers.isSelected()) {
					Config.i().set("nb_player", 4);
					System.out.println("4 player selected");
				} else {
					Config.i().set("nb_player", 2);
					System.out.println("2 player selected");
				}
				music.setSound((float) volumeSlider.getValue());
				Config.i().set(Config.VOLUME, (float) volumeSlider.getValue());
				close();
				app.newGame();
			}
		});
		change.setOnAction(new EventHandler<ActionEvent>() {
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
							Config.i().set("player" + (i - 2), PlayerType.values()[(int) currentBox.iaLvl.getValue()].name());
							Config.i().set("player" + (i - 2) + "_style", ps.name());
							listPType.add(new Pair<>(PlayerType.values()[(int) currentBox.iaLvl.getValue()], ps));
						} else {
							Config.i().set("player" + (i - 2), "USER");
							Config.i().set("player" + (i - 2) + "_style", "RAND_PIECE");
							listPType.add(new Pair<>(PlayerType.USER, null));
						}
					}
				}
				Config.i().set("typeGame", typeBox.getValue());
				if (fourplayers.isSelected()) {
					Config.i().set("nb_player", 4);
					System.out.println("4 player selected");
				} else {
					Config.i().set("nb_player", 2);
					System.out.println("2 player selected");
				}
				music.setSound((float) volumeSlider.getValue());
				Config.i().set(Config.VOLUME, (float) volumeSlider.getValue());
				close();
				app.changeAllPlayers(listPType);
			}
		});
		HBox buttonBox = new HBox(valider, change, cancel);
		borderPane.setBottom(buttonBox);
		borderPane.setTop(tabpane);
		// ----------------------- game options --------------------------------
		CheckBox fullscreenBox = new CheckBox("plein ecran");
		fullscreenBox.setSelected(primaryStage.isFullScreen());
		volumeOption.getChildren().addAll(new Label("volume de la musique : "), volumeSlider);
		HBox fullscreenHBox = new HBox();
		fullscreenHBox.getChildren().addAll(fullscreenBox);
		optionsGameVbox.getChildren().addAll(fullscreenBox, volumeOption);
		Scene scene = new Scene(borderPane, 600, 500);
		cancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				music.setSound(Config.i().getf("volume"));
				close();
			}
		});
		this.setOnCloseRequest(evt -> {
			cancel.fire();
		});
		fullscreenBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				primaryStage.setFullScreen(fullscreenBox.isSelected());
				setAlwaysOnTop(true);
			}
		});
		// setAlwaysOnTop(true);
		// VBox.setMargin(volumeOption, new Insets(20, 0, 0, 20));
		// VBox.setMargin(fullscreenHBox, new Insets(20, 0, 0, 20));
		// VBox.setMargin(optionsGameVbox, new Insets(20, 0, 0, 20));
		optionsGameVbox.setPadding(new Insets(20, 0, 0, 20));
		optionsGameVbox.setSpacing(20);
		tabgameopt.setContent(optionsGameVbox);
		// ---------------------------------------------------------------------
		tabpane.getTabs().addAll(tabplayers, tabgameopt);
		tabpane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		this.setScene(scene);
		this.show();
	}
}