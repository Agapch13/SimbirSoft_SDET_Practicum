package helpers;

import java.util.*;

public class RandomHelper {

    public static  <T> List<T> getRandomElements(Set<T> list, int count) {
        if (list == null || list.isEmpty()) {
            throw new IllegalStateException("List is null or empty");
        }
        if (list.size() < count) {
            count = list.size();
        }
        List<T> randomList = new ArrayList<>(list);
        Collections.shuffle(randomList);
        return randomList.subList(0, count);
    }

    public static int getRandomIntNumberBetween(int minValue, int maxValue) {
        return new Random()
                .ints(minValue, maxValue)
                .findFirst()
                .orElse(-1);
    }

    public static <T> T getRandomElement(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalStateException("List is null or empty");
        }
        return list.get(new Random().nextInt(list.size()));
    }
}
