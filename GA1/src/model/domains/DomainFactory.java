package model.domains;

import java.util.List;
import java.util.Map;

/**
 * Factory per la creazione di istanze di 'Domain'.
 * Utilizza l'enum 'DomainType' per decidere quale classe concreta
 * istanziare e valida che tutti i parametri necessari siano presenti.
 */
public class DomainFactory {

    public Domain createDomain(DomainType type, Map<String, Double> params) {
        // 1. validazione: controlla se la mappa "params"
        //    contiene tutte le chiavi che l'enum richiede
        validateParameters(type, params);

        // 2. creazione (switch-case sull'enum)
        //    Cuore del factory.
        //    Type-safe, pulito e veloce
        return switch (type) {
            case CIRCLE ->
                //sappiamo che raggio esiste grazie alla validazione
                    new CircleDomain(params.get("raggio"));
            case RECTANGLE ->
                //sappiamo che altezza e larghezza esistono
                    new RectangularDomain(params.get("larghezza"), params.get("altezza"));
            default -> throw new IllegalArgumentException("Tipo di dominio non supportato: " + type);
        };

    }

    /**
     * Metodo helper privato per validare i parametri.
     * Controlla che tutte le chiavi richieste dall'enum
     * siano presenti nella mappa dei parametri.
     *
     * @param type Il tipo di dominio, usato per ottenere la lista dei parametri.
     * @param params La mappa dei parametri da controllare.
     */
    private void validateParameters(DomainType type, Map<String, Double> params) {
        // 1. ottiene la lista dei nomi richiesti dall'enum
        List<String> requiredKeys = type.getRequiredParameters();

        // 2. itera sulla lista e controlla la mappa
        for (String key : requiredKeys) {
            if (!params.containsKey(key) || params.get(key) == null) {
                // lancio un errore specifico se un parametro manca
                throw new IllegalArgumentException(
                        "Parametro mancante per il dominio '" + type.getDisplayName() + "': " + key
                );
            }
        }
    }
}
