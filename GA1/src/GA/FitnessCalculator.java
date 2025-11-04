package GA;

import model.domains.Domain;
import model.Individual;
import model.Point;

import java.util.List;

public class FitnessCalculator {

    // ------------------- ATTRIBUTI (Pesi di Penalizzazione) -------------------

    private final Domain currentDomain;

    // Penalità fissa molto alta per ogni gene (Point) che viola i confini del dominio.
    // Garantisce che le soluzioni fuori dal dominio abbiano una fitness quasi zero.
    private static final double DOMAIN_PENALTY = 10000.0;

    // Peso applicato alla quantità di sovrapposizione tra i punti.
    // Un valore alto assicura che l'overlap sia l'obiettivo primario da minimizzare.
    private static final double OVERLAP_WEIGHT = 100.0;

    // NOTA: Per il futuro, qui potremmo aggiungere DISTANCE_WEIGHT per l'obiettivo di distribuzione.

    // ------------------- COSTRUTTORE -------------------

    /**
     * Costruisce il calcolatore di fitness associandolo al dominio specifico del problema.
     * @param domain Il dominio geometrico che definisce i vincoli di confine.
     * * Scelta Implementativa: L'uso di 'final' garantisce che la configurazione del dominio non cambi.
     */
    public FitnessCalculator(Domain domain) {
        this.currentDomain = domain;
    }

    // ------------------- METODO PRINCIPALE -------------------

    /**
     * Calcola il valore di fitness per un dato individuo.
     * La fitness è una misura della qualità, dove un valore più alto è migliore.
     * @param individual L'individuo (soluzione candidata) da valutare.
     * @return Il valore di fitness, compreso tra 0 e 1 (se totalPenalty >= 0).
     */
    public double getFitness(Individual individual) {
        List<Point> chromosomes = individual.getChromosomes();
        double totalPenalty = 0.0;

        // --- PENALITÀ DI DOMINIO (Complessità O(N)) ---
        // Controlla il vincolo di confine per ogni singolo punto (gene).
        for (Point p : chromosomes) {
            if (!this.currentDomain.isPointInside(p.getX(), p.getY())) {
                totalPenalty += DOMAIN_PENALTY;
            }
        }

        // --- PENALITÀ DI OVERLAP (Complessità O(N^2)) ---
        // Controlla il vincolo di non-sovrapposizione per ogni coppia di punti.
        for (int i = 0; i < chromosomes.size(); i++) {
            Point p_i = chromosomes.get(i);
            double r_i = p_i.getRadius();

            // Il ciclo interno parte da i+1 per evitare di confrontare un punto con sé stesso e contare le coppie due volte.
            for (int j = i+1; j < chromosomes.size(); j++) {
                Point p_j = chromosomes.get(j);
                double r_j = p_j.getRadius();

                double requiredDistance = r_i + r_j;
                double actualDistance = getDistance(p_i, p_j);

                // Se la distanza attuale è minore della distanza richiesta, c'è sovrapposizione.
                if (actualDistance < requiredDistance) {
                    double overlapAmount = requiredDistance - actualDistance;
                    // * Scelta Implementativa: Penalità Quadratica.
                    // L'uso di (overlapAmount * overlapAmount) penalizza le sovrapposizioni maggiori in modo esponenziale,
                    // spingendo l'AG a risolverle rapidamente.
                    totalPenalty += (overlapAmount * overlapAmount) * OVERLAP_WEIGHT;
                }
            }
        }

        // --- CONVERSIONE FINALE (Minimizzazione -> Massimizzazione) ---
        // Funzione di conversione: F = 1 / (1 + C).
        // Una penalità totale (C) di 0 restituisce la fitness massima (1.0).
        // Una penalità alta restituisce una fitness che tende a 0.
        return 1.0 / (1.0 + totalPenalty);
    }

    // ------------------- UTILITY -------------------

    /**
     * Calcola la distanza euclidea tra i centri di due punti.
     * @param p1 Centro del primo punto.
     * @param p2 Centro del secondo punto.
     * @return La distanza tra i centri.
     * * Scelta Implementativa: Uso di Math.hypot.
     * Math.hypot è più robusto di Math.sqrt(dx*dx + dy*dy) per prevenire potenziali overflow
     * con coordinate molto grandi.
     */
    public double getDistance(Point p1, Point p2) {
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        return Math.hypot(dx, dy);
    }
}
