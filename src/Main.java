import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        GridPane gridPane = new GridPane();

        Grid grid = new Grid(25, 50, 20, 20, false);

        attachGrid(gridPane, grid);
        grid.createObstacleNodes();
        List<Node> pathToNode = grid.aStarSearch();
        grid.colourFinalPath(pathToNode);

        Scene scene = new Scene(gridPane, 1000, 500);

        primaryStage.setTitle("Pathfinding Algorithm Visualizer");
        primaryStage.setScene(scene);
        primaryStage.show();
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

