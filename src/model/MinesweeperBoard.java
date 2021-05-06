package model;

import java.io.Serializable;
import java.util.ArrayList;

public class MinesweeperBoard implements Serializable {

	private static final long serialVersionUID = 1L;

	private MinesweeperCell[][] mineSweepBoard;
	private ArrayList<MinesweeperCell> bombsArray;
	private int rows;
	private int cols;
	private int mines;
	private int cellsHidden;
	private int time;
	private ArrayList<Integer> highScore;

	public MinesweeperBoard(int rows, int cols, int mines, int cellsHidden,
			MinesweeperCell[][] mineSweepBoard, ArrayList<MinesweeperCell> bombsArray, ArrayList<Integer> highScore, int time) {
		this.rows = rows;
		this.cols = cols;
		this.mines = mines;
		this.cellsHidden = cellsHidden;
		this.mineSweepBoard = mineSweepBoard;
		this.bombsArray = bombsArray;
		this.highScore = highScore;
		this.time = time;
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getCols() {
		return cols;
	}
	
	public int getMines() {
		return mines;
	}
	
	public int getCellsHidden() {
		return cellsHidden;
	}
	
	public MinesweeperCell[][] getMineSweepBoard(){
		return mineSweepBoard;
	}
	
	public ArrayList<MinesweeperCell> getBombsArray(){
		return bombsArray;
	}
	
	public MinesweeperCell getCell(int row, int col) {
		return mineSweepBoard[row][col];
	}
	
	public ArrayList<Integer> getHighScore(){
		return highScore;
	}
	
	public int getTime() {
		return time;
	}

}