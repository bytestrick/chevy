package chevy.model.entity.staticEntity.environment;

import chevy.model.entity.CommonEntityType;
import chevy.utils.Vector2;

public class Wall extends Environment {
    private final WallTypes type;

    public Wall(Vector2<Integer> initVelocity, WallTypes type) {
        super(initVelocity, Type.WALL);
        this.type = type;
    }

    @Override
    public WallTypes getSpecificType() {
        return type;
    }

    @Override
    public CommonEntityType getGenericType() {
        return super.getSpecificType();
    }

    public enum WallTypes implements CommonEntityType {
        TOP, CORNER_INTERIOR_TOP_LEFT, CORNER_INTERIOR_TOP_RIGHT, CORNER_INTERIOR_BOTTOM_LEFT,
        CORNER_INTERIOR_BOTTOM_RIGHT, TOP_TORCH, TOP_HOLE, TOP_HOLE_2, TOP_BROKEN, BOTTOM, LEFT, RIGHT,
        EXTERNAL_CORNER_BOTTOM_LEFT, EXTERNAL_CORNER_BOTTOM_RIGHT, EXTERNAL_CORNER_TOP_LEFT, EXTERNAL_CORNER_TOP_RIGHT
    }
}
