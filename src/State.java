import java.util.LinkedList;
import java.util.List;

class State {

    private Node node;
    private List<Node> pathToNode;

    State(Node node, LinkedList<Node> pathToNode) {
        this.node = node;
        this.pathToNode = pathToNode;
    }

    Node getNode() {
        return node;
    }

    List<Node> getPathToNode() {
        return pathToNode;
    }

}
