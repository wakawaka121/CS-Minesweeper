package model;

import java.util.ArrayList;
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

	private int[][] gameGrid;
	private int[][] bombPlaced;
	private int[][] bombMarked;
	private int gridR;
	private int gridC;
	private int fClickRow;
	private int fClickCol;
	private int mark;
	public int[][] bombs, board; //remove after testing

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
		board = new int[rows][cols];//remove
		this.mines = mines;
		cellsHidden = rows * cols;
		bombsArray = new ArrayList<MinesweeperCell>();
		mineSweepBoard = new MinesweeperCell[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				mineSweepBoard[i][j] = new MinesweeperCell(i, j);
			}
		}
	}

	public void setBombs(int row, int col) {
		int[][] bombConfig;
		
		do {
			bombConfig = newBombConfig(row, col);
			break;//ensure 1 move
		} while(!solveBoard(bombConfig));
		
		for(int i=0; i<rows; i++) {
			for(int j=0; j<cols; j++) {
				if(bombConfig[i][j] == -1) {
					mineSweepBoard[i][j].setMine(true);
					bombsArray.add(mineSweepBoard[i][j]);
				}
				else {
					mineSweepBoard[i][j].setAdjacentMines(bombConfig[i][j]);
				}
			}
		}
		
		bombs = bombConfig;
		
		for(int i=0; i<rows; i++) {
			for(int j=0; j<cols; j++) {
				if(mineSweepBoard[i][j].isHidden()) {
					board[i][j] = -1;
				}
				else {
					board[i][j] = mineSweepBoard[i][j].getMines();
				}
			}
		}
	}
	
	private int[][] newBombConfig(int row, int col) {
		Random randRow = new Random();
		Random randCol = new Random();
		int[][] bombs = new int[rows][cols];
		
		for(int i=0; i<rows; i++) {
			for(int j=0; j<cols; j++) {
				bombs[i][j] = 0;
			}
		}
		
		for (int mine = 0; mine < mines; mine++) {
			int mineRow = randRow.nextInt(rows);
			int mineCol = randCol.nextInt(cols);
			while (bombs[mineRow][mineCol] == -1 || !validBombLocation(mineRow, mineCol, row, col)) {
				mineRow = randRow.nextInt(rows);
				mineCol = randCol.nextInt(cols);
			}
			bombs[mineRow][mineCol] = -1;
			updateAdjacentCells(mineRow, mineCol, bombs);
			
		}
		
		return bombs;
	}
	
	private boolean validBombLocation(int mineRow, int mineCol, int row, int col) {
		return (Math.abs(mineRow - row) > 1 && Math.abs(mineCol - col) > 1);
	}
	
	public boolean testSolveBoard() {
		for(int i=0; i<rows; i++) {
			for(int j=0; j<cols; j++) {
				if(mineSweepBoard[i][j].isHidden()) {
					board[i][j] = -1;
				} else if(mineSweepBoard[i][j].isFlagged()) {
					board[i][j] = -2;
				} else {
					board[i][j] = mineSweepBoard[i][j].getMines();
				}
			}
		}
		System.out.println("Entered solve board");
		int[] ref;
		
		for(int k=1; k<=8; k++) {
			for(int i=0; i<rows; i++) {
				for(int j=0; j<cols; j++) {
					if(board[i][j] == k) {
						ref = countValidNeighbors(board, i, j);

						System.out.print("(" + Integer.toString(i) +"," + Integer.toString(j) + ") =>");
						System.out.println("(" + Integer.toString(ref[0]) +"," + Integer.toString(ref[1]) + ")");
						if(ref[1] == k) {
							flagNeighbors(board, i, j);
						}
						
						if(ref[0] == k && ref[1] > k) {
							playMove(board, i, j, bombs);
							return false; // ensure one move
						}
					}
				}
			}
		}
		
		return true;
	}
	
	private boolean solveBoard(int[][] bombs) {
		int[][] board = new int[rows][cols];
		int[] ref;
		
		for(int i=0; i<rows; i++) {
			for(int j=0; j<cols; j++) {
				board[i][j] = -1;
			}
		}
		
		for(int k=1; k<=8; k++) {
			for(int i=0; i<rows; i++) {
				for(int j=0; j<cols; j++) {
					if(board[i][j] == k) {
						ref = countValidNeighbors(board, i, j);
						if(ref[1] == k) {
							flagNeighbors(board, i, j);
						} else if(ref[0] == k && ref[1] > k) {
							playMove(board, i, j, bombs);
						}
					}
				}
			}
		}
		
		return true;
	}
	
	private void playMove(int[][] board, int row, int col, int[][] bombs) {
		for(int i=row-1; i<=row+1; i++) {
			for(int j=col-1; j<=col+1; j++) {
				if(i == row && j == col) {
					continue;
				}
				
				if(validIndex(i, j)) {
					if(board[i][j] == -1) {
						board[i][j] = bombs[i][j];
						mineSweepBoard[i][j].setHidden();
						return;//ensure one move;
					}
				}
			}
		}
	}
	
	private void flagNeighbors(int[][] board, int row, int col) {
		for(int i=row-1; i<=row+1; i++) {
			for(int j=col-1; j<=col+1; j++) {
				if(i == row && j == col) {
					continue;
				}
				
				if(validIndex(i, j)) {
					if(!mineSweepBoard[i][j].isFlagged()) {
						board[i][j] = -2;
						mineSweepBoard[i][j].setFlagged();
						return;
					}
				}
			}
		}
	}
	
	private int[] countValidNeighbors(int[][] board, int row, int col) {
		int[] ret = new int[2];
		
		ret[0] = 0;
		ret[1] = 0;
		
		for(int i=row-1; i<=row+1; i++) {
			for(int j=col-1; j<=col+1; j++) {
				if(i == row && j == col) {
					continue;
				}
				
				if(validIndex(i, j)) {
					if(board[i][j] < 0) {
						ret[1]++;
					}
					
					if(board[i][j] == -2) {
						ret[0]++;
					}
				}
			}
		}
		
		return ret;
	}
	
	private boolean validIndex(int row, int col) {
		return (row >= 0 && col >=0 && row < rows && col < cols);
	}

	public ArrayList<MinesweeperCell> getBombs() {
		return bombsArray;
	}

	public MinesweeperCell[][] getBoard() {
		return mineSweepBoard;
	}

	public MinesweeperBoard getSerialized() {
		return new MinesweeperBoard(rows, cols, mines, cellsHidden, mineSweepBoard,
				bombsArray);
	}

	private void updateAdjacentCells(int row, int col, int[][] bombs) {
		for(int i=row-1; i<=row+1; i++) {
			for(int j=col-1; j<=col+1; j++) {
				if(i == row && j == col) {
					continue;
				}
				
				if(validIndex(i, j)) {
					bombs[i][j]++;
				}
			}
		}
	}

	private void updateAdjacentBombs(int row, int col) {
		for(int i=row-1; i<=row+1; i++) {
			for(int j=col-1; j<=col+1; j++) {
				if(i == row && j == col) {
					continue;
				}
				
				if(validIndex(i, j)) {
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

	public MinesweeperCell getCell(int row, int col) {
		return mineSweepBoard[row][col];
	}

}