package blokus.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import blokus.model.APlayer;
import blokus.model.GameType;
import blokus.model.PColor;
import blokus.model.Piece;
import blokus.model.PlayStyle;
import blokus.model.PlayerType;
import blokus.view.IApp;

/**
 * HeuristicsTest
 */
public class HeuristicsTest implements IApp {
    Game g;
    ArrayList<PlayerType> types = new ArrayList<>();
    ArrayList<Long> times = new ArrayList<>();
    HashMap<PlayerType, HashMap<PlayStyle, Integer>> final_score = new HashMap<>();

    public void Heuristics_Test(PlayerType pT1, PlayStyle pS1) {
        int countWins = 0;
        for (int i = 0; i < 2; i++) {
            g = new Game();
            types.add(pT1);
            types.add(PlayerType.RANDOM_PIECE);
            g.addPlayer(pT1, pS1);
            APlayer p = g.getPlayers().get(0);
            g.addPlayer(PlayerType.RANDOM_PIECE, PlayStyle.RAND_PIECE);
            g.init(GameType.DUO);
            long startTime = System.currentTimeMillis();
            while (!g.isEndOfGame()) {
                g.refresh();
            }
            long endTime = System.currentTimeMillis();
            times.add(endTime - startTime);
            System.out.println("scores:");
            for (Entry<PColor, Integer> sc : g.getScore().entrySet()) {
                System.out.println(g.getPlayer(sc.getKey()) + ": " + sc.getValue());
            }
            if (g.getWinner().contains(p)) {
                countWins++;
            }
            if (final_score.containsKey(pT1)) {
                final_score.get(pT1).put(pS1, countWins);
            } else {
                final_score.put(pT1, new HashMap<>());
                final_score.get(pT1).put(pS1, countWins);
            }

        }
        results();
    }

    public String getResult(int i) {
        int i2 = i * 2;
        return ("Game with players of type " + types.get(i2) + " and " + types.get(i2 + 1) + ": Total execution time: "
                + times.get(i) + "ms");
    }

    public void results() {
        for (int i = 0; i < times.size(); i++) {
            System.out.println(getResult(i));
        }
    }

    @Override
    public void update(APlayer oldPlayer, Piece playedPiece) {

    }

    @Override
    public void undo(APlayer oldPlayer, Piece removedPiece) {

    }

    @Override
    public void playerPassed(APlayer player) {

    }

    public HeuristicsTest() {
        for (PlayStyle pS1 : PlayStyle.values()) {
            Heuristics_Test(PlayerType.AI, pS1);
            System.out.println(final_score);
            Heuristics_Test(PlayerType.MCAI, pS1);
            System.out.println(final_score);
        }
        System.out.println(final_score);
    }
}