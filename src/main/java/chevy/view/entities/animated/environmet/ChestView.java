package chevy.view.entities.animated.environmet;

import chevy.model.entity.staticEntity.environment.Chest;
import chevy.model.entity.staticEntity.environment.Chest.State;

import java.awt.Point;

public final class ChestView extends EnvironmentView {
    public ChestView(Chest chest) {
        super(chest);

        final String res = "/sprites/chest/";
        final Point offset = new Point(0, -4);
        float idleDuration = 0f;
        animate(State.IDLE_LOCKED, null, 1, idleDuration, offset, res + "idle/locked");
        animate(State.IDLE_UNLOCKED, null, 1, idleDuration, offset, res + "idle/unlocked");
        animate(State.CLOSE, null, 5, environment.getState(State.CLOSE).getDuration(), offset,
                res + "close");
        animate(State.OPEN, null, 6, environment.getState(State.OPEN).getDuration(), offset,
                res + "open");
        animate(State.UNLOCK, null, 3, environment.getState(State.UNLOCK).getDuration(), offset,
                res + "unlock");
    }
}