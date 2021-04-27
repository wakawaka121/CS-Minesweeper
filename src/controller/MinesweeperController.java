package controller;

import model.MinesweeperModel;

public class MinesweeperController {
	
	private MinesweeperModel model;
	private boolean gameOver;
	private int mineCount;
	
	public MinesweeperController(MinesweeperModel model) {
		this.model = model;
		gameOver = false;
		mineCount = model.countOfMines();
	}
	
	public void flagCell(int row, int col) {
		model.getBoard()[row][col].setFlagged();
	}
	
	public void playMove(int row, int col) {
		if(model.getBoard()[row][col].isMined()) {
			gameOver = true;
		}
		else {
			updateBoard(row, col);
		}
	}
	
	//Should scan in the NW,N,NE,E,SE,S,SW,W to find a cell that has a number greater than 0
	//and update mineCount
	private void updateBoard(int row, int col) {
		if(row-1 >=0 && col-1 >=0) {
			moveNW(row-1,col-1);
		}
		if(row-1 >= 0) {
			//TODO
		}
		if(row-1 >=0 && col+1 < model.getCol()) {
			//TODO
		}
		if(row-1 >=0 && col-1 >=0) {
			//TODO
		}
		if(row-1 >=0 && col-1 >=0) {
			//TODO
		}
		if(row-1 >=0 && col-1 >=0) {
			//TODO
		}
		if(row-1 >=0 && col-1 >=0) {
			//TODO
		}
		if(row-1 >=0 && col-1 >=0) {
			//TODO
		}
	}
	
	private void moveNW(int row, int col) {
		
	}
	private void moveN(int row, int col) {
		
	}
	private void moveNE(int row, int col) {
	
	}
	private void moveE(int row, int col) {
		
	}
	private void moveSE(int row, int col) {
		
	}
	private void moveS(int row, int col) {
		
	}
	private void moveSW(int row, int col) {
		
	}
	private void moveW(int row, int col) {
		
	}
	
	

//	public int getCellClue(int row, int col) {
//		return 0;
//		
//	}
	
	public boolean isGameOver() {
		return gameOver;
		
	}

}
