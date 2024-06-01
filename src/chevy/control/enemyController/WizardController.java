package chevy.control.enemyController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Wizard;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;

public class WizardController {
    private final Chamber chamber;
    private Wizard wizard;


    public WizardController(Chamber chamber) {
        this.chamber = chamber;
    }

    public void playerInteraction(PlayerStates action, int value) {

    }

    public void enemyUpdate(EnemyUpdateController enemyUpdateController) {

    }

    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }
}
