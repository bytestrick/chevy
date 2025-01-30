package chevy.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

public final class Utils {
    /**
     * Global unique random number generator
     */
    public static final RandomGenerator random = new Random();

    /**
     * This function takes a value and a range defined by min and max, and returns an equivalent
     * value within that range. If the provided value is outside the range, the function wraps it to
     * bring it back within the range.
     *
     * @param value value to wrap
     * @param min   minimum value of the range
     * @param max   maximum value of the range
     */
    public static int wrap(int value, int min, int max) {
        int range = max - min + 1; // number of values in the range
        if (value < min) { // if the value is smaller than the minimum bring it back in the range
            return max - ((min - value - 1) % range);
        } else if (value > max) { // if the value is bigger than the maximum bring it back in the range
            return min + ((value - max - 1) % range);
        }
        return value; // the value may already be in the range
    }

    public static boolean isOccurring(int occurringPercentage) {
        return random.nextInt(1, 101) <= Math.clamp(occurringPercentage, 0, 100);
    }

    /**
     * Given an array of values from 0 to 100, the function extracts an index of the array with the
     * inserted probabilities. If there are more indices, it chooses one at random.
     * <p>
     * <br/>
     * EXAMPLE:
     * If you use the array [10, 50] you will have that:
     *     <ul>
     *         <li>the index 0 has 10% probability to get extracted</li>
     *         <li>the index 1 has 50% probability to get extracted</li>
     *     </ul>
     * <p/>
     */
    public static int isOccurring(int[] occurringPercentages) {
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < occurringPercentages.length; i++) {
            if (random.nextInt(1, 101) <= occurringPercentages[i]) {
                result.add(i);
            }
        }

        if (result.isEmpty()) {
            return -1;
        }
        return result.get(random.nextInt(result.size()));
    }

    /**
     * @param ms time in milliseconds
     * @return formatted string with this format: {@code "%d h, %d min, %d s"}
     */
    public static String msToString(final int ms) {
        int s = ms / 1000 % 60;
        int min = ms / (1000 * 60) % 60;
        int h = ms / (1000 * 60 * 60) % 24;
        return String.format("%d h, %d min, %d s", h, min, s);
    }
}
