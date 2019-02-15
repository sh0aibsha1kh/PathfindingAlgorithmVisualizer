import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane gridPane = new GridPane();

        Grid grid = new Grid(25, 50, 20, 20);

        attachGrid(gridPane, grid);

        grid.createStartingNode();
        grid.createGoalNode();
        grid.createObstacleNodes();

        breadth_first_search(grid);

        Scene scene = new Scene(gridPane, 1000, 500);

        primaryStage.setTitle("Grid");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void breadth_first_search(Grid grid) {
        Set<Node> seen = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();
        Node startingNode = grid.getStartingNode();
        Node goalNode = grid.getGoalNode();
        seen.add(startingNode);
        queue.add(startingNode);
        while (queue.size() != 0) {
                Node currentNode = queue.poll();
                currentNode.setFill(Color.YELLOW);
                if (currentNode.equals(goalNode)) {
                    currentNode.setFill(Color.GREEN);
                    startingNode.setFill(Color.PURPLE);
                    return;
                }

                for (Node n : grid.getNeighbours(currentNode)) {
                    if (!seen.contains(n)) {
                        seen.add(n);
                        queue.add(n);
                    }
                }
        }
    }

    private void attachGrid(GridPane gridPane, Grid grid) {
        Node[][] nodes = grid.getNodes();
        for (int row = 0; row < nodes.length; row++) {
            for (int col = 0; col < nodes[0].length; col++) {
                Node node = nodes[row][col];
                GridPane.setConstraints(node, col, row);
                gridPane.getChildren().addAll(node);
            }
        }
    }
}

