package helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomHelper {

    public static <T> List<Integer> getRandomIndexes(List<T> list, int count) {
        if (count > list.size()) {
            throw new IllegalArgumentException("The number of requested elements is greater than the list size.");
        }

        int size = list.size() - 1;

        List<Integer> ints = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ints.add(i);
        }

        List<Integer> shuffled = new ArrayList<>(ints);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, count);
    }

    public static int getRandomIntNumber(int minValue, int maxValue) {
        return new Random()
                .ints(minValue, maxValue)
                .findFirst()
                .orElse(-1);
    }
}
