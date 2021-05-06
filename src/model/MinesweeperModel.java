package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

@SuppressWarnings("deprecation")
public class MinesweeperModel {
	private static final int DEFAULT_SIZE = 10;
	private MinesweeperCell[][] mineSweepBoard;
	private ArrayList<MinesweeperCell> bombsArray;
	private int rows;
	private int cols;
	private int mines;
	private int cellsHidden;
	private ArrayList<Integer> highScore;
	private int time;

	public MinesweeperModel() {
		buildBoard(DEFAULT_SIZE, DEFAULT_SIZE, DEFAULT_SIZE); // Default board if no parameters are mentioned. (10 by 10
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
		bombsArray = new ArrayList<MinesweeperCell>();
		mineSweepBoard = new MinesweeperCell[rows][cols];
		time = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				mineSweepBoard[i][j] = new MinesweeperCell(i, j);
			}
		}
	}
	
	public void setBombs(int row, int col) {
		ArrayList<int[][]> bombConfig;

		do {
			bombConfig = newBombConfig(row, col);
		} while (!solveBoard(bombConfig.get(0), row, col));

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (bombConfig.get(1)[i][j] == 1) {
					mineSweepBoard[i][j].setMine(true);
					bombsArray.add(mineSweepBoard[i][j]);
				} else {
					mineSweepBoard[i][j].setAdjacentMines(bombConfig.get(0)[i][j]);
				}
			}
		}
	}

	private ArrayList<int[][]> newBombConfig(int row, int col) {
		Random randRow = new Random();
		Random randCol = new Random();
		int[][] bombs = new int[rows][cols];
		int[][] currentLocations = new int[rows][cols];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				bombs[i][j] = 0;
				currentLocations[i][j] = 0;
			}
		}

		for (int mine = 0; mine < mines; mine++) {
			int mineRow = randRow.nextInt(rows);
			int mineCol = randCol.nextInt(cols);
			while (currentLocations[mineRow][mineCol] == 1
					|| !validBombLocation(mineRow, mineCol, row, col)) {
				mineRow = randRow.nextInt(rows);
				mineCol = randCol.nextInt(cols);
			}
			currentLocations[mineRow][mineCol] = 1;
			updateAdjacentCells(mineRow, mineCol, bombs);
		}
		ArrayList<int[][]> bombData = new ArrayList<>();
		bombData.add(bombs);
		bombData.add(currentLocations);
		return bombData;
	}

	private boolean validBombLocation(int mineRow, int mineCol, int row, int col) {
		return (Math.abs(mineRow - row) > 1 && Math.abs(mineCol - col) > 1);
	}

	private boolean solveBoard(int[][] bombs, int row, int col) {
		MinesweeperCell[][] board = new MinesweeperCell[rows][cols];
		int[] ref;

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				board[i][j] = new MinesweeperCell(i, j);
				board[i][j].setAdjacentMines(bombs[i][j]);
				if (i == row && j == col) {
					board[i][j].setHidden();
				}
			}
		}
		boolean modifiedCell = false;
		int totalMods = 0;
		while (true) {
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					if (!board[i][j].isHidden() && !board[i][j].isFlagged()) {
						ref = countValidNeighbors(board, i, j);
						if (ref[1] == board[i][j].getMines() && ref[0] != board[i][j].getMines() && board[i][j].getMines() > 0) {
							totalMods = totalMods + flagNeighbors(board, i, j);
							modifiedCell = true;
						} else if (ref[0] == board[i][j].getMines() && ref[1] > board[i][j].getMines()) {
							totalMods = totalMods + playMove(board, i, j, bombs);
							modifiedCell = true;
						}
					}
				}
			}
			if (modifiedCell == false) {
				break;
			}
			modifiedCell = false;
		}
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (board[i][j].isHidden() && !board[i][j].isFlagged()) {
					return false;
				}
			}
		}
		return true;
	}

	private int playMove(MinesweeperCell[][] board, int row, int col, int[][] bombs) {
		int modified = 0;
		for (int i = row - 1; i <= row + 1; i++) {
			for (int j = col - 1; j <= col + 1; j++) {
				if (i == row && j == col) {
					continue;
				}

				if (validIndex(i, j)) {
					if (board[i][j].isHidden() && !board[i][j].isFlagged()) {
						modified++;
						board[i][j].setHidden();
					}
				}
			}
		}
		return modified;
	}

	private int flagNeighbors(MinesweeperCell[][] board, int row, int col) {
		int modified = 0;
		for (int i = row - 1; i <= row + 1; i++) {
			for (int j = col - 1; j <= col + 1; j++) {
				if (i == row && j == col) {
					continue;
				}

				if (validIndex(i, j)) {
					if (board[i][j].isHidden() && !board[i][j].isFlagged()) {
						modified++;
						board[i][j].setFlagged();
					}
				}
			}
		}
		return modified;
	}

	private int[] countValidNeighbors(MinesweeperCell[][] board, int row, int col) {
		int[] ret = new int[2];

		ret[0] = 0; // Number of surrounding flagged cells
		ret[1] = 0;	// Number of surrounding hidden cells

		for (int i = row - 1; i <= row + 1; i++) {
			for (int j = col - 1; j <= col + 1; j++) {
				if (i == row && j == col) {
					continue;
				}

				if (validIndex(i, j)) {
					if (board[i][j].isHidden()) {
						ret[1]++;
					}

					if (board[i][j].isFlagged()) {
						ret[0]++;
					}
				}
			}
		}

		return ret;
	}

	private boolean validIndex(int row, int col) {
		return (row >= 0 && col >= 0 && row < rows && col < cols);
	}

	public ArrayList<MinesweeperCell> getBombs() {
		return bombsArray;
	}

	public MinesweeperCell[][] getBoard() {
		return mineSweepBoard;
	}

	public MinesweeperBoard getSerialized() {
		return new MinesweeperBoard(rows, cols, mines, cellsHidden, mineSweepBoard,
				bombsArray, highScore, time);
	}

	private void updateAdjacentCells(int row, int col, int[][] bombs) {
		for (int i = row - 1; i <= row + 1; i++) {
			for (int j = col - 1; j <= col + 1; j++) {
				if (i == row && j == col) {
					continue;
				}

				if (validIndex(i, j)) {
					bombs[i][j]++;
				}
			}
		}
	}

	private void updateAdjacentBombs(int row, int col) {
		for (int i = row - 1; i <= row + 1; i++) {
			for (int j = col - 1; j <= col + 1; j++) {
				if (i == row && j == col) {
					continue;
				}

				if (validIndex(i, j)) {
					mineSweepBoard[i][j].increaseMines();
				}
			}
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
		time = loadedGame.getTime();
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
		highScore.remove(Collections.max(highScore));
		highScore.add(time);
	} else {
		highScore.add(time);
		}
	}

	public MinesweeperCell getCell(int row, int col) {
		return mineSweepBoard[row][col];
	}
	
	public void setTime(int time) {
		this.time = time;
	}
	
	public int getTime() {
		return time;
	}
}