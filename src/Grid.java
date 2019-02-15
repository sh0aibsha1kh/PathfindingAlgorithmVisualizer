import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

class Grid {

    private Node[][] nodes;
    private int numberOfRows, numberOfColumns, nodeHeight, nodeWidth;

    Grid(int numberOfRows, int numberOfColumns, int nodeWidth, int nodeHeight) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.nodeWidth = nodeWidth;
        this.nodeHeight = nodeHeight;
        this.nodes = new Node[numberOfRows][numberOfColumns];


        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfColumns; col++) {
                Node node = new Node(row, col, nodeWidth, nodeHeight);
                nodes[row][col] = node;

            }
        }
    }

    Node[][] getNodes() {
        return nodes;
    }

    void createStartingNode() {
        Random r = new Random();
        nodes[r.nextInt(nodes.length)][r.nextInt(nodes[0].length)].setFill(Color.rgb(155, 39, 175));
    }

    void createGoalNode() {
        Random r = new Random();
        Node potentialGoalNode = nodes[r.nextInt(nodes.length)][r.nextInt(nodes[0].length)];
        while (potentialGoalNode.getFill().equals(Color.rgb(155, 39, 175))) {
            potentialGoalNode = nodes[r.nextInt(nodes.length)][r.nextInt(nodes[0].length)];
        }
        potentialGoalNode.setFill(Color.rgb(33, 150, 243));
    }

    void createObstacleNodes() {
        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfColumns; col++) {
                if (Math.random() <= 0.25
                        && !(nodes[row][col].equals(getStartingNode())
                        || nodes[row][col].equals(getGoalNode())
                        || nodes[row][col].getFill().equals(Color.BLACK))) {

                    nodes[row][col].setFill(Color.BLACK);

                }
            }
        }
    }

    Node getStartingNode() {
        for (Node[] node : nodes) {
            for (Node n : node) {
                if (n.getFill().equals(Color.rgb(155, 39, 175))) {
                    return n;
                }
            }
        }
        return null;
    }

    Node getGoalNode() {
        for (Node[] node : nodes) {
            for (Node n : node) {
                if (n.getFill().equals(Color.rgb(33, 150, 243))) {
                    return n;
                }
            }
        }
        return null;
    }

    ArrayList<Node> getNeighbours(Node node) {
        ArrayList<Node> neighbours = new ArrayList<>();
        int x = node.getXCoordinate();
        int y = node.getYCoordinate();
        if (x - 1 >= 0 && !nodes[x - 1][y].getFill().equals(Color.BLACK)) {
            neighbours.add(nodes[x - 1][y]);
        }
        if (x + 1 < nodes.length && !nodes[x + 1][y].getFill().equals(Color.BLACK)) {
            neighbours.add(nodes[x + 1][y]);
        }
        if (y - 1 >= 0 && !nodes[x][y - 1].getFill().equals(Color.BLACK)) {
            neighbours.add(nodes[x][y - 1]);
        }
        if (y + 1 < nodes[0].length && !nodes[x][y + 1].getFill().equals(Color.BLACK)) {
            neighbours.add(nodes[x][y + 1]);
        }
        return neighbours;
    }
}
