package chevy.control.enemyController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.BigSlime;
import chevy.model.entity.dinamicEntity.stateMachine.BigSlimeStates;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;

public class BigSlimeController {
    private final Chamber chamber;
    private BigSlime bigSlime;


    public BigSlimeController(Chamber chamber) {
        this.chamber = chamber;
    }


    public void playerInteraction(PlayerStates action, int value) {
        if (bigSlime == null)
            return;
        // ---

        switch (action) {
            case ATTACK -> {
                if (bigSlime.changeState(BigSlimeStates.HIT))
                    bigSlime.changeHealth(-1 * value);
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

        // ---
        bigSlime = null;
    }

    public void enemyUpdate() {

    }


    public void setBigSlime(BigSlime bigSlime) {
        this.bigSlime = bigSlime;
    }
}
