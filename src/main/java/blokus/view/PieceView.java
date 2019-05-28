package blokus.view;

import java.util.ArrayList;

import blokus.controller.Game;
import blokus.model.APlayer;
import blokus.model.Coord;
import blokus.model.PColor;
import blokus.model.Piece;
import javafx.geometry.Insets;
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

/**
 * PieceView
 */
public class PieceView extends IntelligentGridPane {
	final double pieceMarginW = 15;
	final double pieceMarginH = 15;
	final double borderSize = BorderWidths.DEFAULT.getLeft();
	private int nbCol = 0;
	private int nbRow = 0;
	private ArrayList<ColumnConstraints> colv = new ArrayList<>();
	private ArrayList<RowConstraints> rowv = new ArrayList<>();
	private ArrayList<Coord> shape;
	private Game game;
	private double pieceSize;
	private PColor pColor;
	private int noPiece;
	private Color color;
	private boolean active;

	public void setSizeSquare(double pieceSize) {
		colv.clear();
		rowv.clear();
		ColumnConstraints col = new ColumnConstraints(pieceSize);
		RowConstraints row = new RowConstraints(pieceSize);
		for (int l = 0; l < nbRow; l++) {
			rowv.add(row);
		}
		for (int l = 0; l < nbCol; l++) {
			colv.add(col);
		}
		this.getColumnConstraints().setAll(colv);
		this.getRowConstraints().setAll(rowv);
		this.pieceSize = pieceSize;
	}

	public void clearPiece() {
		this.getChildren().clear();
	}

	public Pane get(int x, int y) {
		Pane res = null;
		for (int i = 0; i < this.getChildren().size(); i++) {
			if (this.getChildren().get(i) instanceof Pane) {
				Pane tempPane = (Pane) this.getChildren().get(i);
				if ((IntelligentGridPane.getColumnIndex(tempPane) == x) && (IntelligentGridPane.getRowIndex(tempPane) == y)) {
					res = tempPane;
				}
			}
		}
		return res;
	}

	public void setColor(Color c) {
		for (int i = 0; i < getColCount(); i++) {
			for (int j = 0; j < getRowCount(); j++) {
				Pane p = this.get(i, j);
				if (p != null && p.getBackground() != null) {
					p.setBackground(new Background(new BackgroundFill(c, CornerRadii.EMPTY, Insets.EMPTY)));
				}
			}
		}
	}

	public void setActive(boolean active) {
		this.active = active;
		if (active) {
			setColor(color);
		} else {
			setColor(color.darker().darker());
		}
	}

	public APlayer getPlayer() {
		return game.getPlayer(pColor);
	}

	/**
	 * @return the piece
	 */
	public Piece getPiece() {
		return getPlayer().getPiece(noPiece);
	}

	/**
	 * @return the nbRow
	 */
	public int getNbRow() {
		return nbRow;
	}

	/**
	 * @return the nbCol
	 */
	public int getNbCol() {
		return nbCol;
	}

	public boolean getActive() {
		return this.active;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @return the pieceSize
	 */
	public double getPieceSize() {
		return pieceSize;

	}

	public void drawPiece() {
		for (Coord var : shape) {
			Pane p = new Pane();
			p.setMaxWidth(Double.MAX_VALUE);
			p.setMaxHeight(Double.MAX_VALUE);
			p.setBackground(new Background(new BackgroundFill(this.color, CornerRadii.EMPTY, Insets.EMPTY)));
			this.add(p, var.x, var.y);
			p.setBorder((new Border(
					new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT))));
		}
		// this.setBorder(
		// (new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
		// CornerRadii.EMPTY, BorderWidths.DEFAULT))));
		nbCol = this.getColCount();
		nbRow = this.getRowCount();

		// if (this.impl_getRowCount() > nbRow) {
		// System.out.println(nbRow);
		// }
	}

	public PieceView(Piece piece, Game game, double pieceSize, APlayer player, Color c) {

		this.noPiece = piece.no;
		this.game = game;
		this.pieceSize = pieceSize;
		this.pColor = player.getColor();
		this.color = c;
		this.active = true;
		shape = piece.getShape();
		this.drawPiece();

		// System.out.println("la taille de la piece est : " + shape.size());
		// System.out.println("nbow = " + nbRow + " nbcol = " + nbCol + " " +
		// colv.size() + " " + rowv.size());

		// this.toFront();
		// root.getChildren().add(this);
		// this.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>()
		// {
		// @Override
		// public void handle(MouseEvent t) {
		// if (timer.isRunning()) {
		// timer.stop();
		// timer.clearMovingPiece();
		// } else {
		// timer.setMovingPiece(this);
		// timer.start();
		// }
		// }
		// });
	}

}