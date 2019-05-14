package test;

import java.util.Observable;
import java.util.Observer;

import controller.Game;
import model.Board;
import model.PlayerType;

public class GameTest implements Observer {
    Game g = new Game();
    PlayerType type;
    long time;

    public GameTest(PlayerType type) {
        this.type = type;
        g.setApp(this);
        g.addPlayer(type);
        g.addPlayer(type);
        System.out.println(g.getBoard());
        long startTime = System.currentTimeMillis();
        while (!g.isEndOfGame()) {
            g.refresh();
        }
        long endTime = System.currentTimeMillis();
        time = endTime - startTime;

    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Board) {
            System.out.println(g.getBoard());
        }
    }

    public String getResult() {
        return ("Game with players of type " + type + ": Total execution time: " + time + "ms");

    }

    public static void main(String[] args) {
        GameTest randPlay = new GameTest(PlayerType.RANDOM_PLAY);
        System.out.println("*****************************************************************************");
        GameTest randPiece = new GameTest(PlayerType.RANDOM_PIECE);
        System.out.println(randPiece.getResult());
        System.out.println(randPlay.getResult());

    }
}