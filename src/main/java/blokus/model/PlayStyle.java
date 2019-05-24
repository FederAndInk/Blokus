package blokus.model;

/**
 * PlayerType
 */
public enum PlayStyle {
	RAND_BIG_PIECE("Grosse piece aleatoire"), //
	RAND_PIECE("Piece aleatoire"), //
	BIG_PIECE("Grosse piece prioritaire"), //
	HEURISTIC("Heuristique");

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
		case HEURISTIC:
			return null;
		default:
			return null;
		}
	}

	@Override
	public String toString() {
		return name;
	}
}
