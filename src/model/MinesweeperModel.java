package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

@SuppressWarnings("deprecation")
public class MinesweeperModel {
	// Needs to be added
	private MinesweeperCell[][] mineSweepBoard;
	private ArrayList<MinesweeperCell> bombsArray;
	// private MinesweeperCell[] bombsArray;
	private int rows;
	private int cols;
	private int mines;
	private int cellsHidden;
	private ArrayList<Integer> highScore;
	//private int[] scoreArray;

	public MinesweeperModel() {
		buildBoard(10, 10, 10); // Default board if no parameters are mentioned. (10 by 10
								// with 10 mines)
	}

	public MinesweeperModel(int rows, int cols, int mines) {
		buildBoard(rows, cols, mines);
	}

	private void buildBoard(int rows, int cols, int mines) {
		this.rows = rows;
		this.cols = cols;
		this.mines = mines;
		cellsHidden = rows * cols;
		//highScore = new ArrayList<Integer>();
		bombsArray = new ArrayList<MinesweeperCell>();
		mineSweepBoard = new MinesweeperCell[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				mineSweepBoard[i][j] = new MinesweeperCell(i, j);
			}
		}
	}
	
	public void setBombs(int row, int col) {
		Random randRow = new Random();
		Random randCol = new Random();
		for (int mine = 0; mine < mines; mine++) {
			int mineRow = randRow.nextInt(rows);
			int mineCol = randCol.nextInt(cols);
			while (isMine(mineRow, mineCol) || (mineRow == row && mineCol == col)) {
				mineRow = randRow.nextInt(rows);
				mineCol = randCol.nextInt(cols);
			}
			mineSweepBoard[mineRow][mineCol].setMine(true);
			System.out.println("(" + Integer.toString(mineRow) + ","
					+ Integer.toString(mineCol) + ")");
			bombsArray.add(mineSweepBoard[mineRow][mineCol]);
			updateAdjacentBombs(mineRow, mineCol);
		}
	}

	public ArrayList<MinesweeperCell> getBombs() {
		return bombsArray;
	}

	public MinesweeperCell[][] getBoard() {
		return mineSweepBoard;
	}

	public MinesweeperBoard getSerialized() {
		return new MinesweeperBoard(rows, cols, mines, cellsHidden, mineSweepBoard,
				bombsArray, highScore);
	}

	private void updateAdjacentBombs(int row, int col) {
		// NW update
		if (row - 1 >= 0 && col - 1 >= 0) {
			mineSweepBoard[row - 1][col - 1].increaseMines();
		}
		// N update
		if (row - 1 >= 0) {
			mineSweepBoard[row - 1][col].increaseMines();
		}
		// NE update
		if (row - 1 >= 0 && col + 1 < cols) {
			mineSweepBoard[row - 1][col + 1].increaseMines();
		}
		// E update
		if (col + 1 < cols) {
			mineSweepBoard[row][col + 1].increaseMines();
		}
		// SE update
		if (row + 1 < rows && col + 1 < cols) {
			mineSweepBoard[row + 1][col + 1].increaseMines();
		}
		// S update
		if (row + 1 < rows) {
			mineSweepBoard[row + 1][col].increaseMines();
		}
		// SW update
		if (row + 1 < rows && col - 1 >= 0) {
			mineSweepBoard[row + 1][col - 1].increaseMines();
		}
		// W update
		if (col - 1 >= 0) {
			mineSweepBoard[row][col - 1].increaseMines();
		}
	}

	private boolean isMine(int row, int col) {
		return mineSweepBoard[row][col].isMined();
	}

	/* Saved Game Constructor */
	public MinesweeperModel(MinesweeperBoard loadedGame) {
		mineSweepBoard = loadedGame.getMineSweepBoard();
		bombsArray = loadedGame.getBombsArray();
		rows = loadedGame.getRows();
		cols = loadedGame.getCols();
		mines = loadedGame.getMines();
		cellsHidden = loadedGame.getCellsHidden();
		highScore = loadedGame.getHighScore();
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

	public int getCellsHidden() {
		return cellsHidden;
	}
	
	public void decCellsHidden() {
		cellsHidden--;
	}
		
	public ArrayList<Integer> getHighScore() {
		return highScore;
	}
	
	public void setHighScore(ArrayList<Integer> scores) {
		highScore = scores;
	}
	
	public void updateScores(int time) {
	if(highScore.size() == 10) {
		highScore.remove(Collections.min(highScore));
		highScore.add(time);
	} else {
		highScore.add(time);
	}
}

	public MinesweeperCell getCell(int row, int col) {
		return mineSweepBoard[row][col];
	}
}