package chevy.control.enemyController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Bat;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.stateMachine.BatStates;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;

public class BatController {
    private final Chamber chamber;
    private Bat bat;

    public BatController(Chamber chamber) {
        this.chamber = chamber;
    }


    public void playerInteraction(PlayerStates action, int value) {
        if (bat == null)
            return;
        // ---

        switch (action) {
            case ATTACK -> {
                if (bat.changeState(BatStates.HIT))
                    bat.changeHealth(-1 * value);
                if (!bat.isAlive() && bat.changeState(BatStates.DEAD)) {
                    chamber.removeEnemyFormEnemies(bat);
                    chamber.removeEntityOnTop(bat);
                }
                else
                    bat.changeState(BatStates.IDLE);
            }
            default -> System.out.println("Il BatController non gestisce questa azione");
        }

        // ---
        bat = null;
    }

    public void enemyUpdate(EnemyUpdateController enemyUpdateController) {
        if (bat == null)
            return;
        if (!bat.isAlive()) {
            enemyUpdateController.stop();
            return;
        }

        System.out.println("Sono stato aggiornato");

        bat = null;
    }

    public void setBat(Bat bat) {
        this.bat = bat;
    }
}
