package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.CommonEntityType;
import chevy.model.entity.staticEntity.environment.Environment;
import chevy.utils.Vector2;

public abstract class Trap extends Environment {
    private final Type type;
    protected boolean canHitFlingEntity;

    public Trap(Vector2<Integer> initVelocity, Type type) {
        super(initVelocity, Environment.Type.TRAP);
        this.type = type;
        this.crossable = true;
        this.canHitFlingEntity = false;
    }

    @Override
    public CommonEntityType getSpecificType() {return type;}

    @Override
    public CommonEntityType getGenericType() {return super.getSpecificType();}

    @Override
    public String toString() {return type.toString();}

    public enum Type implements CommonEntityType {
        SLUDGE, VOID, SPIKED_FLOOR, TRAPDOOR, TOTEM,
        ICY_FLOOR
    }
}
