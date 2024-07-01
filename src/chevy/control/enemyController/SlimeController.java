package chevy.control.enemyController;

import chevy.control.InteractionTypes;
import chevy.control.PlayerController;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.chamber.Chamber;
import chevy.model.entity.staticEntity.environment.traps.Trap;

/**
 * La classe SlimeController è responsabile della gestione del comportamento e delle interazioni
 * del nemico Slime all'interno del gioco. Gestisce come lo Slime risponde agli attacchi del giocatore,
 * ai colpi dei proiettili e coordina il suo stato e i suoi movimenti.
 */
public class SlimeController {
    /**
     * Riferimento alla stanza di gioco in cui si trova lo Slime. Utilizzato per verificare le posizioni,
     * aggiungere/rimuovere entità.
     */
    private final Chamber chamber;
    /**
     * Riferimento al controller del giocatore, utilizzato per gestire le interazioni tra lo Slime e il giocatore.
     */
    private final PlayerController playerController;

    /**
     * Inizializza il controller con i riferimenti alla stanza di gioco e al controller del giocatore.
     * @param chamber riferimento della stanza
     * @param playerController riferimento al controllo del giocatore
     */
    public SlimeController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }

    /**
     * Gestisce le interazioni dello Slime con il giocatore.
     * @param player il giocatore che interagisce con lo Slime.
     * @param slime lo Slime che subisce l'interazione.
     */
    public void playerInInteraction(Player player, Slime slime) {
        switch (player.getCurrentEumState()) {
            // Se il giocatore è in stato di attacco, lo Slime viene danneggiato in base al danno del giocatore.
            case Player.EnumState.ATTACK -> {
                slime.setDirection(DirectionsModel.positionToDirection(player, slime));
                hitSlime(slime, -1 * player.getDamage());
            }
            default -> System.out.println("[!] Lo slimeController non gestisce questa azione: " + player.getCurrentEumState());
        }
    }

    /**
     * Aggiorna lo stato dello Slime a ogni ciclo di gioco.
     * @param slime lo Slime da aggiornare.
     */
    public void update(Slime slime) {
        if (!slime.isAlive()) {
            if (slime.getState(Slime.EnumState.DEAD).isFinished()) {
                chamber.removeEntityOnTop(slime);
                slime.removeToUpdate();
                return;
            }
        } else if (slime.getHealth() <= 0 && slime.checkAndChangeState(Slime.EnumState.DEAD)) {
            slime.kill();
        }

        if (slime.canChange(Slime.EnumState.MOVE)) {
            DirectionsModel direction = chamber.getHitDirectionPlayer(slime);
            if (direction == null) {
                if (chamber.moveRandom(slime)) {
                    slime.changeState(Slime.EnumState.MOVE);
                }
            }
            else if (slime.canAttack() && slime.getState(Slime.EnumState.ATTACK).isFinished()) {
                Entity entity = chamber.getNearEntityOnTop(slime, direction);
                if (entity instanceof Player) {
                    playerController.handleInteraction(InteractionTypes.ENEMY, slime);
                    slime.setCanAttack(false);
                }
            }
            else if (slime.canChange(Slime.EnumState.ATTACK)) {
                Entity entity = chamber.getNearEntityOnTop(slime, direction);
                if (entity instanceof Player && slime.changeState(Slime.EnumState.ATTACK)) {
                    slime.setCanAttack(true);
                }
            }
        }
        slime.checkAndChangeState(Slime.EnumState.IDLE);
    }

    /**
     * Gestisce le interazioni dello Slime con i proiettili.
     * @param projectile il proiettile che colpisce lo Slime.
     * @param slime lo Slime che subisce l'interazione.
     */
    public void projectileInteraction(Projectile projectile, Slime slime) {
        slime.setDirection(DirectionsModel.positionToDirection(projectile, slime));
        hitSlime(slime, -1 * projectile.getDamage());
    }

    /**
     * Applica danno allo Slime e aggiorna il suo stato.
     * @param slime lo Slime che subisce il danno.
     * @param damage il danno da applicare.
     */
    private void hitSlime(Slime slime, int damage) {
        if (slime.changeState(Slime.EnumState.HIT))
            slime.changeHealth(damage);
    }

    public void trapInteraction(Trap trap, Slime slime) {
        switch (trap.getSpecificType()) {
            case Trap.Type.SPIKED_FLOOR -> {
                hitSlime(slime, -1 * trap.getDamage());
            }
            default -> {}
        }
    }
}
