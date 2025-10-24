package utils;

import java.util.*;

public class RandomUtils {

    public static List<Integer> UniqueIndices(int n, int maxExclusive) {
        Set<Integer> uniqueIndices = new HashSet<>();
        Random rng = new Random();

        //genera numeri fino a che il Set non ha k elementi
        while (uniqueIndices.size() < n) {
            uniqueIndices.add(rng.nextInt(maxExclusive));
        }

        return new ArrayList<>(uniqueIndices);
    }

}
