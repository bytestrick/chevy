package chevy.view.entities.animated.projectile;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.projectile.Projectile.State;
import chevy.model.entity.dynamicEntity.projectile.SlimeShot;

import java.awt.*;

public final class SlimeShotView extends ProjectileView {
    public SlimeShotView(SlimeShot slimeShot) {
        super(slimeShot);

        String res = "/sprites/projectile/slimeShot/";
        float startDuration = projectile.getState(State.START).getDuration();
        Point[] loopStartOffsets = new Point[]{new Point(1, -16), new Point(18, 2), new Point(1, 16), new Point(-16, 2)};
        Point[] loopEndOffsets = new Point[]{new Point(2, 0), new Point(4, 2), new Point(2, 4), new Point(0, 2)};
        float loopDuration = projectile.getState(State.LOOP).getDuration();
        float endDuration = projectile.getState(State.END).getDuration();

        for (Direction direction : Direction.values()) {
            String dir = direction.toString().toLowerCase();
            int i = direction.ordinal();
            animate(State.START, direction, 4, startDuration, loopStartOffsets[i], res + "start/" + dir);
            animate(State.LOOP, direction, 4, true, 3, loopDuration, loopEndOffsets[i], 1, res + "loop/" + dir);
            animate(State.END, direction, 5, endDuration, loopEndOffsets[i], res + "end");
        }
    }
}
