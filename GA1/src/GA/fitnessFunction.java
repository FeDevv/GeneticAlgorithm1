package GA;

import model.Individual;
import model.Point;

import java.util.List;

public class fitnessFunction {
    /**
     * Ogni cromosoma (Individuo) è un array di Point. Ogni point conosce la sua
     * posizione e la sua distanza minima. Doppio ciclo per calcolare
     * la distanza di un punto da ogni altro punto.
     *
     * @param individual : un individuo del quale si vuole calcolare la fitness.
     * @return la fitness dell'individuo
     * */
    public double getFitness(Individual individual) {
        List<Point> chromosomes = individual.getChromosomes();
        double fitness = 0;
        //calcola la fitness per ogni coppia di punti
        for (int i = 0; i < chromosomes.size(); i++) {
            Point pi =  chromosomes.get(i);
            for (int j = i+1; j < chromosomes.size(); j++) {
                Point pj = chromosomes.get(j);
                fitness += getDistance(pi, pj);
            }
        }
        return fitness;
    }

    public double getDistance(Point p1, Point p2) {
        // Calcola la differenza tra le coordinate x dei centri
        double dx = p2.getCoordinates().get(0) - p1.getCoordinates().get(0);
        // Calcola la differenza tra le coordinate y dei centri
        double dy = p2.getCoordinates().get(1) - p1.getCoordinates().get(1);

        // Calcola la distanza euclidea (ipotenusa) tra i centri dei due cerchi.
        // Math.hypot(dx, dy) è il modo robusto per calcolare sqrt(dx*dx + dy*dy)
        double distanceBetweenCenters = Math.hypot(dx, dy);

        // Calcola la somma dei raggi dei due cerchi.
        double sumOfRadii = p1.getRadius() + p2.getRadius();

        // Calcola il "gap" (spazio) tra i bordi dei due cerchi.
        // Se gap > 0, i cerchi sono separati da quello spazio.
        // Se gap == 0, i cerchi si toccano.
        // Se gap < 0, i cerchi si sovrappongono di quella quantità.
        double gap = distanceBetweenCenters - sumOfRadii;

        if (gap < 0.0) {
            // --- QUESTA È LA FUNZIONE DI PENALITÀ ---
            // Se c'è una sovrapposizione (gap è negativo),
            // restituisci il gap moltiplicato per 10.
            // Questo "punisce" la soluzione in modo molto più severo
            // rispetto a una semplice sovrapposizione.
            // Esempio: un overlap di -2 diventa una penalità di -20.
            return gap * 10;
        } else {
            // Se i cerchi si toccano (gap = 0) o sono separati (gap > 0),
            // restituisci semplicemente il valore del gap.
            return gap;
        }
    }

}
