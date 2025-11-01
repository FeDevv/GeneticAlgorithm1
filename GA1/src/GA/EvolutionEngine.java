package GA;

import model.Individual;
import model.Point;

import java.util.List;

/**
 * Questa classe si occupa di coordinare le varie parti dell'algoritmo genetico
 * */
public class EvolutionEngine { }

/*

    public List<Individual> reproduce() {
        List<Individual> newPopulation = new ArrayList<>(oldPopulation.size());

        //seleziona i migliori, da ricopiare nella prossima generazione
        List<Individual> elites = selectElites();
        //inseriscili direttamente nella nuova generazione
        newPopulation.addAll(elites);

        //numero di posti da riempire per avere nella nuova popolazione lo stesso numero di individuo della vecchia generazione
        int commonsNumber = oldPopulation.size() - elites.size();

        //ciclo per creare i nuovi individui
        for (int i = 0; i < commonsNumber; i++) {
            Individual dad = tournament();
            Individual mom = tournament();

            //scegli mom e dad diversi
            while (dad == mom) {
                mom = tournament();
            }

            //TODO crossover
            //TODO mutazione
            //TODO aggiungi l'individuo nella nuova generazione
        }

        return newPopulation;
    }


*/
