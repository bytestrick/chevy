package chevy.view.entities.animated.enemy;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.BigSlime;
import chevy.model.entity.stateMachine.CommonState;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class BigSlimeView extends AnimatedEntityView {
    private static final String BIG_SLIME_RESOURCES = "/assets/enemy/bigSlime/";
    private final BigSlime bigSlime;

    public BigSlimeView(BigSlime bigSlime) {
        super();
        this.bigSlime = bigSlime;
        this.currentViewPosition = new Vector2<>((double) bigSlime.getCol(), (double) bigSlime.getRow());
        currentGlobalState = bigSlime.getState(bigSlime.getCurrentState());

        float duration = bigSlime.getState(bigSlime.getCurrentState()).getDuration();
        moveInterpolationX = new Interpolation(currentViewPosition.first, bigSlime.getCol(), duration,
                Interpolation.Type.EASE_OUT_SINE);
        moveInterpolationY = new Interpolation(currentViewPosition.second, bigSlime.getRow(), duration,
                Interpolation.Type.EASE_OUT_SINE);

        initAnimation();
    }

    private void initAnimation() {
        // Idle
        float idleDuration = bigSlime.getState(BigSlime.State.IDLE).getDuration();
        createAnimation(BigSlime.State.IDLE, 0, 4, true, 2, idleDuration, BIG_SLIME_RESOURCES + "idle", ".png");

        // Move
        float moveDuration = bigSlime.getState(BigSlime.State.MOVE).getDuration();
        createAnimation(BigSlime.State.MOVE, 0, 4, moveDuration, BIG_SLIME_RESOURCES + "move", ".png");

        // Attack
        float attackDuration = bigSlime.getState(BigSlime.State.ATTACK).getDuration();
        createAnimation(BigSlime.State.ATTACK, 0, 4, attackDuration, BIG_SLIME_RESOURCES + "attack/up", ".png");
        createAnimation(BigSlime.State.ATTACK, 1, 4, attackDuration, BIG_SLIME_RESOURCES + "attack/down", ".png");
        createAnimation(BigSlime.State.ATTACK, 2, 4, attackDuration, BIG_SLIME_RESOURCES + "attack/right", ".png");
        createAnimation(BigSlime.State.ATTACK, 3, 4, attackDuration, BIG_SLIME_RESOURCES + "attack/left", ".png");

        // Hit
        float hitDuration = bigSlime.getState(BigSlime.State.HIT).getDuration();
        createAnimation(BigSlime.State.HIT, 0, 1, hitDuration, BIG_SLIME_RESOURCES + "hit", ".png");

        // Dead
        float deadDuration = bigSlime.getState(BigSlime.State.DEAD).getDuration();
        createAnimation(BigSlime.State.DEAD, 0, 4, deadDuration, BIG_SLIME_RESOURCES + "dead", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonState currentState = bigSlime.getCurrentState();
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
        DirectionsModel currentDirection = bigSlime.getDirection();
        return switch (currentState) {
            case BigSlime.State.ATTACK -> switch (currentDirection) {
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
            currentGlobalState = bigSlime.getState(bigSlime.getCurrentState());
            firstTimeInState = true;
        } else if (firstTimeInState) {
            float duration = currentGlobalState.getDuration();
            moveInterpolationX.changeStart(currentViewPosition.first);
            moveInterpolationX.changeEnd(bigSlime.getCol());
            moveInterpolationX.changeDuration(duration);
            moveInterpolationX.restart();
            moveInterpolationY.changeStart(currentViewPosition.second);
            moveInterpolationY.changeEnd(bigSlime.getRow());
            moveInterpolationY.changeDuration(duration);
            moveInterpolationY.restart();
            firstTimeInState = false;
        }

        currentViewPosition.changeFirst(moveInterpolationX.getValue());
        currentViewPosition.changeSecond(moveInterpolationY.getValue());
        return currentViewPosition;
    }
}