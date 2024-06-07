package chevy.control.projectileController;

import chevy.control.PlayerController;
import chevy.control.enemyController.EnemyController;
import chevy.control.enemyController.InteractionType;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntityTypes;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
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


    private boolean checkCollision(Projectile projectile) {
        Entity[] currentEntity = {
                chamber.getEntityBelowTheTop(projectile),
                chamber.getEntityOnTop(projectile)
        };
        int cont = 0;

        for (Entity c : currentEntity) {
            switch (c.getGenericType()) {
                case LiveEntityTypes.PLAYER ->
                    playerController.handleInteraction(InteractionType.PROJECTILE, projectile, (Player) c);
                case LiveEntityTypes.ENEMY -> {
                }
                default -> ++cont;
            }
        }
        return cont != currentEntity.length;
    }


    public void update(Projectile projectile) {
        boolean remove = checkCollision(projectile);

        if (!remove && chamber.canCross(projectile, projectile.getDirection())) {
            chamber.moveDynamicEntity(projectile, projectile.getDirection());
        }
        else if (!remove) {
            chamber.moveDynamicEntity(projectile, projectile.getDirection());
            checkCollision(projectile);
            remove = true;
        }

        if (remove) {
            chamber.findAndRemoveEntity(projectile);
            chamber.removeFromProjectiles(projectile);
        }
    }
}
