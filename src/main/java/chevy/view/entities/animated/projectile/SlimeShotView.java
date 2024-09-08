package chevy.view.entities.animated.projectile;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.projectile.Projectile.State;
import chevy.model.entity.dynamicEntity.projectile.SlimeShot;
import chevy.utils.Vector2;

public final class SlimeShotView extends ProjectileView {
    private static final String RES = "/sprites/projectile/slimeShot/";

    public SlimeShotView(SlimeShot slimeShot) {super(slimeShot);}

    @Override
    protected void initializeAnimation() {
        final float startDuration = projectile.getState(State.START).getDuration();
        final Vector2<Integer>[] loopStartOffsets = new Vector2[]{new Vector2<>(1, -16),
                new Vector2<>(18, 2), new Vector2<>(1, 16), new Vector2<>(-16, 2)};
        final Vector2<Integer>[] loopEndOffsets = new Vector2[]{new Vector2<>(2, 0), new Vector2<>(4,
                2), new Vector2<>(2, 4), new Vector2<>(0, 2)};
        final float loopDuration = projectile.getState(State.LOOP).getDuration();
        final float endDuration = projectile.getState(State.END).getDuration();
        for (Direction direction : Direction.values()) {
            final String dir = direction.toString().toLowerCase();
            final int i = direction.ordinal();
            animate(State.START, direction, 4, startDuration, loopStartOffsets[i],
                    RES + "start/" + dir);
            animate(State.LOOP, direction, 4, true, 3, loopDuration, loopEndOffsets[i], 1, RES +
                    "loop/" + dir);
            animate(State.END, direction, 5, endDuration, loopEndOffsets[i], RES + "end/");
        }
    }
}