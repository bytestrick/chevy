package chevy.control.enemyController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;
import chevy.model.entity.dinamicEntity.stateMachine.SlimeStates;

public class SlimeController {
    private final Chamber chamber;
    private Slime slime;

    public SlimeController(Chamber chamber) {
        this.chamber = chamber;
    }


    public void playerInteraction(PlayerStates action, int value) {
        if (slime == null)
            return;
        // ---

        switch (action) {
            case ATTACK -> {
                if (slime.changeState(SlimeStates.HIT))
                    slime.changeHealth(-1 * value);
                if (!slime.isAlive() && slime.changeState(SlimeStates.DEAD)) {
                    chamber.removeEnemyFormEnemies(slime);
                    chamber.removeEntityOnTop(slime);
                }
                else
                    slime.changeState(SlimeStates.IDLE);
            }
            default -> System.out.println("Lo SlimeController non gestisce questa azione");
        }

        // ---
        slime = null;
    }

    public void enemyUpdate() {

    }

    public void setSlime(Slime slime) {
        this.slime = slime;
    }
}
