package model;

import java.util.ArrayList;

public interface PieceChooser {
    Piece pickPiece(ArrayList<Piece> availablePieces);

    RandomPlayAI.Placement pickPlacement(ArrayList<RandomPlayAI.Placement> placements);
}