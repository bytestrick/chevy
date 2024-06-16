package chevy.view.chamber;

import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.EnemyTypes;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.liveEntity.player.Knight;
import chevy.model.entity.dinamicEntity.liveEntity.player.PlayerTypes;
import chevy.model.entity.staticEntity.environment.EnvironmentTypes;
import chevy.model.entity.staticEntity.environment.Ground;
import chevy.model.entity.staticEntity.environment.Wall;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.view.entityView.EntityView;
import chevy.view.entityView.GroundView;
import chevy.view.entityView.TrapView;
import chevy.view.entityView.WallView;
import chevy.view.entityView.entityViewAnimated.enemy.SlimeView;
import chevy.view.entityView.entityViewAnimated.player.KnightView;

import java.util.HashMap;
import java.util.Map;


public class EntityToEntityView {
    private static final Map<Entity, EntityView> entityView = new HashMap<>();

    public static EntityView getSpecific(Entity entity) {
        return switch (entity.getSpecificType()) {
            case EnemyTypes.SLIME -> {
                SlimeView slimeView = null;
                if (!entityView.containsKey(entity)) {
                    slimeView = new SlimeView((Slime) entity);
                    entityView.put(entity, slimeView);
                }
                else
                    slimeView = (SlimeView) entityView.get(entity);

                yield slimeView;
            }
            case PlayerTypes.KNIGHT -> {
                KnightView knightView = null;
                if (!entityView.containsKey(entity)) {
                    knightView = new KnightView((Knight) entity);
                    entityView.put(entity, knightView);
                }
                else
                    knightView = (KnightView) entityView.get(entity);

                yield knightView;
            }
            default -> null;
        };
    }

    public static EntityView getGeneric(Entity entity) {
        return switch (entity.getGenericType()) {
            case EnvironmentTypes.WALL -> {
                WallView wallView = null;
                if (!entityView.containsKey(entity)) {
                    wallView = new WallView((Wall) entity);
                    entityView.put(entity, wallView);
                }
                else
                    wallView = (WallView) entityView.get(entity);

                yield wallView;
            }
            case EnvironmentTypes.GROUND -> {
                GroundView groundView = null;
                if (!entityView.containsKey(entity)) {
                    groundView = new GroundView((Ground) entity);
                    entityView.put(entity, groundView);
                }
                else
                    groundView = (GroundView) entityView.get(entity);

                yield groundView;
            }
            case EnvironmentTypes.TRAP -> {
                TrapView trapView = null;
                if (!entityView.containsKey(entity)) {
                    trapView = new TrapView((Trap) entity);
                    entityView.put(entity, trapView);
                }
                else
                    trapView = (TrapView) entityView.get(entity);

                yield trapView;
            }
            default -> null;
        };
    }
}
