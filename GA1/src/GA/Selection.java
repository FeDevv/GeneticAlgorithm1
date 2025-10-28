package GA;

import model.Individual;
import utils.RandomUtils;

import java.util.*;

/**
 * Selezionare i migliori individui per dare il via
 * a una nuova generazione
 * */
public class Selection {

    private final List<Individual> oldPopulation;
    private final int tournamentSize;
    private final double percent;
    //private final double crossoverRate;
    //private final double mutationRate;

    //costruttore generare oggetto selezione: questo oggetto permette di creare nuove generazioni
    public Selection(List<Individual> oldPopulation, int tournamentSize, double percent) {
        this.oldPopulation = oldPopulation;
        this.tournamentSize = tournamentSize;
        this.percent = percent;
    }

    /**
     * Seleziona i migliori individui di una generazione cosi da ricopiarli nella prossima generazione.
     * Tramite percent voglio prendere i migliori X% del totale (soluzioni piu grandi prendono piu individui)
     * @return ritorna la lista dei migliori (non in ordine)
     * */
    private List<Individual> selectElites() {
        int populationSize = oldPopulation.size();
        int eliteSize = Math.max(1 , (int)Math.floor(populationSize * percent));

        PriorityQueue<Individual> elites = new PriorityQueue<>(eliteSize, Comparator.comparingDouble(Individual::getFitness));

        for (Individual ind : oldPopulation) {
            if (elites.size() < eliteSize) {
                elites.offer(ind);
                // L'IDE potrebbe segnalare un potenziale NullPointerException su peek(),
                // ma è un falso positivo. Questo blocco 'else if' viene eseguito solo
                // quando la coda è piena (size == eliteSize).
                // Dato che eliteSize è garantito essere >= 1 (da Math.max),
                // la coda non sarà mai vuota in questo punto, quindi peek() è sicuro.
                // linea di codice prima: else if (ind.getFitness() > elites.peek().getFitness())
            } else if (ind.getFitness() > Objects.requireNonNull(elites.peek()).getFitness()) {
                //Rimuovi il peggiore tra i migliori
                elites.poll();
                //inserisci il record breaker
                elites.offer(ind);
            }
        }

        return elites.stream().toList();
    }

    /**
     * Torneo tra N individui, viene selezionato solo il migliore.
     * Usato per cercare il padre e la madre.
     * @return winner, il migliore dei partecipanti
     * */
    private Individual tournament() {
        List<Integer> indices = RandomUtils.UniqueIndices(tournamentSize, oldPopulation.size());
        List<Individual> participants = new ArrayList<>();

        for (int i : indices) {
            //prendi i 'tournamentSize' individui random
            participants.add(oldPopulation.get(i));
        }

        Individual winner = participants.getFirst();
        for (Individual individual : participants) {
            if (winner.getFitness() < individual.getFitness()) {
                winner = individual;
            }
        }
        return winner;
    }
}
