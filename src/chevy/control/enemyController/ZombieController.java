package chevy.control.enemyController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Zombie;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;
import chevy.model.entity.dinamicEntity.stateMachine.ZombieStates;

public class ZombieController {
    private final Chamber chamber;
    private Zombie zombie;


    public ZombieController(Chamber chamber) {
        this.chamber = chamber;
    }


    public void playerInteraction(PlayerStates action) {
        if (zombie == null)
            return;
        // ---

        switch (action) {
            case ATTACK -> {
                if (zombie.changeState(ZombieStates.HIT))
                    zombie.changeHealth(-5);
                if (!zombie.isAlive() && zombie.changeState(ZombieStates.DEAD))
                    chamber.removeEntityOnTop(zombie);
                else
                    zombie.changeState(ZombieStates.IDLE);
            }
            default -> System.out.println("Lo Zombie Controller non gestisce questa azione");
        }

        // ---
        zombie = null;
    }

    public void setZombie(Zombie zombie) {
        this.zombie = zombie;
    }
}
