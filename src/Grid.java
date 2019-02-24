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
    private boolean allowDiagonal;

    Grid(int numberOfRows, int numberOfColumns, int nodeWidth, int nodeHeight, boolean allowDiagonal) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.nodeWidth = nodeWidth;
        this.nodeHeight = nodeHeight;
        this.allowDiagonal = allowDiagonal;
        this.nodes = new Node[numberOfRows][numberOfColumns];
        this.heuristicValues = new double[numberOfRows][numberOfColumns];

        createGrid();

        this.startingNode = createStartingNode();
        this.goalNode = createGoalNode();

        createHeuristicValues();
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

    private Node createStartingNode() {
        Random r = new Random();
        Node startingNode = nodes[r.nextInt(nodes.length)][r.nextInt(nodes[0].length)];
        startingNode.setFill(Color.rgb(155, 39, 175));
        return startingNode;
    }

    private Node createGoalNode() {
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
                if (nodes[row][col].getFill().equals(Color.GREY)) {
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

    private ArrayList<Node> getNeighbours(Node node) {
        ArrayList<Node> neighbours = new ArrayList<>();
        ArrayList<Node> directions = new ArrayList<>();
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

        directions.add(up);
        if (allowDiagonal) directions.add(upRight);
        directions.add(right);
        if (allowDiagonal) directions.add(downRight);
        directions.add(down);
        if (allowDiagonal) directions.add(downLeft);
        directions.add(left);
        if (allowDiagonal) directions.add(upLeft);

        for (Node d : directions) {
            if (d != null && !isBlocked(d)) {
                neighbours.add(d);
            }
        }
        return neighbours;
    }

    /**
     * This method runs breadth first search on the current grid in order to find a path from the starting node
     * to the goal node.
     */
    void breadthFirstSearch() {

        Queue<State> queue = new LinkedList<>();
        State startingState = new State(startingNode, new LinkedList<>());
        Map<Node, Double> seen = new HashMap<>();
        seen.put(startingState.getNode(), 0.0);
        queue.add(startingState);

        while (!queue.isEmpty()) {

            State currentState = queue.remove();

            if (calculatePathCost(currentState.getPathToNode()) <= seen.get(currentState.getNode())) {

                if (currentState.getNode().equals(goalNode)) {
                    return;
                }

                for (Node n : getNeighbours(currentState.getNode())) {

                    LinkedList<Node> path = new LinkedList<>(currentState.getPathToNode());
                    path.add(n);

                    double newCost = calculatePathCost(path);

                    if (!seen.containsKey(n) || newCost < seen.get(n)) {
                        queue.add(new State(n, path));
                        seen.put(n, newCost);
                        toBeColoured.add(n);
                    }
                }
            }
        }
    }

    /**
     * This method runs depth first search on the current grid in order to find a path from the starting node
     * to the goal node. It goes North first, then Northeast, then East, then ..., and finally Northwest.
     */
    void depthFirstSearch() {
        Stack<State> stack = new Stack<>();
        State startingState = new State(startingNode, new LinkedList<>());
        Set<Node> visited = new HashSet<>();
        stack.push(startingState);

        while (!stack.empty()) {

            State currentState = stack.pop();
            visited.add(currentState.getNode());

            toBeColoured.add(currentState.getNode());

            if (currentState.getNode().equals(goalNode)) {
                return;
            }

            List<Node> neighbours = getNeighbours(currentState.getNode());

            for (int i = neighbours.size() - 1; i >= 0; i--) {
                Node n = neighbours.get(i);
                if (!visited.contains(n)) {
                    stack.push(new State(n, null));
                }
            }
        }
    }

    /**
     * This method runs a* search on the current grid in order to find a path from the starting node
     * to the goal node efficiently.
     */
    List<Node> aStarSearch() {
        PriorityQueue<State> priorityQueue = new PriorityQueue<>((State s1, State s2) -> {
            double s1Cost = calculatePathCost(s1.getPathToNode()) + s1.getNode().getH();
            double s2Cost = calculatePathCost(s2.getPathToNode()) + s2.getNode().getH();
            if (s1Cost < s2Cost) return -1;
            else if (s1Cost > s2Cost) return 1;
            return 0;
        });
        startingNode.setH(heuristicValues[startingNode.getXCoordinate()][startingNode.getYCoordinate()]);
        State startingState = new State(startingNode, new LinkedList<>());
        Map<Node, Double> seen = new HashMap<>();
        seen.put(startingState.getNode(), 0.0);
        priorityQueue.add(startingState);

        while (!priorityQueue.isEmpty()) {

            State currentState = priorityQueue.remove();

            if (calculatePathCost(currentState.getPathToNode()) <= seen.get(currentState.getNode())) {
                if (currentState.getNode().equals(goalNode)) {
                    return currentState.getPathToNode();
                }
            }

            for (Node n : getNeighbours(currentState.getNode())) {

                n.setH(heuristicValues[n.getXCoordinate()][n.getYCoordinate()]);

                LinkedList<Node> path = new LinkedList<>(currentState.getPathToNode());
                path.add(n);

                double newCost = calculatePathCost(path) + n.getH();

                if (!seen.containsKey(n) || newCost < seen.get(n)) {
                    priorityQueue.add(new State(n, path));
                    seen.put(n, newCost);
                    toBeColoured.add(n);
                }
            }
        }
        return null;
    }

    void colourPath() {
        Timeline t = new Timeline();
        for (Node n : toBeColoured) {
            int SPEED = 10;
            KeyFrame kf = new KeyFrame(Duration.millis(SPEED * (toBeColoured.indexOf(n) + 1)), event -> {
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

    void colourFinalPath(List<Node> finalPath) {
        Timeline t = new Timeline();
        for (Node n : finalPath) {
            int SPEED = 25;
            KeyFrame kf = new KeyFrame(Duration.millis(SPEED * (toBeColoured.indexOf(n) + 1)), event -> {
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

    /* ========== HELPERS ========== */

    /**
     * Check to see if the provided Node is blocked.
     *
     * @param n the Node that is being checked
     * @return true if *n* is blocked, false otherwise
     */
    private boolean isBlocked(Node n) {
        return n.getFill().equals(Color.GREY);
    }

    private double calculatePathCost(List<Node> path) {
        double cost = 0;
        if (path == null || path.size() == 0) {
            return 0;
        }
        Node previousNode = path.get(0);
        for (int i = 1; i < path.size(); i++) {
            Node currentNode = path.get(i);
            cost += heuristic(previousNode, currentNode);
            previousNode = currentNode;
        }
        return cost;
    }

    private double heuristic(Node currentNode, Node goalNode) {
        if (allowDiagonal) {
            return euclideanDistance(currentNode, goalNode);
        } else {
            return manhattanDistance(currentNode, goalNode);
        }
    }

    /**
     * Generate the heuristic values for all of the nodes in the grid.
     */
    private void createHeuristicValues() {
        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfColumns; col++) {
                Node currentNode = nodes[row][col];
                if (currentNode.equals(startingNode)) {
                    heuristicValues[row][col] = heuristic(startingNode, goalNode);
                } else if (currentNode.equals(goalNode)) {
                    heuristicValues[row][col] = 0;
                } else {
                    heuristicValues[row][col] = heuristic(currentNode, goalNode);
                }
            }
        }
    }

    /**
     * Return the Euclidean distance between two nodes
     *
     * @param currentNode the current node
     * @param goalNode the goal node
     * @return a Double value representing the Euclidean distance
     */
    private double euclideanDistance(Node currentNode, Node goalNode) {
        return Math.sqrt(Math.pow(goalNode.getXCoordinate() - currentNode.getXCoordinate(), 2)
                + Math.pow(goalNode.getYCoordinate() - currentNode.getYCoordinate(), 2));
    }

    /**
     * Return the Manhattan distance between two nodes
     *
     * @param currentNode the current node
     * @param goalNode the goal node
     * @return a Double value representing the Manhattan distance
     */
    private double manhattanDistance(Node currentNode, Node goalNode) {
        return Math.abs(goalNode.getXCoordinate() - currentNode.getXCoordinate())
                + Math.abs(goalNode.getYCoordinate() - currentNode.getYCoordinate());
    }


}
