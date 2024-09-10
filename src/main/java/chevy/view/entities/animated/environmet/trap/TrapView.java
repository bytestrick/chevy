package chevy.view.entities.animated.environmet.trap;

import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.view.animation.AnimatedSprite;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

abstract class TrapView extends AnimatedEntityView {
    final Trap trap;

    TrapView(Trap trap) {
        this.trap = trap;
        viewPosition = new Point2D.Double(trap.getCol(), trap.getRow());
    }

    @Override
    public void remove() {deleteAnimations();}

    @Override
    public BufferedImage getFrame() {
        final AnimatedSprite animatedSprite = getAnimatedSprite(trap.getState(), null);
        if (animatedSprite != null) {
            if (animatedSprite.isNotRunning()) {
                animatedSprite.restart();
            }
            return animatedSprite.getFrame();
        }
        return null;
    }

    @Override
    public Point getOffset() {
        return getAnimatedSprite(trap.getState(), null).getOffset();
    }
}