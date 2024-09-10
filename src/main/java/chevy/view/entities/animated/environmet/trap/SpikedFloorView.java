package chevy.view.entities.animated.environmet.trap;

import chevy.model.entity.staticEntity.environment.traps.SpikedFloor;
import chevy.model.entity.staticEntity.environment.traps.SpikedFloor.State;

import java.awt.Point;

public final class SpikedFloorView extends TrapView {
    public SpikedFloorView(SpikedFloor spikedFloor) {
        super(spikedFloor);

        final Point offset = new Point(0, -7);
        final String res = "/sprites/traps/spikedFloor/";

        final float disabledDuration = trap.getState(State.DISABLED).getDuration();
        animate(State.DISABLED, null, 1, disabledDuration, offset, res + "disabled");

        final float activatedDuration = trap.getState(State.ACTIVATED).getDuration();
        animate(State.ACTIVATED, null, 2, activatedDuration, offset, res + "active");

        final float damageDuration = trap.getState(State.DAMAGE).getDuration();
        animate(State.DAMAGE, null, 1, damageDuration, offset, res + "damage");
    }
}