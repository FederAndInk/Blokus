package blokus.model;

public class Placement {
    Piece piece;
    PieceTransform trans;
    Coord pos;

    public Placement(Piece p, PieceTransform tr, Coord c) {
        piece = p;
        trans = tr;
        pos = c;
    }

}