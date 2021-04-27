package model;

public class MinesweeperCell {
	
	private boolean hidden;
	private boolean mined;
	private boolean flagged;
	private int adjacentMines;
	
	public MinesweeperCell() {
		hidden = true;
		mined = false;
		flagged = false;
		adjacentMines = -1;
	}
	
	public boolean isMined() {
		return mined;
	}
	
	public void setMine(boolean value) {
		mined = value;
	}
	
	public void increaseMines() {
		adjacentMines++;
	}
	
	public boolean isHidden() {
		return hidden;
	}
	
	public void setHidden(boolean value) {
		hidden = value;
	}
	
	public boolean isFlagged() {
		return flagged;
	}
	
	public void setFlagged(boolean value) {
		flagged = value;
	}

}
