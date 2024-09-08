package chevy.model.entity.staticEntity.environment;

import chevy.model.entity.EntityType;
import chevy.utils.Vector2;

public final class Wall extends Environment {
    private final WallTypes type;

    public Wall(Vector2<Integer> initVelocity, WallTypes type) {
        super(initVelocity, Type.WALL);
        this.type = type;
    }

    @Override
    public WallTypes getType() {return type;}

    @Override
    public EntityType getGenericType() {return super.getType();}

    public enum WallTypes implements EntityType {
        TOP, CORNER_INTERIOR_TOP_LEFT, CORNER_INTERIOR_TOP_RIGHT, CORNER_INTERIOR_BOTTOM_LEFT,
        CORNER_INTERIOR_BOTTOM_RIGHT, TOP_TORCH, TOP_HOLE, TOP_HOLE_2, TOP_BROKEN, BOTTOM, LEFT,
        RIGHT, EXTERNAL_CORNER_BOTTOM_LEFT, EXTERNAL_CORNER_BOTTOM_RIGHT, EXTERNAL_CORNER_TOP_LEFT,
        EXTERNAL_CORNER_TOP_RIGHT
    }
}