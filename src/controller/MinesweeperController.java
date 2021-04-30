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
	
	public MinesweeperController(MinesweeperModel model) {
		this.model = model;
		refToBoard = model.getBoard();
		gameOver = false;
		cellsHidden = model.getRow() * model.getCol();
		flagCount = 0;
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
		if(curMove.isHidden()) {
			if(cellsHidden == model.getRow() * model.getCol()) {
				model.setBombs(row, col);
				updateBoard(row,col);
				revealCells(row, col);
			} else if(curMove.isMined()) {
				gameOver = true;
				showBombs();
			}
			else {
				updateBoard(row, col);
				revealCells(row, col);
			}
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
//			revealCells(row, col - 1);
//			revealCells(row, col + 1);
//			revealCells(row - 1, col);
//			revealCells(row - 1, col - 1);
//			revealCells(row - 1, col + 1);
//			revealCells(row + 1, col);
//			revealCells(row + 1, col - 1);
//			revealCells(row + 1, col + 1);

			revealCells(row + 1, col);
			revealCells(row - 1, col);
			revealCells(row, col + 1);
			revealCells(row, col - 1);
		}
	}
	
	//should loop through bomb array and call setHidden() on all cells in the bomb array
	private void showBombs() {
		ArrayList<MinesweeperCell> bombsArray = model.getBombs();
		
		for(int i=0; i<bombsArray.size(); i++) {
			bombsArray.get(i).setHidden();
		}
	}

	//Should scan in the NW,N,NE,E,SE,S,SW,W to find a cell that has a number greater than 0
	//and update until bomb found
	private void updateBoard(int row, int col) {
		if(row-1 >=0 && col-1 >=0) {
			//moveNW(row-1,col-1);
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
	
	//row-- col-- cellsHidden--
	private void moveNW(int row, int col) {
		while(!refToBoard[row][col].isMined()) {
			refToBoard[row][col].setHidden();
			cellsHidden--;
			row--;
			col--;
			if(row < 0 || col < 0) {
				break;
			}
		}
	}
	
	// row--
	private void moveN(int row, int col) {
		//TODO
	}
	
	// row-- col++
	private void moveNE(int row, int col) {
		//TODO
	}
	
	// col++
	private void moveE(int row, int col) {
		//TODO
	}
	
	// row++ col++
	private void moveSE(int row, int col) {
		//TODO
	}
	
	// row++
	private void moveS(int row, int col) {
		//TODO
	}
	
	// row ++ col--
	private void moveSW(int row, int col) {
		//TODO
	}
	
	// col--
	private void moveW(int row, int col) {
		//TODO
	}
	
	public MinesweeperCell getCellClue(int row, int col) {
		return refToBoard[row][col];
		
	}
	
	public boolean isGameOver() {
		int mineCount = model.countOfMines();
		return cellsHidden == mineCount;
		
	}
}
