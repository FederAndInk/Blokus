package blokus.model;

import blokus.model.piecechooser.AdversaryLimitingChooser;
import blokus.model.piecechooser.BigPieceChooser;
import blokus.model.piecechooser.CenterPieceChooser;
import blokus.model.piecechooser.PieceChooser;
import blokus.model.piecechooser.RandBigPieceChooser;
import blokus.model.piecechooser.RandPieceChooser;
import blokus.model.piecechooser.RoundPieceChooser;
import blokus.model.piecechooser.TwoHeuristicsPieceChooser;

/**
 * PlayerType
 */
public enum PlayStyle {
	RAND_BIG_PIECE("Grosse piece aleatoire"), //
	RAND_PIECE("Piece aleatoire"), //
	BIG_PIECE("Grosse piece prioritaire"), //
	ADVERSARY_LIMITING("Bloque adversaire"), //
	CENTER("Premier au centre"), //
	TWO_HEURISTICS("utiliser 2 heuristiques pour filtrer les resultats"),
	ROUND_HEURISTIC("utiliser les heuristiques les plus adaptees au tour de jeu courant");

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
		case TWO_HEURISTICS:
			return new TwoHeuristicsPieceChooser(new BigPieceChooser(), new AdversaryLimitingChooser());
		case ROUND_HEURISTIC:
			return new RoundPieceChooser();
		default:
			return null;
		}
	}

	@Override
	public String toString() {
		return name;
	}
}
