package chevy.control.enemyController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Frog;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;

public class FrogController {
    private final Chamber chamber;
    private  Frog frog;


    public FrogController(Chamber chamber) {
        this.chamber = chamber;
    }

    public void playerInInteraction(PlayerStates action, int value) {

    }

    public void enemyUpdate() {

    }

    public void setFrog(Frog frog) {
        this.frog = frog;
    }
}
