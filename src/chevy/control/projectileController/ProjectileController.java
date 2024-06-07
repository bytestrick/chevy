package chevy.control.projectileController;

import chevy.control.PlayerController;
import chevy.control.enemyController.EnemyController;
import chevy.control.enemyController.InteractionType;
import chevy.control.trapsController.*;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.entity.dinamicEntity.projectile.ProjectileTypes;
import chevy.model.entity.staticEntity.environment.traps.*;
import chevy.model.entity.staticEntity.environment.traps.Void;

public class ProjectileController {
    private final ArrowController arrowController;


    public ProjectileController(Chamber chamber, PlayerController playerController, EnemyController enemyController) {
        this.arrowController = new ArrowController(chamber, playerController, enemyController);
    }


    public synchronized void updateProjectile(Projectile projectile) {
        switch (projectile.getSpecificType()) {
            case ProjectileTypes.ARROW -> arrowController.update(projectile);
            case ProjectileTypes.FIRE_BALL -> {}
            default -> {}
        }
    }
}
