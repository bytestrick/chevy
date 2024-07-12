package chevy.view.entityView.entityViewAnimated.projectile;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.projectile.Arrow;
import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolate;
import chevy.view.animation.InterpolationTypes;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.image.BufferedImage;

public class ArrowView extends EntityViewAnimated {
    private static final String ARROW_PATH = "/assets/projectile/arrow/";
    private final Arrow arrow;
    private final Interpolate moveInterpolationX;
    private final Interpolate moveInterpolationY;
    private State currentState;
    private boolean firstTimeInState = false;


    public ArrowView(Arrow arrow) {
        super();
        this.arrow = arrow;
        this.currentViewPosition = new Vector2<>(
                (double) arrow.getCol(),
                (double) arrow.getRow()
        );
        currentState = arrow.getState(arrow.getCurrentEumState());
        moveInterpolationX = new Interpolate(currentViewPosition.first,
                arrow.getCol(),
                arrow.getState(arrow.getCurrentEumState()).getDuration(),
                InterpolationTypes.LINEAR
        );
        moveInterpolationY = new Interpolate(currentViewPosition.second,
                arrow.getRow(),
                arrow.getState(arrow.getCurrentEumState()).getDuration(),
                InterpolationTypes.LINEAR
        );

        initAnimation();
    }


    private void initAnimation() {
        float durationLoop = arrow.getState(Arrow.EnumState.LOOP).getDuration();
        float durationEnd = arrow.getState(Arrow.EnumState.END).getDuration();

        // LOOP
        createAnimation(Arrow.EnumState.LOOP, 0,
                4, true, 2, durationLoop,
                ARROW_PATH + "up", ".png");

        createAnimation(Arrow.EnumState.LOOP, 1,
                4, true, 2, durationLoop,
                ARROW_PATH + "down", ".png");

        createAnimation(Arrow.EnumState.LOOP, 2,
                4, true, 2, durationLoop,
                ARROW_PATH + "right", ".png");

        createAnimation(Arrow.EnumState.LOOP, 3,
                4, true, 2, durationLoop,
                ARROW_PATH + "left", ".png");


        // END
        createAnimation(Arrow.EnumState.END, 3,
                1, durationEnd,
                ARROW_PATH + "up", ".png");

        createAnimation(Arrow.EnumState.END, 2,
                1, durationEnd,
                ARROW_PATH + "down", ".png");

        createAnimation(Arrow.EnumState.END, 1,
                1, durationEnd,
                ARROW_PATH + "right", ".png");

        createAnimation(Arrow.EnumState.END, 0,
                1, durationEnd,
                ARROW_PATH + "left", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonEnumStates currentEnumState = arrow.getCurrentEumState();
        int type = getAnimationType(currentEnumState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentEnumState, type);

        if (currentAnimatedSprite != null) {
            if (currentEnumState == Arrow.EnumState.END && currentState.isFinished()) {
                arrow.setToDraw(false);
            }
            else if (!currentAnimatedSprite.isRunning()) {
                currentAnimatedSprite.restart();
            }
            return currentAnimatedSprite.getCurrentFrame();
        }
        return null;
    }

    public Vector2<Integer> getOffset() {
        CommonEnumStates currentState = arrow.getCurrentEumState();
        int type = getAnimationType(currentState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, type);
        return currentAnimatedSprite.getOffset();
    }

    private int getAnimationType(CommonEnumStates currentState) {
        DirectionsModel currentDirection = arrow.getDirection();
        return switch (currentState) {
            case Arrow.EnumState.LOOP, Arrow.EnumState.END ->
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
        if (arrow.isCollide()) {
            return currentViewPosition;
        }

        if (!currentState.isFinished()) {
            if (firstTimeInState) {
                float duration = currentState.getDuration();
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
        }
        else {
            currentState = arrow.getState(arrow.getCurrentEumState());
            firstTimeInState = true;
        }
        return currentViewPosition;
    }

    @Override
    public void wasRemoved() {
        moveInterpolationX.delete();
        moveInterpolationY.delete();
        deleteAnimations();
    }
}
