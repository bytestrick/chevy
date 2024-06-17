package chevy.control.projectileController;

import chevy.control.PlayerController;
import chevy.control.enemyController.EnemyController;
import chevy.control.InteractionType;
import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.entity.dinamicEntity.projectile.ProjectileTypes;

public class ProjectileController {
    private final ArrowController arrowController;


    public ProjectileController(Chamber chamber, PlayerController playerController, EnemyController enemyController) {
        this.arrowController = new ArrowController(chamber, playerController, enemyController);
    }


    public void handleInteraction(InteractionType interaction, DynamicEntity subject, DynamicEntity object) {
        switch (interaction) {
            case PLAYER_IN -> playerInInteraction((Player) subject, (Projectile) object);
            case UPDATE -> updateProjectile((Projectile) subject);
        }
    }

    private void playerInInteraction(Player player, Projectile projectile) {
        switch (projectile.getSpecificType()) {
            case ProjectileTypes.ARROW -> arrowController.playerInInteraction(projectile);
            case ProjectileTypes.FIRE_BALL -> {}
            default -> {}
        }
    }

    public synchronized void updateProjectile(Projectile projectilePair) {
        switch (projectilePair.getSpecificType()) {
            case ProjectileTypes.ARROW -> arrowController.update(projectilePair);
            case ProjectileTypes.FIRE_BALL -> {}
            default -> {}
        }
    }
}
