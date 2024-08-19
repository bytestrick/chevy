package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.CommonEntityType;
import chevy.model.entity.collectable.Collectable;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntity;
import chevy.utils.Utils;
import chevy.utils.Vector2;

import java.util.HashMap;
import java.util.Map;

public abstract class Enemy extends LiveEntity {
    /**
     * Oggetti collezionabili che i nemici possono rilasciare
     */
    private static final Collectable.Type[] DROPPABLE_COLLECTABLE = {
            Collectable.Type.COIN,
            Collectable.Type.KEY,
            Collectable.Type.HEALTH
    };
    /**
     * Probabilità di rilascio degli oggetti collezionabili
     */
    private static final int[] DROPPABLE_COLLECTABLE_PROB = {
            40,
            10,
            15
    };
    private final Type type;
    protected boolean canAttack = false;
    protected int dropProbability = 50;

    public Enemy(Vector2<Integer> initPosition, Type type) {
        super(initPosition, LiveEntity.Type.ENEMY);
        this.type = type;

        this.drawLayer = 4;
    }

    /**
     * @return ritorna true se il player è in grado di attaccare false altrimenti
     */
    public boolean canAttack() {
        return canAttack;
    }

    public void setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }

    public boolean canDrop() {
        return Utils.isOccurring(dropProbability);
    }

    public Collectable.Type getDrop() {
        int index = Utils.isOccurring(DROPPABLE_COLLECTABLE_PROB);
        if (index == -1)
            return null;
        return DROPPABLE_COLLECTABLE[index];
    }

    @Override
    public CommonEntityType getSpecificType() { return type; }

    @Override
    public CommonEntityType getGenericType() { return super.getSpecificType(); }

    @Override
    public String toString() {
        return type.toString();
    }

    public enum Type implements CommonEntityType {
        WRAITH, ZOMBIE, SKELETON, SLIME, BIG_SLIME, BEETLE;
    }
}