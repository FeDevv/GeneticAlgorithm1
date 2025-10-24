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

    /**
     * Fai riprodurre gli individui per generare una nuova popolazione.
     * La riproduzione include crossover e mutazioni.
     * */
    public List<Individual> reproduce() {
        List<Individual> newPopulation = new ArrayList<>(oldPopulation.size());

        //seleziona i migliori, da ricopiare nella prossima generazione
        List<Individual> elites = selectElites();
        //inseriscili direttamente nella nuova generazione
        newPopulation.addAll(elites);

        //numero di posti da riempire per avere nella nuova popolazione lo stesso numero di individuo della vecchia generazione
        int commonsNumber = oldPopulation.size() - elites.size();

        //ciclo per creare i nuovi individui
        for (int i = 0; i < commonsNumber; i++) {
            Individual dad = tournament();
            Individual mom = tournament();

            //scegli mom e dad diversi
            while (dad == mom) {
                mom = tournament();
            }

            //TODO crossover
            //TODO mutazione
            //TODO aggiungi l'individuo nella nuova generazione
        }

        return newPopulation;
    }

}
