package chevy.view.entities.animated.environmet.trap;

import chevy.model.entity.staticEntity.environment.traps.IcyFloor;
import chevy.model.entity.staticEntity.environment.traps.IcyFloor.State;

public final class IcyFloorView extends TrapView {
    public IcyFloorView(IcyFloor icyFloor) {
        super(icyFloor);

        String res = "/sprites/traps/icyFloor/";
        float icyFloorDuration = trap.getState(State.ICY_FLOOR).getDuration();
        animate(State.ICY_FLOOR, null, 1, icyFloorDuration, res + "base");

        float sparklingDuration = trap.getState(State.ICY_FLOOR_SPARKLING).getDuration();
        animate(State.ICY_FLOOR_SPARKLING, null, 8, sparklingDuration, res + "sparkling");
    }
}
