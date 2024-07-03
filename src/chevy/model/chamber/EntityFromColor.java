package chevy.model.chamber;

import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Beetle;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.BigSlime;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Skeleton;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Wraith;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Zombie;
import chevy.model.entity.dinamicEntity.liveEntity.player.Archer;
import chevy.model.entity.dinamicEntity.liveEntity.player.Knight;
import chevy.model.entity.dinamicEntity.liveEntity.player.Ninja;
import chevy.model.entity.staticEntity.environment.Ground;
import chevy.model.entity.staticEntity.environment.Wall;
import chevy.model.entity.staticEntity.environment.traps.IcyFloor;
import chevy.model.entity.staticEntity.environment.traps.Sludge;
import chevy.model.entity.staticEntity.environment.traps.SpikedFloor;
import chevy.model.entity.staticEntity.environment.traps.Totem;
import chevy.model.entity.staticEntity.environment.traps.Trapdoor;
import chevy.model.entity.staticEntity.environment.traps.Void;
import chevy.utils.Vector2;

/**
 * Crea entità in base a valori di colore.
 * Ogni valore di colore corrisponde a un tipo specifico di entità nel gioco.
 */
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
    private static final int SKELETON = 145;

    //
    private static final int NULL = 0;

    public static Entity get(int r, int row, int col) {
        Vector2<Integer> startPosition = new Vector2<>(row, col);
        return switch (r) {
            // Wall
            case WALL_TOP -> new Wall(startPosition, Wall.WallTypes.TOP);
            case WALL_INTERIOR_CORNER_TOP_LEFT -> new Wall(startPosition, Wall.WallTypes.CORNER_INTERIOR_TOP_LEFT);
            case WALL_INTERIOR_CORNER_TOP_RIGHT -> new Wall(startPosition, Wall.WallTypes.CORNER_INTERIOR_TOP_RIGHT);
            case WALL_INTERIOR_CORNER_BOTTOM_LEFT ->
                    new Wall(startPosition, Wall.WallTypes.CORNER_INTERIOR_BOTTOM_LEFT);
            case WALL_INTERIOR_CORNER_BOTTOM_RIGHT ->
                    new Wall(startPosition, Wall.WallTypes.CORNER_INTERIOR_BOTTOM_RIGHT);
            case WALL_BOTTOM -> new Wall(startPosition, Wall.WallTypes.BOTTOM);
            case WALL_LEFT -> new Wall(startPosition, Wall.WallTypes.LEFT);
            case WALL_RIGHT -> new Wall(startPosition, Wall.WallTypes.RIGHT);
            case WALL_EXTERNAL_CORNER_BOTTOM_LEFT ->
                    new Wall(startPosition, Wall.WallTypes.EXTERNAL_CORNER_BOTTOM_LEFT);
            case WALL_EXTERNAL_CORNER_BOTTOM_RIGHT ->
                    new Wall(startPosition, Wall.WallTypes.EXTERNAL_CORNER_BOTTOM_RIGHT);
            case WALL_EXTERNAL_CORNER_TOP_LEFT -> new Wall(startPosition, Wall.WallTypes.EXTERNAL_CORNER_TOP_LEFT);
            case WALL_EXTERNAL_CORNER_TOP_RIGHT -> new Wall(startPosition, Wall.WallTypes.EXTERNAL_CORNER_TOP_RIGHT);

            // Ground
            case GROUND_TOP -> new Ground(startPosition, Ground.GroundTypes.TOP);
            case GROUND_INTERIOR_CORNER_TOP_LEFT ->
                    new Ground(startPosition, Ground.GroundTypes.INTERIOR_CORNER_TOP_LEFT);
            case GROUND_INTERIOR_CORNER_TOP_RIGHT ->
                    new Ground(startPosition, Ground.GroundTypes.INTERIOR_CORNER_TOP_RIGHT);
            case GROUND_LEFT -> new Ground(startPosition, Ground.GroundTypes.LEFT);
            case GROUND_CENTRAL -> new Ground(startPosition, Ground.GroundTypes.CENTRAL);
            case GROUND_CENTRAL_PATTERNED -> new Ground(startPosition, Ground.GroundTypes.CENTRAL_PATTERNED);
            case GROUND_CENTRAL_PATTERNED_2 -> new Ground(startPosition, Ground.GroundTypes.CENTRAL_PATTERNED_2);
            case GROUND_CENTRAL_BROKEN -> new Ground(startPosition, Ground.GroundTypes.CENTRAL_BROKEN);
            case GROUND_CENTRAL_BROKEN_2 -> new Ground(startPosition, Ground.GroundTypes.CENTRAL_BROKEN_2);
            case GROUND_CENTRAL_BROKEN_3 -> new Ground(startPosition, Ground.GroundTypes.CENTRAL_BROKEN_3);
            case GROUND_RIGHT -> new Ground(startPosition, Ground.GroundTypes.RIGHT);
            case GROUND_EXTERNAL_CORNER_BOTTOM_LEFT ->
                    new Ground(startPosition, Ground.GroundTypes.EXTERNAL_CORNER_BOTTOM_LEFT);
            case GROUND_EXTERNAL_CORNER_BOTTOM_RIGHT ->
                    new Ground(startPosition, Ground.GroundTypes.EXTERNAL_CORNER_BOTTOM_RIGHT);
            case GROUND_EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_BOTTOM ->
                    new Ground(startPosition, Ground.GroundTypes.EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_BOTTOM);
            case GROUND_EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_RIGHT ->
                    new Ground(startPosition, Ground.GroundTypes.EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_RIGHT);
            case GROUND_EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_LEFT ->
                    new Ground(startPosition, Ground.GroundTypes.EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_LEFT);
            case GROUND_EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_BOTTOM ->
                    new Ground(startPosition, Ground.GroundTypes.EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_BOTTOM);
            case GROUND_EXTERNAL_CORNER_TOP_LEFT_SIDE_LEFT ->
                    new Ground(startPosition, Ground.GroundTypes.EXTERNAL_CORNER_TOP_LEFT_SIDE_LEFT);
            case GROUND_EXTERNAL_CORNER_TOP_RIGHT_SIDE_RIGHT ->
                    new Ground(startPosition, Ground.GroundTypes.EXTERNAL_CORNER_TOP_RIGHT_SIDE_RIGHT);

            // Trap
            case VOID -> new Void(startPosition);
            case ICY_FLOOR -> new IcyFloor(startPosition);
            case SLUDGE -> new Sludge(startPosition);
            case TRAPDOOR -> new Trapdoor(startPosition);
            case SPIKED_FLOOR -> new SpikedFloor(startPosition);
            case TOTEM_UP -> new Totem(startPosition, DirectionsModel.UP);
            case TOTEM_RIGHT -> new Totem(startPosition, DirectionsModel.RIGHT);
            case TOTEM_DOWN -> new Totem(startPosition, DirectionsModel.DOWN);
            case TOTEM_LEFT -> new Totem(startPosition, DirectionsModel.LEFT);

            // Player
            case KNIGHT -> new Knight(startPosition);
            case ARCHER -> new Archer(startPosition);
            case NINJA -> new Ninja(startPosition);

            // Enemy
            case WRAITH -> new Wraith(startPosition);
            case ZOMBIE -> new Zombie(startPosition);
            case SLIME -> new Slime(startPosition);
            case BIG_SLIME -> new BigSlime(startPosition);
            case BEETLE -> new Beetle(startPosition);
            case SKELETON -> new Skeleton(startPosition);

            // ---
            case NULL -> null;
            default -> null;
        };
    }
}