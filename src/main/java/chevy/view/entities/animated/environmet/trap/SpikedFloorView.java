package chevy.view.entities.animated.environmet.trap;

import chevy.model.entity.staticEntity.environment.traps.SpikedFloor;
import chevy.model.entity.staticEntity.environment.traps.SpikedFloor.State;

import java.awt.Point;

public final class SpikedFloorView extends TrapView {
    public SpikedFloorView(SpikedFloor spikedFloor) {
        super(spikedFloor);

        Point offset = new Point(0, -7);
        String res = "/sprites/traps/spikedFloor/";

        float disabledDuration = trap.getState(State.DISABLED).getDuration();
        animate(State.DISABLED, null, 1, disabledDuration, offset, res + "disabled");

        float activatedDuration = trap.getState(State.ACTIVATED).getDuration();
        animate(State.ACTIVATED, null, 2, activatedDuration, offset, res + "active");

        float damageDuration = trap.getState(State.DAMAGE).getDuration();
        animate(State.DAMAGE, null, 1, damageDuration, offset, res + "damage");
    }
}
