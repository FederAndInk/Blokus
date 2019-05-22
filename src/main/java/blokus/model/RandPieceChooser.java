package blokus.model;

import java.util.List;
import java.util.Random;

public class RandPieceChooser implements PieceChooser {
    Random r = new Random();

    @Override
    public Piece pickPiece(List<Piece> availablePieces) {
        return availablePieces.get(r.nextInt(availablePieces.size()));
    }

    @Override
    public Move pickMove(List<Move> moves) {
        return moves.get(r.nextInt(moves.size()));
    }

}