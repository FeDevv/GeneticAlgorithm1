package GA;

import model.Individual;
import model.Point;
import utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * crossover function
 * given a crossover probability P_c two individuals will
 * cross their genes.
 * */
public class Crossover {
    private final double CrossoverProbability;

    public Crossover(double CrossoverProbability) {
        this.CrossoverProbability = CrossoverProbability;
    }

    /**
     * effettua crossover uniforme tra due individui, generando un figlio
     * con materiale genetico di entrambi i genitori (se il crossover avviene).
     * se crossover non avviene, viene scelto randomicamente uno dei genitori.
     * @param I1 genitore
     * @param I2 genitore
     * @return a new child
     * */
    public Individual uniformCrossover(Individual I1, Individual I2) {
        int lenght = I1.getDimension();
        List<Point> childChromosomes = new ArrayList<Point>(lenght);

        if(RandomUtils.randDouble() < CrossoverProbability) {
            for (int i = 0; i < lenght; i++) {
                if (RandomUtils.CoinToss() == 0) {
                    childChromosomes.add(I1.getChromosomes().get(i));
                } else {
                    childChromosomes.add(I2.getChromosomes().get(i));
                }
            }
            return new Individual(childChromosomes);

        } else { //il caso in cui il randomDoble > crossoverProb
            if (RandomUtils.CoinToss() == 0) {
                return new Individual(I1.getChromosomes());
            } else {
                return new Individual(I2.getChromosomes());
            }
        }
    }
}
