package chevy.control.projectileController;

import chevy.control.PlayerController;
import chevy.control.enemyController.EnemyController;
import chevy.control.InteractionTypes;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntityTypes;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.projectile.Projectile;

public class ArrowController {
    private final Chamber chamber;
    private final PlayerController playerController;
    private final EnemyController enemyController;


    public ArrowController(Chamber chamber, PlayerController playerController, EnemyController enemyController) {
        this.chamber = chamber;
        this.playerController = playerController;
        this.enemyController = enemyController;
    }


    public void playerInInteraction(Projectile projectile) {
        chamber.findAndRemoveEntity(projectile);
        playerController.handleInteraction(InteractionTypes.PROJECTILE, projectile);
        projectile.setCollide(true);
    }

    public void update(Projectile projectile) {
        Entity nextEntity = chamber.getNearEntityOnTop(projectile, projectile.getDirection());

        switch (nextEntity.getGenericType()) {
            case LiveEntityTypes.PLAYER ->
                    playerController.handleInteraction(InteractionTypes.PROJECTILE, projectile);
            case LiveEntityTypes.ENEMY ->
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
