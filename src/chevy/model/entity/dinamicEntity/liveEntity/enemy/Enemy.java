package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.EntityCommonEnumTypes;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntity;
import chevy.utils.Vector2;

public abstract class Enemy extends LiveEntity {
    private final Type type;
    protected boolean canAttack = false;

    public Enemy(Vector2<Integer> initPosition, Type type) {
        super(initPosition, LiveEntity.Type.ENEMY);
        this.type = type;
        this.layer = 2;
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

    @Override
    public EntityCommonEnumTypes getSpecificType() { return type; }

    @Override
    public EntityCommonEnumTypes getGenericType() { return super.getSpecificType(); }

    @Override
    public String toString() {
        return type.toString();
    }

    public enum Type implements EntityCommonEnumTypes {
        WRAITH, ZOMBIE, SKELETON, SLIME, BIG_SLIME, BEETLE;
    }
}