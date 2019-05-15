package blokus.controller;

import org.testng.annotations.Test;

import blokus.model.PlayerType;
import blokus.view.IApp;

public class GameTest implements IApp {
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
    public void update() {
        System.out.println(g.getBoard());
    }
}