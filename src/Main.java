import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;


public class Main extends Application {

    private List<Node> visited = new ArrayList<>();

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

        breadthFirstSearch(grid);
        colourPath(visited, grid);

        Scene scene = new Scene(gridPane, 1000, 500);

        primaryStage.setTitle("Grid");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void breadthFirstSearch(Grid grid) {
        Set<Node> seen = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();
        Node startingNode = grid.getStartingNode();
        Node goalNode = grid.getGoalNode();
        seen.add(startingNode);
        queue.add(startingNode);
        visited.add(startingNode);

        while (queue.size() != 0) {

            Node currentNode = queue.remove();

            if (currentNode.equals(goalNode)) {
                return;
            }
            for (Node n : grid.getNeighbours(currentNode, false)) {
                if (!seen.contains(n)) {
                    queue.add(n);
                    seen.add(n);
                    visited.add(n);
                }
            }
        }

    }

    private void colourPath(List<Node> visited, Grid grid) {
        Timeline t = new Timeline();
        for (Node n : visited) {
            KeyFrame kf = new KeyFrame(Duration.millis(25 * (visited.indexOf(n) + 1)), event -> {
                if (n.equals(grid.getGoalNode())) {
                    n.setFill(Color.GREEN);
                    t.stop();
                    return;
                }
                if (!n.equals(grid.getStartingNode())) {
                    n.setFill(Color.ORANGE);
                }
            });

            t.getKeyFrames().add(kf);
        }
        t.play();

    }

    private void depthFirstSearch(Grid grid) {
        Set<Node> seen = new HashSet<>();
        Stack<Node> stack = new Stack<>();
        Node startingNode = grid.getStartingNode();
        Node goalNode = grid.getGoalNode();
        seen.add(startingNode);
        stack.push(startingNode);
        visited.add(startingNode);

        while (!stack.empty()) {

            Node currentNode = stack.pop();
            seen.add(currentNode);
            visited.add(currentNode);
            if (currentNode.equals(goalNode)) {
                return;
            }
            for (Node n : grid.getNeighbours(currentNode, true)) {
                if (!seen.contains(n)) {
                    stack.push(n);
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

