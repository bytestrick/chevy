package chevy.control.projectileController;

import chevy.control.PlayerController;
import chevy.control.enemyController.EnemyController;
import chevy.control.InteractionTypes;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.projectile.Projectile;

public class ArrowController {
    private final Chamber chamber;
    private final PlayerController playerController;
    private final EnemyController enemyController;

    // TODO: da modificare il comportamento nell'update vista la modifica fatta alla gestione degli aggiornamenti delle classi Projectile

    public ArrowController(Chamber chamber, PlayerController playerController, EnemyController enemyController) {
        this.chamber = chamber;
        this.playerController = playerController;
        this.enemyController = enemyController;
    }


    public void playerInInteraction(Projectile projectile) {
        chamber.findAndRemoveEntity(projectile);
        projectile.setCollide(true);
        playerController.handleInteraction(InteractionTypes.PROJECTILE, projectile);
    }

    public void update(Projectile projectile) {
        Entity nextEntity = chamber.getNearEntityOnTop(projectile, projectile.getDirection());

        switch (nextEntity.getGenericType()) {
            case LiveEntity.Type.PLAYER ->
                    playerController.handleInteraction(InteractionTypes.PROJECTILE, projectile);
            case LiveEntity.Type.ENEMY ->
                    enemyController.handleInteraction(InteractionTypes.PROJECTILE, projectile, (Enemy) nextEntity);
            default -> {}
        }

        if (nextEntity.isCrossable()) {
            chamber.moveDynamicEntity(projectile, projectile.getDirection());
        }
        else {
            chamber.findAndRemoveEntity(projectile);
            projectile.setCollide(true);
        }
    }
}
