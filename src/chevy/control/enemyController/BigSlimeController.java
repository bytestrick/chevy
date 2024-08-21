package chevy.control.enemyController;

import chevy.Sound;
import chevy.control.InteractionType;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.BigSlime;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.utils.Log;

/**
 * Gestisce il comportamento e le interazioni * del nemico BigSlime all'interno del gioco.
 * Gestisce come il BigSlime risponde agli attacchi del giocatore, ai colpi dei proiettili e coordina il suo stato e
 * i suoi movimenti.
 */
public class BigSlimeController {
    /**
     * Riferimento alla stanza di gioco in cui si trova il BigSlime. Utilizzato per verificare le posizioni,
     * aggiungere/rimuovere entità e gestire le interazioni.
     */
    private final Chamber chamber;
    /**
     * Riferimento al controller del giocatore, utilizzato per gestire le interazioni tra il BigSlime e il giocatore.
     */
    private final PlayerController playerController;

    /**
     * @param chamber          la stanza di gioco
     * @param playerController il controller del giocatore
     */
    public BigSlimeController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }

    /**
     * Gestisce le interazioni del BigSlime con il giocatore.
     *
     * @param player   il giocatore che interagisce con il BigSlime
     * @param bigSlime il BigSlime che subisce l'interazione
     */
    public void playerInInteraction(Player player, BigSlime bigSlime) {
        switch (player.getCurrentState()) {
            // Se il giocatore è in stato di attacco, il BigSlime viene danneggiato in base al danno del giocatore.
            case Player.State.ATTACK -> {
                bigSlime.setDirection(DirectionsModel.positionToDirection(player, bigSlime));
                hitBigSlime(bigSlime, -1 * player.getDamage());
            }
            default -> Log.warn("Il BigSlimeController non gestisce questa azione");
        }
    }

    /**
     * Aggiorna lo stato del BigSlime a ogni ciclo di gioco. Gestisce il cambiamento di stato, il movimento
     * e le azioni del BigSlime.
     *
     * @param bigSlime il BigSlime da aggiornare
     */
    public void update(BigSlime bigSlime) {
        // Gestione della morte del BigSlime
        if (bigSlime.isDead()) {
            if (bigSlime.getState(BigSlime.State.DEAD).isFinished()) {
                chamber.removeEntityOnTop(bigSlime);
                bigSlime.removeToUpdate();
                chamber.spawnCollectable(bigSlime);
                return;
            }
        } else if (bigSlime.getCurrentHealth() <= 0 && bigSlime.checkAndChangeState(BigSlime.State.DEAD)) {
            chamber.spawnSlimeAroundEntity(bigSlime, 2);
            Sound.getInstance().play(Sound.Effect.SLIME_DEATH);
            bigSlime.kill();
        }

        // Movimento e attacco
        if (bigSlime.canChange(BigSlime.State.MOVE)) {
            DirectionsModel direction = chamber.getHitDirectionPlayer(bigSlime);
            if (direction == null) {
                if (chamber.wanderChasePlus(bigSlime, 3)) {
                    bigSlime.changeState(BigSlime.State.MOVE);
                    bigSlime.setCanAttack(false);
                }
            } else if (bigSlime.canChange(BigSlime.State.ATTACK)) {
                Entity entity = chamber.getNearEntityOnTop(bigSlime, direction);
                if (entity instanceof Player && bigSlime.changeState(BigSlime.State.ATTACK)) {
                    Sound.getInstance().play(Sound.Effect.SLIME_HIT);
                    bigSlime.setCanAttack(true);
                }
            }
        }

        if (bigSlime.canAttack() && bigSlime.getState(BigSlime.State.ATTACK).isFinished()) {
            DirectionsModel direction = chamber.getHitDirectionPlayer(bigSlime);
            if (direction != null) {
                Entity entity = chamber.getNearEntityOnTop(bigSlime, direction);
                if (entity instanceof Player) {
                    Sound.getInstance().play(Sound.Effect.SLIME_HIT);
                    playerController.handleInteraction(InteractionType.ENEMY, bigSlime);
                    bigSlime.setCanAttack(false);
                }
            }
        }

        if (bigSlime.checkAndChangeState(BigSlime.State.IDLE)) {
            bigSlime.setCanAttack(false);
        }
    }

    /**
     * Gestisce le interazioni del BigSlime con i proiettili.
     *
     * @param projectile il proiettile che colpisce il BigSlime
     * @param bigSlime   il BigSlime che subisce l'interazione
     */
    public void projectileInteraction(Projectile projectile, BigSlime bigSlime) {
        bigSlime.setDirection(DirectionsModel.positionToDirection(projectile, bigSlime));
        hitBigSlime(bigSlime, -1 * projectile.getDamage());
    }

    /**
     * Applica danno al BigSlime e cambia il suo stato a "colpito" se possibile.
     *
     * @param bigSlime il BigSlime che subisce il danno
     * @param damage   la quantità di danno da applicare
     */
    private void hitBigSlime(BigSlime bigSlime, int damage) {
        if (bigSlime.changeState(BigSlime.State.HIT)) {
            bigSlime.decreaseHealthShield(damage);
            Sound.getInstance().play(Sound.Effect.SLIME_HIT);
        }
    }

    public void trapInteraction(Trap trap, BigSlime bigSlime) {
        switch (trap.getSpecificType()) {
            case Trap.Type.SPIKED_FLOOR -> {
                hitBigSlime(bigSlime, -1 * trap.getDamage());
            }
            default -> { }
        }
    }
}
