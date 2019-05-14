package blokus.controller;

import java.util.Observable;
import java.util.Observer;

import org.testng.annotations.Test;

import blokus.model.Board;
import blokus.model.PlayerType;

public class GameTest implements Observer {
    Game g = new Game();

    @Test
    public void game_test() {
        g.setApp(this);
        g.addPlayer(PlayerType.RANDOM);
        g.addPlayer(PlayerType.RANDOM);
        System.out.println(g.getBoard());
        while (!g.isEndOfGame()) {
            // g.getCurPlayer().;
            g.refresh();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Board) {
            System.out.println(g.getBoard());
        }
    }
}