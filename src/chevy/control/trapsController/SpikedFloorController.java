package chevy.control.trapsController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.EntityTypes;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.EnemyTypes;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.stateMachine.BatStates;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;
import chevy.model.entity.dinamicEntity.stateMachine.ZombieStates;
import chevy.model.entity.staticEntity.environment.traps.SpikedFloor;
import chevy.utilz.Vector2;

public class SpikedFloorController {
    private final Chamber chamber;

    public SpikedFloorController(Chamber chamber) {
        this.chamber = chamber;
    }


    public void playerInInteraction(Player player) {
        player.changeState(PlayerStates.IDLE);
    }


    public void update(SpikedFloor spikedFloor) {
        spikedFloor.toggleStateActive();

        if (spikedFloor.isActive()) {
            Entity entity = chamber.getEntityOnTop(spikedFloor);
            boolean mayBeAttacked = false;
            if (entity instanceof LiveEntity liveEntity) {
                if (liveEntity instanceof Player) {
                    if (liveEntity.changeState(PlayerStates.HIT))
                        mayBeAttacked = true;
                }
                else
                    switch (liveEntity.getSpecificType()) {
                        case EnemyTypes.ZOMBIE -> {
                            if (liveEntity.changeState(ZombieStates.HIT))
                                mayBeAttacked = true;
                        }
                        default -> {}
                    }

                if (mayBeAttacked) {
                    liveEntity.changeHealth(-1 * spikedFloor.getDamage());
                    liveEntity.changeToPreviousState();
                }
            }
        }
    }
}
