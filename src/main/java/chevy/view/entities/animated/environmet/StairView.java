package chevy.view.entities.animated.environmet;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.staticEntity.environment.Stair;
import chevy.utils.Vector2;

public final class StairView extends EnvironmentView {
    private static final String RES = "/sprites/stair/";

    public StairView(Stair stair) {super(stair);}

    @Override
    protected void initializeAnimation() {
        Vector2<Integer> offset = new Vector2<>(0, -5);
        String folder = "right/";

        if (environment.getDirection() == Direction.LEFT) {
            offset = new Vector2<>(-16, -5);
            folder = "left/";
        }

        animate(Stair.State.IDLE, null, 1, environment.getState(Stair.State.IDLE).getDuration(),
                offset, RES + folder + "idle");

        animate(Stair.State.OPEN, null, 6, environment.getState(Stair.State.OPEN).getDuration(),
                offset, RES + folder + "open");

        animate(Stair.State.IDLE_ENTRY, null, 6,
                environment.getState(Stair.State.IDLE_ENTRY).getDuration(), offset,
                RES + folder + "idleEntry");
    }
}
