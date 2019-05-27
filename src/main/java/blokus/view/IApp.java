package blokus.view;

import blokus.model.APlayer;
import blokus.model.Piece;

/**
 * IApp
 */
public interface IApp {

  void update(APlayer oldPlayer, Piece playedPiece);
  void undo(APlayer oldPlayer, Piece removedPiece);
  void playerPassed(APlayer player);
}