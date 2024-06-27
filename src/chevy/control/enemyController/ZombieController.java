package chevy.control.enemyController;

import chevy.control.InteractionTypes;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Zombie;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;

public class ZombieController {
    private final Chamber chamber;
    private final PlayerController playerController;


    public ZombieController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }


    public void playerInInteraction(Player player, Zombie zombie) {
        switch (player.getCurrentEumState()) {
            case Player.EnumState.ATTACK -> {
                zombie.setDirection(DirectionsModel.positionToDirection(player, zombie));
                hitZombie(zombie, -1 * player.getDamage());
            }
            default -> System.out.println("[!] Lo ZombieController non gestisce questa azione: " + player.getCurrentEumState());
        }
    }

    public void update(Zombie zombie) {
        if (!zombie.isAlive()) {
            if (zombie.getState(Zombie.EnumState.DEAD).isFinished()) {
                chamber.removeEntityOnTop(zombie);
                zombie.removeToUpdate();
                return;
            }
        }
        else if (zombie.getHealth() <= 0 && zombie.checkAndChangeState(Zombie.EnumState.DEAD)) {
            zombie.kill();
        }

        if (zombie.canChange(Zombie.EnumState.MOVE)) {
            DirectionsModel direction = chamber.getHitDirectionPlayer(zombie);
            if (direction == null) {
                if (chamber.wanderChase(zombie, 4)) {
                    zombie.changeState(Zombie.EnumState.MOVE);
                }
            }
            else if (zombie.canChange(Zombie.EnumState.ATTACK)) {
                Entity entity = chamber.getNearEntityOnTop(zombie, direction);
                if (entity instanceof Player && zombie.changeState(Zombie.EnumState.ATTACK)) {
                    playerController.handleInteraction(InteractionTypes.ENEMY, zombie);
                }
            }
        }
        zombie.checkAndChangeState(Zombie.EnumState.IDLE);
    }

    public void projectileInteraction(Projectile projectile, Zombie zombie) {
        zombie.setDirection(DirectionsModel.positionToDirection(projectile, zombie));
        hitZombie(zombie, -1 * projectile.getDamage());
    }

    private void hitZombie(Zombie zombie, int damage) {
        if (zombie.changeState(Zombie.EnumState.HIT))
            zombie.changeHealth(damage);
    }
}
