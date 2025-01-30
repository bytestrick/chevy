package chevy.view.entities.animated.projectile;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.projectile.Arrow;

public final class ArrowView extends ProjectileView {
    public ArrowView(Arrow arrow) {
        super(arrow);

        String res = "/sprites/projectile/arrow/";
        float loopDuration = 0.3f;
        float endDuration = projectile.getState(Arrow.State.END).getDuration();
        for (Direction direction : Direction.values()) {
            String dir = direction.toString().toLowerCase();
            animate(Arrow.State.LOOP, direction, 4, loopDuration, res + "loop/" + dir);
            animate(Arrow.State.END, direction, 4, endDuration, res + "end");
        }
    }
}
