package blokus.controller;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.testng.annotations.Test;

import blokus.model.APlayer;
import blokus.model.Piece;
import blokus.model.PieceChooser;
import blokus.model.PlayerType;
import blokus.model.RandBigPieceChooser;
import blokus.view.IApp;
import javafx.scene.paint.Color;

public class GameTest implements IApp {
    Game g = new Game();
    ArrayList<PlayerType> types = new ArrayList<>();
    ArrayList<Long> times = new ArrayList<>();

    public void game_test(PlayerType pt1, PlayerType pt2) {
        types.add(pt1);
        types.add(pt2);
        g.setApp(this);
        PieceChooser pc = new RandBigPieceChooser();
        g.addPlayer(pt1, pc);
        g.addPlayer(pt2, pc);
        g.init(14);
        System.out.println(g.getBoard());
        long startTime = System.currentTimeMillis();
        while (!g.isEndOfGame()) {
            g.refresh();
        }
        long endTime = System.currentTimeMillis();
        times.add(endTime - startTime);
        System.out.println("scores:");
        for (Entry<Color, Integer> sc : g.getScore().entrySet()) {
            System.out.println(g.getPlayer(sc.getKey()) + ": " + sc.getValue());
        }
    }

    @Override
    public void update(APlayer oldPlayer, Piece playedPiece) {
        System.out.println(g.getBoard());
    }

    public String getResult(int i) {
        i *= 2;
        return ("Game with players of type " + types.get(i) + " and " + types.get(i + 1) + ": Total execution time: "
                + times.get(i) + "ms");
    }

    public void results() {
        for (int i = 0; i < times.size(); i++) {
            System.out.println(getResult(i));
        }
    }

    @Test
    public void game_test() {
        game_test(PlayerType.RANDOM_PIECE, PlayerType.RANDOM_PIECE);
        System.out.println("*****************************************************************************");
        game_test(PlayerType.RANDOM_PLAY, PlayerType.RANDOM_PLAY);
        System.out.println("*****************************************************************************");
        game_test(PlayerType.AI, PlayerType.RANDOM_PIECE);
        results();
    }

    @Override
    public void playerPassed(APlayer player) {

    }
}