package test;

import java.io.InputStream;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import model.APlayer;
import model.Board;
import model.Config;
import model.Coord;
import model.Direction;
import model.Piece;
import model.PieceReader;
import controller.Game;
import test.tools.Test;
import view.App;
import model.PieceTransform;
import java.util.Random;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class GameTest {

    public static void main(String[] args) {
        Game g = new Game();
        Board board = g.getBoard();
        g.addPlayer(false);
        g.addPlayer(false);

        Random r = new Random();
        APlayer player;
        Piece piece;
        Coord pos = new Coord(17, 0);

        player = g.getCurPlayer();
        piece = player.getPieces().get(11);
        piece.right();
        piece.right();
        player.play(piece, board, pos);

        g.nextPlayer();

        while (!g.isEndOfGame()) {
            player = g.getCurPlayer();
            if (!player.hasToPass(board)) {
                System.out.println(player + ":");
                HashMap<PieceTransform, HashSet<Coord>> possiblePlacements;
                do {
                    piece = player.getPieces().get(r.nextInt(player.getPieces().size()));
                    System.out.println("try piece " + piece);

                    possiblePlacements = board.whereToPlay(piece, player.getColor());
                } while (possiblePlacements.isEmpty());
                PieceTransform pt = possiblePlacements.keySet().toArray(new PieceTransform[0])[r
                        .nextInt(possiblePlacements.keySet().size())];
                piece.apply(pt);
                pos = possiblePlacements.get(pt).toArray(new Coord[0])[r.nextInt(possiblePlacements.get(pt).size())];

                player.play(piece, board, pos);
                System.out.println(board);
            } else {
                System.out.println(player + " can't play");
            }
            g.nextPlayer();
        }

    }

}