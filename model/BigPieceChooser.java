package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import model.RandomPlayAI.Placement;

public class BigPieceChooser implements PieceChooser {
    Random r = new Random();

    @Override
    public Piece pickPiece(ArrayList<Piece> availablePieces) {
        int max = availablePieces.stream().max((p1, p2) -> {
            return Integer.compare(p1.size(), p2.size());
        }).get().size();

        Piece[] ps = (Piece[]) (availablePieces.stream().filter((p) -> {
            return p.size() == max;
        }).toArray());
        return ps[r.nextInt(ps.length)];
    }

    @Override
    public Placement pickPlacement(ArrayList<Placement> placements) {
        int max = placements.stream().max((p1, p2) -> {
            return Integer.compare(p1.piece.size(), p2.piece.size());
        }).get().piece.size();

        Placement[] ps = (Placement[]) (placements.stream().filter((p) -> {
            return p.piece.size() == max;
        }).toArray());
        return ps[r.nextInt(ps.length)];
    }

}