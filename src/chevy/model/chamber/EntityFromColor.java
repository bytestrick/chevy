package chevy.model.chamber;

import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.*;
import chevy.model.entity.dinamicEntity.liveEntity.player.Archer;
import chevy.model.entity.dinamicEntity.liveEntity.player.Knight;
import chevy.model.entity.dinamicEntity.liveEntity.player.Ninja;
import chevy.model.entity.staticEntity.environment.*;
import chevy.model.entity.staticEntity.environment.traps.*;
import chevy.model.entity.staticEntity.environment.traps.Void;
import chevy.utilz.Vector2;

public class EntityFromColor {
    // Wall
    private static final int WALL_INTERIOR_CORNER_BOTTOM_LEFT = 255;
    private static final int WALL_BOTTOM = 254;
    private static final int WALL_RIGHT = 252;
    private static final int WALL_LEFT = 250;
    private static final int WALL_INTERIOR_CORNER_TOP_LEFT = 249;
    private static final int WALL_TOP = 248;
    private static final int WALL_INTERIOR_CORNER_TOP_RIGHT = 247;
    private static final int WALL_INTERIOR_CORNER_BOTTOM_RIGHT = 253;
//     private static final int WALL_TOP_TORCH = ;
//     private static final int WALL_TOP_HOLE = ;
//     private static final int WALL_TOP_HOLE_2 = ;
//     private static final int WALL_TOP_BROKEN = ;
    private static final int WALL_EXTERNAL_CORNER_TOP_LEFT = 246;
    private static final int WALL_EXTERNAL_CORNER_TOP_RIGHT = 245;
    private static final int WALL_EXTERNAL_CORNER_BOTTOM_LEFT = 244;
    private static final int WALL_EXTERNAL_CORNER_BOTTOM_RIGHT = 243;

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

    // Trap
    private static final int VOID = 251;
    private static final int ICY_FLOOR = 223;
    private static final int SLUDGE = 222;
    private static final int TRAPDOOR = 221;
    private static final int SPIKED_FLOOR = 220;
    private static final int TOTEM_UP = 143;
    private static final int TOTEM_RIGHT = 142;
    private static final int TOTEM_DOWN = 141;
    private static final int TOTEM_LEFT = 140;

    // Players
    private static final int KNIGHT = 1;
    private static final int ARCHER = 2;
    private static final int NINJA = 3;

    // Enemy
    private static final int WRAITH = 150;
    private static final int ZOMBIE = 149;
    private static final int SLIME = 148;
    private static final int BIG_SLIME = 147;
    private static final int BEETLE = 146;
    private static final int WIZARD = 145;
    private static final int SKELETON = 144;

    //
    private static final int NULL = 0;


    public static Entity get(int r, int row, int col) {
        return switch (r) {
            // Wall
            case WALL_TOP -> new Wall(new Vector2<>(row, col), Wall.WallTypes.TOP);
            case WALL_INTERIOR_CORNER_TOP_LEFT -> new Wall(new Vector2<>(row, col), Wall.WallTypes.CORNER_INTERIOR_TOP_LEFT);
            case WALL_INTERIOR_CORNER_TOP_RIGHT -> new Wall(new Vector2<>(row, col), Wall.WallTypes.CORNER_INTERIOR_TOP_RIGHT);
            case WALL_INTERIOR_CORNER_BOTTOM_LEFT -> new Wall(new Vector2<>(row, col), Wall.WallTypes.CORNER_INTERIOR_BOTTOM_LEFT);
            case WALL_INTERIOR_CORNER_BOTTOM_RIGHT -> new Wall(new Vector2<>(row, col), Wall.WallTypes.CORNER_INTERIOR_BOTTOM_RIGHT);
            case WALL_BOTTOM -> new Wall(new Vector2<>(row, col), Wall.WallTypes.BOTTOM);
            case WALL_LEFT -> new Wall(new Vector2<>(row, col), Wall.WallTypes.LEFT);
            case WALL_RIGHT -> new Wall(new Vector2<>(row, col), Wall.WallTypes.RIGHT);
            case WALL_EXTERNAL_CORNER_BOTTOM_LEFT -> new Wall(new Vector2<>(row, col), Wall.WallTypes.EXTERNAL_CORNER_BOTTOM_LEFT);
            case WALL_EXTERNAL_CORNER_BOTTOM_RIGHT -> new Wall(new Vector2<>(row, col), Wall.WallTypes.EXTERNAL_CORNER_BOTTOM_RIGHT);
            case WALL_EXTERNAL_CORNER_TOP_LEFT -> new Wall(new Vector2<>(row, col), Wall.WallTypes.EXTERNAL_CORNER_TOP_LEFT);
            case WALL_EXTERNAL_CORNER_TOP_RIGHT -> new Wall(new Vector2<>(row, col), Wall.WallTypes.EXTERNAL_CORNER_TOP_RIGHT);

            // Ground
            case GROUND_TOP -> new Ground(new Vector2<>(row, col), Ground.GroundTypes.TOP);
            case GROUND_INTERIOR_CORNER_TOP_LEFT -> new Ground(new Vector2<>(row, col), Ground.GroundTypes.INTERIOR_CORNER_TOP_LEFT);
            case GROUND_INTERIOR_CORNER_TOP_RIGHT -> new Ground(new Vector2<>(row, col), Ground.GroundTypes.INTERIOR_CORNER_TOP_RIGHT);
            case GROUND_LEFT -> new Ground(new Vector2<>(row, col), Ground.GroundTypes.LEFT);
            case GROUND_CENTRAL -> new Ground(new Vector2<>(row, col), Ground.GroundTypes.CENTRAL);
            case GROUND_CENTRAL_PATTERNED -> new Ground(new Vector2<>(row, col), Ground.GroundTypes.CENTRAL_PATTERNED);
            case GROUND_CENTRAL_PATTERNED_2 -> new Ground(new Vector2<>(row, col), Ground.GroundTypes.CENTRAL_PATTERNED_2);
            case GROUND_CENTRAL_BROKEN -> new Ground(new Vector2<>(row, col), Ground.GroundTypes.CENTRAL_BROKEN);
            case GROUND_CENTRAL_BROKEN_2 -> new Ground(new Vector2<>(row, col), Ground.GroundTypes.CENTRAL_BROKEN_2);
            case GROUND_CENTRAL_BROKEN_3 -> new Ground(new Vector2<>(row, col), Ground.GroundTypes.CENTRAL_BROKEN_3);
            case GROUND_RIGHT -> new Ground(new Vector2<>(row, col), Ground.GroundTypes.RIGHT);
            case GROUND_EXTERNAL_CORNER_BOTTOM_LEFT -> new Ground(new Vector2<>(row, col), Ground.GroundTypes.EXTERNAL_CORNER_BOTTOM_LEFT);
            case GROUND_EXTERNAL_CORNER_BOTTOM_RIGHT -> new Ground(new Vector2<>(row, col), Ground.GroundTypes.EXTERNAL_CORNER_BOTTOM_RIGHT);
            case GROUND_EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_BOTTOM -> new Ground(new Vector2<>(row, col), Ground.GroundTypes.EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_BOTTOM);
            case GROUND_EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_RIGHT -> new Ground(new Vector2<>(row, col), Ground.GroundTypes.EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_RIGHT);
            case GROUND_EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_LEFT -> new Ground(new Vector2<>(row, col), Ground.GroundTypes.EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_LEFT);
            case GROUND_EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_BOTTOM -> new Ground(new Vector2<>(row, col), Ground.GroundTypes.EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_BOTTOM);
            case GROUND_EXTERNAL_CORNER_TOP_LEFT_SIDE_LEFT -> new Ground(new Vector2<>(row, col), Ground.GroundTypes.EXTERNAL_CORNER_TOP_LEFT_SIDE_LEFT);
            case GROUND_EXTERNAL_CORNER_TOP_RIGHT_SIDE_RIGHT -> new Ground(new Vector2<>(row, col), Ground.GroundTypes.EXTERNAL_CORNER_TOP_RIGHT_SIDE_RIGHT);

            // Trap
            case VOID -> new Void(new Vector2<>(row, col));
            case ICY_FLOOR -> new IcyFloor(new Vector2<>(row, col));
            case SLUDGE -> new Sludge(new Vector2<>(row, col));
            case TRAPDOOR -> new Trapdoor(new Vector2<>(row, col));
            case SPIKED_FLOOR -> new SpikedFloor(new Vector2<>(row, col));
            case TOTEM_UP -> new Totem(new Vector2<>(row, col), DirectionsModel.UP);
            case TOTEM_RIGHT -> new Totem(new Vector2<>(row, col), DirectionsModel.RIGHT);
            case TOTEM_DOWN -> new Totem(new Vector2<>(row, col), DirectionsModel.DOWN);
            case TOTEM_LEFT -> new Totem(new Vector2<>(row, col), DirectionsModel.LEFT);

            // Player
            case KNIGHT -> new Knight(new Vector2<>(row, col));
            case ARCHER -> new Archer(new Vector2<>(row, col));
            case NINJA -> new Ninja(new Vector2<>(row, col));

            // Enemy
            case WRAITH -> new Wraith(new Vector2<>(row, col));
            case ZOMBIE -> new Zombie(new Vector2<>(row, col));
            case SLIME -> new Slime(new Vector2<>(row, col));
            case BIG_SLIME -> new BigSlime(new Vector2<>(row, col));
            case BEETLE -> new Beetle(new Vector2<>(row, col));
            case WIZARD -> new Wizard(new Vector2<>(row, col));
            case SKELETON -> new Skeleton(new Vector2<>(row, col));

            // ---
            case NULL -> null;
            default -> null;
        };
    }
}
