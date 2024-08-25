package chevy.view.entities.animated.projectile;

import chevy.model.entity.dynamicEntity.DirectionsModel;
import chevy.model.entity.dynamicEntity.projectile.Arrow;
import chevy.model.entity.stateMachine.CommonState;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class ArrowView extends AnimatedEntityView {
    private static final String ARROW_PATH = "/assets/projectile/arrow/";
    private final Arrow arrow;

    public ArrowView(Arrow arrow) {
        super();
        this.arrow = arrow;
        this.currentViewPosition = new Vector2<>((double) arrow.getCol(), (double) arrow.getRow());
        currentGlobalState = arrow.getState(arrow.getCurrentState());

        final float duration = arrow.getState(arrow.getCurrentState()).getDuration();
        moveInterpolationX = new Interpolation(currentViewPosition.first, arrow.getCol(), duration,
                Interpolation.Type.LINEAR);
        moveInterpolationY = new Interpolation(currentViewPosition.second, arrow.getRow(), duration,
                Interpolation.Type.LINEAR);

        initAnimation();
    }

    private void initAnimation() {
        // Loop
        float durationLoop = 0.3f;
        createAnimation(Arrow.State.LOOP, 0, 4, durationLoop, ARROW_PATH + "up", ".png");
        createAnimation(Arrow.State.LOOP, 1, 4, durationLoop, ARROW_PATH + "down", ".png");
        createAnimation(Arrow.State.LOOP, 2, 4, durationLoop, ARROW_PATH + "right", ".png");
        createAnimation(Arrow.State.LOOP, 3, 4, durationLoop, ARROW_PATH + "left", ".png");

        // End
        float durationEnd = arrow.getState(Arrow.State.END).getDuration();
        createAnimation(Arrow.State.END, 3, 1, durationEnd, ARROW_PATH + "up", ".png");
        createAnimation(Arrow.State.END, 2, 1, durationEnd, ARROW_PATH + "down", ".png");
        createAnimation(Arrow.State.END, 1, 1, durationEnd, ARROW_PATH + "right", ".png");
        createAnimation(Arrow.State.END, 0, 1, durationEnd, ARROW_PATH + "left", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonState currentEnumState = arrow.getCurrentState();
        int type = getAnimationType(currentEnumState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentEnumState, type);

        if (currentAnimatedSprite != null) {
            if (currentEnumState == Arrow.State.END && currentGlobalState.isFinished()) {
                arrow.setToDraw(false);
            } else if (!currentAnimatedSprite.isRunning()) {
                currentAnimatedSprite.restart();
            }
            return currentAnimatedSprite.getCurrentFrame();
        }
        return null;
    }

    public Vector2<Integer> getOffset() {
        CommonState currentState = arrow.getCurrentState();
        int type = getAnimationType(currentState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, type);
        return currentAnimatedSprite.getOffset();
    }

    private int getAnimationType(CommonState currentState) {
        DirectionsModel currentDirection = arrow.getDirection();
        return switch (currentState) {
            case Arrow.State.LOOP, Arrow.State.END -> switch (currentDirection) {
                case UP -> 0;
                case DOWN -> 1;
                case RIGHT -> 2;
                case LEFT -> 3;
            };
            default -> 0;
        };
    }

    @Override
    public Vector2<Double> getCurrentViewPosition() {
        if (currentGlobalState.isFinished()) {
            currentGlobalState = arrow.getState(arrow.getCurrentState());
            firstTimeInState = true;
        } else if (firstTimeInState && currentGlobalState.getState() != Arrow.State.END) {
            float duration = currentGlobalState.getDuration();
            moveInterpolationX.changeStart(currentViewPosition.first);
            moveInterpolationX.changeEnd(arrow.getCol());
            moveInterpolationX.changeDuration(duration);
            moveInterpolationX.restart();
            moveInterpolationY.changeStart(currentViewPosition.second);
            moveInterpolationY.changeEnd(arrow.getRow());
            moveInterpolationY.changeDuration(duration);
            moveInterpolationY.restart();
            firstTimeInState = false;
        }

        currentViewPosition.changeFirst(moveInterpolationX.getValue());
        currentViewPosition.changeSecond(moveInterpolationY.getValue());
        return currentViewPosition;
    }

    @Override
    public void wasRemoved() {
        moveInterpolationX.delete();
        moveInterpolationY.delete();
        super.deleteAnimations();
    }
}