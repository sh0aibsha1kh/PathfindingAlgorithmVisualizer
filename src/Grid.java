import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;


import java.util.*;

class Grid {

    private Node[][] nodes;
    private Node startingNode, goalNode;
    private int numberOfRows, numberOfColumns, nodeHeight, nodeWidth;
    private List<Node> toBeColoured = new ArrayList<>();
    private double[][] heuristicValues;

    Grid(int numberOfRows, int numberOfColumns, int nodeWidth, int nodeHeight) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.nodeWidth = nodeWidth;
        this.nodeHeight = nodeHeight;
        this.nodes = new Node[numberOfRows][numberOfColumns];
        this.heuristicValues = new double[numberOfRows][numberOfColumns];

        createGrid();

        this.startingNode = createStartingNode();
        this.goalNode = createGoalNode();
    }

    Node[][] getNodes() {
        return nodes;
    }

    private void createGrid() {
        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfColumns; col++) {
                Node node = new Node(row, col, nodeWidth, nodeHeight);
                nodes[row][col] = node;

            }
        }
    }

    Node createStartingNode() {
        Random r = new Random();
        Node startingNode = nodes[r.nextInt(nodes.length)][r.nextInt(nodes[0].length)];
        startingNode.setFill(Color.rgb(155, 39, 175));
        return startingNode;
    }

    Node createGoalNode() {
        Random r = new Random();
        Node goalNode = nodes[r.nextInt(nodes.length)][r.nextInt(nodes[0].length)];
        while (goalNode.getFill().equals(Color.rgb(155, 39, 175))) {
            goalNode = nodes[r.nextInt(nodes.length)][r.nextInt(nodes[0].length)];
        }
        goalNode.setFill(Color.rgb(33, 150, 243));
        return goalNode;
    }

    void createObstacleNodes() {
        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfColumns; col++) {
                Node currentNode = nodes[row][col];
                if (Math.random() <= 0.25
                        && !(currentNode.equals(getStartingNode()) || currentNode.equals(getGoalNode())
                        || isBlocked(currentNode))) {

                    currentNode.setFill(Color.GREY);

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

    void breadthFirstSearch() {
        Set<Node> seen = new HashSet<>();
        Queue<Node> queue = new LinkedList<>();
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

    void depthFirstSearch() {
        Set<Node> seen = new HashSet<>();
        Stack<Node> stack = new Stack<>();
        seen.add(startingNode);
        stack.push(startingNode);
        toBeColoured.add(startingNode);

        while (!stack.empty()) {

            Node currentNode = stack.pop();
            seen.add(currentNode);
            toBeColoured.add(currentNode);
            List<Node> neighbours = getNeighbours(currentNode, true);

            if (currentNode.equals(goalNode)) {
                return;
            }

            for (int i = neighbours.size() - 1; i >= 0; i--) {
                Node n = getNeighbours(currentNode, true).get(i);
                if (!seen.contains(n)) {
                    stack.push(n);
                }
            }
        }
    }

    void aStarSearch(boolean allowDiagonal) {
        createHeuristicValues(allowDiagonal);
        Comparator<Node> comparator = (Node n1, Node n2) -> {
            if (n1.getH() < n2.getH()) return -1;
            else if (n1.getH() > n2.getH()) return 1;
            return 0;
        };
        startingNode.setH(heuristicValues[startingNode.getXCoordinate()][startingNode.getYCoordinate()]);
        Map<Node, Double> seen = new HashMap<>();
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(comparator);
        seen.put(startingNode, startingNode.getH());
        priorityQueue.add(startingNode);
        toBeColoured.add(startingNode);

        while (!priorityQueue.isEmpty()) {

            Node currentNode = priorityQueue.remove();

            if (currentNode.equals(goalNode)){
                return;
            }
            for (Node n : getNeighbours(currentNode, allowDiagonal)) {
                n.setH(heuristicValues[n.getXCoordinate()][n.getYCoordinate()]);
                if(!seen.containsKey(n) || n.getH() < seen.get(n)) {
                    priorityQueue.add(n);
                    seen.put(n, n.getH());
                    System.out.println(heuristicValues[n.getXCoordinate()][n.getYCoordinate()]);
                    toBeColoured.add(n);
                }
            }
        }


    }

    void colourPath() {
        Timeline t = new Timeline();
        for (Node n : toBeColoured) {
            int DURATION = 1000;
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

    double cost(Node startingNode, Node currentNode) {
        return 0;
    }


    double heuristic(Node currentNode, Node goalNode, boolean allowDiagonal) {
        if (allowDiagonal) {
            return euclideanDistance(currentNode, goalNode);
        } else {
            return manhattanDistance(currentNode, goalNode);
        }
    }

    /* ========== HELPERS ========== */

    private boolean isBlocked(Node n) {
        return n.getFill().equals(Color.GREY);
    }

    private void createHeuristicValues(boolean allowDiagonal) {
        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfColumns; col++) {
                Node currentNode = nodes[row][col];
                if (currentNode.equals(startingNode)) {
                    heuristicValues[row][col] = heuristic(startingNode, goalNode, allowDiagonal);
                } else if (currentNode.equals(goalNode)) {
                    heuristicValues[row][col] = 0;
                } else {
                    heuristicValues[row][col] = heuristic(currentNode, goalNode, allowDiagonal);
                }
            }
        }
    }

    private double euclideanDistance(Node currentNode, Node goalNode) {
        return Math.sqrt(Math.pow(goalNode.getXCoordinate() - currentNode.getXCoordinate(), 2)
                + Math.pow(goalNode.getYCoordinate() - currentNode.getYCoordinate(), 2));
    }

    private double manhattanDistance(Node currentNode, Node goalNode) {
        return Math.abs(goalNode.getXCoordinate() - currentNode.getXCoordinate())
                + Math.abs(goalNode.getYCoordinate() - currentNode.getYCoordinate());
    }


}
