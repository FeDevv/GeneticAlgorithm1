package GA;

import model.Individual;
import model.Point;
import utils.RandomUtils;

import java.awt.geom.Rectangle2D;
import java.util.List;

public class Mutation {
    private final double mutationProbability;
    private final double mutationStrength;

    public Mutation(double mutationProbability, double mutationStrength) {
        this.mutationProbability = mutationProbability;
        this.mutationStrength = mutationStrength;
    }

    public void mutate(Individual I) {

        //cicla su ogni cromosoma (per ogni punto)
        for (int i = 0; i < I.getDimension(); i++) {
            //if (true) fai avvenire la mutazione
            if (RandomUtils.randDouble() < mutationProbability) {
                Point oldPoint = I.getChromosomes().get(i);    //punto i dell'individuo, è stato scelto per essere mutato
                double radius = oldPoint.getRadius();

                double newX = oldPoint.getX() + (RandomUtils.randDouble() * 2 - 1) * mutationStrength;
                double newY = oldPoint.getY() + (RandomUtils.randDouble() * 2 - 1) * mutationStrength;

                Point newPoint = new Point(newX, newY, radius);
                I.getChromosomes().set(i, newPoint);
            }
        }
        // devo solo mutare un individuo, non devo ritornare nulla
        // in quanto sto mutando l'individuo stesso e non una sua copia
    }
}
