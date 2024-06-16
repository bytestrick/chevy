package chevy.control.enemyController;

import chevy.control.InteractionTypes;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Zombie;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;

public class ZombieController {
    private final Chamber chamber;
    private PlayerController playerController;


    public ZombieController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }


    public void playerInInteraction(Player player, Zombie zombie) {
        switch (player.getCurrentEumState()) {
            case Player.EnumState.ATTACK ->
                    hitZombie(zombie, -1 * player.getDamage());
            default -> System.out.println("Lo ZombieController non gestisce questa azione");
        }
    }

    public void update(Zombie zombie) {
        // attacca se hai il player di fianco
        DirectionsModel direction = chamber.getHitDirectionPlayer(zombie);
        if (direction != null) {
            if (zombie.changeState(Zombie.EnumState.ATTACK)) {
                playerController.handleInteraction(InteractionTypes.ENEMY, zombie);
            }
        }
        // altrimenti muoviti
        else if (zombie.changeState(Zombie.EnumState.MOVE))
            chamber.wanderChase(zombie, 5);

        zombie.changeState(Zombie.EnumState.IDLE);
    }

    public void projectileInteraction(Projectile projectile, Zombie zombie) {
        hitZombie(zombie, -1 * projectile.getDamage());
    }

    private void hitZombie(Zombie zombie, int damage) {
        if (zombie.changeState(Zombie.EnumState.HIT))
            zombie.changeHealth(damage);
        if (!zombie.isAlive() && zombie.changeState(Zombie.EnumState.DEAD)) {
            chamber.removeEnemy(zombie);
            chamber.removeEntityOnTop(zombie);
        }
        else
            zombie.changeState(Zombie.EnumState.IDLE);
    }
}
