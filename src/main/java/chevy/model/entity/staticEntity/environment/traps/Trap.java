package chevy.model.entity.staticEntity.environment.traps;

import chevy.model.entity.CommonEntityType;
import chevy.model.entity.staticEntity.environment.Environment;
import chevy.utils.Vector2;

public abstract class Trap extends Environment {
    protected boolean canHitFlingEntity;
    public enum Type implements CommonEntityType {
        SLUDGE,
        VOID,
        SPIKED_FLOOR,
        TRAPDOOR,
        TOTEM,
        ICY_FLOOR;
    }
    private final Type type;


    public Trap(Vector2<Integer> initVelocity, Type type) {
        super(initVelocity, Environment.Type.TRAP);
        this.type = type;
        this.crossable = true;
        this.canHitFlingEntity = false;
    }


    @Override
    public CommonEntityType getSpecificType() {
        return type;
    }

    @Override
    public CommonEntityType getGenericType() {
        return super.getSpecificType();
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
