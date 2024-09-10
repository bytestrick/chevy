package chevy.view.entities.animated.environmet.trap;

import chevy.model.entity.staticEntity.environment.traps.Sludge;

public final class SludgeView extends TrapView {
    public SludgeView(Sludge sludge) {
        super(sludge);

        final float sludgeDuration = trap.getState(Sludge.State.SLUDGE_BUBBLES).getDuration();
        animate(Sludge.State.SLUDGE_BUBBLES, null, 4, sludgeDuration, "/sprites/traps/sludge/");
    }
}