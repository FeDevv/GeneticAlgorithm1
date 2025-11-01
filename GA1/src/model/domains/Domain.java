package model.domains;

import java.awt.geom.Rectangle2D;

public interface Domain {
    //controlla se un punto è all'interno del dominio
    boolean isPointInside(double x, double y);
    //bounding box per la generazione di punti casuali
    Rectangle2D getBoundingBox();
}
