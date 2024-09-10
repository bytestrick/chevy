package chevy.view.entities.animated.environmet;

import chevy.model.entity.staticEntity.environment.Chest;
import chevy.model.entity.staticEntity.environment.Chest.State;

import java.awt.Point;

public final class ChestView extends EnvironmentView {
    private static final String RES = "/sprites/chest/";

    public ChestView(Chest chest) {super(chest);}

    @Override
    protected void initializeAnimation() {
        final Point offset = new Point(0, -4);
        float idleDuration = 0f;
        animate(State.IDLE_LOCKED, null, 1, idleDuration, offset, RES + "idle/locked");
        animate(State.IDLE_UNLOCKED, null, 1, idleDuration, offset, RES + "idle/unlocked");
        animate(State.CLOSE, null, 5, environment.getState(State.CLOSE).getDuration(), offset,
                RES + "close");
        animate(State.OPEN, null, 6, environment.getState(State.OPEN).getDuration(), offset,
                RES + "open");
        animate(State.UNLOCK, null, 3, environment.getState(State.UNLOCK).getDuration(), offset,
                RES + "unlock");
    }
}