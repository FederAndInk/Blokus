package blokus.model;

import blokus.model.piecechooser.AccCornersMaximizer;
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
	ROUND_HEURISTIC("utilise les heuristiques les plus adapter au round du jeu courant"),
	CORNER_MAXIMIZER("Maximise les coins"),
	BIG_PIECE_CORNER_MAXIMIZER("Maximise les coins en prenant les grosse pieces"),
	;

	private String name;

	PlayStyle(String name) {
		this.name = name;
	}

	public PieceChooser create(PColor color) {
		switch (this) {
		case BIG_PIECE:
			return new BigPieceChooser(color);
		case RAND_PIECE:
			return new RandPieceChooser(color);
		case RAND_BIG_PIECE:
			return new RandBigPieceChooser(color);
		case ADVERSARY_LIMITING:
			return new AdversaryLimitingChooser(color);
		case CENTER:
			return new CenterPieceChooser(color);
		case TWO_HEURISTICS:
			return new TwoHeuristicsPieceChooser(color, new BigPieceChooser(color), new AdversaryLimitingChooser(color));
		case ROUND_HEURISTIC:
			return new RoundPieceChooser(color);
		case CORNER_MAXIMIZER:
			return new AccCornersMaximizer(color);
		case BIG_PIECE_CORNER_MAXIMIZER:
			return new TwoHeuristicsPieceChooser(color, new BigPieceChooser(color), new AccCornersMaximizer(color));
		}
		return null;
	}

	@Override
	public String toString() {
		String ret = name;
		if (this == TWO_HEURISTICS) {
			TwoHeuristicsPieceChooser pc = (TwoHeuristicsPieceChooser) this.create(PColor.NO_COLOR);
			ret += "(" + pc.getPc1().getClass().getSimpleName() + ", " + pc.getPc2().getClass().getSimpleName() + ")";
		}
		return ret;
	}
}
