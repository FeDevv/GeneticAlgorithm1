package model.domains;

import java.util.List;
import java.util.Map;

public class DomainFactory {

    /**
     * Metodo pubblico per creare un'istanza di Domain.
     * È il punto d'ingresso del pattern Factory Method.
     * * @param type Il tipo di dominio da creare (es. CIRCLE, RECTANGLE).
     * @param params La mappa dei parametri di configurazione (es. {"raggio": 10.0}).
     * @return L'istanza concreta del Domain richiesto.
     * @throws IllegalArgumentException Se i parametri sono mancanti o il tipo non è supportato.
     */
    public Domain createDomain(DomainType type, Map<String, Double> params) {
        // 1. Validazione: Controlla che tutti i parametri richiesti dall'enum siano presenti.
        // Scelta Implementativa: La validazione preventiva impedisce l'instanziazione con valori nulli/mancanti,
        // evitando errori a runtime nei costruttori.
        validateParameters(type, params);

        // 2. Creazione (switch-case sull'enum)
        // Cuore del factory. L'uso dello switch expression è pulito e type-safe.
        return switch (type) {
            case CIRCLE ->
                // L'esistenza di "raggio" è garantita dal metodo validateParameters.
                    new CircleDomain(params.get("raggio"));
            case RECTANGLE ->
                // L'esistenza di "larghezza" e "altezza" è garantita dalla validazione.
                    new RectangularDomain(params.get("larghezza"), params.get("altezza"));
            default ->
                // Meccanismo di sicurezza: Cattura eventuali nuovi tipi non ancora gestiti nel factory.
                    throw new IllegalArgumentException("Tipo di dominio non supportato: " + type);
        };

    }

    /**
     * Metodo helper privato per validare i parametri.
     * Controlla che tutte le chiavi richieste dal DomainType siano presenti e non nulle nella mappa dei parametri.
     * * @param type Il tipo di dominio, usato per ottenere la lista dei parametri richiesti.
     * @param params La mappa dei parametri forniti dall'utente.
     * @throws IllegalArgumentException Se un parametro obbligatorio è mancante o nullo.
     */
    private void validateParameters(DomainType type, Map<String, Double> params) {
        // 1. Ottiene la lista dei nomi dei parametri obbligatori dall'enum (metadati).
        List<String> requiredKeys = type.getRequiredParameters();

        // 2. Itera sui requisiti e controlla la mappa fornita.
        for (String key : requiredKeys) {
            // Controlla sia la presenza della chiave che l'eventuale nullità del valore.
            if (!params.containsKey(key) || params.get(key) == null) {
                // Lancia un errore specifico per il debugging.
                throw new IllegalArgumentException(
                        "Parametro mancante per il dominio '" + type.getDisplayName() + "': " + key
                );
            }
        }
    }
}
