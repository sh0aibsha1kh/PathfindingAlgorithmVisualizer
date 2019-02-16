import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();

        Grid grid = new Grid(25, 50, 20, 20);


        attachGrid(gridPane, grid);

//        grid.createStartingNode();
//        grid.createGoalNode();
//        grid.createObstacleNodes();

//        grid.breadthFirstSearch();
//        grid.depthFirstSearch();
        grid.aStarSearch(false);
        grid.colourPath();

        Scene scene = new Scene(gridPane, 1000, 500);

        primaryStage.setTitle("Grid");
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

