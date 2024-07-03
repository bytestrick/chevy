package chevy.control.projectileController;

import chevy.control.InteractionTypes;
import chevy.control.PlayerController;
import chevy.control.enemyController.EnemyController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.projectile.Projectile;

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
     * @param projectile il proiettile (freccia) che interagisce con il giocatore
     */
    public void playerInInteraction(Projectile projectile) {
        chamber.findAndRemoveEntity(projectile);
        projectile.setCollision(true);
        playerController.handleInteraction(InteractionTypes.PROJECTILE, projectile);
    }

    /**
     * Aggiorna lo stato della freccia a ogni ciclo di gioco, gestendo la sua posizione e le sue collisioni.
     *
     * @param projectile il proiettile (freccia) da aggiornare
     */
    public void update(Projectile projectile) {
        // Ottiene l'entità più vicina nella direzione della freccia.
        Entity nextEntity = chamber.getNearEntityOnTop(projectile, projectile.getDirection());

        switch (nextEntity.getGenericType()) {
            // Se la freccia colpisce il giocatore, gestisce l'interazione con il giocatore.
            case LiveEntity.Type.PLAYER -> playerController.handleInteraction(InteractionTypes.PROJECTILE, projectile);

            // Se la freccia colpisce un nemico, gestisce l'interazione con il nemico.
            case LiveEntity.Type.ENEMY ->
                    enemyController.handleInteraction(InteractionTypes.PROJECTILE, projectile, (Enemy) nextEntity);
            default -> { }
        }

        if (nextEntity.isCrossable()) {
            // Se l'entità successiva è attraversabile, muove la freccia.
            chamber.moveDynamicEntity(projectile, projectile.getDirection());
        } else {
            // Se l'entità non è attraversabile, rimuove la freccia e imposta lo stato di collisione.
            chamber.findAndRemoveEntity(projectile);
            projectile.setCollision(true);
        }
    }
}