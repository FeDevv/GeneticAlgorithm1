import controllers.DomainController;
import controllers.EvolutionEngine;
import model.Individual;
import model.domains.Domain;
import view.DomainConsoleView;
import view.EvolutionConsoleView;


import java.util.*;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        /*
        System.out.println("--- Configurazione Algoritmo Genetico (Dominio Circolare) ---");

        // === 1. Raccolta Parametri Dominio ===

        double radius = 0;
        while (radius <= 0) {
            System.out.print("Inserisci il RAGGIO del Dominio Circolare (> 0): ");
            if (scanner.hasNextDouble()) {
                radius = scanner.nextDouble();
            } else {
                System.out.println("Input non valido. Riprova.");
                scanner.next(); // Consuma l'input non valido
            }
        }

        double pointRadius = 0;
        while (pointRadius <= 0 || pointRadius > radius) { // Il raggio del punto non può superare il raggio del dominio
            System.out.printf("Inserisci il RAGGIO (r) di ogni Punto/Oggetto (> 0 e <= %.2f): ", radius);
            if (scanner.hasNextDouble()) {
                pointRadius = scanner.nextDouble();
                if (pointRadius > radius) {
                    System.out.println("Il raggio del punto non può essere maggiore del raggio del dominio. Riprova.");
                }
            } else {
                System.out.println("Input non valido. Riprova.");
                scanner.next();
            }
        }

        // === 2. Raccolta Parametri AG Essenziali ===

        int individualSize = 0;
        while (individualSize <= 0) {
            System.out.print("Inserisci la DIMENSIONE INDIVIDUALE (numero di Punti, > 0): ");
            if (scanner.hasNextInt()) {
                individualSize = scanner.nextInt();
            } else {
                System.out.println("Input non valido. Riprova.");
                scanner.next();
            }
        }

        int generations = 0;
        while (generations <= 0) {
            System.out.print("Inserisci il NUMERO DI GENERAZIONI da eseguire (> 0): ");
            if (scanner.hasNextInt()) {
                generations = scanner.nextInt();
            } else {
                System.out.println("Input non valido. Riprova.");
                scanner.next();
            }
        }

        scanner.close(); // Chiude lo scanner dopo aver finito di leggere l'input

        // === 3. Creazione del Dominio (Usando la Factory) ===
        Map<String, Double> domainParams = new HashMap<>();
        domainParams.put("raggio", radius);

        Domain problemDomain;
        try {
            DomainFactory factory = new DomainFactory();
            problemDomain = factory.createDomain(DomainType.CIRCLE, domainParams);
            System.out.printf("\nDominio configurato: Cerchio (Raggio: %.2f)\n", radius);
        } catch (IllegalArgumentException e) {
            System.err.println("Errore fatale nella configurazione del dominio: " + e.getMessage());
            return;
        }
        */
        DomainConsoleView domainConsoleView = new DomainConsoleView(scanner);


        DomainController controller = new DomainController(domainConsoleView);
        Domain problemDomain;

        Optional<Domain> domainOptional = controller.createDomain();
        if (domainOptional.isPresent()) {
            problemDomain = domainOptional.get();
        } else {
            System.out.println("Creazione del dominio annullata o fallita.");
            return;
        }

        int individualSize = 0;
        while (individualSize <= 0) {
            System.out.print("Inserisci la DIMENSIONE INDIVIDUALE (numero di Punti, > 0): ");
            if (scanner.hasNextInt()) {
                individualSize = scanner.nextInt();
            } else {
                System.out.println("Input non valido. Riprova.");
                scanner.next();
            }
        }

        double pointRadius = 0;
        while (pointRadius <= 0 ) { // Il raggio del punto non può superare il raggio del dominio
            System.out.print("Inserisci il RAGGIO (r) di ogni Punto/Oggetto (> 0 e <= %.2f): ");
            if (scanner.hasNextDouble()) {
                pointRadius = scanner.nextDouble();
            } else {
                System.out.println("Input non valido. Riprova.");
                scanner.next();
            }
        }

        int generations = 0;
        while (generations <= 0) {
            System.out.print("Inserisci il NUMERO DI GENERAZIONI da eseguire (> 0): ");
            if (scanner.hasNextInt()) {
                generations = scanner.nextInt();
            } else {
                System.out.println("Input non valido. Riprova.");
                scanner.next();
            }
        }

        scanner.close(); // Chiude lo scanner dopo aver finito di leggere l'input


        // === 4. Configurazione dei Parametri AG Standard ===

        // Mantieni questi fissi per semplicità nel test iniziale
        int POPULATION_SIZE = 100;
        int TOURNAMENT_SIZE = 3;
        double ELITES_PERCENTAGE = 0.05;
        double CROSSOVER_PROB = 0.9;
        double MUTATION_PROB = 0.02;
        double MUTATION_STRENGTH = 1.0;

        // === 5. Creazione e Avvio del Motore ===

        EvolutionEngine engine = new EvolutionEngine(
                new EvolutionConsoleView(),
                problemDomain,
                POPULATION_SIZE,
                individualSize, // Valore inserito dall'utente
                generations,    // Valore inserito dall'utente
                TOURNAMENT_SIZE,
                ELITES_PERCENTAGE,
                CROSSOVER_PROB,
                MUTATION_PROB,
                MUTATION_STRENGTH,
                pointRadius
        );

        Individual bestSolution = engine.runEvolutionEngine();

        // === 6. Output dei Risultati ===

        //System.out.printf("Generazioni completate: %d\n", generations);
        System.out.printf("Fitness della soluzione migliore: %.6f\n", bestSolution.getFitness());
        System.out.println(bestSolution);
    }
}