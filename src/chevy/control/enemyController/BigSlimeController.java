package chevy.control.enemyController;

import chevy.control.InteractionTypes;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.BigSlime;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.traps.Trap;

/**
 * La classe BigSlimeController è responsabile della gestione del comportamento e delle interazioni
 * del nemico BigSlime all'interno del gioco. Gestisce come il BigSlime risponde agli attacchi del giocatore,
 * ai colpi dei proiettili e coordina il suo stato e i suoi movimenti.
 */
public class BigSlimeController {
    /**
     *  Riferimento alla stanza di gioco in cui si trova il BigSlime. Utilizzato per verificare le posizioni,
     *  aggiungere/rimuovere entità e gestire le interazioni.
     */
    private final Chamber chamber;
    /**
     * Riferimento al controller del giocatore, utilizzato per gestire le interazioni tra il BigSlime e il giocatore.
     */
    private final PlayerController playerController;


    /**
     * Inizializza il controller con i riferimenti alla stanza di gioco e al controller del giocatore.
     * @param chamber riferimento della stanza
     * @param playerController riferimento al controllo del giocatore
     */
    public BigSlimeController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }

    /**
     * Gestisce le interazioni del BigSlime con il giocatore.
     * @param player il giocatore che interagisce con il BigSlime.
     * @param bigSlime il BigSlime che subisce l'interazione.
     */
    public void playerInInteraction(Player player, BigSlime bigSlime) {
        switch (player.getCurrentEumState()) {
            // Se il giocatore è in stato di attacco, il BigSlime viene danneggiato in base al danno del giocatore.
            case Player.EnumState.ATTACK -> {
                bigSlime.setDirection(DirectionsModel.positionToDirection(player, bigSlime));
                hitBigSlime(bigSlime, -1 * player.getDamage());
            }
            default -> System.out.println("[!] Il BigSlimeController non gestisce questa azione");
        }
    }

    /**
     * Aggiorna lo stato del BigSlime a ogni ciclo di gioco. Gestisce il cambiamento di stato, il movimento
     * e le azioni del BigSlime.
     * @param bigSlime il BigSlime da aggiornare.
     */
    public void update(BigSlime bigSlime) {
        // Gestione della morte del BigSlime
        if (!bigSlime.isAlive()) {
            if (bigSlime.getState(BigSlime.EnumState.DEAD).isFinished()) {
                chamber.removeEntityOnTop(bigSlime);
                bigSlime.removeToUpdate();
                return;
            }
        }
        else if (bigSlime.getHealth() <= 0 && bigSlime.checkAndChangeState(BigSlime.EnumState.DEAD)) {
            chamber.spawnSlimeAroundEntity(bigSlime, 2);
            bigSlime.kill();
        }

        // Movimento e attacco
        if (bigSlime.canChange(BigSlime.EnumState.MOVE)) {
            DirectionsModel direction = chamber.getHitDirectionPlayer(bigSlime);
            if (direction == null) {
                if (chamber.wanderChasePlus(bigSlime, 3)) {
                    bigSlime.changeState(BigSlime.EnumState.MOVE);
                }
            }
            else if (bigSlime.canAttack() && bigSlime.getState(BigSlime.EnumState.ATTACK).isFinished()) {
                Entity entity = chamber.getNearEntityOnTop(bigSlime, direction);
                if (entity instanceof Player) {
                    playerController.handleInteraction(InteractionTypes.ENEMY, bigSlime);
                    bigSlime.setCanAttack(false);
                }
            }
            else if (bigSlime.canChange(BigSlime.EnumState.ATTACK)) {
                Entity entity = chamber.getNearEntityOnTop(bigSlime, direction);
                if (entity instanceof Player && bigSlime.changeState(BigSlime.EnumState.ATTACK)) {
                    bigSlime.setCanAttack(true);
                }
            }
        }
        bigSlime.checkAndChangeState(BigSlime.EnumState.IDLE);
    }

    /**
     * Gestisce le interazioni del BigSlime con i proiettili.
     * @param projectile il proiettile che colpisce il BigSlime.
     * @param bigSlime il BigSlime che subisce l'interazione.
     */
    public void projectileInteraction(Projectile projectile, BigSlime bigSlime) {
        bigSlime.setDirection(DirectionsModel.positionToDirection(projectile, bigSlime));
        hitBigSlime(bigSlime, -1 * projectile.getDamage());
    }

    /**
     * Applica danno al BigSlime e cambia il suo stato a "colpito" se possibile.
     * @param bigSlime il BigSlime che subisce il danno.
     * @param damage la quantità di danno da applicare.
     */
    private void hitBigSlime(BigSlime bigSlime, int damage) {
        if (bigSlime.changeState(BigSlime.EnumState.HIT))
            bigSlime.changeHealth(damage);
    }

    public void trapInteraction(Trap trap, BigSlime bigSlime) {
        switch (trap.getSpecificType()) {
            case Trap.Type.SPIKED_FLOOR -> {
                hitBigSlime(bigSlime, -1 * trap.getDamage());
            }
            default -> {}
        }
    }
}
