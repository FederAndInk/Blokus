package blokus.view;

import javafx.animation.AnimationTimer;

class StatusTimer extends AnimationTimer {

	private boolean running;
	public PieceView movingPiece = null;

	public void setMovingPiece(PieceView movingPiece) {
		this.movingPiece = movingPiece;
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