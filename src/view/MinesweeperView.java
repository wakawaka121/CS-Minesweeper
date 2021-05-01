package view;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

import controller.MinesweeperController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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
public class MinesweeperView extends Application implements Observer {

	private Text[][] texts;
	private Circle[][] circles;
	private StackPane[][] panes;
	private MinesweeperModel model;
	private MinesweeperController control;
	private Label timer;
	private Integer seconds = 0;
	private Integer minute = 0;
	private Integer hour = 0;
	private int highScoreSec =0;
	private int currSec = 0;
	private boolean markMode;


	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Minesweeper");
		model = new MinesweeperModel(10, 10, 10);
		control = new MinesweeperController(model);
		BorderPane window = new BorderPane();
		GridPane board = new GridPane();
		window.setCenter(board);
		board.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
		board.setPadding(new Insets(8));
		
		EventHandler<MouseEvent> eventHandlerMouseClick = new EventHandler<MouseEvent>() {

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
					stage.close();
				}
			}

		};
		board.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerMouseClick);
		MenuBar menuBar = new MenuBar();
		window.setTop(menuBar);
		createMenuItems(menuBar);
		addFlagToMenu(menuBar, board);
		addStackPanes(board, model.getRow(), model.getCol());
		
		timer = new Label();
		startTime(timer);
		window.setBottom(timer);
		
		Scene scene = new Scene(window);
		EventHandler<WindowEvent> eventHandlerWindowClose = new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				// TODO
			}

		};
		stage.setOnCloseRequest(eventHandlerWindowClose);
		stage.setScene(scene);
		stage.show();
	}
	

	private void createMenuItems(MenuBar menuBar) {
		Menu menu = new Menu("File");
		menuBar.getMenus().add(menu);
		MenuItem menuItem = new MenuItem("New 10x10 Game");

		menu.getItems().add(menuItem);
		
		EventHandler<ActionEvent> eventHandlerNewGame = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// new game
			}
		};
		menuItem.addEventHandler(ActionEvent.ANY, eventHandlerNewGame);
		
		
		
	}
	
	private void addFlagToMenu(MenuBar menuBar, GridPane board) {
		Menu menu = new Menu("Feature");
		menuBar.getMenus().add(menu);
		MenuItem mark = new MenuItem("Flag");
		menu.getItems().add(mark);
		
		
		EventHandler<ActionEvent> eventHandlerFlag = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				//flag a cell
				markMode = true;
				EventHandler<MouseEvent> flagACell = new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						
						if(markMode) {
							double x = event.getX() - 8;
							double y = event.getY() - 8;
							if(x < 0) {
								x = 0;
							}
							
							if(y < 0) {
								y = 0;
							}
							int row = (int) (y / 26);
							int col = (int) (x / 26);
							control.flagCell(row, col);
						}

						markMode = false;
					}
					
				};
				
				board.addEventHandler(MouseEvent.MOUSE_CLICKED, flagACell);
				
				
			}
		};
		
		
		
		
		
		
	}

	private void addStackPanes(GridPane board, int rows, int cols) {
		panes = new StackPane[rows][cols];
		texts = new Text[rows][cols];
		circles = new Circle[rows][cols];
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				StackPane pane = new StackPane();
				panes[i][j] = pane;
				MinesweeperCell cur = control.getCellClue(j, i);
				pane.setPadding(new Insets(2));
				pane.setBorder(
						new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
								CornerRadii.EMPTY, BorderWidths.DEFAULT)));
				Circle circle = new Circle(10);
				Text text = new Text();
				texts[i][j] = text;
				circles[i][j] = circle;
				text.setFont(new Font(15));
				text.setFill(Color.RED);
				if (cur.isHidden()) {
					pane.setBackground(new Background(
							new BackgroundFill(Color.DARKGREY, null, null)));
					circle.setFill(Color.TRANSPARENT);
				} else {
					pane.setBackground(
							new Background(new BackgroundFill(Color.GRAY, null, null)));
					if (cur.isFlagged()) {
						circle.setFill(Color.RED);
					} else if (cur.isMined()) {
						circle.setFill(Color.BLACK);
					} else {
						text.setText(String.valueOf(cur.getMines()));
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
	

	@Override
	public void update(Observable o, Object arg) {
		MinesweeperBoard board = (MinesweeperBoard) arg;
		for (int i = 0; i < board.getCols(); i++) {
			for (int j = 0; j < board.getRows(); j++) {
				MinesweeperCell cur = board.getCell(j, i);
				StackPane pane = panes[i][j];
				Circle circle = circles[i][j];
				Text text = texts[i][j];
				if (cur.isHidden()) {
					pane.setBackground(new Background(
							new BackgroundFill(Color.DARKGREY, null, null)));
					circle.setFill(Color.TRANSPARENT);
				} else {
					pane.setBackground(
							new Background(new BackgroundFill(Color.GRAY, null, null)));
					if (cur.isFlagged()) {
						circle.setFill(Color.RED);
					} else if (cur.isMined()) {
						circle.setFill(Color.BLACK);
					} else {
						text.setText(String.valueOf(cur.getMines()));
						circle.setFill(Color.TRANSPARENT);
					}
				}
			}
		}

	}

}