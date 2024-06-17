package chevy.control.enemyController;

import chevy.control.InteractionType;
import chevy.control.PlayerController;
import chevy.model.Timer;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;
import chevy.model.entity.dinamicEntity.stateMachine.SlimeStates;

public class SlimeController {
    private final Chamber chamber;
    private final PlayerController playerController;

    private final Timer moveTimer = new Timer(2);


    public SlimeController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }


    public void playerInInteraction(Player player, Slime slime) {
        switch (player.getCurrentEumState()) {
            case PlayerStates.ATTACK ->
                hitSlime(slime, -1 * player.getDamage());
            default -> System.out.println("Lo slimeController non gestisce questa azione");
        }
    }

    public void update(Slime slime) {
        if (slime.canChange(SlimeStates.MOVE)) {
            DirectionsModel direction = chamber.getHitDirectionPlayer(slime);
            if (direction == null) {
                if (chamber.moveRandom(slime))
                    slime.changeState(SlimeStates.MOVE);
            }
            else if (slime.canChange(SlimeStates.ATTACK)) {
                Entity entity = chamber.getNearEntityOnTop(slime, chamber.getHitDirectionPlayer(slime));
                if (entity instanceof Player && slime.changeState(SlimeStates.ATTACK)) {
                    playerController.handleInteraction(InteractionType.ENEMY, slime);
                }
            }
        }
        if (slime.canChange(SlimeStates.IDLE)) {
            slime.changeState(SlimeStates.IDLE);
        }
    }

    public void projectileInteraction(Projectile projectile, Slime slime) {
        hitSlime(slime, -1 * projectile.getDamage());
    }

    private void hitSlime(Slime slime, int damage) {
        if (slime.changeState(SlimeStates.HIT))
            slime.changeHealth(damage);
        if (!slime.isAlive() && slime.changeState(SlimeStates.DEAD)) {
            chamber.removeEnemy(slime);
            chamber.removeEntityOnTop(slime);
        }
        else
            slime.changeState(SlimeStates.IDLE);
    }
}
