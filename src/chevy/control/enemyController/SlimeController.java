package chevy.control.enemyController;

import chevy.control.InteractionTypes;
import chevy.control.PlayerController;
import chevy.model.Timer;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;

public class SlimeController {
    private final Chamber chamber;
    private final PlayerController playerController;


    public SlimeController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }


    public void playerInInteraction(Player player, Slime slime) {
        switch (player.getCurrentEumState()) {
            case Player.EnumState.ATTACK ->
                hitSlime(slime, -1 * player.getDamage());
            default -> System.out.println("Lo slimeController non gestisce questa azione");
        }
    }

    public void update(Slime slime) {
        if (!slime.isAlive()) {
            if (slime.getState(Slime.EnumState.DEAD).isFinished()) {
                chamber.removeEnemy(slime);
                chamber.removeEntityOnTop(slime);
                return;
            }
        }
        else if (slime.checkAndChangeState(Slime.EnumState.DEAD))
            slime.kill();

        if (slime.canChange(Slime.EnumState.MOVE)) {
            DirectionsModel direction = chamber.getHitDirectionPlayer(slime);
            if (direction == null) {
                if (chamber.moveRandom(slime)) {
                    slime.changeState(Slime.EnumState.MOVE);
                }
            }
            else if (slime.canChange(Slime.EnumState.ATTACK)) {
                Entity entity = chamber.getNearEntityOnTop(slime, chamber.getHitDirectionPlayer(slime));
                if (entity instanceof Player && slime.changeState(Slime.EnumState.ATTACK)) {
                    playerController.handleInteraction(InteractionTypes.ENEMY, slime);
                }
            }
        }
       slime.checkAndChangeState(Slime.EnumState.IDLE);
    }

    public void projectileInteraction(Projectile projectile, Slime slime) {
        hitSlime(slime, -1 * projectile.getDamage());
    }

    private void hitSlime(Slime slime, int damage) {
        if (slime.changeState(Slime.EnumState.HIT))
            slime.changeHealth(damage);
    }
}
