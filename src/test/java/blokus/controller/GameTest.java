package blokus.controller;

import java.util.ArrayList;

import org.testng.annotations.Test;

import blokus.model.PieceChooser;
import blokus.model.PlayerType;
import blokus.model.RandBigPieceChooser;
import blokus.view.IApp;

public class GameTest implements IApp {
    Game g = new Game();
    ArrayList<PlayerType> types = new ArrayList<>();
    ArrayList<Long> times = new ArrayList<>();

    public void game_test(PlayerType type) {
        types.add(type);
        g.setApp(this);
        PieceChooser pc = new RandBigPieceChooser();
        g.addPlayer(type, pc);
        g.addPlayer(type, pc);
        System.out.println(g.getBoard());
        long startTime = System.currentTimeMillis();
        while (!g.isEndOfGame()) {
            g.refresh();
        }
        long endTime = System.currentTimeMillis();
        times.add(endTime - startTime);
    }

    @Override
    public void update() {
        System.out.println(g.getBoard());
    }

    public String getResult(int i) {
        return ("Game with players of type " + types.get(i) + ": Total execution time: " + times.get(i) + "ms");

    }

    @Test
    public void game_test() {
        game_test(PlayerType.RANDOM_PLAY);
        System.out.println("*****************************************************************************");
        game_test(PlayerType.RANDOM_PIECE);

        for (int i = 0; i < times.size(); i++) {
            System.out.println(getResult(i));
        }
    }
}