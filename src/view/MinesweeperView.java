package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import controller.MinesweeperController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import model.MinesweeperBoard;
import model.MinesweeperCell;
import model.MinesweeperModel;

@SuppressWarnings("deprecation")
public class MinesweeperView extends Application {

	private Text[][] texts;
	private Circle[][] circles;
	private StackPane[][] panes;

	private GridPane board;
	private BorderPane window;

	private MinesweeperModel model;
	private MinesweeperController control;

	private Label timer;
	private Integer seconds = 0;
	private Integer minute = 0;
	private Integer hour = 0;


	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Minesweeper");

		loadFile();

		BorderPane window = new BorderPane();
		board = new GridPane();
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
				addStackPanes(board, model.getRow(), model.getCol());
				
				if(control.isGameOver()) {
					String message = "You lost!";
					
					if(control.gameWon()) {
						message = "You won!";
					}
					
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setContentText(message);
					alert.showAndWait();
					deleteSaveData();
				}

			}

		};
		board.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerMouseClick);
		MenuBar menuBar = new MenuBar();
		window.setTop(menuBar);
		createMenuItems(menuBar);
		addStackPanes(board, model.getRow(), model.getCol());

		
		timer = new Label();
		startTime(timer);
		window.setBottom(timer);
		
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
			control = new MinesweeperController(model);
			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			model = new MinesweeperModel(15, 10, 10);
			control = new MinesweeperController(model);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void createMenuItems(MenuBar menuBar) {
		Menu menu = new Menu("File");
		menuBar.getMenus().add(menu);
		MenuItem menuItem = new MenuItem("New 10x15 Game");
		menu.getItems().add(menuItem);
		EventHandler<ActionEvent> eventHandlerNewGame = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				resetGame(15, 10, 10);
			}
		};
		menuItem.addEventHandler(ActionEvent.ANY, eventHandlerNewGame);
	}
	
	private void resetGame(int rows, int cols, int mines) {
		model = new MinesweeperModel(rows, cols, mines);
		control = new MinesweeperController(model);
		addStackPanes(board, rows, cols);
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
	
	private void startTime(Label timer) {
		timer.setTextFill(Color.BLACK);
		timer.setFont(Font.font(20));
		HBox layout = new HBox(5);
		layout.getChildren().add(timer);
		start();
		
	}
	
	private void start() {
		Timeline t = new Timeline();
		t.setCycleCount(Timeline.INDEFINITE);
		KeyFrame frame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				seconds++;
				if(seconds > 59) {
					seconds = 0;
					minute++;
				}
				if(minute > 59) {
					minute = 0;
					hour++;
				}
				timer.setText(hour.toString() + ":"+ minute.toString() + ":" + seconds.toString());
				
				//if(new game is pressed) {
				//	
				//	t.stop();
				//}
			}
		});
		t.getKeyFrames().add(frame);
		t.playFromStart();
	}

}

