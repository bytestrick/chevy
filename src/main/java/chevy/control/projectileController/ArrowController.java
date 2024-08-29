package chevy.control.projectileController;

import chevy.control.InteractionType;
import chevy.control.PlayerController;
import chevy.control.enemyController.EnemyController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dynamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dynamicEntity.projectile.Arrow;

/**
 * Gestisce le frecce (Projectile) nel gioco, inclusa la gestione delle collisioni e degli aggiornamenti delle loro
 * posizioni.
 */
public class ArrowController {
    /**
     * Riferimento alla stanza di gioco in cui si trova la freccia.
     */
    private final Chamber chamber;
    /**
     * Riferimento al controller del giocatore, utilizzato per gestire le interazioni tra la freccia e il giocatore.
     */
    private final PlayerController playerController;
    /**
     * Riferimento al controller del nemico, utilizzato per gestire le interazioni tra la freccia e i nemici.
     */
    private final EnemyController enemyController;

    /**
     * @param chamber          la stanza di gioco
     * @param playerController il controller del giocatore
     * @param enemyController  il controller dei nemici
     */
    public ArrowController(Chamber chamber, PlayerController playerController, EnemyController enemyController) {
        this.chamber = chamber;
        this.playerController = playerController;
        this.enemyController = enemyController;
    }

    /**
     * Gestisce le interazioni della freccia con il giocatore.
     *
     * @param arrow il proiettile (freccia) che interagisce con il giocatore
     */
    public void playerInInteraction(Arrow arrow) {
        if (arrow.changeState(Arrow.State.END)) {
            chamber.findAndRemoveEntity(arrow);
            arrow.setCollision(true);
            playerController.handleInteraction(InteractionType.PROJECTILE, arrow);
        }
    }

    /**
     * Aggiorna lo stato della freccia a ogni ciclo di gioco, gestendo la sua posizione e le sue collisioni.
     *
     * @param arrow il proiettile (freccia) da aggiornare
     */
    public void update(Arrow arrow) {
        if (arrow.checkAndChangeState(Arrow.State.LOOP)) {
            Entity nextEntity = chamber.getNearEntityOnTop(arrow, arrow.getDirection());

            switch (nextEntity.getGenericType()) {
                case LiveEntity.Type.PLAYER -> playerController.handleInteraction(InteractionType.PROJECTILE, arrow);
                case LiveEntity.Type.ENEMY ->
                        enemyController.handleInteraction(InteractionType.PROJECTILE, arrow, (Enemy) nextEntity);
                default -> { }
            }

            if (nextEntity.isCrossable()) {
                chamber.moveDynamicEntity(arrow, arrow.getDirection());
            } else if (arrow.changeState(Arrow.State.END)) {
                chamber.findAndRemoveEntity(arrow);
                arrow.setCollision(true);
            }
        }
    }
}
