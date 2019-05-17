package blokus.model;

import java.util.ArrayList;
import java.util.Random;

import blokus.model.Placement;

public class RandPieceChooser implements PieceChooser {
    Random r = new Random();

    @Override
    public Piece pickPiece(ArrayList<Piece> availablePieces) {
        return availablePieces.get(r.nextInt(availablePieces.size()));
    }

    @Override
    public Placement pickPlacement(ArrayList<Placement> placements) {
        return placements.get(r.nextInt(placements.size()));
    }

}