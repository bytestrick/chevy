package chevy.control.enemyController;

import chevy.control.Interaction;
import chevy.control.PlayerController;
import chevy.model.Statistics;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Skeleton;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.service.Sound;
import chevy.utils.Log;

/**
 * Gestisce il comportamento e le interazioni del nemico Skeleton all'interno del gioco.
 * Gestisce come lo Skeleton risponde agli attacchi del giocatore, ai colpi dei proiettili e coordina il suo stato e
 * i suoi movimenti.
 */
public final class SkeletonController {
    /**
     * Riferimento alla stanza di gioco in cui si trova lo Skeleton. Utilizzato per verificare le posizioni,
     * aggiungere/rimuovere entità e gestire le interazioni.
     */
    private final Chamber chamber;
    /**
     * Riferimento al controller del giocatore, utilizzato per gestire le interazioni tra lo Skeleton e il giocatore.
     */
    private final PlayerController playerController;

    /**
     * @param chamber          riferimento della stanza di gioco
     * @param playerController riferimento al controller del giocatore
     */
    public SkeletonController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }

    /**
     * Gestisce le interazioni dello Skeleton con il giocatore.
     *
     * @param player   il giocatore che interagisce con lo Skeleton
     * @param skeleton lo Skeleton che subisce l'interazione
     */
    public void playerInInteraction(Player player, Skeleton skeleton) {
        switch (player.getCurrentState()) {
            // Se il giocatore è in stato di attacco, lo Skeleton viene danneggiato in base al danno del giocatore.
            case Player.State.ATTACK -> {
                Sound.play(Sound.Effect.SKELETON_HIT);
                hitSkeleton(skeleton, -1 * player.getDamage());
                skeleton.setDirection(Direction.positionToDirection(player, skeleton));
            }
            default -> Log.warn("Lo SkeletonController non gestisce questa azione: " + player.getCurrentState());
        }
    }

    /**
     * Aggiorna lo stato dello Skeleton a ogni ciclo di gioco.
     *
     * @param skeleton lo Skeleton da aggiornare
     */
    public void update(Skeleton skeleton) {
        // Gestione della morte
        if (skeleton.isDead()) {
            if (skeleton.getState(Skeleton.State.DEAD).isFinished()) {
                chamber.removeEntityOnTop(skeleton);
                skeleton.removeFromUpdate();
                chamber.decreaseEnemyCounter();
                chamber.spawnCollectable(skeleton);
                Statistics.increase(Statistics.KILLED_ENEMY, 1);
                Statistics.increase(Statistics.KILLED_SKELETON, 1);
                return;
            }
        } else if (skeleton.getCurrentHealth() <= 0 && skeleton.checkAndChangeState(Skeleton.State.DEAD)) {
            chamber.spawnSlime(skeleton); // power up
            Sound.play(Sound.Effect.SKELETON_DISASSEMBLED);
            skeleton.kill();
        }

        // Gestione del movimento/attacco
        if (skeleton.canChange(Skeleton.State.MOVE)) {
            Direction direction = chamber.getDirectionToHitPlayer(skeleton);
            if (direction == null) {
                if (chamber.chase(skeleton)) {
                    skeleton.changeState(Skeleton.State.MOVE);
                    skeleton.setCanAttack(false);
                }
            } else if (skeleton.canChange(Skeleton.State.ATTACK)) {
                Entity entity = chamber.getEntityNearOnTop(skeleton, direction);
                if (entity instanceof Player && skeleton.changeState(Skeleton.State.ATTACK)) {
                    Sound.play(Sound.Effect.SKELETON_HIT);
                    skeleton.setCanAttack(true);
                }
            }
        }

        if (skeleton.canAttack() && skeleton.getState(Skeleton.State.ATTACK).isFinished()) {
            Direction direction = chamber.getDirectionToHitPlayer(skeleton);
            if (direction != null) {
                Entity entity = chamber.getEntityNearOnTop(skeleton, direction);
                if (entity instanceof Player) {
                    Sound.play(Sound.Effect.SKELETON_HIT);
                    playerController.handleInteraction(Interaction.ENEMY, skeleton);
                    skeleton.setCanAttack(false);
                }
            }
        }

        if (skeleton.checkAndChangeState(Skeleton.State.IDLE)) {
            skeleton.setCanAttack(false);
        }
    }

    /**
     * Gestisce le interazioni dello Skeleton con i proiettili.
     *
     * @param projectile il proiettile che colpisce lo Skeleton
     * @param skeleton   lo Skeleton che subisce l'interazione
     */
    public void projectileInteraction(Projectile projectile, Skeleton skeleton) {
        skeleton.setDirection(Direction.positionToDirection(projectile, skeleton));
        hitSkeleton(skeleton, -1 * projectile.getDamage());
    }

    /**
     * Applica danno allo Skeleton e aggiorna il suo stato.
     *
     * @param skeleton lo Skeleton che subisce il danno
     * @param damage   il danno da applicare
     */
    private void hitSkeleton(Skeleton skeleton, int damage) {
        if (skeleton.changeState(Skeleton.State.HIT)) {
            skeleton.decreaseHealthShield(damage);
        }
    }

    public void trapInteraction(Trap trap, Skeleton skeleton) {
        switch (trap.getSpecificType()) {
            case Trap.Type.SPIKED_FLOOR -> {
                hitSkeleton(skeleton, -1 * trap.getDamage());
            }
            default -> { }
        }
    }
}
