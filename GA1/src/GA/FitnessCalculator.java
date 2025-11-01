package GA;

import model.domains.Domain;
import model.Individual;
import model.Point;

import java.util.List;

public class FitnessCalculator {

    private final Domain currentDomain;
    private static final double DOMAIN_PENALTY = 10000.0;
    private static final double OVERLAP_WEIGHT = 100.0;

    //costruttore per ricevere il dominio
    public FitnessCalculator(Domain domain) {
        this.currentDomain = domain;
    }


    public double getFitness(Individual individual) {
        List<Point> chromosomes = individual.getChromosomes();
        double totalPenalty = 0.0;

        // PENALITA' DI DOMINIO
        // controlla ogni punto "velocemente" (O(N))
        for (Point p : chromosomes) {
            if (!this.currentDomain.isPointInside(p.getX(), p.getY())) {
                totalPenalty += DOMAIN_PENALTY;
            }
        }

        // PENALITà DI OVERLAP
        // controlla ogni coppia di punti, fissato il primo, lento (O(N^2))
        for (int i = 0; i < chromosomes.size(); i++) {
            Point p_i = chromosomes.get(i);
            double r_i = p_i.getRadius();

            for (int j = i+1; j < chromosomes.size(); j++) {
                Point p_j = chromosomes.get(j);
                double r_j = p_j.getRadius();

                double requiredDistance = r_i + r_j;
                double actualDistance = getDistance(p_i, p_j);

                // calcola overlap se ce n'è e aggiungi alla penalità
                if (actualDistance < requiredDistance) {
                    double overlapAmount = requiredDistance - actualDistance;
                    totalPenalty += (overlapAmount * overlapAmount) * OVERLAP_WEIGHT;
                }
            }
        }

        // conversione finale
        return 1.0 / (1.0 + totalPenalty);
    }

    //calcola la distanza tra i centri
    public double getDistance(Point p1, Point p2) {
        // Calcola la differenza tra le coordinate x dei centri
        double dx = p2.getX() - p1.getX();
        // Calcola la differenza tra le coordinate y dei centri
        double dy = p2.getY() - p1.getY();

        return Math.hypot(dx, dy);
    }

}
