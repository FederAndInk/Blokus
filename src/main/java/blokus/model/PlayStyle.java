package blokus.model;

/**
 * PlayerType
 */
public enum PlayStyle {
	RANDBIGPIECE("Grosse piece aleatoire"), //
	RANDPIECE("Piece aleatoire"), //
	BIGPIECE("Grosse piece prioritaire"), //
	HEURISTIC("Heuristique");

	private String name;

	PlayStyle(String name) {
		this.name = name;
	}

	public PieceChooser create() {
		switch (this) {
		case BIGPIECE:
			return new BigPieceChooser();
		case RANDPIECE:
			return new RandPieceChooser();
		case RANDBIGPIECE:
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
