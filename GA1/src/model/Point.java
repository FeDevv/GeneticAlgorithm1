package model;

import java.util.Arrays;
import java.util.List;

public class Point {
    //coordinate spaziali + raggio distanza
    private final double x;
    private final double y;
    private final double radius;

    //costruttore parziale per il posizionamento
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        this.radius = Double.MAX_VALUE;
    }
    //costruttore totale per aggiungere il raggio
    public Point(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    //getter base - lascio privati
    public double getX() { return x; }
    public double getY() { return y; }
    public double getRadius() { return radius; }

    //getter per la coppia di coordinate
    public List<Double> getCoordinates() { return Arrays.asList(getX(), getY()); }

    @Override
    public String toString() {
        return String.format("[%.4f, %.4f]", this.x, this.y);
    }
}
