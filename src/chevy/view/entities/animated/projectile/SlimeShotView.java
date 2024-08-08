package chevy.view.entities.animated.projectile;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.projectile.Arrow;
import chevy.model.entity.dinamicEntity.projectile.SlimeShot;
import chevy.model.entity.stateMachine.CommonState;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class SlimeShotView extends AnimatedEntityView {
    private static final String SLIME_SHOT_RESOURCES = "/assets/projectile/slimeShot/";
    private final SlimeShot slimeShot;

    public SlimeShotView(SlimeShot slimeShot) {
        super();
        this.slimeShot = slimeShot;
        this.currentViewPosition = new Vector2<>((double) slimeShot.getCol(), (double) slimeShot.getRow());

        final float duration = slimeShot.getState(slimeShot.getCurrentState()).getDuration();
        currentGlobalState = slimeShot.getState(slimeShot.getCurrentState());
        moveInterpolationX = new Interpolation(currentViewPosition.first, slimeShot.getCol(), duration,
                Interpolation.Type.LINEAR);
        moveInterpolationY = new Interpolation(currentViewPosition.second, slimeShot.getRow(), duration,
                Interpolation.Type.LINEAR);

        initAnimation();
    }

    private void initAnimation() {
        // --- START
        Vector2<Integer> startOffsetUp = new Vector2<>(1, -16);
        Vector2<Integer> startOffsetDown = new Vector2<>(1, 16);
        Vector2<Integer> startOffsetRight = new Vector2<>(18, 2);
        Vector2<Integer> startOffsetLeft = new Vector2<>(-16, 2);

        float durationStart = slimeShot.getState(SlimeShot.State.START).getDuration();
        float durationLoop = slimeShot.getState(SlimeShot.State.LOOP).getDuration();
        float durationEnd = slimeShot.getState(SlimeShot.State.END).getDuration();

        createAnimation(SlimeShot.State.START, 0, 4, durationStart, startOffsetUp, 1, SLIME_SHOT_RESOURCES +
                "start/up", ".png");
        createAnimation(SlimeShot.State.START, 1, 4, durationStart, startOffsetDown, 1, SLIME_SHOT_RESOURCES +
                "start/down", ".png");
        createAnimation(SlimeShot.State.START, 2, 4, durationStart, startOffsetRight, 1, SLIME_SHOT_RESOURCES +
                "start/right", ".png");
        createAnimation(SlimeShot.State.START, 3, 4, durationStart, startOffsetLeft, 1, SLIME_SHOT_RESOURCES +
                "start/left", ".png");

        // --- LOOP
        Vector2<Integer> loopEndOffsetUp = new Vector2<>(2, 0);
        Vector2<Integer> loopEndOffsetDown = new Vector2<>(2, 4);
        Vector2<Integer> loopEndOffsetRight = new Vector2<>(4, 2);
        Vector2<Integer> loopEndOffsetLeft = new Vector2<>(0, 2);

        createAnimation(SlimeShot.State.LOOP, 0, 4, true, 3, durationLoop, loopEndOffsetUp, 1,
                SLIME_SHOT_RESOURCES + "loop/up", ".png");
        createAnimation(SlimeShot.State.LOOP, 1, 4, true, 3, durationLoop, loopEndOffsetDown, 1,
                SLIME_SHOT_RESOURCES + "loop/down", ".png");
        createAnimation(SlimeShot.State.LOOP, 2, 4, true, 3, durationLoop, loopEndOffsetRight, 1,
                SLIME_SHOT_RESOURCES + "loop/right", ".png");
        createAnimation(SlimeShot.State.LOOP, 3, 4, true, 3, durationLoop, loopEndOffsetLeft, 1,
                SLIME_SHOT_RESOURCES + "loop/left", ".png");

        // --- END
        createAnimation(SlimeShot.State.END, 0, 5, durationEnd, loopEndOffsetUp, 1, SLIME_SHOT_RESOURCES + "end",
                ".png");
        createAnimation(SlimeShot.State.END, 1, 5, durationEnd, loopEndOffsetDown, 1, SLIME_SHOT_RESOURCES + "end"
                , ".png");
        createAnimation(SlimeShot.State.END, 2, 5, durationEnd, loopEndOffsetRight, 1, SLIME_SHOT_RESOURCES +
                "end", ".png");
        createAnimation(SlimeShot.State.END, 3, 5, durationEnd, loopEndOffsetLeft, 1, SLIME_SHOT_RESOURCES + "end"
                , ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonState currentEnumState = slimeShot.getCurrentState();
        int type = getAnimationType(currentEnumState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentEnumState, type);

        if (currentAnimatedSprite != null) {
            if (currentEnumState == SlimeShot.State.END && currentGlobalState.isFinished()) {
                slimeShot.setToDraw(false);
            } else if (!currentAnimatedSprite.isRunning()) {
                currentAnimatedSprite.start();
            }
            return currentAnimatedSprite.getCurrentFrame();
        }
        return null;
    }

    public Vector2<Integer> getOffset() {
        CommonState currentState = slimeShot.getCurrentState();
        int type = getAnimationType(currentState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, type);
        return currentAnimatedSprite.getOffset();
    }

    public float getScale() {
        CommonState currentState = slimeShot.getCurrentState();
        int type = getAnimationType(currentState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, type);
        return currentAnimatedSprite.getScale();
    }

    private int getAnimationType(CommonState currentState) {
        DirectionsModel currentDirection = slimeShot.getDirection();
        return switch (currentState) {
            case SlimeShot.State.START, SlimeShot.State.LOOP, SlimeShot.State.END ->
                    switch (currentDirection) {
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
            currentGlobalState = slimeShot.getState(slimeShot.getCurrentState());
            firstTimeInState = true;
        } else if (firstTimeInState && currentGlobalState.getState() != SlimeShot.State.END) {
            float duration = currentGlobalState.getDuration();
            moveInterpolationX.changeStart(currentViewPosition.first);
            moveInterpolationX.changeEnd(slimeShot.getCol());
            moveInterpolationX.changeDuration(duration);
            moveInterpolationX.restart();
            moveInterpolationY.changeStart(currentViewPosition.second);
            moveInterpolationY.changeEnd(slimeShot.getRow());
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