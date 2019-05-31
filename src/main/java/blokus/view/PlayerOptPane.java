package blokus.view;

import blokus.controller.Game;
import blokus.model.Config;
import blokus.model.PColor;
import blokus.model.PlayStyle;
import blokus.model.PlayerType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * playerOptPane
 */
public class PlayerOptPane extends VBox {
	Game game;
	RadioButton user;
	RadioButton ia;
	Slider iaLvl;
	ComboBox<String> typeBox;

	public PlayerOptPane(Game game, int i) {
		super();
		this.game = game;
		setAll(i);
	}

	public void setAll(int i) {

		user = new RadioButton("Utilisateur");
		ia = new RadioButton("IA");
		ToggleGroup playertype = new ToggleGroup();
		int l = PlayerType.valueOf(Config.i().get("player" + i)).ordinal();
		user.setToggleGroup(playertype);
		ia.setToggleGroup(playertype);
		HBox typePlayer = new HBox(new Label(PColor.get((byte) (i)).getName() + " : "), user, ia);
		iaLvl = new Slider(1, PlayerType.values().length - 1, l);
		iaLvl.setMaxWidth(Double.MAX_VALUE);
		iaLvl.setMaxHeight(Double.MAX_VALUE);
		iaLvl.setShowTickMarks(true);
		iaLvl.setShowTickLabels(true);
		iaLvl.setSnapToTicks(true);
		iaLvl.setMinorTickCount(1);
		iaLvl.setMajorTickUnit(1);
		iaLvl.setBlockIncrement(1.0);
		iaLvl.valueProperty().addListener((obs, oldval, newVal) -> {
			iaLvl.setValue(Math.round(newVal.doubleValue()));
		});
		Label typeIaLabel = new Label("Type d'ia :");
		HBox typeIa = new HBox(typeIaLabel, iaLvl);
		String aiLvlToolTip = "";
		for (int no = 1; no < PlayerType.values().length; no++) {
			aiLvlToolTip += no + ": " + PlayerType.values()[no] + "\n";
		}
		iaLvl.setTooltip(new Tooltip(aiLvlToolTip));
		typeBox = new ComboBox<>();
		// typeBox.getItems().addAll("piece aleatoire", "grosse piece", "grosse piece
		// aleatoire", "heuristic");
		for (int k = 0; k < PlayStyle.values().length; k++) {
			typeBox.getItems().add(PlayStyle.values()[k].toString());
		}
		Label typeLabel = new Label("type de de choix de piece: ");
		HBox typeS = new HBox(typeLabel, typeBox);
		playertype.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				if (ia.isSelected()) {
					typeIa.setVisible(true);
					typeS.setVisible(true);
				} else {
					typeIa.setVisible(false);
					typeS.setVisible(false);
				}
			}
		});

		// typeBox.getSelectionModel().selectFirst();
		int f = PlayStyle.valueOf(Config.i().get("player" + i + "_style")).ordinal();
		typeBox.getSelectionModel().select(f);
		if (l == 0) {
			user.setSelected(true);
		} else {
			ia.setSelected(true);
		}
		this.getChildren().addAll(typePlayer, typeIa, typeS);
		this.setBorder(
				(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))));
	}
}