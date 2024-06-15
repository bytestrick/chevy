package chevy.view.chamber;

import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.EnemyTypes;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.liveEntity.player.PlayerTypes;
import chevy.model.entity.staticEntity.environment.GroundTypes;
import chevy.model.entity.staticEntity.environment.WallTypes;
import chevy.model.entity.staticEntity.environment.traps.TrapsTypes;
import chevy.settings.GameSettings;
import chevy.view.Image;
import chevy.view.entityView.EntityView;
import chevy.view.entityView.enemy.SlimeView;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class EntityToImage {
    private static BufferedImage WALL_TOP = null;
    private static BufferedImage WALL_BOTTOM = null;
    private static BufferedImage WALL_LEFT = null;
    private static BufferedImage WALL_RIGHT = null;
    private static BufferedImage WALL_EXTERNAL_CORNER_TOP_LEFT = null;
    private static BufferedImage WALL_EXTERNAL_CORNER_TOP_RIGHT = null;
    private static BufferedImage WALL_EXTERNAL_CORNER_BOTTOM_LEFT = null;
    private static BufferedImage WALL_EXTERNAL_CORNER_BOTTOM_RIGHT = null;

    private static BufferedImage GROUND_CENTRAL = null;
    private static BufferedImage GROUND_LEFT = null;
    private static BufferedImage GROUND_RIGHT = null;
    private static BufferedImage GROUND_TOP = null;
    private static BufferedImage GROUND_INTERIOR_CORNER_TOP_LEFT = null;
    private static BufferedImage GROUND_INTERIOR_CORNER_TOP_RIGHT = null;

    private static BufferedImage TRAP_VOID = null;

    private static final Map<Entity, EntityView> entityView = new HashMap<>();



    public static BufferedImage get(Entity entity) {
        return switch (entity.getSpecificType()) {
            // Wall
            case WallTypes.TOP -> {
                if (WALL_TOP == null)
                    WALL_TOP = Image.load("/assets/chamberTiles/wallTiles/top.png");
               yield WALL_TOP;
            }
            case WallTypes.BOTTOM -> {
                if (WALL_BOTTOM == null)
                    WALL_BOTTOM = Image.load("/assets/chamberTiles/wallTiles/bottom.png");
                yield WALL_BOTTOM;
            }
            case WallTypes.LEFT -> {
                if (WALL_LEFT == null)
                    WALL_LEFT = Image.load("/assets/chamberTiles/wallTiles/left.png");
                yield WALL_LEFT;
            }
            case WallTypes.RIGHT -> {
                if (WALL_RIGHT == null)
                    WALL_RIGHT = Image.load("/assets/chamberTiles/wallTiles/right.png");
                yield WALL_RIGHT;
            }
            case WallTypes.EXTERNAL_CORNER_BOTTOM_LEFT -> {
                if (WALL_EXTERNAL_CORNER_BOTTOM_LEFT == null)
                    WALL_EXTERNAL_CORNER_BOTTOM_LEFT = Image.load("/assets/chamberTiles/wallTiles/externalCornerBottomLeft.png");
                yield WALL_EXTERNAL_CORNER_BOTTOM_LEFT;
            }
            case WallTypes.EXTERNAL_CORNER_BOTTOM_RIGHT -> {
                if (WALL_EXTERNAL_CORNER_BOTTOM_RIGHT == null)
                    WALL_EXTERNAL_CORNER_BOTTOM_RIGHT = Image.load("/assets/chamberTiles/wallTiles/externalCornerBottomRight.png");
                yield WALL_EXTERNAL_CORNER_BOTTOM_RIGHT;
            }
            case WallTypes.EXTERNAL_CORNER_TOP_LEFT -> {
                if (WALL_EXTERNAL_CORNER_TOP_LEFT == null)
                    WALL_EXTERNAL_CORNER_TOP_LEFT = Image.load("/assets/chamberTiles/wallTiles/externalCornerTopLeft.png");
                yield WALL_EXTERNAL_CORNER_TOP_LEFT;
            }
            case WallTypes.EXTERNAL_CORNER_TOP_RIGHT -> {
                if (WALL_EXTERNAL_CORNER_TOP_RIGHT == null)
                    WALL_EXTERNAL_CORNER_TOP_RIGHT = Image.load("/assets/chamberTiles/wallTiles/externalCornerTopRight.png");
                yield WALL_EXTERNAL_CORNER_TOP_RIGHT;
            }

            // Ground
            case GroundTypes.CENTRAL -> {
                if (GROUND_CENTRAL == null)
                    GROUND_CENTRAL = Image.load("/assets/chamberTiles/groundTiles/central.png");
                yield GROUND_CENTRAL;
            }
            case GroundTypes.LEFT -> {
                if (GROUND_LEFT == null)
                    GROUND_LEFT = Image.load("/assets/chamberTiles/groundTiles/left.png");
                yield GROUND_LEFT;
            }
            case GroundTypes.RIGHT -> {
                if (GROUND_RIGHT == null)
                    GROUND_RIGHT = Image.load("/assets/chamberTiles/groundTiles/right.png");
                yield GROUND_RIGHT;
            }
            case GroundTypes.TOP -> {
                if (GROUND_TOP == null)
                    GROUND_TOP = Image.load("/assets/chamberTiles/groundTiles/top.png");
                yield GROUND_TOP;
            }
            case GroundTypes.INTERIOR_CORNER_TOP_LEFT -> {
                if (GROUND_INTERIOR_CORNER_TOP_LEFT == null)
                    GROUND_INTERIOR_CORNER_TOP_LEFT = Image.load("/assets/chamberTiles/groundTiles/interiorCornerTopLeft.png");
                yield GROUND_INTERIOR_CORNER_TOP_LEFT;
            }
            case GroundTypes.INTERIOR_CORNER_TOP_RIGHT -> {
                if (GROUND_INTERIOR_CORNER_TOP_RIGHT == null)
                    GROUND_INTERIOR_CORNER_TOP_RIGHT = Image.load("/assets/chamberTiles/groundTiles/interiorCornerTopRight.png");
                yield GROUND_INTERIOR_CORNER_TOP_RIGHT;
            }

            // Trap
            case TrapsTypes.VOID -> {
                if (TRAP_VOID == null)
                    TRAP_VOID = Image.load("/assets/chamberTiles/trapTiles/void.png");
                yield TRAP_VOID;
            }

            case EnemyTypes.SLIME -> {
                SlimeView slimeView = null;
                if (!entityView.containsKey(entity)) {
                    slimeView = new SlimeView((Slime) entity);
                    entityView.put(entity, slimeView);
                    }
                else
                    slimeView = (SlimeView) entityView.get(entity);

                yield slimeView.getCurrentFrame();
            }

            case PlayerTypes.KNIGHT -> {
                if (TRAP_VOID == null)
                    TRAP_VOID = Image.load("/assets/chamberTiles/trapTiles/void.png");
                yield TRAP_VOID;
            }

            default -> null;
        };
    }

    private static BufferedImage getSubImage(BufferedImage image, int x, int y) {
        return image.getSubimage(x * GameSettings.SIZE_TILE, y * GameSettings.SIZE_TILE, GameSettings.SIZE_TILE, GameSettings.SIZE_TILE);
    }
}
