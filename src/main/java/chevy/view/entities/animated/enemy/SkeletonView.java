package chevy.view.entities.animated.enemy;

import chevy.model.entity.dynamicEntity.DirectionsModel;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Skeleton;
import chevy.model.entity.stateMachine.CommonState;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class SkeletonView extends AnimatedEntityView {
    private static final String SKELETON_RESOURCES = "/sprites/enemy/skeleton/";
    private final Skeleton skeleton;

    public SkeletonView(Skeleton skeleton) {
        super();
        this.skeleton = skeleton;
        this.currentViewPosition = new Vector2<>((double) skeleton.getCol(), (double) skeleton.getRow());
        currentGlobalState = skeleton.getState(skeleton.getCurrentState());

        float duration = skeleton.getState(skeleton.getCurrentState()).getDuration();
        moveInterpolationX = new Interpolation(currentViewPosition.first, skeleton.getCol(), duration,
                Interpolation.Type.EASE_OUT_SINE);
        moveInterpolationY = new Interpolation(currentViewPosition.second, skeleton.getRow(), duration,
                Interpolation.Type.EASE_OUT_SINE);

        initAnimation();
    }

    private void initAnimation() {
        // Idle
        float idleDuration = skeleton.getState(Skeleton.State.IDLE).getDuration();
        createAnimation(Skeleton.State.IDLE, 0, 4, true, 4, idleDuration, SKELETON_RESOURCES + "idle/up", ".png");
        createAnimation(Skeleton.State.IDLE, 1, 4, true, 4, idleDuration, SKELETON_RESOURCES + "idle/down", ".png");
        createAnimation(Skeleton.State.IDLE, 2, 4, true, 4, idleDuration, SKELETON_RESOURCES + "idle/right",
                ".png");
        createAnimation(Skeleton.State.IDLE, 3, 4, true, 4, idleDuration, SKELETON_RESOURCES + "idle/left", ".png");

        // Move
        float moveDuration = skeleton.getState(Skeleton.State.MOVE).getDuration();
        createAnimation(Skeleton.State.MOVE, 0, 4, moveDuration, SKELETON_RESOURCES + "move/up", ".png");
        createAnimation(Skeleton.State.MOVE, 1, 4, moveDuration, SKELETON_RESOURCES + "move/down", ".png");
        createAnimation(Skeleton.State.MOVE, 2, 4, moveDuration, SKELETON_RESOURCES + "move/right", ".png");
        createAnimation(Skeleton.State.MOVE, 3, 4, moveDuration, SKELETON_RESOURCES + "move/left", ".png");

        // Attack
        float attackDuration = skeleton.getState(Skeleton.State.ATTACK).getDuration();
        createAnimation(Skeleton.State.ATTACK, 0, 4, attackDuration, SKELETON_RESOURCES + "attack/up", ".png");
        createAnimation(Skeleton.State.ATTACK, 1, 4, attackDuration, SKELETON_RESOURCES + "attack/down", ".png");
        createAnimation(Skeleton.State.ATTACK, 2, 4, attackDuration, SKELETON_RESOURCES + "attack/right", ".png");
        createAnimation(Skeleton.State.ATTACK, 3, 4, attackDuration, SKELETON_RESOURCES + "attack/left", ".png");

        // Hit
        float hitDuration = skeleton.getState(Skeleton.State.HIT).getDuration();
        createAnimation(Skeleton.State.HIT, 0, 1, hitDuration, SKELETON_RESOURCES + "hit/up", ".png");
        createAnimation(Skeleton.State.HIT, 1, 1, hitDuration, SKELETON_RESOURCES + "hit/down", ".png");
        createAnimation(Skeleton.State.HIT, 2, 1, hitDuration, SKELETON_RESOURCES + "hit/right", ".png");
        createAnimation(Skeleton.State.HIT, 3, 1, hitDuration, SKELETON_RESOURCES + "hit/left", ".png");

        // Dead
        float deadDuration = skeleton.getState(Skeleton.State.DEAD).getDuration();
        createAnimation(Skeleton.State.DEAD, 0, 4, deadDuration, SKELETON_RESOURCES + "dead/left", ".png");
        createAnimation(Skeleton.State.DEAD, 1, 4, deadDuration, SKELETON_RESOURCES + "dead/right", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonState currentState = skeleton.getCurrentState();
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

    private int getAnimationType(CommonState currentState) {
        DirectionsModel currentDirection = skeleton.getDirection();
        return switch (currentState) {
            case Skeleton.State.ATTACK, Skeleton.State.IDLE, Skeleton.State.MOVE ->
                    switch (currentDirection) {
                        case UP -> 0;
                        case DOWN -> 1;
                        case RIGHT -> 2;
                        case LEFT -> 3;
                    };
            case Skeleton.State.DEAD -> {
                if (currentDirection == DirectionsModel.RIGHT) {
                    yield 1;
                } else {
                    yield 0;
                }
            }
            default -> 0;
        };
    }

    @Override
    public Vector2<Double> getCurrentViewPosition() {
        if (currentGlobalState.isFinished()) {
            currentGlobalState = skeleton.getState(skeleton.getCurrentState());
            firstTimeInState = true;
        } else if (firstTimeInState) {
            float duration = currentGlobalState.getDuration();
            moveInterpolationX.changeStart(currentViewPosition.first);
            moveInterpolationX.changeEnd(skeleton.getCol());
            moveInterpolationX.changeDuration(duration);
            moveInterpolationX.restart();
            moveInterpolationY.changeStart(currentViewPosition.second);
            moveInterpolationY.changeEnd(skeleton.getRow());
            moveInterpolationY.changeDuration(duration);
            moveInterpolationY.restart();
            firstTimeInState = false;
        }

        currentViewPosition.changeFirst(moveInterpolationX.getValue());
        currentViewPosition.changeSecond(moveInterpolationY.getValue());
        return currentViewPosition;
    }
}
