package chevy.view.chamber;

import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.*;
import chevy.model.entity.dinamicEntity.liveEntity.player.Knight;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Arrow;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.entity.dinamicEntity.projectile.SlimeShot;
import chevy.model.entity.staticEntity.environment.Environment;
import chevy.model.entity.staticEntity.environment.Ground;
import chevy.model.entity.staticEntity.environment.Wall;
import chevy.model.entity.staticEntity.environment.traps.*;
import chevy.view.entityView.EntityView;
import chevy.view.entityView.GroundView;
import chevy.view.entityView.TrapView;
import chevy.view.entityView.WallView;
import chevy.view.entityView.entityViewAnimated.enemy.*;
import chevy.view.entityView.entityViewAnimated.player.KnightView;
import chevy.view.entityView.entityViewAnimated.projectile.ArrowView;
import chevy.view.entityView.entityViewAnimated.projectile.SlimeShotView;
import chevy.view.entityView.entityViewAnimated.trap.*;

import java.util.HashMap;
import java.util.Map;

/**
 * La classe EntityToEntityView gestisce la mappatura tra le entità del gioco e le loro rispettive visualizzazioni.
 * Questa classe contiene metodi per ottenere la visualizzazione specifica o generica di un'entità.
 */
public class EntityToEntityView {
    private static final Map<Entity, EntityView> entityView = new HashMap<>();

    public static EntityView getSpecific(Entity entity) {
        return switch (entity.getSpecificType()) {
            // ENEMY
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
            case Enemy.Type.BIG_SLIME -> {
                BigSlimeView bigSlimeView = null;
                if (!entityView.containsKey(entity)) {
                    bigSlimeView = new BigSlimeView((BigSlime) entity);
                    entityView.put(entity, bigSlimeView);
                }
                else
                    bigSlimeView = (BigSlimeView) entityView.get(entity);

                yield bigSlimeView;
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

            // PLAYER
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

            // PROJECTILE
            case Projectile.Type.SLIME_SHOT -> {
                SlimeShotView slimeShotView = null;
                if (!entityView.containsKey(entity)) {
                    slimeShotView = new SlimeShotView((SlimeShot) entity);
                    entityView.put(entity, slimeShotView);
                }
                else
                    slimeShotView = (SlimeShotView) entityView.get(entity);

                yield slimeShotView;
            }
            case Projectile.Type.ARROW -> {
                ArrowView arrowView = null;
                if (!entityView.containsKey(entity)) {
                    arrowView = new ArrowView((Arrow) entity);
                    entityView.put(entity, arrowView);
                }
                else
                    arrowView = (ArrowView) entityView.get(entity);

                yield arrowView;
            }

            // TRAPS
            case Trap.Type.SPIKED_FLOOR -> {
                SpikedFloorView spikedFloorView = null;
                if (!entityView.containsKey(entity)) {
                    spikedFloorView = new SpikedFloorView((SpikedFloor) entity);
                    entityView.put(entity, spikedFloorView);
                }
                else
                    spikedFloorView = (SpikedFloorView) entityView.get(entity);

                yield spikedFloorView;
            }
            case Trap.Type.SLUDGE -> {
                SludgeView sludgeView = null;
                if (!entityView.containsKey(entity)) {
                    sludgeView = new SludgeView((Sludge) entity);
                    entityView.put(entity, sludgeView);
                }
                else
                    sludgeView = (SludgeView) entityView.get(entity);

                yield sludgeView;
            }
            case Trap.Type.ICY_FLOOR -> {
                IcyFloorView icyFloorView = null;
                if (!entityView.containsKey(entity)) {
                    icyFloorView = new IcyFloorView((IcyFloor) entity);
                    entityView.put(entity, icyFloorView);
                }
                else
                    icyFloorView = (IcyFloorView) entityView.get(entity);

                yield icyFloorView;
            }
            case Trap.Type.TOTEM -> {
                TotemView totemView = null;
                if (!entityView.containsKey(entity)) {
                    totemView = new TotemView((Totem) entity);
                    entityView.put(entity, totemView);
                }
                else
                    totemView = (TotemView) entityView.get(entity);

                yield totemView;
            }
            case Trap.Type.TRAPDOOR -> {
                TrapdoorView trapdoorView = null;
                if (!entityView.containsKey(entity)) {
                    trapdoorView = new TrapdoorView((Trapdoor) entity);
                    entityView.put(entity, trapdoorView);
                }
                else
                    trapdoorView = (TrapdoorView) entityView.get(entity);

                yield trapdoorView;
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
