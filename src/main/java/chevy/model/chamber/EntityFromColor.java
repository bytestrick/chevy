package chevy.model.chamber;

import chevy.model.entity.Entity;
import chevy.model.entity.collectable.Coin;
import chevy.model.entity.collectable.Health;
import chevy.model.entity.collectable.Key;
import chevy.model.entity.collectable.powerUp.Agility;
import chevy.model.entity.collectable.powerUp.AngelRing;
import chevy.model.entity.collectable.powerUp.BrokenArrows;
import chevy.model.entity.collectable.powerUp.CoinOfGreed;
import chevy.model.entity.collectable.powerUp.ColdHeart;
import chevy.model.entity.collectable.powerUp.GoldArrows;
import chevy.model.entity.collectable.powerUp.HealingFlood;
import chevy.model.entity.collectable.powerUp.HedgehogSpines;
import chevy.model.entity.collectable.powerUp.HobnailBoots;
import chevy.model.entity.collectable.powerUp.HolyShield;
import chevy.model.entity.collectable.powerUp.HotHeart;
import chevy.model.entity.collectable.powerUp.KeySKeeper;
import chevy.model.entity.collectable.powerUp.LongSword;
import chevy.model.entity.collectable.powerUp.SlimePiece;
import chevy.model.entity.collectable.powerUp.StoneBoots;
import chevy.model.entity.collectable.powerUp.VampireFangs;
import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Beetle;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.BigSlime;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Skeleton;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Wraith;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Zombie;
import chevy.model.entity.dynamicEntity.liveEntity.player.Archer;
import chevy.model.entity.dynamicEntity.liveEntity.player.Knight;
import chevy.model.entity.dynamicEntity.liveEntity.player.Ninja;
import chevy.model.entity.staticEntity.environment.Chest;
import chevy.model.entity.staticEntity.environment.Ground;
import chevy.model.entity.staticEntity.environment.Stair;
import chevy.model.entity.staticEntity.environment.Wall;
import chevy.model.entity.staticEntity.environment.traps.IcyFloor;
import chevy.model.entity.staticEntity.environment.traps.Sludge;
import chevy.model.entity.staticEntity.environment.traps.SpikedFloor;
import chevy.model.entity.staticEntity.environment.traps.Totem;
import chevy.model.entity.staticEntity.environment.traps.Trapdoor;
import chevy.model.entity.staticEntity.environment.traps.Void;
import chevy.view.Menu;

import java.awt.Point;

/**
 * Maps the Red values of the pixels of a level image to game entities
 */
final class EntityFromColor {
    // Wall
    private static final int WALL_INTERIOR_CORNER_BOTTOM_LEFT = 255;
    private static final int WALL_BOTTOM = 254;
    private static final int WALL_RIGHT = 252;
    private static final int WALL_LEFT = 250;
    private static final int WALL_INTERIOR_CORNER_TOP_LEFT = 249;
    private static final int WALL_TOP = 248;
    private static final int WALL_INTERIOR_CORNER_TOP_RIGHT = 247;
    private static final int WALL_INTERIOR_CORNER_BOTTOM_RIGHT = 253;
    private static final int WALL_TOP_HOLE = 219;
    private static final int WALL_TOP_HOLE_2 = 218;
    private static final int WALL_TOP_BROKEN = 217;
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

    // Player
    private static final int PLAYER = 1;

    // Enemy
    private static final int WRAITH = 150;
    private static final int ZOMBIE = 149;
    private static final int SLIME = 148;
    private static final int BIG_SLIME = 147;
    private static final int BEETLE = 146;
    private static final int SKELETON = 145;

    // Collectable
    private static final int HEALTH = 10;
    private static final int COIN = 11;
    private static final int KEY = 12;

    // Collectable: Power Up
    private static final int AGILITY = 13;
    private static final int ANGEL_RING = 14;
    private static final int BROKEN_ARROWS = 15;
    private static final int COIN_OF_GREED = 16;
    private static final int COLD_HEART = 17;
    private static final int HOBNAIL_BOOTS = 18;
    private static final int GOLD_ARROWS = 19;
    private static final int KEY_S_KEEPER = 20;
    private static final int HEDGEHOG_SPINES = 21;
    private static final int HOLY_SHIELD = 22;
    private static final int HOT_HEARTH = 23;
    private static final int LONG_SWORD = 24;
    private static final int SLIME_PIECE = 25;
    private static final int STONE_BOOTS = 26;
    private static final int HEALING_FLOOD = 27;
    private static final int VAMPIRE_FANGS = 28;

    // Environment
    private static final int CHEST = 50;
    private static final int STAIR_RIGHT = 51;
    private static final int STAIR_LEFT = 52;

    static Entity get(int r, int row, int col) {
        final Point startPosition = new Point(col, row);
        return switch (r) {
            //@formatter:off
            // Wall
            case WALL_TOP -> new Wall(startPosition, Wall.WallTypes.TOP);
            case WALL_INTERIOR_CORNER_TOP_LEFT -> new Wall(startPosition, Wall.WallTypes.CORNER_INTERIOR_TOP_LEFT);
            case WALL_INTERIOR_CORNER_TOP_RIGHT -> new Wall(startPosition, Wall.WallTypes.CORNER_INTERIOR_TOP_RIGHT);
            case WALL_INTERIOR_CORNER_BOTTOM_LEFT -> new Wall(startPosition, Wall.WallTypes.CORNER_INTERIOR_BOTTOM_LEFT);
            case WALL_INTERIOR_CORNER_BOTTOM_RIGHT -> new Wall(startPosition, Wall.WallTypes.CORNER_INTERIOR_BOTTOM_RIGHT);
            case WALL_BOTTOM -> new Wall(startPosition, Wall.WallTypes.BOTTOM);
            case WALL_LEFT -> new Wall(startPosition, Wall.WallTypes.LEFT);
            case WALL_RIGHT -> new Wall(startPosition, Wall.WallTypes.RIGHT);
            case WALL_EXTERNAL_CORNER_BOTTOM_LEFT -> new Wall(startPosition, Wall.WallTypes.EXTERNAL_CORNER_BOTTOM_LEFT);
            case WALL_EXTERNAL_CORNER_BOTTOM_RIGHT -> new Wall(startPosition, Wall.WallTypes.EXTERNAL_CORNER_BOTTOM_RIGHT);
            case WALL_EXTERNAL_CORNER_TOP_LEFT -> new Wall(startPosition, Wall.WallTypes.EXTERNAL_CORNER_TOP_LEFT);
            case WALL_EXTERNAL_CORNER_TOP_RIGHT -> new Wall(startPosition, Wall.WallTypes.EXTERNAL_CORNER_TOP_RIGHT);
            case WALL_TOP_HOLE -> new Wall(startPosition, Wall.WallTypes.TOP_HOLE);
            case WALL_TOP_HOLE_2 -> new Wall(startPosition, Wall.WallTypes.TOP_HOLE_2);
            case WALL_TOP_BROKEN -> new Wall(startPosition, Wall.WallTypes.TOP_BROKEN);

            // Ground
            case GROUND_TOP -> new Ground(startPosition, Ground.GroundTypes.TOP);
            case GROUND_INTERIOR_CORNER_TOP_LEFT -> new Ground(startPosition, Ground.GroundTypes.INTERIOR_CORNER_TOP_LEFT);
            case GROUND_INTERIOR_CORNER_TOP_RIGHT -> new Ground(startPosition, Ground.GroundTypes.INTERIOR_CORNER_TOP_RIGHT);
            case GROUND_LEFT -> new Ground(startPosition, Ground.GroundTypes.LEFT);
            case GROUND_CENTRAL -> new Ground(startPosition, Ground.GroundTypes.CENTRAL);
            case GROUND_CENTRAL_PATTERNED -> new Ground(startPosition, Ground.GroundTypes.CENTRAL_PATTERNED);
            case GROUND_CENTRAL_PATTERNED_2 -> new Ground(startPosition, Ground.GroundTypes.CENTRAL_PATTERNED_2);
            case GROUND_CENTRAL_BROKEN -> new Ground(startPosition, Ground.GroundTypes.CENTRAL_BROKEN);
            case GROUND_CENTRAL_BROKEN_2 -> new Ground(startPosition, Ground.GroundTypes.CENTRAL_BROKEN_2);
            case GROUND_CENTRAL_BROKEN_3 -> new Ground(startPosition, Ground.GroundTypes.CENTRAL_BROKEN_3);
            case GROUND_RIGHT -> new Ground(startPosition, Ground.GroundTypes.RIGHT);
            case GROUND_EXTERNAL_CORNER_BOTTOM_LEFT -> new Ground(startPosition, Ground.GroundTypes.EXTERNAL_CORNER_BOTTOM_LEFT);
            case GROUND_EXTERNAL_CORNER_BOTTOM_RIGHT -> new Ground(startPosition, Ground.GroundTypes.EXTERNAL_CORNER_BOTTOM_RIGHT);
            case GROUND_EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_BOTTOM -> new Ground(startPosition, Ground.GroundTypes.EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_BOTTOM);
            case GROUND_EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_RIGHT -> new Ground(startPosition, Ground.GroundTypes.EXTERNAL_CORNER_BOTTOM_RIGHT_SIDE_RIGHT);
            case GROUND_EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_LEFT -> new Ground(startPosition, Ground.GroundTypes.EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_LEFT);
            case GROUND_EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_BOTTOM -> new Ground(startPosition, Ground.GroundTypes.EXTERNAL_CORNER_BOTTOM_LEFT_SIDE_BOTTOM);
            case GROUND_EXTERNAL_CORNER_TOP_LEFT_SIDE_LEFT -> new Ground(startPosition, Ground.GroundTypes.EXTERNAL_CORNER_TOP_LEFT_SIDE_LEFT);
            case GROUND_EXTERNAL_CORNER_TOP_RIGHT_SIDE_RIGHT -> new Ground(startPosition, Ground.GroundTypes.EXTERNAL_CORNER_TOP_RIGHT_SIDE_RIGHT);
            //@formatter:on

            // Trap
            case VOID -> new Void(startPosition);
            case ICY_FLOOR -> new IcyFloor(startPosition);
            case SLUDGE -> new Sludge(startPosition);
            case TRAPDOOR -> new Trapdoor(startPosition);
            case SPIKED_FLOOR -> new SpikedFloor(startPosition);
            case TOTEM_UP -> new Totem(startPosition, Direction.UP);
            case TOTEM_RIGHT -> new Totem(startPosition, Direction.RIGHT);
            case TOTEM_DOWN -> new Totem(startPosition, Direction.DOWN);
            case TOTEM_LEFT -> new Totem(startPosition, Direction.LEFT);

            // Player
            case PLAYER -> switch (Menu.playerType) {
                case KNIGHT -> new Knight(startPosition);
                case ARCHER -> new Archer(startPosition);
                case NINJA -> new Ninja(startPosition);
            };

            // Enemy
            case WRAITH -> new Wraith(startPosition);
            case ZOMBIE -> new Zombie(startPosition);
            case SLIME -> new Slime(startPosition);
            case BIG_SLIME -> new BigSlime(startPosition);
            case BEETLE -> new Beetle(startPosition);
            case SKELETON -> new Skeleton(startPosition);

            // Collectable
            case HEALTH -> new Health(startPosition);
            case COIN -> new Coin(startPosition);
            case KEY -> new Key(startPosition);
            // Collectable: Power Up
            case AGILITY -> new Agility(startPosition);
            case ANGEL_RING -> new AngelRing(startPosition);
            case BROKEN_ARROWS -> new BrokenArrows(startPosition);
            case COLD_HEART -> new ColdHeart(startPosition);
            case HOT_HEARTH -> new HotHeart(startPosition);
            case GOLD_ARROWS -> new GoldArrows(startPosition);
            case HOLY_SHIELD -> new HolyShield(startPosition);
            case VAMPIRE_FANGS -> new VampireFangs(startPosition);
            case LONG_SWORD -> new LongSword(startPosition);
            case STONE_BOOTS -> new StoneBoots(startPosition);
            case COIN_OF_GREED -> new CoinOfGreed(startPosition);
            case HEALING_FLOOD -> new HealingFlood(startPosition);
            case KEY_S_KEEPER -> new KeySKeeper(startPosition);
            case HOBNAIL_BOOTS -> new HobnailBoots(startPosition);
            case HEDGEHOG_SPINES -> new HedgehogSpines(startPosition);
            case SLIME_PIECE -> new SlimePiece(startPosition);

            case CHEST -> new Chest(startPosition);
            case STAIR_LEFT, STAIR_RIGHT -> new Stair(startPosition);

            default -> null;
        };
    }
}
