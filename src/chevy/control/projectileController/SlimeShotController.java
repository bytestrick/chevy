package chevy.control.projectileController;

import chevy.control.InteractionType;
import chevy.control.PlayerController;
import chevy.control.enemyController.EnemyController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.projectile.SlimeShot;

/**
 * Gestisce le interazioni e gli aggiornamenti specifici dei proiettili di tipo Slime Shot nel gioco.
 */
public class SlimeShotController {
    private final Chamber chamber;
    private final PlayerController playerController;
    private final EnemyController enemyController;

    /**
     * @param chamber          stanza in cui si trova lo slime shot
     * @param playerController il controller del giocatore
     * @param enemyController  il controller dei nemici
     */
    public SlimeShotController(Chamber chamber, PlayerController playerController, EnemyController enemyController) {
        this.chamber = chamber;
        this.playerController = playerController;
        this.enemyController = enemyController;
    }

    /**
     * Gestisce l'interazione del giocatore con lo Slime Shot.
     *
     * @param projectile il proiettile che viene colpito dal giocatore
     */
    public void playerInInteraction(SlimeShot projectile) {
        if (projectile.changeState(SlimeShot.State.END)) {
            chamber.findAndRemoveEntity(projectile);
            projectile.setCollision(true);
            playerController.handleInteraction(InteractionType.PROJECTILE, projectile);
        }
    }

    /**
     * Gestisce l'aggiornamento dello Slime Shot.
     *
     * @param projectile Slime Shot da aggiornare
     */
    public void update(SlimeShot projectile) {
        if (projectile.checkAndChangeState(SlimeShot.State.LOOP)) {
            Entity nextEntity = chamber.getNearEntityOnTop(projectile, projectile.getDirection());

            switch (nextEntity.getGenericType()) {
                case LiveEntity.Type.PLAYER ->
                        playerController.handleInteraction(InteractionType.PROJECTILE, projectile);
                case LiveEntity.Type.ENEMY ->
                        enemyController.handleInteraction(InteractionType.PROJECTILE, projectile, (Enemy) nextEntity);
                default -> { }
            }

            if (nextEntity.isCrossable()) {
                chamber.moveDynamicEntity(projectile, projectile.getDirection());
            } else if (projectile.changeState(SlimeShot.State.END)) {
                chamber.findAndRemoveEntity(projectile);
                projectile.setCollision(true);
            }
        }
    }
}
