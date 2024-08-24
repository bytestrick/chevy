package chevy.control.enemyController;

import chevy.service.Sound;
import chevy.control.InteractionType;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dynamicEntity.DirectionsModel;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Skeleton;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.utils.Log;

/**
 * Gestisce il comportamento e le interazioni del nemico Skeleton all'interno del gioco.
 * Gestisce come lo Skeleton risponde agli attacchi del giocatore, ai colpi dei proiettili e coordina il suo stato e
 * i suoi movimenti.
 */
public class SkeletonController {
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
                Sound.getInstance().play(Sound.Effect.SKELETON_HIT);
                hitSkeleton(skeleton, -1 * player.getDamage());
                skeleton.setDirection(DirectionsModel.positionToDirection(player, skeleton));
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
                skeleton.removeToUpdate();
                chamber.decreaseEnemyCounter();
                chamber.spawnCollectable(skeleton);
                return;
            }
        } else if (skeleton.getCurrentHealth() <= 0 && skeleton.checkAndChangeState(Skeleton.State.DEAD)) {
            chamber.spawnSlime(skeleton); // power up
            Sound.getInstance().play(Sound.Effect.SKELETON_DISASSEMBLED);
            skeleton.kill();
        }

        // Gestione del movimento/attacco
        if (skeleton.canChange(Skeleton.State.MOVE)) {
            DirectionsModel direction = chamber.getHitDirectionPlayer(skeleton);
            if (direction == null) {
                if (chamber.chase(skeleton)) {
                    skeleton.changeState(Skeleton.State.MOVE);
                    skeleton.setCanAttack(false);
                }
            }else if (skeleton.canChange(Skeleton.State.ATTACK)) {
                Entity entity = chamber.getNearEntityOnTop(skeleton, direction);
                if (entity instanceof Player && skeleton.changeState(Skeleton.State.ATTACK)) {
                    Sound.getInstance().play(Sound.Effect.SKELETON_HIT);
                    skeleton.setCanAttack(true);
                }
            }
        }

        if (skeleton.canAttack() && skeleton.getState(Skeleton.State.ATTACK).isFinished()) {
            DirectionsModel direction = chamber.getHitDirectionPlayer(skeleton);
            if (direction != null) {
                Entity entity = chamber.getNearEntityOnTop(skeleton, direction);
                if (entity instanceof Player) {
                    Sound.getInstance().play(Sound.Effect.SKELETON_HIT);
                    playerController.handleInteraction(InteractionType.ENEMY, skeleton);
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
        skeleton.setDirection(DirectionsModel.positionToDirection(projectile, skeleton));
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
