package controller;

import java.util.ArrayList;

import model.MinesweeperCell;
import model.MinesweeperModel;

public class MinesweeperController {
	
	private MinesweeperModel model;
	private MinesweeperCell[][] refToBoard;
	private boolean gameOver;
	private int mineCount;
	private int flagCount;
	private int cellsHidden;
	private boolean gameWon;
	
	public MinesweeperController(MinesweeperModel model) {
		this.model = model;
		refToBoard = model.getBoard();
		gameOver = false;
		gameWon = false;
		cellsHidden = model.getRow() * model.getCol();
		flagCount = 0;
	}
	
	public boolean gameWon() {
		return gameWon;
	}
	
	public void flagCell(int row, int col) {
		MinesweeperCell curCell = refToBoard[row][col];
		curCell.setFlagged();
		if(curCell.isFlagged()) {
			flagCount++;
		} else {
			flagCount--;
		}
	}
	
	public void playMove(int row, int col) {
		MinesweeperCell  curMove = refToBoard[row][col];
		if(curMove.isHidden() && !gameOver) {
			if(cellsHidden == model.getRow() * model.getCol()) {
				model.setBombs(row, col);
				revealCells(row, col);
			} else if(curMove.isMined()) {
				gameOver = true;
				showBombs();
			}
			else {
				revealCells(row, col);
			}
		}
		
		if(isGameOver()) {
			gameOver = true;
		}
	}
	
	// Reveal all cells after a move is made.
	// If a cells has 0 mines around it, it will recursively reveal all cells until it stops.
	private void revealCells(int row, int col) {
		if(row < 0 || col < 0 || row >= model.getRow() || col >= model.getCol()) {
			return;
		}
		
		if(!refToBoard[row][col].isHidden()) {
			return;
		}
		
		refToBoard[row][col].setHidden();
		cellsHidden--;
		
		if(refToBoard[row][col].getMines() == 0) {
			revealCells(row, col - 1);
			revealCells(row, col + 1);
			revealCells(row - 1, col);
			revealCells(row - 1, col - 1);
			revealCells(row - 1, col + 1);
			revealCells(row + 1, col);
			revealCells(row + 1, col - 1);
			revealCells(row + 1, col + 1);
		}
	}
	
	//should loop through bomb array and call setHidden() on all cells in the bomb array
	private void showBombs() {
		ArrayList<MinesweeperCell> bombsArray = model.getBombs();
		
		for(int i=0; i<bombsArray.size(); i++) {
			bombsArray.get(i).setHidden();
		}
	}
	
	public MinesweeperCell getCellClue(int row, int col) {
		return refToBoard[row][col];
		
	}
	
	public boolean isGameOver() {
		ArrayList<MinesweeperCell> bombsArray = model.getBombs();
		
		if(gameOver) {
			return true;
		}
		
		if(cellsHidden != model.countOfMines()) {
			return false;
		}
		
		gameWon = true;
		return true;
	}
}
