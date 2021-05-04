import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.junit.jupiter.api.Test;

import controller.MinesweeperController;
import controller.ReversiController;
import model.MinesweeperBoard;
import model.MinesweeperModel;
import model.ReversiBoard;
import model.ReversiModel;

class JUnitMinesweeper {

	@Test
	void newModelController(){
		MinesweeperModel zeroArg = new MinesweeperModel();
		MinesweeperModel threeArg = new MinesweeperModel(10,10,25);
		MinesweeperController controller = new MinesweeperController(zeroArg);
		controller.playMove(0, 0);
	}
	
	@Test
	void savedBoardModel() {
		MinesweeperModel model = new MinesweeperModel();
		MinesweeperController controller = new MinesweeperController(model);
		try {
			FileInputStream fis = new FileInputStream("save_game.dat");
			ObjectInputStream ois = new ObjectInputStream(fis);
			MinesweeperBoard load = (MinesweeperBoard) ois.readObject();
			model = new MinesweeperModel(load);
			model.addObserver(this);
			controller = new MinesweeperController(model);
			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			model = new MinesweeperModel();
			model.addObserver(this);
			controller = new MinesweeperController(model);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
