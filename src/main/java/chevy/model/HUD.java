package chevy.model;

public class HUD {
    private int coin = 0;
    private int key = 0;

    public HUD() {
        // funzione che carica da file i valori salvati di chiavi e monete
    }

    public void addCoin(int value) {
        coin += value;
    }

    public void addKey(int value) {
        key += value;
    }

    public int getCoin() {
        return coin;
    }

    public int getKey() {
        return key;
    }
}
