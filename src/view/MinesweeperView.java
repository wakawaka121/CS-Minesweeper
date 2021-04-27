package view;

import java.util.Observable;
import java.util.Observer;

import controller.MinesweeperController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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
import model.MinesweeperModel;

@SuppressWarnings("deprecation")
public class MinesweeperView extends Application implements Observer {

	private MinesweeperModel model;
	private MinesweeperController control;
	private static final int DEFAULT_SIZE = 20;

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Minesweeper");
		model = new MinesweeperModel();
		control = new MinesweeperController(model);
		BorderPane window = new BorderPane();
		GridPane board = new GridPane();
		window.setCenter(board);
		board.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
		board.setPadding(new Insets(8));
		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu("File");
		menuBar.getMenus().add(menu);
		MenuItem menuItem = new MenuItem("New 20x20 Game");
		menu.getItems().add(menuItem);
		window.setTop(menuBar);
		addStackPanes(board, DEFAULT_SIZE);
		Scene scene = new Scene(window);
		stage.setScene(scene);
		stage.show();
	}

	private void addStackPanes(GridPane board, int size) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				StackPane pane = new StackPane();
				pane.setPadding(new Insets(2));
				pane.setBorder(
						new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
								CornerRadii.EMPTY, BorderWidths.DEFAULT)));
				pane.setBackground(
						new Background(new BackgroundFill(Color.DARKGRAY, null, null)));
				Circle circle = new Circle(10);
				Text text = new Text();
				text.setFont(new Font(15));
				text.setFill(Color.RED);
				pane.getChildren().add(circle);
				pane.getChildren().add(text);
				circle.setFill(Color.TRANSPARENT);
				board.add(pane, i, j);
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
