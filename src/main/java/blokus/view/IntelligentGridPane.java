package blokus.view;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;

/**
 * GridPaneMethod
 */
public class IntelligentGridPane extends GridPane {

	public int getRowCount() {
		int numRows = getRowConstraints().size();
		for (int i = 0; i < getChildren().size(); i++) {
			Node child = getChildren().get(i);
			if (child.isManaged()) {
				Integer rowIndex = GridPane.getRowIndex(child);
				if (rowIndex != null) {
					numRows = Math.max(numRows, rowIndex + 1);
				}
			}
		}
		return numRows;
	}

	public int getColCount() {
		int numCols = getColumnConstraints().size();
		for (int i = 0; i < getChildren().size(); i++) {
			Node child = getChildren().get(i);
			if (child.isManaged()) {
				Integer colIndex = GridPane.getColumnIndex(child);
				if (colIndex != null) {
					numCols = Math.max(numCols, colIndex + 1);
				}
			}
		}
		return numCols;
	}
}