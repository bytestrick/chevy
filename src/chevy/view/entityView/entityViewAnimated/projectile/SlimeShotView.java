package chevy.view.entityView.entityViewAnimated.projectile;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.projectile.SlimeShot;
import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolate;
import chevy.view.animation.InterpolationTypes;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.image.BufferedImage;

public class SlimeShotView extends EntityViewAnimated {
    private static final String SLIME_SHOT_RESOURCES = "/assets/projectile/slimeShot/";
    private final SlimeShot slimeShot;
    private final Interpolate moveInterpolationX;
    private final Interpolate moveInterpolationY;
    private State currentState;
    private boolean firstTimeInState = false;


    public SlimeShotView(SlimeShot slimeShot) {
        super();
        this.slimeShot = slimeShot;
        this.currentViewPosition = new Vector2<>(
                (double) slimeShot.getCol(),
                (double) slimeShot.getRow()
        );
        currentState = slimeShot.getState(slimeShot.getCurrentEumState());
        moveInterpolationX = new Interpolate(currentViewPosition.first,
                slimeShot.getCol(),
                slimeShot.getState(slimeShot.getCurrentEumState()).getDuration(),
                InterpolationTypes.LINEAR
        );
        moveInterpolationY = new Interpolate(currentViewPosition.second,
                slimeShot.getRow(),
                slimeShot.getState(slimeShot.getCurrentEumState()).getDuration(),
                InterpolationTypes.LINEAR
        );

        initAnimation();
    }


    private void initAnimation() {
        // --- START
        Vector2<Integer> startOffsetUp = new Vector2<>(1, -16);
        Vector2<Integer> startOffsetDown = new Vector2<>(1, 16);
        Vector2<Integer> startOffsetRight = new Vector2<>(18, 2);
        Vector2<Integer> startOffsetLeft = new Vector2<>(-16, 2);

        float durationStart = slimeShot.getState(SlimeShot.EnumState.START).getDuration();
        float durationLoop = slimeShot.getState(SlimeShot.EnumState.LOOP).getDuration();
        float durationEnd = slimeShot.getState(SlimeShot.EnumState.END).getDuration();

        createAnimation(SlimeShot.EnumState.START, 0,
                4, durationStart,
                startOffsetUp, 1,
                SLIME_SHOT_RESOURCES + "start/up", ".png");

        createAnimation(SlimeShot.EnumState.START, 1,
                4, durationStart,
                startOffsetDown, 1,
                SLIME_SHOT_RESOURCES + "start/down", ".png");

        createAnimation(SlimeShot.EnumState.START, 2,
                4, durationStart,
                startOffsetRight, 1,
                SLIME_SHOT_RESOURCES + "start/right", ".png");

        createAnimation(SlimeShot.EnumState.START, 3,
                4, durationStart,
                startOffsetLeft, 1,
                SLIME_SHOT_RESOURCES + "start/left", ".png");

        // --- LOOP
        Vector2<Integer> loopEndOffsetUp = new Vector2<>(2, 0);
        Vector2<Integer> loopEndOffsetDown = new Vector2<>(2, 4);
        Vector2<Integer> loopEndOffsetRight = new Vector2<>(4, 2);
        Vector2<Integer> loopEndOffsetLeft = new Vector2<>(0, 2);

        createAnimation(SlimeShot.EnumState.LOOP, 0,
                4, true, 3, durationLoop,
                loopEndOffsetUp, 1,
                SLIME_SHOT_RESOURCES + "loop/up", ".png");

        createAnimation(SlimeShot.EnumState.LOOP, 1,
                4, true, 3, durationLoop,
                loopEndOffsetDown, 1,
                SLIME_SHOT_RESOURCES + "loop/down", ".png");

        createAnimation(SlimeShot.EnumState.LOOP, 2,
                4, true, 3, durationLoop,
                loopEndOffsetRight, 1,
                SLIME_SHOT_RESOURCES + "loop/right", ".png");

        createAnimation(SlimeShot.EnumState.LOOP, 3,
                4, true, 3, durationLoop,
                loopEndOffsetLeft, 1,
                SLIME_SHOT_RESOURCES + "loop/left", ".png");

        // --- END
        createAnimation(SlimeShot.EnumState.END, 0,
                5, durationEnd,
                loopEndOffsetUp, 1,
                SLIME_SHOT_RESOURCES + "end", ".png");

        createAnimation(SlimeShot.EnumState.END, 1,
                5, durationEnd,
                loopEndOffsetDown, 1,
                SLIME_SHOT_RESOURCES + "end", ".png");

        createAnimation(SlimeShot.EnumState.END, 2,
                5, durationEnd,
                loopEndOffsetRight, 1,
                SLIME_SHOT_RESOURCES + "end", ".png");

        createAnimation(SlimeShot.EnumState.END, 3,
                5, durationEnd,
                loopEndOffsetLeft, 1,
                SLIME_SHOT_RESOURCES + "end", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonEnumStates currentEnumState = slimeShot.getCurrentEumState();
        int type = getAnimationType(currentEnumState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentEnumState, type);

        if (currentAnimatedSprite != null) {
            if (currentEnumState == SlimeShot.EnumState.END && currentState.isFinished()) {
                slimeShot.setToDraw(false);
            }
            else if (!currentAnimatedSprite.isRunning()) {
                currentAnimatedSprite.start();
            }
            return currentAnimatedSprite.getCurrentFrame();
        }
        return null;
    }

    public Vector2<Integer> getOffset() {
        CommonEnumStates currentState = slimeShot.getCurrentEumState();
        int type = getAnimationType(currentState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, type);
        return currentAnimatedSprite.getOffset();
    }

    public float getScale() {
        CommonEnumStates currentState = slimeShot.getCurrentEumState();
        int type = getAnimationType(currentState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, type);
        return currentAnimatedSprite.getScale();
    }

    private int getAnimationType(CommonEnumStates currentState) {
        DirectionsModel currentDirection = slimeShot.getDirection();
        return switch (currentState) {
            case SlimeShot.EnumState.START, SlimeShot.EnumState.LOOP, SlimeShot.EnumState.END ->
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
    public Vector2<Double> getCurrentViewPosition()  {
        if (slimeShot.isCollide()) {
            return currentViewPosition;
        }

        if (!currentState.isFinished()) {
            if (firstTimeInState) {
                float duration = currentState.getDuration();
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
        }
        else {
            currentState = slimeShot.getState(slimeShot.getCurrentEumState());
            firstTimeInState = true;
        }
        return currentViewPosition;
    }

    @Override
    public void wasRemoved() {
        moveInterpolationX.delete();
        moveInterpolationY.delete();
        super.deleteAnimations();
    }
}
