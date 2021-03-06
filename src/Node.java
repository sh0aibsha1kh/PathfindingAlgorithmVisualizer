import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;


class Node extends Rectangle {

    private int x, y;
    private Double h;

    Node(int x, int y, int width, int height) {
        super(width, height);
        this.x = x;
        this.y = y;
        this.setFill(Color.TRANSPARENT);
        this.setStroke(Color.BLACK);
        this.setStrokeType(StrokeType.INSIDE);
        this.setStrokeWidth(0.1);
    }

    int getXCoordinate() {
        return x;
    }

    int getYCoordinate() {
        return y;
    }

    Double getH() {
        return h;
    }

    void setH(Double heuristic) {
        this.h = heuristic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return x == node.x &&
                y == node.y;
    }
}
