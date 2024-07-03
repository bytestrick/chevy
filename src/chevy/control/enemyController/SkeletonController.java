package chevy.control.enemyController;

import chevy.control.InteractionTypes;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Skeleton;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
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
        switch (player.getCurrentEumState()) {
            // Se il giocatore è in stato di attacco, lo Skeleton viene danneggiato in base al danno del giocatore.
            case Player.States.ATTACK -> {
                hitSkeleton(skeleton, -1 * player.getDamage());
                skeleton.setDirection(DirectionsModel.positionToDirection(player, skeleton));
            }
            default -> Log.warn("Lo SkeletonController non gestisce questa azione: " + player.getCurrentEumState());
        }
    }

    /**
     * Aggiorna lo stato dello Skeleton a ogni ciclo di gioco.
     *
     * @param skeleton lo Skeleton da aggiornare
     */
    public void update(Skeleton skeleton) {
        // Gestione della morte
        if (!skeleton.isAlive()) {
            if (skeleton.getState(Skeleton.States.DEAD).isFinished()) {
                chamber.removeEntityOnTop(skeleton);
                skeleton.removeToUpdate();
                return;
            }
        } else if (skeleton.getHealth() <= 0 && skeleton.checkAndChangeState(Skeleton.States.DEAD)) {
            skeleton.kill();
        }

        // Gestione del movimento/attacco
        if (skeleton.canChange(Skeleton.States.MOVE)) {
            DirectionsModel direction = chamber.getHitDirectionPlayer(skeleton);
            if (direction == null) {
                if (chamber.chase(skeleton)) {
                    skeleton.changeState(Skeleton.States.MOVE);
                }
            } else if (skeleton.canAttack() && skeleton.getState(Skeleton.States.ATTACK).isFinished()) {
                Entity entity = chamber.getNearEntityOnTop(skeleton, direction);
                if (entity instanceof Player) {
                    playerController.handleInteraction(InteractionTypes.ENEMY, skeleton);
                    skeleton.setCanAttack(false);
                }
            } else if (skeleton.canChange(Skeleton.States.ATTACK)) {
                Entity entity = chamber.getNearEntityOnTop(skeleton, direction);
                if (entity instanceof Player && skeleton.changeState(Skeleton.States.ATTACK)) {
                    skeleton.setCanAttack(true);
                }
            }
        }
        skeleton.checkAndChangeState(Skeleton.States.IDLE);
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
        if (skeleton.changeState(Skeleton.States.HIT)) {
            skeleton.changeHealth(damage);
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