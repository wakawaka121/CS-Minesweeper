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
		adjacentMines = 0;
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
	
	public void setHidden(boolean value) {
		hidden = value;
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

}
