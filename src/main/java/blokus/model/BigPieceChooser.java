package blokus.model;

import java.util.List;
import java.util.Random;

public class BigPieceChooser implements PieceChooser {
    Random r = new Random();

    @Override
    public Piece pickPiece(List<Piece> availablePieces) {
        int max = availablePieces.stream().max((p1, p2) -> {
            return Integer.compare(p1.size(), p2.size());
        }).get().size();

        Piece[] ps = availablePieces.stream().filter((p) -> {
            return p.size() == max;
        }).toArray(Piece[]::new);
        return ps[r.nextInt(ps.length)];
    }

    @Override
    public Move pickMove(List<Move> moves) {
        int max = moves.stream().max((p1, p2) -> {
            return Integer.compare(p1.getPiece().size(), p2.getPiece().size());
        }).get().getPiece().size();

        Move[] ps = moves.stream().filter((p) -> {
            return p.getPiece().size() == max;
        }).toArray(Move[]::new);
        return ps[r.nextInt(ps.length)];
    }

}