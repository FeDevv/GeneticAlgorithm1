package model.domains;

import model.domains.types.CircleDomain;
import model.domains.types.RectangularDomain;

import java.util.List;
import java.util.Map;

/**
 * Implementazione concreta del <b>Factory Method Pattern</b>.
 * <p>
 * Questa classe è responsabile esclusiva della creazione e della validazione preliminare
 * degli oggetti {@code Domain} concreti (es. {@code CircleDomain}, {@code RectangularDomain}).
 * Utilizza {@code DomainType} (i metadati) per sapere quali parametri sono necessari
 * e quale oggetto istanziare.
 * <p>
 * Aderisce al Single Responsibility Principle (SRP): il suo unico compito è la creazione.
 */
public class DomainFactory {

    /**
     * Metodo pubblico per creare un'istanza di Domain.
     * È il punto d'ingresso del pattern Factory Method: delega la logica
     * di istanziazione specifica a un blocco {@code switch}.
     *
     * @param type Il tipo di dominio da creare, fornito tramite l'enum {@code DomainType}.
     * @param params La mappa dei parametri di configurazione, dove la chiave è il nome del parametro
     * e il valore è il dato numerico (es. {"radius": 10.0}).
     * @return L'istanza concreta del {@code Domain} richiesto, trattata come interfaccia {@code Domain}.
     * @throws IllegalArgumentException Se i parametri sono mancanti/nulli o se il {@code DomainType}
     * non è supportato da questa factory.
     */
    public Domain createDomain(DomainType type, Map<String, Double> params) {

        // 1. Validazione: Controlla che tutti i parametri richiesti dall'enum siano presenti.
        // @implNote La validazione preventiva impedisce l'instanziazione con valori nulli/mancanti,
        // evitando potenziali eccezioni a runtime nei costruttori dei domini.
        validateParameters(type, params);

        // 2. Creazione (switch-case sull'enum)
        // Cuore del factory. L'uso dello switch expression è pulito e type-safe.
        return switch (type) {
            case CIRCLE ->
                    // L'esistenza della chiave "raggio" è garantita dal metodo validateParameters.
                    // Si assume che la validazione del valore (raggio > 0) sia gestita all'interno del costruttore di CircleDomain.
                    new CircleDomain(params.get("radius"));
            case RECTANGLE ->
                    // L'esistenza delle chiavi "larghezza" e "altezza" è garantita dalla validazione.
                    new RectangularDomain(params.get("width"), params.get("height"));
            default ->
                // Meccanismo di sicurezza: Cattura eventuali nuovi tipi non ancora gestiti nel factory.
                    throw new IllegalArgumentException("Domain type not supported " + type);
        };

    }

    /**
     * Metodo helper privato responsabile della validazione della presenza e della non-nullità dei parametri.
     *
     * @param type Il tipo di dominio, usato per ottenere la lista dei nomi dei parametri obbligatori (metadati).
     * @param params La mappa dei parametri forniti dal codice client/utente.
     * @throws IllegalArgumentException Se un parametro obbligatorio è mancante o il suo valore è {@code null}.
     */
    private void validateParameters(DomainType type, Map<String, Double> params) {

        // 1. Ottiene la lista dei nomi dei parametri obbligatori dall'enum (metadati).
        List<String> requiredKeys = type.getRequiredParameters();

        // 2. Itera sui requisiti e controlla la mappa fornita.
        for (String key : requiredKeys) {
            // Controlla sia la presenza della chiave che l'eventuale nullità del valore.
            if (!params.containsKey(key) || params.get(key) == null) {
                // Lancia un errore specifico e descrittivo
                throw new IllegalArgumentException(
                        "Missing parameter for the domain '" + type.getDisplayName() + "': " + key
                );
            }
        }
        // Nota: la validazione del valore (es. raggio > 0) è delegata al costruttore del Domain specifico
    }
}
