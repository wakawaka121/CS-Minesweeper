package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

import controller.MinesweeperController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.MinesweeperBoard;
import model.MinesweeperCell;
import model.MinesweeperModel;

@SuppressWarnings("deprecation")
public class MinesweeperView extends Application implements Observer {

	private Text[][] texts;
	private Circle[][] circles;
	private StackPane[][] panes;
	private MinesweeperModel model;
	private MinesweeperController control;

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Minesweeper");
		loadFile();
		BorderPane window = new BorderPane();
		GridPane board = new GridPane();
		window.setCenter(board);
		board.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
		board.setPadding(new Insets(8));

		EventHandler<MouseEvent> eventHandlerMouseClick = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				double x = arg0.getX() - 8;
				double y = arg0.getY() - 8;
				if(x < 0) {
					x = 0;
				}
				
				if(y < 0) {
					y = 0;
				}
				int row = (int) (y / 26);
				int col = (int) (x / 26);
				
				if(row >= 0 && col >= 0 &&  row < model.getRow() && col < model.getCol()) {
					if(arg0.getButton().toString().equals("PRIMARY")) {
						control.playMove(row, col);
					}
					else if(arg0.getButton().toString().equals("SECONDARY")) {
						control.flagCell(row, col);
					}
				}
				System.out.print(arg0.getButton());
				
				
				System.out.println("(" + Integer.toString(row) +"," + Integer.toString(col) + ")");
				control.modelUpdate();
				
				if(control.isGameOver()) {
					String message = "You lost!";
					
					if(control.gameWon()) {
						message = "You won!";
					}
					
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText(message);
					alert.showAndWait();
					deleteSaveData();
					stage.close();
				}
			}

		};
		board.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerMouseClick);
		MenuBar menuBar = new MenuBar();
		window.setTop(menuBar);
		createMenuItems(menuBar);
		addStackPanes(board, model.getRow(), model.getCol());
		Scene scene = new Scene(window);
		EventHandler<WindowEvent> eventHandlerWindowClose = new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				try {
					FileOutputStream fos = new FileOutputStream("save_game.dat");
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(model.getSerialized());
					fos.close();
					oos.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};
		stage.setOnCloseRequest(eventHandlerWindowClose);
		stage.setScene(scene);
		stage.show();
	}
	
	private void loadFile() throws ClassNotFoundException {
		try {
			FileInputStream fis = new FileInputStream("save_game.dat");
			ObjectInputStream ois = new ObjectInputStream(fis);
			MinesweeperBoard load = (MinesweeperBoard) ois.readObject();
			model = new MinesweeperModel(load);
			model.addObserver(this);
			control = new MinesweeperController(model);
			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			model = new MinesweeperModel();
			model.addObserver(this);
			control = new MinesweeperController(model);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createMenuItems(MenuBar menuBar) {
		Menu menu = new Menu("File");
		menuBar.getMenus().add(menu);
		MenuItem menuItem = new MenuItem("New 10x10 Game");
		menu.getItems().add(menuItem);
		EventHandler<ActionEvent> eventHandlerNewGame = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				resetGame(10, 10, 10);
			}
		};
		menuItem.addEventHandler(ActionEvent.ANY, eventHandlerNewGame);
	}
	
	private void resetGame(int rows, int cols, int mines) {
		model = new MinesweeperModel(rows, cols, mines);
		model.addObserver(this);
		control = new MinesweeperController(model);
		control.modelUpdate();
		deleteSaveData();
	}
	
	private void deleteSaveData() {
		File saveData = new File("save_game.dat");
		if (saveData.exists()) {
			saveData.delete();
		}
	}

	private void addStackPanes(GridPane board, int rows, int cols) {
		panes = new StackPane[rows][cols];
		texts = new Text[rows][cols];
		circles = new Circle[rows][cols];
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				StackPane pane = new StackPane();
				panes[j][i] = pane;
				MinesweeperCell cur = control.getCellClue(j, i);
				pane.setPadding(new Insets(2));
				pane.setBorder(
						new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
								CornerRadii.EMPTY, BorderWidths.DEFAULT)));
				Circle circle = new Circle(10);
				Text text = new Text();
				texts[j][i] = text;
				circles[j][i] = circle;
				text.setFont(new Font(15));
				text.setFill(Color.RED);
				if (cur.isHidden()) {
					pane.setBackground(new Background(
							new BackgroundFill(Color.DARKGREY, null, null)));
					if (cur.isFlagged()) {
						circle.setFill(Color.RED);
					}
					else {
						circle.setFill(Color.TRANSPARENT);
					}
				} else {
					pane.setBackground(
							new Background(new BackgroundFill(Color.GRAY, null, null)));
					if (cur.isMined()) {
						circle.setFill(Color.BLACK);
					} else {
						if(cur.getMines() != 0) {
							text.setText(String.valueOf(cur.getMines()));
						}
						circle.setFill(Color.TRANSPARENT);
					}
				}
				pane.getChildren().add(circle);
				pane.getChildren().add(text);
				board.add(pane, i, j);
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		MinesweeperBoard board = (MinesweeperBoard) arg;
		for (int i = 0; i < board.getCols(); i++) {
			for (int j = 0; j < board.getRows(); j++) {
				MinesweeperCell cur = board.getCell(i, j);
				StackPane pane = panes[i][j];
				Circle circle = circles[i][j];
				Text text = texts[i][j];
				if (cur.isHidden()) {
					pane.setBackground(new Background(
							new BackgroundFill(Color.DARKGREY, null, null)));
					if (cur.isFlagged()) {
						circle.setFill(Color.RED);
					}
					else {
						circle.setFill(Color.TRANSPARENT);
					}
					text.setText("");
				} else {
					pane.setBackground(
							new Background(new BackgroundFill(Color.GRAY, null, null)));
					if (cur.isMined()) {
						circle.setFill(Color.BLACK);
					} else {
						if(cur.getMines() != 0) {
							text.setText(String.valueOf(cur.getMines()));
						}
						else {
							text.setText("");
						}
						circle.setFill(Color.TRANSPARENT);
					}
				}
			}
		}

	}

}
