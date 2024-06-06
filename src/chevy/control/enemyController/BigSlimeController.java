package chevy.control.enemyController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.BigSlime;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.stateMachine.BigSlimeStates;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;

public class BigSlimeController {
    private final Chamber chamber;


    public BigSlimeController(Chamber chamber) {
        this.chamber = chamber;
    }


    public void playerInInteraction(Player player, BigSlime bigSlime) {
        switch (player.getCurrentEumState()) {
            case PlayerStates.ATTACK -> {
                if (bigSlime.changeState(BigSlimeStates.HIT))
                    bigSlime.changeHealth(-1 * player.getDamage());
                if (!bigSlime.isAlive() && bigSlime.changeState(BigSlimeStates.DEAD)) {
                    chamber.spawnSlimeAroundEntity(bigSlime, 2);
                    chamber.removeEnemyFormEnemies(bigSlime);
                    chamber.removeEntityOnTop(bigSlime);
                }
                else
                    bigSlime.changeState(BigSlimeStates.IDLE);
            }
            default -> System.out.println("Il BigSlimeController non gestisce questa azione");
        }
    }

    public void enemyUpdate() {

    }
}
