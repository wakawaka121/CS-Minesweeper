package model;

import java.util.ArrayList;
import java.util.Random;

public class MinesweeperModel {
	//Needs to be added
	private MinesweeperCell[][] mineSweepBoard;
	private ArrayList<MinesweeperCell> bombsArray;
	//private MinesweeperCell[] bombsArray;
	private int rows;
	private int cols;
	private int mines;
	
	
	private int[][] gameGrid;
	private int[][] bombPlaced;
	private int[][] bombMarked;
	private int gridR;
	private int gridC;
	private int fClickRow;
	private int fClickCol;
	private int mark;

	
	public MinesweeperModel(int rows, int cols, int mines) {
		this.rows = rows;
		this.cols = cols;
		this.mines = mines;
		bombsArray = new ArrayList<MinesweeperCell>();
		mineSweepBoard = new MinesweeperCell[rows][cols];
		for(int i = 0; i< rows; i++) {
			for(int j = 0; j < cols; j++) {
				mineSweepBoard[rows][cols] = new MinesweeperCell(i,j);
			}
		}
		
//		Random randRow = new Random();
//		Random randCol = new Random();
//		for(int mine = 0; mine < mines; mine++) {
//			int mineRow = randRow.nextInt(rows);
//			int mineCol = randCol.nextInt(cols);
//			while(!isMine(mineRow,mineCol)) {
//				mineRow = randRow.nextInt(rows);
//				mineCol = randCol.nextInt(cols);
//			}
//			mineSweepBoard[mineRow][mineCol].setMine(true);
//			bombsArray.add(mineSweepBoard[mineRow][mineCol]);
//			updateAdjacentBombs(mineRow,mineCol);
//			
//		}	
	}
	
	public void setBombs(MinesweeperCell firstMove) {
		Random randRow = new Random();
		Random randCol = new Random();
		for(int mine = 0; mine < mines; mine++) {
			int mineRow = randRow.nextInt(rows);
			int mineCol = randCol.nextInt(cols);
			while(!isMine(mineRow,mineCol) && !mineSweepBoard[mineRow][mineCol].equals(firstMove)) {
				mineRow = randRow.nextInt(rows);
				mineCol = randCol.nextInt(cols);
			}
			mineSweepBoard[mineRow][mineCol].setMine(true);
			bombsArray.add(mineSweepBoard[mineRow][mineCol]);
			updateAdjacentBombs(mineRow,mineCol);
			
		}
	}
	
	public ArrayList<MinesweeperCell> getBombs() {
		return bombsArray;
	}
	
	public MinesweeperCell[][] getBoard() {
		return mineSweepBoard;
	}
	
	private void updateAdjacentBombs(int row, int col){
		//NW update
		if(row-1 >= 0 && col-1 >= 0) {
			mineSweepBoard[row-1][col-1].increaseMines();
		}
		//N update
		if(row-1 >=0) {
			mineSweepBoard[row-1][col].increaseMines();
		}
		//NE update
		if(row+1 < rows && col-1 >=0) {
			mineSweepBoard[row+1][col-1].increaseMines();
		}
		//E update
		if(row+1 < rows) {
			mineSweepBoard[row+1][col].increaseMines();
		}
		//SE update
		if(row+1 < rows && col+1 < cols) {
			mineSweepBoard[row+1][col+1].increaseMines();
		}
		//S update
		if(row+1 < rows) {
			mineSweepBoard[row+1][col].increaseMines();
		}
		//SW update
		if(row+1 < rows && col-1 >= 0) {
			mineSweepBoard[row+1][col-1].increaseMines();
		}
		//W update 
		if(col-1 >= 0) {
			mineSweepBoard[row][col-1].increaseMines();
		}
		
	}
	private boolean isMine(int row, int col) {
		if(mineSweepBoard[row][col].isMined()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/*Saveed Game Constructor*/
	public MinesweeperModel(MinesweeperBoard loadedGame) {
		
	}
	
	public int getRow() {
		return rows;
	}
	
	public int getCol() {
		return cols;
	}
	
	public int countOfMines() {
		return mines;
	}
		
	public MinesweeperCell getCell(int row, int col) {
		return mineSweepBoard[row][col];
	}
	

}
