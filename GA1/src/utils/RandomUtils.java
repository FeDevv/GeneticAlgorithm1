package utils;

import java.util.*;

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
}
