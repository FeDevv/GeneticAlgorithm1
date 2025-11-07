package controllers;

import service.Crossover;
import service.FitnessCalculator;
import service.Mutation;
import service.Selection;
import exceptions.MaxAttemptsExceededException;
import model.Individual;
import model.Point;
import model.domains.Domain;
import utils.RandomUtils;
import view.DomainConsoleView;
import view.EvolutionConsoleView;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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
    private final EvolutionConsoleView view;

    // ==================================================================================
    // 🔨 COSTRUTTORE
    // ==================================================================================

    /**
     * Costruttore completo per configurare tutti i parametri dell'algoritmo genetico.
     */
    public EvolutionEngine(EvolutionConsoleView view, Domain domain, int populationSize, int individualSize, int generations, int tournamentSize, double elitesPercentage,
                           double crossoverProbability, double mutationProbability, double initialMutationStrenght, double radius) {
        // Inizializza tutti gli attributi finali di configurazione.
        this.view = view;
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
        this.gammaRays = new Mutation(mutationProbability, initialMutationStrenght, domain, generations);
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
    private Individual runEvolutionCore() {
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

            final List<Individual> currentGeneration = oldGeneration;
            List<Individual> newGeneration = new ArrayList<>(populationSize);

            // 1. Elitismo: seleziona i migliori della generazione precedente.
            List<Individual> elites = selector.selectElites(oldGeneration);
            newGeneration.addAll(elites);

            // 2. Crossover e Mutazione: riempie il resto della popolazione.
            int childrenToGenerate = populationSize - elites.size();

            // Genera i figli in parallelo e raccoglili in una lista temporanea
            final int currentGenerationAge = i;
            List<Individual> children = IntStream.range(0, childrenToGenerate)
                .parallel() // L'operazione chiave per eseguire i passi successivi in parallelo
                .mapToObj(j -> {
                    // --- Operazioni di creazione del singolo figlio (Eseguite in parallelo su core diversi) ---

                    // a. Selezione di genitori distinti (Nota: il while non è efficiente in AG ma è thread-safe)
                    Individual dad = selector.tournament(currentGeneration);
                    Individual mom = selector.tournament(currentGeneration);
                    while (mom == dad) {
                        mom = selector.tournament(currentGeneration);
                    }

                    // b. Crossover
                    Individual child = mixer.uniformCrossover(mom, dad);

                    // c. Mutazione
                    gammaRays.mutate(child, currentGenerationAge);

                    // d. Calcolo Fitness (uso di fitnessCalculator.getFitness() thread-safe)
                    child.setFitness(fitnessCalculator.getFitness(child));

                    // Ritorna l'oggetto creato
                    return child;
                })
                // La funzione collect() si occupa di raccogliere in modo thread-safe tutti i risultati
                .toList(); //genera lista immutabile

            // Aggiungi tutti i figli generati in parallelo alla newGeneration
            newGeneration.addAll(children);

            // 3. Aggiornamento: Verifica il record globale (Elitismo Globale).
            solution = currentBestSolution(newGeneration, solution);

            // La nuova generazione diventa la base per la prossima iterazione.
            oldGeneration = newGeneration;
        }

        // Restituisce una copia profonda per garantire che il risultato finale sia immutabile per l'utente.
        return solution.copy();
    }

    public Individual runEvolutionEngine() {
        final int MAX_RETRY_ATTEMPTS = 3;
        int currentAttempt = 0;
        Individual lastAttemptSolution = null;
        double lastExecutionTimeMs = 0;
        double totalExecutionTimeMs = 0;

        view.displayStartMessage(generations, populationSize);

        do {
            Instant startTime = Instant.now();

            // aggiorna counter
            currentAttempt++;

            // 1. esecuzione del core
            lastAttemptSolution = runEvolutionCore();

            Instant endTime = Instant.now();
            lastExecutionTimeMs = Duration.between(startTime,endTime).toMillis();
            // se la soluzione viene trovata (e.g.) al secondo giro, devo indicare la somma dei tempi dei 2 giri come tempo di esecuzione
            totalExecutionTimeMs += lastExecutionTimeMs;

            // 2. verifica di validità
            if (domain.isValidIndividual(lastAttemptSolution)) {
                // mostra che una soluzione corretta è stata provata
                view.displaySuccess(currentAttempt, totalExecutionTimeMs / 1000);
                return  lastAttemptSolution.copy();
            }

            // individuo non valido, mostralo all'utente
            view.displayRetryWarning(currentAttempt, MAX_RETRY_ATTEMPTS, lastExecutionTimeMs / 1000);

        } while (currentAttempt < MAX_RETRY_ATTEMPTS);

        // 4. FALLIMENTO controllato, l'algoritmo non è riuscito a trovare una soluzione.

        view.displayCriticalFailure(MAX_RETRY_ATTEMPTS, lastAttemptSolution.getFitness(), totalExecutionTimeMs / 1000);

        throw new MaxAttemptsExceededException(
                String.format(
                "L'algoritmo genetico ha fallito dopo %d tentativi (%.2f s totali). " +
                        "Ultima fitness: %.4f. Prova a modificare i parametri.",
                MAX_RETRY_ATTEMPTS,
                totalExecutionTimeMs / 1000.0,
                lastAttemptSolution.getFitness()
        ));
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

