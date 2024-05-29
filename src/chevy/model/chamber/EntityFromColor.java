package chevy.model.chamber;

import chevy.model.entity.Entity;
import chevy.model.entity.staticEntity.environment.*;
import chevy.model.entity.staticEntity.environment.traps.Void;
import chevy.utilz.Vector2;

public class EntityFromColor {
    // Wall
    private static final int WALL_TOP = 248;
    private static final int WALL_CORNER_INTERIOR_TOP_LEFT = 249;
    private static final int WALL_CORNER_INTERIOR_TOP_RIGHT = 247;
    private static final int WALL_CORNER_INTERIOR_BOTTOM_LEFT = 255;
    private static final int WALL_CORNER_INTERIOR_BOTTOM_RIGHT = 253;
    // private static final int WALL_TOP_TORCH = ;
    // private static final int WALL_TOP_HOLE = ;
    // private static final int WALL_TOP_HOLE_2 = ;
    // private static final int WALL_TOP_BROKEN = ;
    private static final int WALL_BOTTOM = 254;
    private static final int WALL_LEFT = 250;
    private static final int WALL_RIGHT = 252;
    private static final int WALL_CORNER_EXTERNAL_BOTTOM_LEFT = 244;
    private static final int WALL_CORNER_EXTERNAL_BOTTOM_RIGHT = 243;
    private static final int WALL_CORNER_EXTERNAL_TOP_LEFT = 246;
    private static final int WALL_CORNER_EXTERNAL_TOP_RIGHT = 245;

    // Ground
    private static final int GROUND_TOP = 241;
    private static final int GROUND_INTERIOR_CORNER_TOP_LEFT = 242;
    private static final int GROUND_INTERIOR_CORNER_TOP_RIGHT = 240;
    private static final int GROUND_LEFT = 239;
    private static final int GROUND_CENTRAL = 227;
    private static final int GROUND_CENTRAL_PATTERNED = 224;
    private static final int GROUND_CENTRAL_PATTERNED_2 = 228;
    private static final int GROUND_CENTRAL_BROKEN = 229;
    private static final int GROUND_CENTRAL_BROKEN_2 = 226;
    private static final int GROUND_CENTRAL_BROKEN_3 = 225;
    private static final int GROUND_RIGHT = 238;
    private static final int GROUND_EXTERNAL_CORNER_BOTTOM_LEFT = 230;
    private static final int GROUND_EXTERNAL_CORNER_BOTTOM_RIGHT = 231;
    private static final int GROUND_EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_BOTTOM = 232;
    private static final int GROUND_EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_RIGHT = 235;
    private static final int GROUND_EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_LEFT = 234;
    private static final int GROUND_EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_BOTTOM = 233;
    private static final int GROUND_EXTERNAL_CORNER_TOP_LEFT_SIDE_LEFT = 237;
    private static final int GROUND_EXTERNAL_CORNER_TOP_RIGHT_SIDE_RIGHT = 236;

    // Traps
    private static final int TRAP_VOID = 251;


    public static Entity get(int r, int row, int col) {
        return switch (r) {
            // Default wall
            case WALL_TOP -> new Wall(new Vector2<>(row, col), WallTypes.TOP);
            case WALL_CORNER_INTERIOR_TOP_LEFT -> new Wall(new Vector2<>(row, col), WallTypes.CORNER_INTERIOR_TOP_LEFT);
            case WALL_CORNER_INTERIOR_TOP_RIGHT -> new Wall(new Vector2<>(row, col), WallTypes.CORNER_INTERIOR_TOP_RIGHT);
            case WALL_CORNER_INTERIOR_BOTTOM_LEFT -> new Wall(new Vector2<>(row, col), WallTypes.CORNER_INTERIOR_BOTTOM_LEFT);
            case WALL_CORNER_INTERIOR_BOTTOM_RIGHT -> new Wall(new Vector2<>(row, col), WallTypes.CORNER_INTERIOR_BOTTOM_RIGHT);
            case WALL_BOTTOM -> new Wall(new Vector2<>(row, col), WallTypes.BOTTOM);
            case WALL_LEFT -> new Wall(new Vector2<>(row, col), WallTypes.LEFT);
            case WALL_RIGHT -> new Wall(new Vector2<>(row, col), WallTypes.RIGHT);
            case WALL_CORNER_EXTERNAL_BOTTOM_LEFT -> new Wall(new Vector2<>(row, col), WallTypes.CORNER_EXTERNAL_BOTTOM_LEFT);
            case WALL_CORNER_EXTERNAL_BOTTOM_RIGHT -> new Wall(new Vector2<>(row, col), WallTypes.CORNER_EXTERNAL_BOTTOM_RIGHT);
            case WALL_CORNER_EXTERNAL_TOP_LEFT -> new Wall(new Vector2<>(row, col), WallTypes.CORNER_EXTERNAL_TOP_LEFT);
            case WALL_CORNER_EXTERNAL_TOP_RIGHT -> new Wall(new Vector2<>(row, col), WallTypes.CORNER_EXTERNAL_TOP_RIGHT);

            // Default ground
            case GROUND_TOP -> new Ground(new Vector2<>(row, col), GroundTypes.TOP);
            case GROUND_INTERIOR_CORNER_TOP_LEFT -> new Ground(new Vector2<>(row, col), GroundTypes.INTERIOR_CORNER_TOP_LEFT);
            case GROUND_INTERIOR_CORNER_TOP_RIGHT -> new Ground(new Vector2<>(row, col), GroundTypes.INTERIOR_CORNER_TOP_RIGHT);
            case GROUND_LEFT -> new Ground(new Vector2<>(row, col), GroundTypes.LEFT);
            case GROUND_CENTRAL -> new Ground(new Vector2<>(row, col), GroundTypes.CENTRAL);
            case GROUND_CENTRAL_PATTERNED -> new Ground(new Vector2<>(row, col), GroundTypes.CENTRAL_PATTERNED);
            case GROUND_CENTRAL_PATTERNED_2 -> new Ground(new Vector2<>(row, col), GroundTypes.CENTRAL_PATTERNED_2);
            case GROUND_CENTRAL_BROKEN -> new Ground(new Vector2<>(row, col), GroundTypes.CENTRAL_BROKEN);
            case GROUND_CENTRAL_BROKEN_2 -> new Ground(new Vector2<>(row, col), GroundTypes.CENTRAL_BROKEN_2);
            case GROUND_CENTRAL_BROKEN_3 -> new Ground(new Vector2<>(row, col), GroundTypes.CENTRAL_BROKEN_3);
            case GROUND_RIGHT -> new Ground(new Vector2<>(row, col), GroundTypes.RIGHT);
            case GROUND_EXTERNAL_CORNER_BOTTOM_LEFT -> new Ground(new Vector2<>(row, col), GroundTypes.EXTERNAL_CORNER_BOTTOM_LEFT);
            case GROUND_EXTERNAL_CORNER_BOTTOM_RIGHT -> new Ground(new Vector2<>(row, col), GroundTypes.EXTERNAL_CORNER_BOTTOM_RIGHT);
            case GROUND_EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_BOTTOM -> new Ground(new Vector2<>(row, col), GroundTypes.EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_BOTTOM);
            case GROUND_EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_RIGHT -> new Ground(new Vector2<>(row, col), GroundTypes.EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_RIGHT);
            case GROUND_EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_LEFT -> new Ground(new Vector2<>(row, col), GroundTypes.EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_LEFT);
            case GROUND_EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_BOTTOM -> new Ground(new Vector2<>(row, col), GroundTypes.EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_BOTTOM);
            case GROUND_EXTERNAL_CORNER_TOP_LEFT_SIDE_LEFT -> new Ground(new Vector2<>(row, col), GroundTypes.EXTERNAL_CORNER_TOP_LEFT_SIDE_LEFT);
            case GROUND_EXTERNAL_CORNER_TOP_RIGHT_SIDE_RIGHT -> new Ground(new Vector2<>(row, col), GroundTypes.EXTERNAL_CORNER_TOP_RIGHT_SIDE_RIGHT);

            // Traps
            case TRAP_VOID -> new Void(new Vector2<>(row, col));

            // ---
            default -> null;
        };
    }
}
