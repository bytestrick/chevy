package chevy.control.enemyController;

import chevy.control.InteractionType;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Bat;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.entity.dinamicEntity.stateMachine.BatStates;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;

public class BatController  {
    private final Chamber chamber;
    private final PlayerController playerController;


    public BatController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }


    public void playerInInteraction(Player player, Bat bat) {
        switch (player.getCurrentEumState()) {
            case PlayerStates.ATTACK ->
                hitBat(bat, -1 * player.getDamage());
            default -> System.out.println("Il BatController non gestisce questa azione");
        }
    }

    public void update(Bat bat) {
        DirectionsModel direction = chamber.getHitDirectionPlayer(bat);
        if (direction == null) {
            chamber.moveRandomPlus(bat);
        }
        else {
            Entity entity = chamber.getNearEntityOnTop(bat, chamber.getHitDirectionPlayer(bat));
            if (entity instanceof Player && bat.changeState(BatStates.ATTACK)) {
                playerController.handleInteraction(InteractionType.ENEMY, bat);
            }
        }

        bat.changeState(BatStates.IDLE);
    }

    public void projectileInteraction(Projectile projectile, Bat bat) {
        hitBat(bat, -1 * projectile.getDamage());
    }

    private void hitBat(Bat bat, int damage) {
        if (bat.changeState(BatStates.HIT))
            bat.changeHealth(damage);
        if (!bat.isAlive() && bat.changeState(BatStates.DEAD)) {
            chamber.removeEnemy(bat);
            chamber.removeEntityOnTop(bat);
        }
        else
            bat.changeState(BatStates.IDLE);
    }
}
