package chevy.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {
    public static final Random random = new Random();

    /**
     * Questa funzione prende un valore e un intervallo definito da min e max, e restituisce un valore equivalente
     * all’interno di quell’intervallo. Se il valore fornito è al di fuori dell’intervallo, la funzione lo “avvolge”
     * per riportarlo all’interno dell’intervallo.
     *
     * @param value valore da "avvolgere"
     * @param min   valore minimo di ritorno
     * @param max   valore massimo di ritorno
     */
    public static int wrap(int value, int min, int max) {
        int range = max - min + 1; // numero di valori nel range
        if (value < min) { // se il valore è più piccolo del minimo riporta il valore nell'intervallo
            return max - ((min - value - 1) % range);
        } else if (value > max) { // se il valore è più grande del massimo riporta il valore nell'intervallo
            return min + ((value - max - 1) % range);
        }
        return value; // il valore è già nell'intervallo
    }

    public static boolean isOccurring(int occurringPercentage) {
        return random.nextInt(1, 101) <= Math.clamp(occurringPercentage, 0, 100);
    }

    /**
     * Dato un array di valori da 0 a 100 la funzione estrae, con le probabilità inserite, un indice dell'array.
     * Se sono presenti più indici ne sceglie uno a caso.
     * <p>
     * <br/>
     * ESEMPIO:
     * Se si usa l'array [10, 50] si avrà che:
     *     <ul>
     *         <li> l'indice 0 ha il 10% di possibilità di essere estratto </li>
     *         <li> l'indice 1 ha il 50% di possibilità di essere estratto </li>
     *     </ul>
     * p/>
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
}
