package chevy.view.entities.animated.enemy;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Skeleton;
import chevy.model.entity.stateMachine.CommonStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class SkeletonView extends AnimatedEntityView {
    private static final String SKELETON_RESOURCES = "/assets/enemy/skeleton/";
    private final Skeleton skeleton;

    public SkeletonView(Skeleton skeleton) {
        super();
        this.skeleton = skeleton;
        this.currentPosition = new Vector2<>((double) skeleton.getCol(), (double) skeleton.getRow());
        currentState = skeleton.getState(skeleton.getCurrentState());

        float duration = skeleton.getState(skeleton.getCurrentState()).getDuration();
        moveInterpolationX = new Interpolation(currentPosition.first, skeleton.getCol(), duration,
                Interpolation.Types.EASE_OUT_SINE);
        moveInterpolationY = new Interpolation(currentPosition.second, skeleton.getRow(), duration,
                Interpolation.Types.EASE_OUT_SINE);

        initAnimation();
    }

    private void initAnimation() {
        // Idle
        float idleDuration = skeleton.getState(Skeleton.States.IDLE).getDuration();
        createAnimation(Skeleton.States.IDLE, 0, 4, true, 4, idleDuration, SKELETON_RESOURCES + "idle/up", ".png");
        createAnimation(Skeleton.States.IDLE, 1, 4, true, 4, idleDuration, SKELETON_RESOURCES + "idle/down", ".png");
        createAnimation(Skeleton.States.IDLE, 2, 4, true, 4, idleDuration, SKELETON_RESOURCES + "idle/right",
                ".png");
        createAnimation(Skeleton.States.IDLE, 3, 4, true, 4, idleDuration, SKELETON_RESOURCES + "idle/left", ".png");

        // Move
        float moveDuration = skeleton.getState(Skeleton.States.MOVE).getDuration();
        createAnimation(Skeleton.States.MOVE, 0, 4, moveDuration, SKELETON_RESOURCES + "move/up", ".png");
        createAnimation(Skeleton.States.MOVE, 1, 4, moveDuration, SKELETON_RESOURCES + "move/down", ".png");
        createAnimation(Skeleton.States.MOVE, 2, 4, moveDuration, SKELETON_RESOURCES + "move/right", ".png");
        createAnimation(Skeleton.States.MOVE, 3, 4, moveDuration, SKELETON_RESOURCES + "move/left", ".png");

        // Attack
        float attackDuration = skeleton.getState(Skeleton.States.ATTACK).getDuration();
        createAnimation(Skeleton.States.ATTACK, 0, 4, attackDuration, SKELETON_RESOURCES + "attack/up", ".png");
        createAnimation(Skeleton.States.ATTACK, 1, 4, attackDuration, SKELETON_RESOURCES + "attack/down", ".png");
        createAnimation(Skeleton.States.ATTACK, 2, 4, attackDuration, SKELETON_RESOURCES + "attack/right", ".png");
        createAnimation(Skeleton.States.ATTACK, 3, 4, attackDuration, SKELETON_RESOURCES + "attack/left", ".png");

        // Hit
        float hitDuration = skeleton.getState(Skeleton.States.HIT).getDuration();
        createAnimation(Skeleton.States.HIT, 0, 1, hitDuration, SKELETON_RESOURCES + "hit/up", ".png");
        createAnimation(Skeleton.States.HIT, 1, 1, hitDuration, SKELETON_RESOURCES + "hit/down", ".png");
        createAnimation(Skeleton.States.HIT, 2, 1, hitDuration, SKELETON_RESOURCES + "hit/right", ".png");
        createAnimation(Skeleton.States.HIT, 3, 1, hitDuration, SKELETON_RESOURCES + "hit/left", ".png");

        // Dead
        float deadDuration = skeleton.getState(Skeleton.States.DEAD).getDuration();
        createAnimation(Skeleton.States.DEAD, 0, 4, deadDuration, SKELETON_RESOURCES + "dead/left", ".png");
        createAnimation(Skeleton.States.DEAD, 1, 4, deadDuration, SKELETON_RESOURCES + "dead/right", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonStates currentState = skeleton.getCurrentState();
        int type = getAnimationType(currentState);

        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, type);

        if (currentAnimatedSprite != null) {
            if (!currentAnimatedSprite.isRunning()) {
                currentAnimatedSprite.restart();
            }
            return currentAnimatedSprite.getCurrentFrame();
        }
        return null;
    }

    private int getAnimationType(CommonStates currentState) {
        DirectionsModel currentDirection = skeleton.getDirection();
        return switch (currentState) {
            case Skeleton.States.ATTACK, Skeleton.States.IDLE, Skeleton.States.MOVE ->
                    switch (currentDirection) {
                        case UP -> 0;
                        case DOWN -> 1;
                        case RIGHT -> 2;
                        case LEFT -> 3;
                    };
            case Skeleton.States.DEAD -> {
                if (currentDirection == DirectionsModel.RIGHT) yield 1;
                else yield 0;
            }
            default -> 0;
        };
    }

    @Override
    public Vector2<Double> getCurrentPosition() {
        if (currentState.isFinished()) {
            currentState = skeleton.getState(skeleton.getCurrentState());
            firstTimeInState = true;
        } else if (firstTimeInState) {
            float duration = currentState.getDuration();
            moveInterpolationX.changeStart(currentPosition.first);
            moveInterpolationX.changeEnd(skeleton.getCol());
            moveInterpolationX.changeDuration(duration);
            moveInterpolationX.restart();
            moveInterpolationY.changeStart(currentPosition.second);
            moveInterpolationY.changeEnd(skeleton.getRow());
            moveInterpolationY.changeDuration(duration);
            moveInterpolationY.restart();
            firstTimeInState = false;
        }

        currentPosition.changeFirst(moveInterpolationX.getValue());
        currentPosition.changeSecond(moveInterpolationY.getValue());
        return currentPosition;
    }
}