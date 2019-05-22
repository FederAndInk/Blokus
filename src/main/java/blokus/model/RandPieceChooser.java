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
    public Placement pickPlacement(List<Placement> placements) {
        return placements.get(r.nextInt(placements.size()));
    }

}