package test;

import java.util.Observable;
import java.util.Observer;

import controller.Game;
import model.Board;
import model.PlayerType;

public class GameTest implements Observer {
    Game g = new Game();

    public GameTest() {
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

    public static void main(String[] args) {
        new GameTest();
    }
}