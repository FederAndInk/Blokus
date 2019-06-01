package blokus.model;

import static blokus.Tools.loadFile;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * TestBoard
 */
public class TestBoard {

    ArrayList<Piece> pieces;

    @BeforeClass
    public void init() {
        pieces = loadFile("piecesDOWN_REVERTED");
    }

    @Test
    public void test_canAdd() { // Test for add/getAccCorners as well?
        // Try some coherente addings (each corner/ + a Piece bordering to an other but
        // with different colors)
        // -----------------------------------------BLOKUS-----------------------------------------//
        Board b = new Board(GameType.BLOKUS);

        // Try canAdd on a non-corner square
        assertFalse(b.canAdd(new Piece(pieces.get(0)), new Coord(1, 1), PColor.RED));

        // Add 1x1 piece in each corner
        assertTrue(b.canAdd(new Piece(pieces.get(0)), new Coord(0, 0), PColor.BLUE));
        b.add(new Piece(pieces.get(0)), new Coord(0, 0), PColor.BLUE);

        assertTrue(b.canAdd(new Piece(pieces.get(0)), new Coord(b.getSize() - 1, b.getSize() - 1), PColor.YELLOW));
        b.add(new Piece(pieces.get(0)), new Coord(b.getSize() - 1, b.getSize() - 1), PColor.YELLOW);

        assertTrue(b.canAdd(new Piece(pieces.get(0)), new Coord(b.getSize() - 1, 0), PColor.RED));
        b.add(new Piece(pieces.get(0)), new Coord(b.getSize() - 1, 0), PColor.RED);

        assertTrue(b.canAdd(new Piece(pieces.get(0)), new Coord(0, b.getSize() - 1), PColor.GREEN));
        b.add(new Piece(pieces.get(0)), new Coord(0, b.getSize() - 1), PColor.GREEN);

        // Adding a few pieces of another colour for the next test
        b.add(new Piece(pieces.get(14)), new Coord(1, b.getSize() - 2), PColor.GREEN);
        b.add(new Piece(pieces.get(14)), new Coord(6, b.getSize() - 1), PColor.GREEN);
        b.add(new Piece(pieces.get(14)), new Coord(14, b.getSize() - 2), PColor.YELLOW);

        // Try canAdd on a border of an other piece of a different color
        assertTrue(b.canAdd(new Piece(pieces.get(3)), new Coord(11, b.getSize() - 1), PColor.YELLOW));
        // Try canAdd on a border of an other piece of the same color
        assertFalse(b.canAdd(new Piece(pieces.get(3)), new Coord(13, b.getSize() - 1), PColor.YELLOW));

        // Try canAdd without color coherence
        assertFalse(b.canAdd(new Piece(pieces.get(0)), new Coord(13, b.getSize() - 3), PColor.GREEN));
        // b.add(new Piece(pieces.get(14)), new Coord(10,b.getSize() -2), b.get(new
        // Coord(b.getSize() -1, b.getSize()-1))); possible test de add apres le canAdd
        // correspondant

        b = new Board(GameType.BLOKUS);

        b.add(new Piece(pieces.get(5)), new Coord(0, 0), PColor.BLUE);
        b.add(new Piece(pieces.get(7)), new Coord(2, 2), PColor.BLUE);
        assertFalse(b.canAdd(new Piece(pieces.get(0)), new Coord(2, 2), PColor.BLUE));
        b.add(new Piece(pieces.get(0)), new Coord(b.getSize() - 1, b.getSize() - 1), PColor.YELLOW);
        b.add(new Piece(pieces.get(6)), new Coord(b.getSize() - 4, 0), PColor.RED);
        b.add(new Piece(pieces.get(14)), new Coord(11, 1), PColor.RED);
        b.add(new Piece(pieces.get(14)), new Coord(6, 0), PColor.RED);
        assertFalse(b.canAdd(new Piece(pieces.get(4)), new Coord(5, 1), PColor.RED));
        // Try canAdd with an exit of the board

        // -------------------------------------------DUO-------------------------------------------------//
        b = new Board(GameType.DUO);
        b.add(new Piece(pieces.get(0)), new Coord(4, 4), PColor.BLUE);
        b.add(new Piece(pieces.get(0)), new Coord(b.getSize() - 5, b.getSize() - 5), PColor.YELLOW);

        // Try canAdd on a non-starting square
        assertFalse(b.canAdd(new Piece(pieces.get(0)), new Coord(1, 1), PColor.BLUE));

        // Try canAdd on a border of an other piece of a different color
        b.add(new Piece(pieces.get(13)), new Coord(5, 5), PColor.BLUE);
        assertTrue(b.canAdd(new Piece(pieces.get(9)), new Coord(b.getSize() - 6, b.getSize() - 7), PColor.YELLOW));
        // Try canAdd on a border of an other piece of the same color
        assertFalse(b.canAdd(new Piece(pieces.get(3)), new Coord(b.getSize() - 6, b.getSize() - 4), PColor.YELLOW));
        // Try canAdd without color coherence
        assertFalse(b.canAdd(new Piece(pieces.get(0)), new Coord(3, 3), PColor.YELLOW));

        // Try canAdd on an occupied square
        assertFalse(b.canAdd(new Piece(pieces.get(5)), new Coord(b.getSize() - 7, b.getSize() - 7), PColor.YELLOW));
        assertFalse(b.canAdd(new Piece(pieces.get(4)), new Coord(b.getSize() - 6, b.getSize() - 6), PColor.BLUE));

        // Try canAdd with an exit of the board
        b.canAdd(new Piece(pieces.get(14)), new Coord(b.getSize() - 3, b.getSize() - 3), PColor.YELLOW);
    }
}