package view;

public class EvolutionConsoleView {
    // --- MESSAGGIO DI INIZIALIZZAZIONE ---

    public static void displayStartMessage(int generations, int populationSize) {
        System.out.println("🧬 Avvio Algoritmo Genetico...");
        System.out.printf("   Configurazione: %d Generazioni | %d Individui.\n\n",
                generations, populationSize);
        System.out.println("Inizio Ciclo Evolutivo ...");
    }

    // --- MESSAGGI DI TENTATIVO / RIPROVA ---

    public static void displayRetryWarning(int currentAttempt, int maxAttempts, double lastTimeSecs) {
        System.out.println("⚠️  AVVISO: Soluzione non valida trovata.");
        System.out.printf("Tentativo #%d di %d\n", currentAttempt, maxAttempts);
        System.out.printf("Tempo stimato per il prossimo tentativo: ~%.2f secondi.\n", lastTimeSecs);
    }

    // --- MESSAGGI DI RISULTATO ---

    public static void displaySuccess(int attempt, double timeSecs) {
        System.out.println("\n✅ Successo! Trovata soluzione valida al tentativo #" + attempt + ".");
        System.out.printf("Tempo di esecuzione: %.2f secondi.\n\n", timeSecs);
    }

    /* Teoricamente lascio la visualizzazione del risultato ad altre classi, non al controller.
    *   public static void displayFinalSolution(double fitness, String details) {
    *       System.out.printf("Fitness Finale: %.6f\n", fitness);
    *       System.out.println("Dettagli Soluzione: " + details);
    *   }
    */


    // --- MESSAGGI DI ERRORE ---

    public static void displayCriticalFailure(int maxAttempts, double lastFitness, double totalTimeSecs) {
        System.err.println("\n--- 🛑 FALLIMENTO CRITICO ---");
        System.err.printf("Impossibile trovare una soluzione valida dopo %d cicli evolutivi completi (totale %.2f secondi di calcolo).\n",
                maxAttempts, totalTimeSecs);
        System.err.printf("Fitness del miglior individuo non valido trovato: %.6f\n", lastFitness);
        System.err.println("Suggerimento: Aumentare il numero di generazioni o allentare i vincoli.");
    }
}
