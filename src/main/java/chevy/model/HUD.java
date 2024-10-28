package chevy.model;

public class HUD {
    private int coins;
    private int keys;

    public void addCoin(int value) {coins += value;}

    public void addKey(int value) {keys += value;}

    public int getCoins() {return coins;}

    public int getKeys() {return keys;}
}
