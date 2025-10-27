package GA;

import model.Individual;

/**
 * crossover function
 * given a crossover probability P_c two individuals will
 * cross their genes.
 * */
public class Crossover {
    private double CrossoverProb;

    public Crossover(double CrossoverProb) {
        this.CrossoverProb = CrossoverProb;
    }

    public Individual uniformCrossover(Individual I1, Individual I2) {

    }
}
