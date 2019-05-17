package blokus.model;

import java.util.ArrayList;

public interface PieceChooser {
    Piece pickPiece(ArrayList<Piece> availablePieces);

    Placement pickPlacement(ArrayList<Placement> placements);
}