package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.CommonEntityType;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntity;
import chevy.utils.Vector2;

public abstract class Enemy extends LiveEntity {
    private final Type type;
    protected boolean canAttack = false;

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