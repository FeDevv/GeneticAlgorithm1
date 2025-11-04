package model.domains;

import java.util.List;

public enum DomainType {

    // Le costanti dell'enum, ognuna configurata con il suo nome visualizzato
    // e la lista dei nomi dei parametri obbligatori per la sua creazione.
    CIRCLE("Cerchio", List.of("raggio")),
    RECTANGLE("Rettangolo", List.of("larghezza", "altezza"));

    // ------------------- ATTRIBUTI -------------------

    // Il nome amichevole usato per la visualizzazione (es. nelle interfacce utente).
    private final String displayName;

    // La lista dei nomi delle chiavi che devono essere fornite nella mappa dei parametri
    // alla DomainFactory per creare correttamente questo tipo di dominio.
    private final List<String> requiredParameters;

    // ------------------- COSTRUTTORE -------------------

    /**
     * Costruttore privato (implicito per gli enum).
     * Associa i metadati (displayName e requiredParameters) a ogni costante.
     */
    DomainType(String displayName, List<String> requiredParameters) {
        this.displayName = displayName;
        this.requiredParameters = requiredParameters;
    }

    // ------------------- GETTER PUBBLICI -------------------

    /**
     * Ritorna la lista dei nomi dei parametri richiesti per questo tipo di dominio.
     * @return Una lista immutabile di stringhe (i nomi delle chiavi).
     * * Scelta Implementativa: List.of() crea una lista immutabile, garantendo che i requisiti
     * non possano essere modificati esternamente.
     */
    public List<String> getRequiredParameters() {
        return this.requiredParameters;
    }

    /**
     * Ritorna il nome "amichevole" del dominio per la visualizzazione.
     * @return Il nome da mostrare (es. "Cerchio").
     */
    public String getDisplayName() {
        return this.displayName;
    }

    // ------------------- OVERRIDE -------------------

    /**
     * Metodo standard di Java, sovrascritto per mostrare
     * il nome corretto nelle interfacce utente o nei log, utilizzando il nome amichevole.
     */
    @Override
    public String toString() {
        return this.displayName;
    }
}
