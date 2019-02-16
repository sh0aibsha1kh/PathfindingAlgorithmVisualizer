import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;


import java.util.*;

class Grid {

    private Node[][] nodes;
    private int numberOfRows, numberOfColumns, nodeHeight, nodeWidth;
    private List<Node> toBeColoured = new ArrayList<>();
    private int DURATION = 25;

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

    private Node getStartingNode() {
        for (Node[] node : nodes) {
            for (Node n : node) {
                if (n.getFill().equals(Color.rgb(155, 39, 175))) {
                    return n;
                }
            }
        }
        return null;
    }

    private Node getGoalNode() {
        for (Node[] node : nodes) {
            for (Node n : node) {
                if (n.getFill().equals(Color.rgb(33, 150, 243))) {
                    return n;
                }
            }
        }
        return null;
    }

    private ArrayList<Node> getNeighbours(Node node, boolean allowDiagonal) {
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

    public void breadthFirstSearch() {
        Set<Node> seen = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();
        Node startingNode = getStartingNode();
        Node goalNode = getGoalNode();
        seen.add(startingNode);
        queue.add(startingNode);
        toBeColoured.add(startingNode);

        while (queue.size() != 0) {

            Node currentNode = queue.remove();

            if (currentNode.equals(goalNode)) {
                return;
            }
            for (Node n : getNeighbours(currentNode, false)) {
                if (!seen.contains(n)) {
                    queue.add(n);
                    seen.add(n);
                    toBeColoured.add(n);
                }
            }
        }
    }

    public void depthFirstSearch() {
        Set<Node> seen = new HashSet<>();
        Stack<Node> stack = new Stack<>();
        Node startingNode = getStartingNode();
        Node goalNode = getGoalNode();
        seen.add(startingNode);
        stack.push(startingNode);
        toBeColoured.add(startingNode);

        while (!stack.empty()) {

            Node currentNode = stack.pop();
            seen.add(currentNode);
            toBeColoured.add(currentNode);
            if (currentNode.equals(goalNode)) {
                return;
            }
            for (Node n : getNeighbours(currentNode, true)) {
                if (!seen.contains(n)) {
                    stack.push(n);
                }
            }
        }
    }

    public void colourPath() {
        Timeline t = new Timeline();
        for (Node n : toBeColoured) {
            KeyFrame kf = new KeyFrame(Duration.millis(DURATION * (toBeColoured.indexOf(n) + 1)), event -> {
                if (n.equals(getGoalNode())) {
                    n.setFill(Color.GREEN);
                    t.stop();
                    return;
                }
                if (!n.equals(getStartingNode())) {
                    n.setFill(Color.ORANGE);
                }
            });

            t.getKeyFrames().add(kf);
        }
        t.play();
    }

}
