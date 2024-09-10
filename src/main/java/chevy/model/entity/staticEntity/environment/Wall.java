package chevy.model.entity.staticEntity.environment;

import chevy.model.entity.EntityType;

import java.awt.Point;

public final class Wall extends Environment {
    private final WallTypes type;

    public Wall(Point position, WallTypes type) {
        super(position, Type.WALL);
        this.type = type;
    }

    @Override
    public WallTypes getType() {return type;}

    @Override
    public EntityType getGenericType() {return super.getType();}

    public enum WallTypes implements EntityType {
        TOP, CORNER_INTERIOR_TOP_LEFT, CORNER_INTERIOR_TOP_RIGHT, CORNER_INTERIOR_BOTTOM_LEFT,
        CORNER_INTERIOR_BOTTOM_RIGHT, BOTTOM, LEFT,
        RIGHT, EXTERNAL_CORNER_BOTTOM_LEFT, EXTERNAL_CORNER_BOTTOM_RIGHT, EXTERNAL_CORNER_TOP_LEFT,
        EXTERNAL_CORNER_TOP_RIGHT
    }
}