import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

import java.util.Random;


public class Main extends Application {

    private Node[][] nodes = new Node[25][50];

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane gridPane = new GridPane();

        createGrid(gridPane, 20, 20, 25, 50);

        setStartingCell(nodes);
        setGoalCell(nodes);

        Node startingCell = getStartingCell(nodes);
        Node goalCell = getGoalCell(nodes);

        Scene scene = new Scene(gridPane, 1000, 500);

        primaryStage.setTitle("Grid");
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    private Node getStartingCell(Node[][] nodes) {
        for (Node[] node : nodes) {
            for (Node n : node) {
                if (n.getFill().equals(Color.rgb(155, 39, 175))) {
                    return n;
                }
            }
        }
        return null;
    }

    private Node getGoalCell(Node[][] nodes) {
        for (Node[] node : nodes) {
            for (Node n : node) {
                if (n.getFill().equals(Color.rgb(33, 150, 243))) {
                    return n;
                }
            }
        }
        return null;
    }


    private void setStartingCell(Node[][] cells) {
        Random r = new Random();
        cells[r.nextInt(cells.length)][r.nextInt(cells[0].length)].setFill(Color.rgb(155, 39, 175));
    }

    private void setGoalCell(Node[][] cells) {
        Random r = new Random();
        Rectangle potentialGoalCell = cells[r.nextInt(cells.length)][r.nextInt(cells[0].length)];
        while (potentialGoalCell.getFill().equals(Color.rgb(155, 39, 175))) {
            potentialGoalCell = cells[r.nextInt(cells.length)][r.nextInt(cells[0].length)];
        }
        potentialGoalCell.setFill(Color.rgb(33, 150, 243));
    }

    private void createGrid(GridPane gridPane, int cellWidth, int cellHeight, int numberOfRows, int numberOfColumns) {

        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfColumns; col++) {
                Node node = new Node(row, col, cellWidth, cellHeight);
                nodes[row][col] = node;
                GridPane.setConstraints(node, col, row);
                gridPane.getChildren().addAll(node);
            }
        }

    }
}

