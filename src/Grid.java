import javafx.scene.paint.Color;

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

    void createStartingCell() {
        Random r = new Random();
        nodes[r.nextInt(nodes.length)][r.nextInt(nodes[0].length)].setFill(Color.rgb(155, 39, 175));
    }

    void createGoalCell() {
        Random r = new Random();
        Node potentialGoalNode = nodes[r.nextInt(nodes.length)][r.nextInt(nodes[0].length)];
        while (potentialGoalNode.getFill().equals(Color.rgb(155, 39, 175))) {
            potentialGoalNode = nodes[r.nextInt(nodes.length)][r.nextInt(nodes[0].length)];
        }
        potentialGoalNode.setFill(Color.rgb(33, 150, 243));
    }

    Node getStartingCell() {
        for (Node[] node : nodes) {
            for (Node n : node) {
                if (n.getFill().equals(Color.rgb(155, 39, 175))) {
                    return n;
                }
            }
        }
        return null;
    }

    Node getGoalCell() {
        for (Node[] node : nodes) {
            for (Node n : node) {
                if (n.getFill().equals(Color.rgb(33, 150, 243))) {
                    return n;
                }
            }
        }
        return null;
    }
}
