package chevy.view.entities.animated.environmet.trap;

import chevy.model.entity.staticEntity.environment.traps.Trapdoor;

public final class TrapdoorView extends TrapView {
    public TrapdoorView(Trapdoor trapdoor) {
        super(trapdoor);

        final String res = "/sprites/traps/trapdoor/";
        animate(Trapdoor.State.IDLE, null, 1, trap.getState(Trapdoor.State.IDLE).getDuration(),
                res + "idle");
        animate(Trapdoor.State.OPEN, null, 3, trap.getState(Trapdoor.State.OPEN).getDuration(),
                res + "open");
        animate(Trapdoor.State.DAMAGE, null, 1,
                trap.getState(Trapdoor.State.DAMAGE).getDuration(), res + "damage");
    }
}