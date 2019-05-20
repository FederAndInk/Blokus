package blokus.model;

public class Placement {
    public Piece piece;
    public PieceTransform trans;
    public Coord pos;

    public Placement(Piece p, PieceTransform tr, Coord c) {
        piece = p;
        trans = tr;
        pos = c;
    }

}