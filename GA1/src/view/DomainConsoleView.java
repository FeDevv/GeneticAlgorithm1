package view;

import model.domains.DomainType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Implementazione concreta della View per l'interfaccia a riga di comando (CLI).
 * <p>
 * Questa classe è responsabile esclusiva di tutta la logica di I/O (System.out e Scanner)
 * e di contenere tutte le stringhe di output visibili all'utente.
 */
public class DomainConsoleView {

    private final Scanner scanner;

    /**
     * Costruttore che inietta l'oggetto Scanner per l'input.
     * @param scanner L'oggetto {@code Scanner} utilizzato per leggere l'input utente.
     */
    public DomainConsoleView(Scanner scanner) {
        this.scanner = scanner;
    }


    //Visualizza un messaggio di successo dopo la creazione di un dominio.
    public void showSuccess() {
        System.out.println("\n✅ Domain successfully created!");
    }

    /**
     * Visualizza un messaggio di errore.
     * @param msg Il messaggio di errore da mostrare.
     */
    public void showError(String msg) {
        System.err.println("\n❌ Creation or validation error: " + msg);
    }

    /**
     * Visualizza il menu dei tipi di dominio e legge la scelta numerica dell'utente.
     * Gestisce e scarta gli input non numerici.
     *
     * @return L'ID numerico selezionato dall'utente (0 per uscire).
     */
    public int readMenuChoice() {
        System.out.print("Enter your choice: ");
        while (!scanner.hasNextInt()) {
            System.err.println("Invalid input. Please enter an integer");
            scanner.next(); // scarta input errato
            System.out.print(">> ");
        }
        return scanner.nextInt();
    }

    /**
     * Legge sequenzialmente i valori per i parametri richiesti, gestendo gli input non numerici.
     *
     * @param keys La lista dei nomi dei parametri da richiedere (chiavi).
     * @param domainName Il nome amichevole del dominio per l'output.
     * @return Una {@code Map<String, Double>} contenente i parametri raccolti.
     */
    public Map<String, Double> readParameters(List<String> keys, String domainName) {
        Map<String, Double> params = new HashMap<>();
        System.out.println("\n--- Parameter Entry for " + domainName + " ---");

        for (String key : keys) {
            while (true) {
                System.out.print("Enter the value for '" + key + "': ");
                if (scanner.hasNextDouble()) {
                    params.put(key, scanner.nextDouble());
                    break;
                } else {
                    System.err.println("Non-numeric value. Retry.");
                    scanner.next();
                }
            }
        }
        return params;
    }

    /**
     * Visualizza il menu dei tipi di dominio disponibili.
     * @param types La lista di {@code DomainType} da mostrare.
     */
    public void showMenu(List<DomainType> types) {
        System.out.println("\n--- Select domain type ---");
        for (DomainType type : types) {
            System.out.println(type.getMenuId() + ") " + type.getDisplayName());
        }
        System.out.println("0) Quit");
    }

}
