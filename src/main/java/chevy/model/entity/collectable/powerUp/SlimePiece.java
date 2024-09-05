package chevy.model.entity.collectable.powerUp;

import chevy.utils.Vector2;

public final class SlimePiece extends PowerUp {
    private final int nSlime = 1;
    private final int damageIncrease = 4;

    public SlimePiece(Vector2<Integer> initVelocity) {
        super(initVelocity, Type.SLIME_PIECE);

        this.occurringPercentage = 100;
        this.name = "Pezzo di Slime\n";
        this.description = "Il danno d'attacco aumenta\ndi " + damageIncrease + ", ma dopo la morte i " + "nemici" +
                "\ngenerano " + nSlime + " piccoli slime.\nI piccoli slime non generano\naltri slime";
    }

    public int getNSlime() {
        return nSlime;
    }

    public int getDamageIncrease() {
        return damageIncrease;
    }
}
