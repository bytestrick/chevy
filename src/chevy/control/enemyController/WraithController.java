package chevy.control.enemyController;

import chevy.control.InteractionTypes;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Wraith;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;

public class WraithController {
    private final Chamber chamber;
    private final PlayerController playerController;


    public WraithController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }


    public void playerInInteraction(Player player, Wraith wraith) {
        switch (player.getCurrentEumState()) {
            case Player.EnumState.ATTACK -> {
                wraith.setDirection(DirectionsModel.positionToDirection(player, wraith));
                hitBat(wraith, -1 * player.getDamage());
            }
            default -> System.out.println("[!] Il WraithController non gestisce questa azione: " + player.getCurrentEumState());
        }
    }

    public void update(Wraith wraith) {
        if (!wraith.isAlive()) {
            if (wraith.getState(Wraith.EnumState.DEAD).isFinished()) {
                chamber.removeEntityOnTop(wraith);
                wraith.removeToUpdate();
                return;
            }
        }
        else if (wraith.getHealth() <= 0 && wraith.checkAndChangeState(Wraith.EnumState.DEAD)) {
            wraith.kill();
        }

        if (wraith.canChange(Wraith.EnumState.MOVE)) {
            DirectionsModel direction = chamber.getHitDirectionPlayer(wraith);
            if (direction == null) {
                if (chamber.moveRandomPlus(wraith)) {
                    wraith.changeState(Wraith.EnumState.MOVE);
                }
            }
            else if (wraith.canChange(Wraith.EnumState.ATTACK)) {
                Entity entity = chamber.getNearEntityOnTop(wraith, direction);
                if (entity instanceof Player && wraith.changeState(Wraith.EnumState.ATTACK)) {
                    playerController.handleInteraction(InteractionTypes.ENEMY, wraith);
                }
            }
        }
        wraith.checkAndChangeState(Wraith.EnumState.IDLE);
    }

    public void projectileInteraction(Projectile projectile, Wraith wraith) {
        wraith.setDirection(DirectionsModel.positionToDirection(projectile, wraith));
        hitBat(wraith, -1 * projectile.getDamage());
    }

    private void hitBat(Wraith wraith, int damage) {
        if (wraith.changeState(Wraith.EnumState.HIT))
            wraith.changeHealth(damage);
    }
}
