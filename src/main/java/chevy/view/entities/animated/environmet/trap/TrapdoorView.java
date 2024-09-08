package chevy.view.entities.animated.environmet.trap;

import chevy.model.entity.staticEntity.environment.traps.Trapdoor;

public final class TrapdoorView extends TrapView {
    private static final String TRAPDOOR_PATH = "/sprites/traps/trapdoor/";

    public TrapdoorView(Trapdoor trapdoor) {super(trapdoor);}

    @Override
    protected void initializeAnimation() {
        final float idleDuration = trap.getState(Trapdoor.State.IDLE).getDuration();
        animate(Trapdoor.State.IDLE, null, 1, idleDuration, TRAPDOOR_PATH + "idle");

        final float openDuration = trap.getState(Trapdoor.State.OPEN).getDuration();
        animate(Trapdoor.State.OPEN, null, 3, openDuration, TRAPDOOR_PATH + "open");

        final float damageDuration = trap.getState(Trapdoor.State.DAMAGE).getDuration();
        animate(Trapdoor.State.DAMAGE, null, 1, damageDuration, TRAPDOOR_PATH + "damage");
    }
}