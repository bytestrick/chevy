package chevy.view.entities.animated.environmet.trap;

import chevy.model.entity.staticEntity.environment.traps.Sludge;

public final class SludgeView extends TrapView {
    public SludgeView(Sludge sludge) {
        super(sludge);

        animate(Sludge.State.SLUDGE_BUBBLES, null, 4,
                trap.getState(Sludge.State.SLUDGE_BUBBLES).getDuration(), "/sprites/traps/sludge");
    }
}
