package chevy.view.chamber;

import chevy.model.entity.Entity;
import chevy.model.entity.collectable.Coin;
import chevy.model.entity.collectable.Collectable;
import chevy.model.entity.collectable.Health;
import chevy.model.entity.collectable.Key;
import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Beetle;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.BigSlime;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Skeleton;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Wraith;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Zombie;
import chevy.model.entity.dinamicEntity.liveEntity.player.Archer;
import chevy.model.entity.dinamicEntity.liveEntity.player.Knight;
import chevy.model.entity.dinamicEntity.liveEntity.player.Ninja;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Arrow;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.entity.dinamicEntity.projectile.SlimeShot;
import chevy.model.entity.staticEntity.environment.Environment;
import chevy.model.entity.staticEntity.environment.Ground;
import chevy.model.entity.staticEntity.environment.Wall;
import chevy.model.entity.staticEntity.environment.traps.IcyFloor;
import chevy.model.entity.staticEntity.environment.traps.Sludge;
import chevy.model.entity.staticEntity.environment.traps.SpikedFloor;
import chevy.model.entity.staticEntity.environment.traps.Totem;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.model.entity.staticEntity.environment.traps.Trapdoor;
import chevy.view.entities.EntityView;
import chevy.view.entities.GroundView;
import chevy.view.entities.TrapView;
import chevy.view.entities.WallView;
import chevy.view.entities.animated.collectable.CoinView;
import chevy.view.entities.animated.collectable.HealthView;
import chevy.view.entities.animated.collectable.KeyView;
import chevy.view.entities.animated.collectable.PowerUpView;
import chevy.view.entities.animated.enemy.BeetleView;
import chevy.view.entities.animated.enemy.BigSlimeView;
import chevy.view.entities.animated.enemy.SkeletonView;
import chevy.view.entities.animated.enemy.SlimeView;
import chevy.view.entities.animated.enemy.WraithView;
import chevy.view.entities.animated.enemy.ZombieView;
import chevy.view.entities.animated.player.ArcherView;
import chevy.view.entities.animated.player.KnightView;
import chevy.view.entities.animated.player.NinjaView;
import chevy.view.entities.animated.projectile.ArrowView;
import chevy.view.entities.animated.projectile.SlimeShotView;
import chevy.view.entities.animated.trap.IcyFloorView;
import chevy.view.entities.animated.trap.SludgeView;
import chevy.view.entities.animated.trap.SpikedFloorView;
import chevy.view.entities.animated.trap.TotemView;
import chevy.view.entities.animated.trap.TrapdoorView;

import java.util.HashMap;
import java.util.Map;

/**
 * Mappa le entità alle loro rappresentazioni visive.
 */
public class EntityToEntityView {
    private static final Map<Entity, EntityView> entityView = new HashMap<>();

    /**
     * Ottieni la rappresentazione specifica di un entità.
     */
    public static EntityView getSpecific(Entity entity) {
        return switch (entity.getSpecificType()) {
            // Enemy
            case Enemy.Type.SLIME -> {
                SlimeView slimeView;
                if (entityView.containsKey(entity)) {
                    slimeView = (SlimeView) entityView.get(entity);
                } else {
                    slimeView = new SlimeView((Slime) entity);
                    entityView.put(entity, slimeView);
                }
                yield slimeView;
            }
            case Enemy.Type.BIG_SLIME -> {
                BigSlimeView bigSlimeView;
                if (entityView.containsKey(entity)) {
                    bigSlimeView = (BigSlimeView) entityView.get(entity);
                } else {
                    bigSlimeView = new BigSlimeView((BigSlime) entity);
                    entityView.put(entity, bigSlimeView);
                }
                yield bigSlimeView;
            }
            case Enemy.Type.ZOMBIE -> {
                ZombieView zombieView;
                if (entityView.containsKey(entity)) {
                    zombieView = (ZombieView) entityView.get(entity);
                } else {
                    zombieView = new ZombieView((Zombie) entity);
                    entityView.put(entity, zombieView);
                }
                yield zombieView;
            }
            case Enemy.Type.WRAITH -> {
                WraithView wraithView;
                if (entityView.containsKey(entity)) {
                    wraithView = (WraithView) entityView.get(entity);
                } else {
                    wraithView = new WraithView((Wraith) entity);
                    entityView.put(entity, wraithView);
                }
                yield wraithView;
            }
            case Enemy.Type.SKELETON -> {
                SkeletonView skeletonView;
                if (entityView.containsKey(entity)) {
                    skeletonView = (SkeletonView) entityView.get(entity);
                } else {
                    skeletonView = new SkeletonView((Skeleton) entity);
                    entityView.put(entity, skeletonView);
                }
                yield skeletonView;
            }
            case Enemy.Type.BEETLE -> {
                BeetleView beetleView;
                if (entityView.containsKey(entity)) {
                    beetleView = (BeetleView) entityView.get(entity);
                } else {
                    beetleView = new BeetleView((Beetle) entity);
                    entityView.put(entity, beetleView);
                }
                yield beetleView;
            }

            // Player
            case Player.Type.KNIGHT -> {
                KnightView knightView;
                if (entityView.containsKey(entity)) {
                    knightView = (KnightView) entityView.get(entity);
                } else {
                    knightView = new KnightView((Knight) entity);
                    entityView.put(entity, knightView);
                }
                yield knightView;
            }
            case Player.Type.ARCHER -> {
                ArcherView archerView;
                if (entityView.containsKey(entity)) {
                    archerView = (ArcherView) entityView.get(entity);
                } else {
                    archerView = new ArcherView((Archer) entity);
                    entityView.put(entity, archerView);
                }
                yield archerView;
            }
            case Player.Type.NINJA -> {
                NinjaView ninjaView;
                if (entityView.containsKey(entity)) {
                    ninjaView = (NinjaView) entityView.get(entity);
                } else {
                    ninjaView = new NinjaView((Ninja) entity);
                    entityView.put(entity, ninjaView);
                }
                yield ninjaView;
            }

            // Projectile
            case Projectile.Type.SLIME_SHOT -> {
                SlimeShotView slimeShotView;
                if (entityView.containsKey(entity)) {
                    slimeShotView = (SlimeShotView) entityView.get(entity);
                } else {
                    slimeShotView = new SlimeShotView((SlimeShot) entity);
                    entityView.put(entity, slimeShotView);
                }
                yield slimeShotView;
            }
            case Projectile.Type.ARROW -> {
                ArrowView arrowView = null;
                if (entityView.containsKey(entity)) {
                    arrowView = (ArrowView) entityView.get(entity);
                } else {
                    arrowView = new ArrowView((Arrow) entity);
                    entityView.put(entity, arrowView);
                }
                yield arrowView;
            }

            // Traps
            case Trap.Type.SPIKED_FLOOR -> {
                SpikedFloorView spikedFloorView;
                if (entityView.containsKey(entity)) {
                    spikedFloorView = (SpikedFloorView) entityView.get(entity);
                } else {
                    spikedFloorView = new SpikedFloorView((SpikedFloor) entity);
                    entityView.put(entity, spikedFloorView);
                }
                yield spikedFloorView;
            }
            case Trap.Type.SLUDGE -> {
                SludgeView sludgeView;
                if (entityView.containsKey(entity)) {
                    sludgeView = (SludgeView) entityView.get(entity);
                } else {
                    sludgeView = new SludgeView((Sludge) entity);
                    entityView.put(entity, sludgeView);
                }
                yield sludgeView;
            }
            case Trap.Type.ICY_FLOOR -> {
                IcyFloorView icyFloorView;
                if (entityView.containsKey(entity)) {
                    icyFloorView = (IcyFloorView) entityView.get(entity);
                } else {
                    icyFloorView = new IcyFloorView((IcyFloor) entity);
                    entityView.put(entity, icyFloorView);
                }
                yield icyFloorView;
            }
            case Trap.Type.TOTEM -> {
                TotemView totemView = null;
                if (entityView.containsKey(entity)) {
                    totemView = (TotemView) entityView.get(entity);
                } else {
                    totemView = new TotemView((Totem) entity);
                    entityView.put(entity, totemView);
                }
                yield totemView;
            }
            case Trap.Type.TRAPDOOR -> {
                TrapdoorView trapdoorView = null;
                if (entityView.containsKey(entity)) {
                    trapdoorView = (TrapdoorView) entityView.get(entity);
                } else {
                    trapdoorView = new TrapdoorView((Trapdoor) entity);
                    entityView.put(entity, trapdoorView);
                }
                yield trapdoorView;
            }

            // COLLECTABLES
            case Collectable.Type.HEALTH -> {
                HealthView healthView = null;
                if (entityView.containsKey(entity)) {
                    healthView = (HealthView) entityView.get(entity);
                } else {
                    healthView = new HealthView((Health) entity);
                    entityView.put(entity, healthView);
                }
                yield healthView;
            }
            case Collectable.Type.COIN -> {
                CoinView coinView = null;
                if (entityView.containsKey(entity)) {
                    coinView = (CoinView) entityView.get(entity);
                } else {
                    coinView = new CoinView((Coin) entity);
                    entityView.put(entity, coinView);
                }
                yield coinView;
            }
            case Collectable.Type.KEY -> {
                KeyView keyView = null;
                if (entityView.containsKey(entity)) {
                    keyView = (KeyView) entityView.get(entity);
                } else {
                    keyView = new KeyView((Key) entity);
                    entityView.put(entity, keyView);
                }
                yield keyView;
            }
            default -> null;
        };
    }

    /**
     * Ottieni la rappresentazione generica di un entità.
     */
    public static <powerupview> EntityView getGeneric(Entity entity) {
        return switch (entity.getGenericType()) {
            case Environment.Type.WALL -> {
                WallView wallView;
                if (entityView.containsKey(entity)) {
                    wallView = (WallView) entityView.get(entity);
                } else {
                    wallView = new WallView((Wall) entity);
                    entityView.put(entity, wallView);
                }
                yield wallView;
            }
            case Environment.Type.GROUND -> {
                GroundView groundView;
                if (entityView.containsKey(entity)) {
                    groundView = (GroundView) entityView.get(entity);
                } else {
                    groundView = new GroundView((Ground) entity);
                    entityView.put(entity, groundView);
                }
                yield groundView;
            }
            case Environment.Type.TRAP -> {
                TrapView trapView;
                if (entityView.containsKey(entity)) {
                    trapView = (TrapView) entityView.get(entity);
                } else {
                    trapView = new TrapView((Trap) entity);
                    entityView.put(entity, trapView);
                }
                yield trapView;
            }
            case Collectable.Type.POWER_UP -> {
                PowerUpView powerUpView = null;
                if (entityView.containsKey(entity)) {
                    powerUpView = (PowerUpView) entityView.get(entity);
                } else {
                    powerUpView = new PowerUpView((PowerUp) entity);
                    entityView.put(entity, powerUpView);
                }
                yield powerUpView;
            }
            default -> null;
        };
    }
}