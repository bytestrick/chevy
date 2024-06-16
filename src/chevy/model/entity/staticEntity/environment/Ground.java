package chevy.model.entity.staticEntity.environment;

import chevy.model.entity.EntityCommonEnumTypes;
import chevy.utilz.Vector2;

public class Ground extends Environment {
    public enum GroundTypes implements EntityCommonEnumTypes {
        TOP,
        INTERIOR_CORNER_TOP_LEFT,
        INTERIOR_CORNER_TOP_RIGHT,
        LEFT,
        CENTRAL,
        CENTRAL_PATTERNED,
        CENTRAL_PATTERNED_2,
        CENTRAL_BROKEN,
        CENTRAL_BROKEN_2,
        CENTRAL_BROKEN_3,
        RIGHT,
        EXTERNAL_CORNER_BOTTOM_LEFT,
        EXTERNAL_CORNER_BOTTOM_RIGHT,
        EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_BOTTOM,
        EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_RIGHT,
        EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_LEFT,
        EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_BOTTOM,
        EXTERNAL_CORNER_TOP_LEFT_SIDE_LEFT,
        EXTERNAL_CORNER_TOP_RIGHT_SIDE_RIGHT
    }
    private final GroundTypes type;


    public Ground(Vector2<Integer> initVelocity, GroundTypes type) {
        super(initVelocity, Type.GROUND);
        this.type = type;
        this.crossable = true;

        this.layer = 0;
    }


    @Override
    public GroundTypes getSpecificType() {
        return type;
    }

    @Override
    public EntityCommonEnumTypes getGenericType() {
        return super.getSpecificType();
    }
}
