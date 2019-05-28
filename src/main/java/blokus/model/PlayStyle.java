package blokus.model;

import blokus.model.piecechooser.AdversaryLimitingChooser;
import blokus.model.piecechooser.BigPieceChooser;
import blokus.model.piecechooser.CenterPieceChooser;
import blokus.model.piecechooser.PieceChooser;
import blokus.model.piecechooser.RandBigPieceChooser;
import blokus.model.piecechooser.RandPieceChooser;

/**
 * PlayerType
 */
public enum PlayStyle {
	RAND_BIG_PIECE("Grosse piece aleatoire"), //
	RAND_PIECE("Piece aleatoire"), //
	BIG_PIECE("Grosse piece prioritaire"), //
	ADVERSARY_LIMITING("Bloque adversaire"),//
	CENTER("Premier au centre"),//
	;

	private String name;

	PlayStyle(String name) {
		this.name = name;
	}

	public PieceChooser create() {
		switch (this) {
		case BIG_PIECE:
			return new BigPieceChooser();
		case RAND_PIECE:
			return new RandPieceChooser();
		case RAND_BIG_PIECE:
			return new RandBigPieceChooser();
		case ADVERSARY_LIMITING:
			return new AdversaryLimitingChooser();
		case CENTER:
			return new CenterPieceChooser();
		default:
			return null;
		}
	}

	@Override
	public String toString() {
		return name;
	}
}
