package model.domains;

import java.util.List;

public enum DomainType {
    CIRCLE("Cerchio", List.of("raggio")),
    RECTANGLE("Rettangolo", List.of("larghezza", "altezza"));

    private final String displayName;
    private final List<String> requiredParameters;

    // Costruttore privato (automatico per gli enum)
    // associa i metadati a ogni costante
    DomainType(String displayName, List<String> requiredParameters) {
        this.displayName = displayName;
        this.requiredParameters = requiredParameters;
    }

    // getters publici

    /**
     * Ritorna la lista dei nomi dei parametri richiesti
     * per questo tipo di dominio (es. ["larghezza", "altezza"]).
     * @return Una lista immutabile di stringhe.
     */
    public List<String> getRequiredParameters() {
        return this.requiredParameters;
    }

    /**
     * Ritorna il nome "amichevole" da mostrare nella UI.
     * @return Il nome da mostrare (es. "Corona Circolare").
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * Metodo standard di Java, sovrascritto per mostrare
     * il nome corretto nei componenti della UI (es. JComboBox).
     */
    @Override
    public String toString() {
        return this.displayName;
    }
}
