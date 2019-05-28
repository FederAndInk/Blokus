package blokus.view;

import javafx.animation.AnimationTimer;
import javafx.scene.input.ScrollEvent;

import javafx.scene.input.MouseEvent;

import javafx.scene.Scene;

abstract class StatusTimer extends AnimationTimer {

	private boolean running;
	public PieceView movingPiece = null;
	double oldPieceCoordX;
	double oldPieceCoordY;
	double oldSizeSquare;

	public void setMovingPiece(PieceView movingPiece) {
		oldPieceCoordX = movingPiece.getLayoutX();
		oldPieceCoordY = movingPiece.getLayoutY();
		oldSizeSquare = movingPiece.getPieceSize();
		this.movingPiece = movingPiece;
	}

	public void cancelMove(Scene sc) {
		this.movingPiece.setLayoutX(oldPieceCoordX);
		this.movingPiece.setLayoutY(oldPieceCoordY);
		this.movingPiece.setSizeSquare(oldSizeSquare);
		// this.movingPiece.drawPiece();
		this.movingPiece.setMouseTransparent(false);
		this.movingPiece.setColor(this.movingPiece.getColor());
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

}