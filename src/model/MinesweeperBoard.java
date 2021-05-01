package model;

import java.io.Serializable;
import java.util.ArrayList;

public class MinesweeperBoard implements Serializable {

	private static final long serialVersionUID = 1L;

	private MinesweeperCell[][] mineSweepBoard;
	private ArrayList<MinesweeperCell> bombsArray;
	// private MinesweeperCell[] bombsArray;
	private int rows;
	private int cols;
	private int mines;

	public MinesweeperBoard(int rows, int cols, int mines,
			MinesweeperCell[][] mineSweepBoard, ArrayList<MinesweeperCell> bombsArray) {
		this.rows = rows;
		this.cols = cols;
		this.mines = mines;
		this.mineSweepBoard = mineSweepBoard;
		this.bombsArray = bombsArray;
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
	
	public MinesweeperCell[][] getMineSweepBoard(){
		return mineSweepBoard;
	}
	
	public ArrayList<MinesweeperCell> getBombsArray(){
		return bombsArray;
	}
	
	public MinesweeperCell getCell(int row, int col) {
		return mineSweepBoard[row][col];
	}

}