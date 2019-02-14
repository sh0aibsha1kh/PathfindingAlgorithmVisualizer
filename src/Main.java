import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

import java.util.Random;


public class Main extends Application {

    private Rectangle[][] cells = new Rectangle[25][50];

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane gridPane = new GridPane();

        createGrid(gridPane, 20, 20, 25, 50);

        setStartingCell(cells);
        setGoalCell(cells);

        Scene scene = new Scene(gridPane, 1000, 500);

        primaryStage.setTitle("Grid");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void setStartingCell(Rectangle[][] cells) {
        Random r = new Random();
        cells[r.nextInt(cells.length)][r.nextInt(cells[0].length)].setFill(Color.rgb(155, 39, 175));
    }

    private void setGoalCell(Rectangle[][] cells) {
        Random r = new Random();
        Rectangle potentialGoalCell = cells[r.nextInt(cells.length)][r.nextInt(cells[0].length)];
        while(potentialGoalCell.getFill() == Color.rgb(155, 39, 175)) {
            potentialGoalCell = cells[r.nextInt(cells.length)][r.nextInt(cells[0].length)];
        }
        potentialGoalCell.setFill(Color.rgb(33, 150, 243));
    }

    private void createGrid(GridPane gridPane, int cellWidth, int cellHeight, int numberOfRows, int numberOfColumns) {

        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfColumns; col++) {
                Rectangle rectangle = createCell(cellWidth, cellHeight);
                cells[row][col] = rectangle;
                GridPane.setConstraints(rectangle, col, row);
                gridPane.getChildren().addAll(rectangle);
            }
        }

    }

    private Rectangle createCell(int cellWidth, int cellHeight) {
        Rectangle rectangle = new Rectangle(cellWidth, cellHeight);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeType(StrokeType.INSIDE);
        rectangle.setStrokeWidth(0.1);
        return rectangle;
    }

}

