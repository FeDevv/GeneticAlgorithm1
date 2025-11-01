package utils;

import model.Point;

import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

    private static final Random random = new Random();

    public static List<Integer> UniqueIndices(int n, int maxExclusive) {
        Set<Integer> uniqueIndices = new HashSet<>();

        //genera numeri fino a che il Set non ha k elementi
        while (uniqueIndices.size() < n) {
            uniqueIndices.add(random.nextInt(maxExclusive));
        }

        return new ArrayList<>(uniqueIndices);
    }

    public static int CoinToss() {
        return random.nextInt(2);
    }

    public static double randDouble() {
        return random.nextDouble();
        // il numero generato è in [0.0 , 0.9999...] ossia tra [0.0 , 1)
    }

    //genera i punti nel bounding box, per la prima generazione di individui
    public static Point insideBoxGenerator(Rectangle2D boundingBox) {
        double randomXfactor = ThreadLocalRandom.current().nextDouble();    //double tra 0.0 e 1.0 escluso
        double randomYfactor = ThreadLocalRandom.current().nextDouble();

        double x = boundingBox.getMinX() + (randomXfactor * boundingBox.getWidth());
        double y = boundingBox.getMinY() + (randomYfactor * boundingBox.getHeight());

        return new Point(x,y);
    }
}
