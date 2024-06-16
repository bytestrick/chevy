package chevy.control.trapsController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.EnemyTypes;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Zombie;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.staticEntity.environment.traps.SpikedFloor;

public class SpikedFloorController {
    private final Chamber chamber;

    public SpikedFloorController(Chamber chamber) {
        this.chamber = chamber;
    }


    public void playerInInteraction(Player player) {
        player.changeState(Player.EnumState.IDLE);
    }


    public void update(SpikedFloor spikedFloor) {
        spikedFloor.toggleStateActive();

        if (spikedFloor.isActive()) {
            Entity entity = chamber.getEntityOnTop(spikedFloor);
            boolean mayBeAttacked = false;
            if (entity instanceof LiveEntity liveEntity) {
                if (liveEntity instanceof Player) {
                    if (liveEntity.changeState(Player.EnumState.HIT))
                        mayBeAttacked = true;
                }
                else
                    switch (liveEntity.getSpecificType()) {
                        case EnemyTypes.ZOMBIE -> {
                            if (liveEntity.changeState(Zombie.EnumState.HIT))
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
