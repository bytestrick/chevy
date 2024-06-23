package chevy.control.enemyController;

import chevy.control.InteractionTypes;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.BigSlime;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;

public class BigSlimeController {
    private final Chamber chamber;
    private final PlayerController playerController;


    public BigSlimeController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }


    public void playerInInteraction(Player player, BigSlime bigSlime) {
        switch (player.getCurrentEumState()) {
            case Player.EnumState.ATTACK -> {
                hitBigSlime(bigSlime, -1 * player.getDamage());
            }
            default -> System.out.println("Il BigSlimeController non gestisce questa azione");
        }
    }

    public void update(BigSlime bigSlime) {
        // attacca se hai il player di fianco
        DirectionsModel direction = chamber.getHitDirectionPlayer(bigSlime);
        if (direction != null) {
            if (bigSlime.changeState(BigSlime.EnumState.ATTACK)) {
                playerController.handleInteraction(InteractionTypes.ENEMY, bigSlime);
            }
        }
        // altrimenti muoviti
        else if (bigSlime.changeState(BigSlime.EnumState.MOVE))
                chamber.wanderChase(bigSlime, 3);

        bigSlime.changeState(BigSlime.EnumState.IDLE);
    }

    public void projectileInteraction(Projectile projectile, BigSlime bigSlime) {
        hitBigSlime(bigSlime, -1 * projectile.getDamage());
    }

    private void hitBigSlime(BigSlime bigSlime, int damage) {
        if (bigSlime.changeState(BigSlime.EnumState.HIT))
            bigSlime.changeHealth(damage);
        if (!bigSlime.isAlive() && bigSlime.changeState(BigSlime.EnumState.DEAD)) {
            chamber.spawnSlimeAroundEntity(bigSlime, 2);
//            chamber.removeEnemy(bigSlime);
            chamber.removeEntityOnTop(bigSlime);
        }
        else
            bigSlime.changeState(BigSlime.EnumState.IDLE);
    }
}
