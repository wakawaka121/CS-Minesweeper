package model;

import java.io.Serializable;

public class MinesweeperCell implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private boolean hidden;
	private boolean mined;
	private boolean flagged;
	private int adjacentMines;
	private int rowCord;
	private int colCord;
	
	public MinesweeperCell(int row, int col) {
		hidden = true;
		mined = false;
		flagged = false;
		adjacentMines = 0;
		rowCord = row;
		colCord = col;
	}
	
	public boolean isMined() {
		return mined;
	}
	
	public void setMine(boolean value) {
		mined = value;
	}
	
	public int getMines() {
		return adjacentMines;
	}
	
	public void increaseMines() {
		adjacentMines++;
	}
	
	public boolean isHidden() {
		return hidden;
	}
	
	public void setHidden() {
		hidden = false;
	}
	
	public boolean isFlagged() {
		return flagged;
	}
	
	public void setFlagged() {
		if(flagged == true) {
			flagged = false;
		} else if(flagged == false) {
			flagged = true;
		}
	}
	
	public int getRow() {
		return rowCord;
	}
	
	public int getCol() {
		return colCord;
	}

}