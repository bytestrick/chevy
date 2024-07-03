package chevy.utils;

public class Utils {
    /**
     * @param value valore sa "avvolgere"
     * @param min   valore minimo di ritorno
     * @param max   valore massimo di ritorno
     * @return un valore equivalente a value, ma "avvolto" all'interno dell'intervallo [min, max].
     */
    public static int wrap(int value, int min, int max) {
        int range = max - min + 1; // numero di valori nel range
        if (value < min) // se il valore è più piccolo del minimo riporta il valore nell'intervallo
            return max - ((min - value - 1) % range);
        else if (value > max) // se il valore è più grande del massimo riporta il valore nell'intervallo
            return min + ((value - max - 1) % range);
        return value; // il valore è già nell'intervallo
    }
}