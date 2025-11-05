package GA;

import model.Individual;
import model.Point;
import model.domains.Domain;
import utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Questo è il controller dell'evoluzione, esegue in maniera ordinata
 * tutti i passaggi necessari per creare una nuova generazione e selezionare il miglior risultato.
 * I passaggi da eseguire sono:
 * <p>
 * 1. crea una lista vuota per mantenere una nuova popolazione
 * 2. dal precedente miglior risultato, prendi gli ELITEs, ossia i migliori e ricopiali (clona) direttamente nella nuova popolazione
 * 3. per il resto degli individui effettua il seguente ciclo tante volte quanti sono i posti liberi :
 *  3.1 attraverso un torneo, cerca la mamma
 *  3.2 attraverso un torneo, cerca il papà
 *  3.3 effettua crossover (con probabilità) tra mamma e papà -> genera 1 solo figlio
 *  3.4 effettua mutazione (con probabilità) sul figlio
 *  3.5 aggiungi il figlio alla popolazione
 * 4. Ritorna la nuova popolazione
 * </p>
 * I punti 1. e 2. implicano che ci deve essere una popolazione precedente, dobbiamo occuparci del passo base.
 * Questa classe si occuperà anche di generare randomimcamente la prima generazione.
 * */
public class EvolutionEngine {

    // ==================================================================================
    // ⚙️ CONFIGURAZIONE E ATTRIBUTI IMMUTABILI
    // ==================================================================================

    // Il vincolo spaziale del problema.
    private final Domain domain;

    // Numero di soluzioni candidate in ogni generazione.
    private final int populationSize;

    // Lunghezza del cromosoma: numero di Point per individuo.
    private final int individualSize;

    // Numero massimo di iterazioni (cicli) evolutivi.
    private final int generations;

    // Il raggio (dimensione) dei punti che compongono gli individui.
    private final double radius;

    // ------------------- SERVIZI E STATO -------------------

    // services
    private final FitnessCalculator fitnessCalculator;
    private final Mutation gammaRays;
    private final Crossover mixer;
    private final Selection selector;

    // ==================================================================================
    // 🔨 COSTRUTTORE
    // ==================================================================================

    /**
     * Costruttore completo per configurare tutti i parametri dell'algoritmo genetico.
     */
    public EvolutionEngine(Domain domain, int populationSize, int individualSize, int generations, int tournamentSize, double elitesPercentage,
                           double crossoverProbability, double mutationProbability, double mutationStrenght, double radius) {
        // Inizializza tutti gli attributi finali di configurazione.
        this.domain = domain;
        this.populationSize = populationSize;
        this.individualSize = individualSize;
        this.generations = generations;
        this.radius = radius;
        // tournamentSize --> Dimensione N per la selezione per torneo.
        // elitesPercentage --> Percentuale della popolazione da copiare direttamente (strategia di elitismo).
        // crossoverProbability --> Probabilità di ricombinazione.
        // mutationStrenght --> L'ampiezza massima della perturbazione della mutazione.
        // mutationProbability --> Probabilità che un singolo gene muti.

        // Non serve memorizzare questi parametri che ho commentato, in quanto servono solo all'inizializzazione del controller
        // verranno salvati nelle classi di servizio corrispondenti.

        // Inizializzazione dei servizi, sono costanti per tutta l'esecuzione. Unica istanza.
        this.fitnessCalculator = new FitnessCalculator(domain);
        this.gammaRays = new Mutation(mutationProbability, mutationStrenght, domain);
        this.mixer = new Crossover(crossoverProbability);
        this.selector = new Selection(tournamentSize, elitesPercentage);
    }

    // ==================================================================================
    // 🚀 STEP 1: INIZIALIZZAZIONE
    // ==================================================================================

    /**
     * Genera un singolo individuo (soluzione) con punti casuali all'interno della Bounding Box.
     * * Scelta Implementativa: Usa la Bounding Box per l'efficienza di inizializzazione.
     */
    private Individual buildIndividual() {
        List<Point> points = new ArrayList<>(individualSize);
        for (int i = 0; i < individualSize; i++) {
            // Usa il raggio del punto (this.radius) per creare il Point.
            points.add(RandomUtils.insideBoxGenerator(domain.getBoundingBox(), radius));
        }
        return new Individual(points);
    }

    /**
     * Crea la prima generazione di individui (popolazione iniziale) in modo casuale.
     */
    private List<Individual> firstGeneration() {
        List<Individual> firstGen = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            firstGen.add(buildIndividual());
        }
        return firstGen;
    }

    // ==================================================================================
    // ♻️ STEP 2: MOTORE DI EVOLUZIONE (Metodo Pubblico)
    // ==================================================================================

    /**
     * Metodo di avvio: Esegue il ciclo evolutivo completo e restituisce la migliore soluzione trovata.
     * @return Una copia della migliore soluzione trovata globalmente.
     */
    public Individual reproduce() {
        // tengo traccia del miglior individuo
        Individual solution;

        // --- Fase 1: Inizializzazione ---
        List<Individual> oldGeneration = firstGeneration();

        // Calcola la fitness iniziale per l'intera popolazione.

        // Esegui la valutazione della fitness su core CPU multipli
        oldGeneration.parallelStream().forEach(ind -> {
            // La lambda expression (ind -> ...) viene eseguita in parallelo
            ind.setFitness(fitnessCalculator.getFitness(ind));
        });

        // Stabilisce la prima soluzione globale migliore.
        solution = currentBestSolution(oldGeneration, null);

        // --- Fase 2: Ciclo di Evoluzione ---
        for (int i = 0; i < generations; i++) {

            List<Individual> newGeneration = new ArrayList<>(populationSize);

            // 1. Elitismo: seleziona i migliori della generazione precedente.
            List<Individual> elites = selector.selectElites(oldGeneration);
            newGeneration.addAll(elites);

            // 2. Crossover e Mutazione: riempie il resto della popolazione.
            int childrenToGenerate = populationSize - elites.size();

            for (int j = 0; j < childrenToGenerate; j++) {
                /**
                 * POTENTIALLY EACH OPERATION INSEDE THIS LOOP COULD BE PARALLELIZED THROUGH MULTITHREADING
                 * */
                // Selezione genitori tramite torneo
                Individual dad = selector.tournament(oldGeneration);
                Individual mom = selector.tournament(oldGeneration);
                while (mom == dad) { // Assicura che i genitori siano individui distinti (per un crossover efficace)
                    mom = selector.tournament(oldGeneration);
                }

                // Crossover
                Individual child = mixer.uniformCrossover(mom, dad);

                // Mutazione
                // L'oggetto Domain è necessario qui per il soft-clamping all'interno di Mutation.
                gammaRays.mutate(child);

                // Calcola fitness del figlio e lo aggiunge alla nuova generazione.
                child.setFitness(fitnessCalculator.getFitness(child));
                /**
                 * SINCE THIS IS THE COMMON ELEMENT BETWEEN THREADS, IT SHOULD BE PROTECTED
                 * */
                newGeneration.add(child);
            }

            // 3. Aggiornamento: Verifica il record globale (Elitismo Globale).
            solution = currentBestSolution(newGeneration, solution);

            // La nuova generazione diventa la base per la prossima iterazione.
            oldGeneration = newGeneration;
        }

        // Restituisce una copia profonda per garantire che il risultato finale sia immutabile per l'utente.
        return solution.copy();
    }

    // ==================================================================================
    // ℹ️ UTILITY
    // ==================================================================================

    /**
     * Determina e traccia il miglior individuo tra la generazione corrente e il record storico.
     * @param individuals La lista degli individui della generazione corrente.
     * @param currentSolution Il miglior individuo trovato fino a quel momento (record globale).
     * @return L'individuo con la fitness più alta (il nuovo record globale, se trovato).
     */
    private Individual currentBestSolution(List<Individual> individuals, Individual currentSolution) {
        // Trova il migliore della generazione corrente (KING)
        Individual KING = individuals.getFirst();

        for (int i = 1; i < individuals.size(); i++) {
            if (KING.getFitness() < individuals.get(i).getFitness()) {
                KING = individuals.get(i);
            }
        }

        // Confronta con il record globale precedente.
        if (currentSolution == null) {
            return KING;
        }

        // Restituisce il migliore tra il KING e il record storico.
        if (KING.getFitness() > currentSolution.getFitness()) {
            return KING;
        } else {
            return currentSolution;
        }
    }
}

