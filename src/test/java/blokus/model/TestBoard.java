package blokus.model;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static blokus.Tools.*;

import java.util.ArrayList;

/**
 * TestBoard
 */
public class TestBoard {

    ArrayList<Piece> pieces;

    @BeforeClass
    public void init() {
        pieces = loadFile("piecesUP");
    }

    @Test
    public void test_canAddCoord() {
    }
}