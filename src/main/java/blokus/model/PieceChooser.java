package blokus.model;

import java.util.List;

public interface PieceChooser {
    Piece pickPiece(List<Piece> availablePieces);

    Move pickMove(List<Move> moves);
}