package chevy.control.enemyController;

import chevy.control.InteractionType;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.BigSlime;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.entity.dinamicEntity.stateMachine.BigSlimeStates;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;
import chevy.model.pathFinding.AStar;
import chevy.utilz.Vector2;

import java.util.List;

public class BigSlimeController {
    private final Chamber chamber;
    private final PlayerController playerController;


    public BigSlimeController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }


    public void playerInInteraction(Player player, BigSlime bigSlime) {
        switch (player.getCurrentEumState()) {
            case PlayerStates.ATTACK -> {
                hitBigSlime(bigSlime, -1 * player.getDamage());
            }
            default -> System.out.println("Il BigSlimeController non gestisce questa azione");
        }
    }

    public void update(BigSlime bigSlime) {
        // attacca se hai il player di fianco
        DirectionsModel direction = chamber.getHitDirectionPlayer(bigSlime);
        if (direction != null) {
            if (bigSlime.changeState(BigSlimeStates.ATTACK)) {
                playerController.handleInteraction(InteractionType.ENEMY, bigSlime);
            }
        }
        // altrimenti muoviti
        else if (bigSlime.changeState(BigSlimeStates.MOVE))
                chamber.wanderChase(bigSlime, 3);

        bigSlime.changeState(BigSlimeStates.IDLE);
    }

    public void projectileInteraction(Projectile projectile, BigSlime bigSlime) {
        hitBigSlime(bigSlime, -1 * projectile.getDamage());
    }

    private void hitBigSlime(BigSlime bigSlime, int damage) {
        if (bigSlime.changeState(BigSlimeStates.HIT))
            bigSlime.changeHealth(damage);
        if (!bigSlime.isAlive() && bigSlime.changeState(BigSlimeStates.DEAD)) {
            chamber.spawnSlimeAroundEntity(bigSlime, 2);
            chamber.removeEnemyFormEnemies(bigSlime);
            chamber.removeEntityOnTop(bigSlime);
        }
        else
            bigSlime.changeState(BigSlimeStates.IDLE);
    }
}
