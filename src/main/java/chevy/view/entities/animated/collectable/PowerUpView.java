package chevy.view.entities.animated.collectable;

import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.model.entity.stateMachine.EntityState;
import chevy.view.animation.AnimatedSprite;

import java.awt.Point;
import java.awt.image.BufferedImage;

import static chevy.model.entity.collectable.powerUp.PowerUp.State.*;

public final class PowerUpView extends CollectableView {
    private EntityState previousAnimationState;

    public PowerUpView(PowerUp powerUp) {
        super(powerUp);

        final String res = "/sprites/collectable/powerUp/";
        final Point offset = new Point(0, -6);

        final float idleDuration = collectable.getState(PowerUp.State.IDLE).getDuration();
        animate(IDLE, null, 3, idleDuration, offset, res + "idle");

        final float selectedDuration = collectable.getState(SELECTED).getDuration();
        animate(SELECTED, null, 4, selectedDuration, offset, res + "selected");

        final float deselectedDuration = collectable.getState(DESELECTED).getDuration();
        animate(DESELECTED, null, 4, deselectedDuration, offset, res + "deselected");

        final float collectedDuration = collectable.getState(PowerUp.State.COLLECTED).getDuration();
        animate(COLLECTED, null, 7, collectedDuration, offset, res + "collected");
    }

    @Override
    public Point getOffset() {
        EntityState state = collectable.getState();
        AnimatedSprite animatedSprite = getAnimatedSprite(state, null);
        if (animatedSprite != null) {
            return animatedSprite.getOffset();
        }
        return super.getOffset();
    }

    @Override
    public BufferedImage getFrame() {
        EntityState currentEnumState = collectable.getState();
        AnimatedSprite currentAnimatedSprite = getAnimatedSprite(currentEnumState, null);

        if (currentAnimatedSprite != null) {
            if (currentAnimatedSprite.isNotRunning()) {
                if (previousAnimationState != SELECTED) {
                    currentAnimatedSprite.restart();
                } else if (currentEnumState != SELECTED) {
                    currentAnimatedSprite.restart();
                }
            }
            previousAnimationState = currentEnumState;
            return currentAnimatedSprite.getFrame();
        }
        return null;
    }
}
