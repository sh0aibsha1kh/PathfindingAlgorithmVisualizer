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
                        || nodes[row][col].getFill().equals(Color.GREY))) {

                    nodes[row][col].setFill(Color.GREY);

                }
            }
        }
    }

    int getNumberOfObstacles() {
        int obstacles = 0;
        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfColumns; col++) {
                if (nodes[row][col].getFill().equals(Color.BLACK)) {
                    obstacles += 1;
                }
            }
        }
        return obstacles;
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

    ArrayList<Node> getNeighbours(Node node, boolean allowDiagonal) {
        ArrayList<Node> neighbours = new ArrayList<>();
        int x = node.getXCoordinate();
        int y = node.getYCoordinate();
        Node up = x - 1 >= 0 ? nodes[x - 1][y] : null;
        Node down = x + 1 < nodes.length ? nodes[x + 1][y] : null;
        Node left = y - 1 >= 0 ? nodes[x][y - 1] : null;
        Node right = y + 1 < nodes[0].length ? nodes[x][y + 1] : null;
        Node upRight = x - 1 >= 0 && y + 1 < nodes[0].length ? nodes[x - 1][y + 1] : null;
        Node upLeft = x - 1 >= 0 && y - 1 >= 0 ? nodes[x - 1][y - 1] : null;
        Node downRight = x + 1 < nodes.length && y + 1 < nodes[0].length ? nodes[x + 1][y + 1] : null;
        Node downLeft = x + 1 < nodes.length && y - 1 >= 0 ? nodes[x + 1][y - 1] : null;

        if (up != null && !isBlocked(up)) {
            neighbours.add(up);
        }
        if (upRight != null && !isBlocked(upRight) && allowDiagonal) {
            neighbours.add(upRight);
        }
        if (right != null && !isBlocked(right)) {
            neighbours.add(right);
        }
        if (downRight != null && !isBlocked(downRight) && allowDiagonal) {
            neighbours.add(downRight);
        }
        if (down != null && !isBlocked(down)) {
            neighbours.add(down);
        }
        if (downLeft != null && !isBlocked(downLeft) && allowDiagonal) {
            neighbours.add(downLeft);
        }
        if (left != null && !isBlocked(left)) {
            neighbours.add(left);
        }
        if (upLeft != null && !isBlocked(upLeft) && allowDiagonal) {
            neighbours.add(upLeft);
        }
        return neighbours;
    }

    private boolean isBlocked(Node n) {
        return n.getFill().equals(Color.GREY);
    }
}
