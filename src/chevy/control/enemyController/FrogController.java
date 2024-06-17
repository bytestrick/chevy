package chevy.control.enemyController;

import chevy.control.InteractionType;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Frog;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.entity.dinamicEntity.stateMachine.FrogStates;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;

public class FrogController {
    private final Chamber chamber;
    private final PlayerController playerController;


    public FrogController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }

    public void playerInInteraction(Player player, Frog frog) {
        switch (player.getCurrentEumState()) {
            case PlayerStates.ATTACK ->
                    hitFrog(frog, -1 * player.getDamage());
            default -> System.out.println("Il BatController non gestisce questa azione");
        }
    }

    public void projectileInteraction(Projectile projectile, Frog frog) {
        hitFrog(frog, -1 * projectile.getDamage());
    }

    public void update(Frog frog) {
            // attacca se hai il player di fianco
            DirectionsModel direction = chamber.getHitDirectionPlayer(frog, 2);
            if (direction != null) {
                if (frog.changeState(FrogStates.ATTACK))
                    playerController.handleInteraction(InteractionType.ENEMY, frog);
            }
            // altrimenti muoviti:
            else if (frog.changeState(FrogStates.MOVE)) {
                chamber.chase(frog);
            }
        frog.changeState(FrogStates.IDLE);
    }

    private void hitFrog(Frog frog, int damage) {
        if (frog.changeState(FrogStates.HIT))
            frog.changeHealth(damage);
        if (!frog.isAlive() && frog.changeState(FrogStates.DEAD)) {
            chamber.removeEnemy(frog);
            chamber.removeEntityOnTop(frog);
        }
        else
            frog.changeState(FrogStates.IDLE);
    }
}
