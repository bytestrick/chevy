package chevy.view.entities.animated.environmet;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.staticEntity.environment.Stair;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class StairView extends AnimatedEntityView {
    private static final String STAIR_RESOURCES = "/assets/stair/";
    private final Stair stair;
    private CommonState previousAnimationState = null;

    public StairView(Stair stair) {
        this.stair = stair;
        currentViewPosition = new Vector2<>(
                (double) stair.getCol(),
                (double) stair.getRow()
        );

        initAnimation();
    }

    private void initAnimation() {
        Vector2<Integer> offset = new Vector2<>(0, -5);
        String folder = "right/";

        if (stair.getDirections() == DirectionsModel.LEFT) {
            offset = new Vector2<>(-16, -5);
            folder = "left/";
        }

        createAnimation(Stair.State.IDLE, 0,
                1, stair.getState(Stair.State.IDLE).getDuration(),
                offset, 1,
                STAIR_RESOURCES + folder + "idle", ".png");

        createAnimation(Stair.State.OPEN, 0,
                6, stair.getState(Stair.State.OPEN).getDuration(),
                offset, 1,
                STAIR_RESOURCES + folder + "open", ".png");

        createAnimation(Stair.State.IDLE_ENTRY, 0,
                6, stair.getState(Stair.State.IDLE_ENTRY).getDuration(),
                offset, 1,
                STAIR_RESOURCES + folder + "idleEntry", ".png");
    }

    @Override
    public Vector2<Integer> getOffset() {
        CommonState currentState = stair.getCurrentState();
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, 0);

        if (currentAnimatedSprite != null) {
            return currentAnimatedSprite.getOffset();
        }

        return super.getOffset();
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonState currentState = stair.getCurrentState();
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, 0);

        if (currentAnimatedSprite != null) {
            if (!currentAnimatedSprite.isRunning()) {
                if (previousAnimationState != Stair.State.OPEN) {
                    currentAnimatedSprite.restart();
                } else if (currentState != Stair.State.OPEN) {
                    currentAnimatedSprite.restart();
                }
            }
            previousAnimationState = currentState;
            return currentAnimatedSprite.getCurrentFrame();
        }
        return null;
    }

    @Override
    public Vector2<Double> getCurrentViewPosition() {
        return currentViewPosition;
    }
}
