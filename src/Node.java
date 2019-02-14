import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

class Node extends Rectangle {

    private int i, j;

    Node(int i, int j, int width, int height) {
        super(width, height);
        this.i = i;
        this.j = j;
        this.setFill(Color.TRANSPARENT);
        this.setStroke(Color.BLACK);
        this.setStrokeType(StrokeType.INSIDE);
        this.setStrokeWidth(0.1);
    }

    int getXCoordinate() {
        return i;
    }

    int getYCoordinate() {
        return j;
    }

    void setCoordinates(int i, int j) {
        this.i = i;
        this.j = j;
    }
}
