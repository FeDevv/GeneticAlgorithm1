package model.domains;

import java.awt.geom.Rectangle2D;

public class RectangularDomain implements Domain{

    private final double width;
    private final double height;
    private final Rectangle2D boundingBox;

    public RectangularDomain(double width, double height) {
        this.width = width;
        this.height = height;
        this.boundingBox = new Rectangle2D.Double(-width/2, -height/2, width, height);
    }

    @Override
    public boolean isPointInside(double x, double y) {
        return x >= -width / 2 && x <= width / 2 &&
        y >= -height / 2 && y <= height / 2;
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return this.boundingBox;
    }
}
