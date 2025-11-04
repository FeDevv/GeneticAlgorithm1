package GA;

import model.Individual;
import model.Point;
import model.domains.Domain;
import utils.RandomUtils;

import java.awt.geom.Rectangle2D;
import java.util.List;

public class Mutation {

    // ------------------- ATTRIBUTI (Parametri di Configurazione) -------------------

    // La probabilità che un singolo gene (Point) subisca una mutazione.
    private final double mutationProbability;

    // L'ampiezza massima della perturbazione applicata alle coordinate X e Y.
    private final double mutationStrength;

    // Riferimento al dominio per ottenere i limiti (Bounding Box) per il clamping.
    private final Domain domain;

    // ------------------- COSTRUTTORE -------------------

    /**
     * Costruisce l'operatore di Mutazione con i parametri di controllo e il dominio.
     * @param mutationProbability La probabilità che la mutazione avvenga su un gene.
     * @param mutationStrength L'entità della perturbazione.
     * @param domain Il riferimento al dominio del problema.
     * * Scelta Implementativa: L'uso di 'final' per tutti i campi garantisce l'immutabilità della configurazione.
     */
    public Mutation(double mutationProbability, double mutationStrength, Domain domain) {
        this.mutationProbability = mutationProbability;
        this.mutationStrength = mutationStrength;
        this.domain = domain;
    }

    // ------------------- METODO PRINCIPALE -------------------

    /**
     * Esegue l'operazione di mutazione su un individuo.
     * Il processo è in-place, modificando l'individuo passato come parametro.
     * @param I L'individuo da mutare.
     */
    public void mutate(Individual I) {

        // Estrae i limiti della Bounding Box (il rettangolo che contiene il dominio).
        // Questo viene fatto una volta per l'efficienza.
        java.awt.geom.Rectangle2D boundingBox = domain.getBoundingBox();
        double minX = boundingBox.getMinX();
        double minY = boundingBox.getMinY();
        double maxX = boundingBox.getMaxX();
        double maxY = boundingBox.getMaxY();

        // Cicla su ogni cromosoma (per ogni punto).
        for (int i = 0; i < I.getDimension(); i++) {

            // Controlla la probabilità di mutazione per questo gene.
            if (RandomUtils.randDouble() < mutationProbability) {

                Point oldPoint = I.getChromosomes().get(i);
                double radius = oldPoint.getRadius(); // Mantiene il raggio invariato.

                // Calcola la perturbazione casuale (Mutazione Gaussiana / Creep Mutation).
                // (RandomUtils.randDouble() * 2 - 1) genera un valore tra [-1.0, 1.0).
                double newX = oldPoint.getX() + (RandomUtils.randDouble() * 2 - 1) * mutationStrength;
                double newY = oldPoint.getY() + (RandomUtils.randDouble() * 2 - 1) * mutationStrength;

                // 🌟 APPLICAZIONE DEL SOFT-CLAMPING
                // Forza le coordinate all'interno dei limiti della Bounding Box.
                // * Scelta Implementativa: Strategia ibrida per l'efficienza.
                // Previene la generazione di troppi individui palesemente fuori dominio,
                // riducendo il lavoro per la FitnessCalculator.
                double finalX = clamp(newX, minX, maxX);
                double finalY = clamp(newY, minY, maxY);

                // Crea un nuovo oggetto Point (necessario perché Point è immutabile).
                Point newPoint = new Point(finalX, finalY, radius);

                // Sostituisce il vecchio gene con il nuovo gene mutato (Mutazione in-place).
                // L'uso di setChromosome() è necessario per aggirare la vista immutabile del getter.
                I.setChromosome(i, newPoint);
            }
        }
        // * Scelta Implementativa: La mutazione avviene "in-place" (modificando il riferimento all'individuo I),
        // come è tipico per gli operatori genetici.
    }

    // ------------------- METODI UTILITY -------------------

    /**
     * Limita un valore 'value' all'interno di un intervallo chiuso [min, max].
     */
    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
