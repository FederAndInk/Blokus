package blokus.model;

import java.util.List;

public interface PieceChooser {
    Piece pickPiece(List<Piece> availablePieces);

    Placement pickPlacement(List<Placement> placements);
}