import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;


public class Main extends Application {

    private Rectangle[][] cells = new Rectangle[30][45];

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane gridPane = new GridPane();

        createGrid(gridPane, 20, 20, 30, 45);

        Scene scene = new Scene(gridPane, 900, 600);

        primaryStage.setTitle("Grid");
        primaryStage.setScene(scene);
        primaryStage.show();

        for (int i = 0; i < cells.length; i += 1) {
            for (int j = 0; j < cells[0].length; j += 1) {
                if (i % 2 == 0) {
                    if (j % 2 == 1) {
                        cells[i][j].setFill(Color.BLACK);
                    }
                } else {
                    if (j % 2 == 0) {
                        cells[i][j].setFill(Color.BLACK);
                    }
                }
            }

        }

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
        rectangle.setStrokeWidth(0.25);
        return rectangle;
    }

}

