package chevy.control.enemyController;

import chevy.control.InteractionTypes;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Wraith;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;

/**
 * La classe WraithController è responsabile della gestione del comportamento e delle interazioni
 * del Wraith all'interno del gioco. Gestisce come il Wraith risponde agli attacchi del giocatore,
 * ai colpi dei proiettili e coordina il suo stato e i suoi movimenti.
 */
public class WraithController {
    /**
     * Riferimento alla stanza di gioco in cui si trova il Wraith. Utilizzato per verificare le posizioni,
     * aggiungere/rimuovere entità.
     */
    private final Chamber chamber;
    /**
     * Riferimento al controller del giocatore, utilizzato per gestire le interazioni tra il Wraith e il giocatore.
     */
    private final PlayerController playerController;

    /**
     * Inizializza il controller con i riferimenti alla stanza di gioco e al controller del giocatore.
     * @param chamber riferimento della stanza
     * @param playerController riferimento al controllo del giocatore
     */
    public WraithController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }

    /**
     * Gestisce le interazioni del Wraith con il giocatore.
     * @param player il giocatore che interagisce con il Wraith.
     * @param wraith il Wraith che subisce l'interazione.
     */
    public void playerInInteraction(Player player, Wraith wraith) {
        switch (player.getCurrentEumState()) {
            // Se il giocatore è in stato di attacco, il Wraith viene danneggiato in base al danno del giocatore.
            case Player.EnumState.ATTACK -> {
                wraith.setDirection(DirectionsModel.positionToDirection(player, wraith));
                hitBat(wraith, -1 * player.getDamage());
            }
            default -> System.out.println("[!] Il WraithController non gestisce questa azione: " + player.getCurrentEumState());
        }
    }

    /**
     * Aggiorna lo stato del Wraith a ogni ciclo di gioco.
     * @param wraith il Wraith da aggiornare.
     */
    public void update(Wraith wraith) {
        if (!wraith.isAlive()) {
            if (wraith.getState(Wraith.EnumState.DEAD).isFinished()) {
                chamber.removeEntityOnTop(wraith);
                wraith.removeToUpdate();
                return;
            }
        } else if (wraith.getHealth() <= 0 && wraith.checkAndChangeState(Wraith.EnumState.DEAD)) {
            wraith.kill();
        }

        if (wraith.canChange(Wraith.EnumState.MOVE)) {
            DirectionsModel direction = chamber.getHitDirectionPlayer(wraith);
            if (direction == null) {
                if (chamber.moveRandomPlus(wraith)) {
                    wraith.changeState(Wraith.EnumState.MOVE);
                }
            } else if (wraith.canChange(Wraith.EnumState.ATTACK)) {
                Entity entity = chamber.getNearEntityOnTop(wraith, direction);
                if (entity instanceof Player && wraith.changeState(Wraith.EnumState.ATTACK)) {
                    playerController.handleInteraction(InteractionTypes.ENEMY, wraith);
                }
            }
        }
        wraith.checkAndChangeState(Wraith.EnumState.IDLE);
    }

    /**
     * Gestisce le interazioni del Wraith con i proiettili.
     * @param projectile il proiettile che colpisce il Wraith.
     * @param wraith il Wraith che subisce l'interazione.
     */
    public void projectileInteraction(Projectile projectile, Wraith wraith) {
        wraith.setDirection(DirectionsModel.positionToDirection(projectile, wraith));
        hitBat(wraith, -1 * projectile.getDamage());
    }

    /**
     * Applica danno al Wraith e aggiorna il suo stato.
     * @param wraith il Wraith che subisce il danno.
     * @param damage il danno da applicare.
     */
    private void hitBat(Wraith wraith, int damage) {
        if (wraith.changeState(Wraith.EnumState.HIT))
            wraith.changeHealth(damage);
    }
}
