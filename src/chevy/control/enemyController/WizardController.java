package chevy.control.enemyController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Wizard;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;

public class WizardController {
    private final Chamber chamber;
    private Wizard wizard;


    public WizardController(Chamber chamber) {
        this.chamber = chamber;
    }

    public void playerInInteraction(Player.EnumState action, int value) {

    }

    public void enemyUpdate() {

    }

    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }
}
