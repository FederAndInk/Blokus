package blokus.view;

import blokus.model.APlayer;
import blokus.model.Piece;

/**
 * IApp
 */
public interface IApp {

  void update(APlayer oldPlayer, Piece playedPiece);
  void playerPassed(APlayer player);
}