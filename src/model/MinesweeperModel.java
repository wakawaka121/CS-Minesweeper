package model;

import java.util.Random;

public class MinesweeperModel {
	private int[][] gameGrid;
	private int[][] bombPlaced;
	private int[][] bombMarked;
	private int mines;
	private int gridR;
	private int gridC;
	private int fClickRow;
	private int fClickCol;
	private int mark;

	
	public MinesweeperModel(int n, int m, int k) {
		gameGrid = new int[n][m];
		bombPlaced = new int[n][m];
		bombMarked = new int[n][m];
		mines = k;
		this.gridR = n;
		this.gridC = m;
		
		
		for(int i =0 ; i< n; i++) {
			for(int j= 0; j< m; j++) {
				gameGrid[i][j] = 0;
				bombPlaced[i][j] =0;
				bombMarked[i][j] = 0; 
			}
		}
		
	}
	
	public void placeBombAt(int row, int col) {
		bombPlaced[row][col] = 1;
		
	}
	
	public boolean bombCheck(int row, int col) {
		if(bombPlaced[row][col] == 1) {
			return true;
		}
		return false;
	}
	
	public int countOfMines() {
		return mines;
	}
	
	public void placeBombRandom(int row, int col, int bombs) {
		Random rand = new Random();
		int i = 0;
		while(i < bombs) {
			int randomRow = rand.nextInt(gridR);
			int randomCol = rand.nextInt(gridC);
			if(randomRow == fClickRow && randomCol == fClickCol) {
				continue;
			}
			if(bombCheck(randomRow, randomCol)) {
				continue;
			}
			placeBombAt(randomRow, randomCol);
			i++;
		}
		
		
	}
	
	public void userMark(int row, int col) {
		bombMarked[row][col] = 1;
		mark++;

	}
	
	public void userUnmark(int row, int col) {
		bombMarked[row][col] = 0;
		mark--;
	}
	
	public boolean checkGameComplete() {
		if(mines == mark) {
			for(int i =0 ; i< gridR; i++) {
				for(int j= 0; j< gridC; j++) {
					if(bombPlaced[i][j] == 1) {
						if(bombMarked[i][j] != 1) {
							return false;
						}
					}
				}
			}
		}
		else {
			return false;
		}
		return true;
	}
	
	 
	
	

}
