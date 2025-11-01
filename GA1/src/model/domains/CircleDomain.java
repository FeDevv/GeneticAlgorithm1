package model.domains;

import java.awt.geom.Rectangle2D;

public class CircleDomain implements Domain{

    private final double radius;
    private final Rectangle2D boundingBox;

    public CircleDomain(double radius) {
        if (radius <= 0) {
            throw new IllegalArgumentException("The radius must be positive");
        }
        this.radius = radius;
        this.boundingBox = new Rectangle2D.Double(-radius, -radius, radius*2, radius*2);
    }

    @Override
    public boolean isPointInside(double x, double y) {
        return (x*x + y*y) <= (radius*radius);
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return this.boundingBox;
    }
}
