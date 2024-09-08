package chevy.view.entities.animated.collectable;

import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.model.entity.stateMachine.EntityState;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;

import java.awt.image.BufferedImage;

import static chevy.model.entity.collectable.powerUp.PowerUp.State.*;

public final class PowerUpView extends CollectableView {
    private static final String RES = "/sprites/collectable/powerUp/";
    private EntityState previousAnimationState;

    public PowerUpView(PowerUp powerUp) {super(powerUp);}

    @Override
    protected void initializeAnimation() {
        final Vector2<Integer> offset = new Vector2<>(0, -6);

        final float idleDuration = collectable.getState(PowerUp.State.IDLE).getDuration();
        animate(IDLE, null, 3, idleDuration, offset,  RES + "idle");

        final float selectedDuration = collectable.getState(SELECTED).getDuration();
        animate(SELECTED, null, 4, selectedDuration, offset,  RES + "selected");

        final float deselectedDuration = collectable.getState(DESELECTED).getDuration();
        animate(DESELECTED, null, 4, deselectedDuration, offset,  RES + "deselected");

        final float collectedDuration = collectable.getState(PowerUp.State.COLLECTED).getDuration();
        animate(COLLECTED, null, 7, collectedDuration, offset,  RES + "collected");
    }

    @Override
    public Vector2<Integer> getOffset() {
        EntityState currentStateState = collectable.getState();
        AnimatedSprite currentAnimatedSprite = getAnimatedSprite(currentStateState, null);
        if (currentAnimatedSprite != null) {
            return currentAnimatedSprite.getOffset();
        }
        return new Vector2<>(0, 0);
    }

    @Override
    public BufferedImage getFrame() {
        EntityState currentEnumState = collectable.getState();
        AnimatedSprite currentAnimatedSprite = getAnimatedSprite(currentEnumState, null);

        if (currentAnimatedSprite != null) {
            if (!currentAnimatedSprite.isRunning()) {
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