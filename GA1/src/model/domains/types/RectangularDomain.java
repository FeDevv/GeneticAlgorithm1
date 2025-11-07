package model.domains.types;

import model.Individual;
import model.Point;
import model.domains.Domain;

import java.awt.geom.Rectangle2D;
import java.util.List;

public class RectangularDomain implements Domain {

    // ------------------- ATTRIBUTI -------------------

    private final double width;
    private final double height;

    // La Bounding Box è il rettangolo stesso, immutabile e centrale.
    private final Rectangle2D boundingBox;

    // ------------------- COSTRUTTORE -------------------

    /**
     * Crea un dominio rettangolare con le dimensioni specificate, centrato su (0, 0).
     * @param width Larghezza totale del rettangolo.
     * @param height Altezza totale del rettangolo.
     * * * Scelta Implementativa: Centramento su (0, 0).
     * Il rettangolo va da [-width/2, width/2] sull'asse X e da [-height/2, height/2] sull'asse Y.
     * Questo semplifica il ragionamento sui confini e rende il dominio neutrale rispetto al quadrante.
     */
    public RectangularDomain(double width, double height) {
        // I campi sono final, garantendo l'immutabilità del dominio.
        this.width = width;
        this.height = height;

        // Creazione della Bounding Box (che è il dominio stesso per un rettangolo).
        // Parametri: X, Y iniziali (angolo superiore sinistro), Larghezza, Altezza.
        this.boundingBox = new Rectangle2D.Double(-width/2, -height/2, width, height);
    }

    // ------------------- IMPLEMENTAZIONE INTERFACCIA DOMAIN -------------------

    /**
     * Verifica se un punto con coordinate (x, y) si trova all'interno del dominio rettangolare.
     * @param x Coordinata X del punto.
     * @param y Coordinata Y del punto.
     * @return True se il punto è compreso tra i limiti [-width/2, width/2] e [-height/2, height/2].
     */
    @Override
    public boolean isPointOutside(double x, double y) {
        // sto facendo !(isPointInside)
        return !((x >= -width / 2) && (x <= width / 2) &&
                (y >= -height / 2) && (y <= height / 2));
    }

    @Override
    public boolean isValidIndividual(Individual individual) {
        List<Point> points = individual.getChromosomes();
        for (Point p : points) {
            if (isPointOutside(p.getX(), p.getY())) { return false; }
        }
        return true;
    }

    /**
     * Ritorna la Bounding Box del dominio.
     * @return L'oggetto Rectangle2D che definisce i limiti del rettangolo.
     * * Scelta Implementativa: Poiché il dominio è un rettangolo, la Bounding Box è il rettangolo stesso.
     */
    @Override
    public Rectangle2D getBoundingBox() {
        return this.boundingBox;
    }
}
