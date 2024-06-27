package chevy.view.chamber;

import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.*;
import chevy.model.entity.dinamicEntity.liveEntity.player.Knight;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.entity.dinamicEntity.projectile.SlimeShot;
import chevy.model.entity.staticEntity.environment.Environment;
import chevy.model.entity.staticEntity.environment.Ground;
import chevy.model.entity.staticEntity.environment.Wall;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.view.entityView.EntityView;
import chevy.view.entityView.GroundView;
import chevy.view.entityView.TrapView;
import chevy.view.entityView.WallView;
import chevy.view.entityView.entityViewAnimated.enemy.*;
import chevy.view.entityView.entityViewAnimated.player.KnightView;
import chevy.view.entityView.entityViewAnimated.projectile.SlimeShotView;

import java.util.HashMap;
import java.util.Map;


public class EntityToEntityView {
    private static final Map<Entity, EntityView> entityView = new HashMap<>();

    public static EntityView getSpecific(Entity entity) {
        return switch (entity.getSpecificType()) {
            case Enemy.Type.SLIME -> {
                SlimeView slimeView = null;
                if (!entityView.containsKey(entity)) {
                    slimeView = new SlimeView((Slime) entity);
                    entityView.put(entity, slimeView);
                }
                else
                    slimeView = (SlimeView) entityView.get(entity);

                yield slimeView;
            }
            case Enemy.Type.ZOMBIE -> {
                ZombieView zombieView = null;
                if (!entityView.containsKey(entity)) {
                    zombieView = new ZombieView((Zombie) entity);
                    entityView.put(entity, zombieView);
                }
                else
                    zombieView = (ZombieView) entityView.get(entity);

                yield zombieView;
            }
            case Enemy.Type.WRAITH -> {
                WraithView wraithView = null;
                if (!entityView.containsKey(entity)) {
                    wraithView = new WraithView((Wraith) entity);
                    entityView.put(entity, wraithView);
                }
                else
                    wraithView = (WraithView) entityView.get(entity);

                yield wraithView;
            }
            case Enemy.Type.SKELETON -> {
                SkeletonView skeletonView = null;
                if (!entityView.containsKey(entity)) {
                    skeletonView = new SkeletonView((Skeleton) entity);
                    entityView.put(entity, skeletonView);
                }
                else
                    skeletonView = (SkeletonView) entityView.get(entity);

                yield skeletonView;
            }
            case Enemy.Type.BEETLE -> {
                BeetleView beetleView = null;
                if (!entityView.containsKey(entity)) {
                    beetleView = new BeetleView((Beetle) entity);
                    entityView.put(entity, beetleView);
                }
                else
                    beetleView = (BeetleView) entityView.get(entity);

                yield beetleView;
            }
            case Player.Type.KNIGHT -> {
                KnightView knightView = null;
                if (!entityView.containsKey(entity)) {
                    knightView = new KnightView((Knight) entity);
                    entityView.put(entity, knightView);
                }
                else
                    knightView = (KnightView) entityView.get(entity);

                yield knightView;
            }
            case Projectile.Type.SLIME_SHOT -> {
                SlimeShotView slimeShotView = null;
                if (!entityView.containsKey(entity)) {
                    slimeShotView = new SlimeShotView((SlimeShot) entity);
                    entityView.put(entity, slimeShotView);
                } else
                    slimeShotView = (SlimeShotView) entityView.get(entity);

                yield slimeShotView;
            }
            default -> null;
        };
    }

    public static EntityView getGeneric(Entity entity) {
        return switch (entity.getGenericType()) {
            case Environment.Type.WALL -> {
                WallView wallView = null;
                if (!entityView.containsKey(entity)) {
                    wallView = new WallView((Wall) entity);
                    entityView.put(entity, wallView);
                }
                else
                    wallView = (WallView) entityView.get(entity);

                yield wallView;
            }
            case Environment.Type.GROUND -> {
                GroundView groundView = null;
                if (!entityView.containsKey(entity)) {
                    groundView = new GroundView((Ground) entity);
                    entityView.put(entity, groundView);
                }
                else
                    groundView = (GroundView) entityView.get(entity);

                yield groundView;
            }
            case Environment.Type.TRAP -> {
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
