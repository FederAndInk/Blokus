package blokus.view;

import blokus.model.Coord;
import javafx.animation.AnimationTimer;

class StatusTimer extends AnimationTimer {

	private boolean running;
	public PieceView movingPiece = null;
	double oldPieceCoordX;
	double oldPieceCoordY;
	double oldSizeSquare;

	public void setMovingPiece(PieceView movingPiece) {
		oldPieceCoordX = movingPiece.getLayoutX();
		oldPieceCoordY = movingPiece.getLayoutY();
		oldSizeSquare = movingPiece.pieceSize;
		this.movingPiece = movingPiece;
	}

	public void cancelMove() {
		this.movingPiece.setLayoutX(oldPieceCoordX);
		this.movingPiece.setLayoutY(oldPieceCoordY);
		this.movingPiece.setSizeSquare(oldSizeSquare);
		// this.movingPiece.drawPiece();
		this.movingPiece.setMouseTransparent(false);
		clearMovingPiece();
		this.stop();
	}

	public void clearMovingPiece() {
		this.movingPiece = null;
	}

	@Override
	public void start() {
		super.start();
		running = true;
	}

	@Override
	public void stop() {
		super.stop();
		running = false;
		this.clearMovingPiece();
	}

	public boolean isRunning() {
		return running;
	}

	@Override
	public void handle(long now) {

	}

}