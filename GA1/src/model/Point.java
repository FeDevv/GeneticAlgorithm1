package model;

import java.util.Arrays;
import java.util.List;

public class Point {
    //coordinate spaziali + raggio distanza
    private double x;
    private double y;
    private final double radius;

    //costruttore parziale per il posizionamento
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        radius = Double.MAX_VALUE;
    }
    //costruttore parziale per aggiungere il raggio
    public Point(double radius) {
        this.radius = radius;
    }

    //getter base - lascio privati
    private double getX() {
        return x;
    }
    private double getY() {
        return y;
    }
    private double getRadius() {
        return radius;
    }

    //getter per la coppia di coordinate
    public List<Double> getCoordinates() {
        return Arrays.asList(getX(), getY());
    }
}
