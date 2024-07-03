package chevy.view.entities.animated.enemy;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.BigSlime;
import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class BigSlimeView extends AnimatedEntityView {
    private static final String BIG_SLIME_RESOURCES = "/assets/enemy/bigSlime/";
    private final BigSlime bigSlime;
    private final Vector2<Double> currentPosition;
    private final Interpolation moveInterpolationX;
    private final Interpolation moveInterpolationY;
    private State currentState;
    private boolean firstTimeInState = false;

    public BigSlimeView(BigSlime bigSlime) {
        super();
        this.bigSlime = bigSlime;
        this.currentPosition = new Vector2<>((double) bigSlime.getCol(), (double) bigSlime.getRow());
        currentState = bigSlime.getState(bigSlime.getCurrentEumState());

        float duration = bigSlime.getState(bigSlime.getCurrentEumState()).getDuration();
        moveInterpolationX = new Interpolation(currentPosition.first, bigSlime.getCol(), duration,
                Interpolation.Types.EASE_OUT_SINE);
        moveInterpolationY = new Interpolation(currentPosition.second, bigSlime.getRow(), duration,
                Interpolation.Types.EASE_OUT_SINE);

        initAnimation();
    }

    private void initAnimation() {
        // Idle
        float idleDuration = bigSlime.getState(BigSlime.States.IDLE).getDuration();
        createAnimation(BigSlime.States.IDLE, 0, 4, true, 2, idleDuration, BIG_SLIME_RESOURCES + "idle", ".png");

        // Move
        float moveDuration = bigSlime.getState(BigSlime.States.MOVE).getDuration();
        createAnimation(BigSlime.States.MOVE, 0, 4, moveDuration, BIG_SLIME_RESOURCES + "move", ".png");

        // Attack
        float attackDuration = bigSlime.getState(BigSlime.States.ATTACK).getDuration();
        createAnimation(BigSlime.States.ATTACK, 0, 4, attackDuration, BIG_SLIME_RESOURCES + "attack/up", ".png");
        createAnimation(BigSlime.States.ATTACK, 1, 4, attackDuration, BIG_SLIME_RESOURCES + "attack/down", ".png");
        createAnimation(BigSlime.States.ATTACK, 2, 4, attackDuration, BIG_SLIME_RESOURCES + "attack/right", ".png");
        createAnimation(BigSlime.States.ATTACK, 3, 4, attackDuration, BIG_SLIME_RESOURCES + "attack/left", ".png");

        // Hit
        float hitDuration = bigSlime.getState(BigSlime.States.HIT).getDuration();
        createAnimation(BigSlime.States.HIT, 0, 1, hitDuration, BIG_SLIME_RESOURCES + "hit", ".png");

        // Dead
        float deadDuration = bigSlime.getState(BigSlime.States.DEAD).getDuration();
        createAnimation(BigSlime.States.DEAD, 0, 4, deadDuration, BIG_SLIME_RESOURCES + "dead", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonEnumStates currentState = bigSlime.getCurrentEumState();
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

    private int getAnimationType(CommonEnumStates currentState) {
        DirectionsModel currentDirection = bigSlime.getDirection();
        return switch (currentState) {
            case BigSlime.States.ATTACK -> switch (currentDirection) {
                case UP -> 0;
                case DOWN -> 1;
                case RIGHT -> 2;
                case LEFT -> 3;
            };
            default -> 0;
        };
    }

    @Override
    public Vector2<Double> getCurrentPosition() {
        if (currentState.isFinished()) {
            currentState = bigSlime.getState(bigSlime.getCurrentEumState());
            firstTimeInState = true;
        } else if (firstTimeInState) {
            float duration = currentState.getDuration();
            moveInterpolationX.changeStart(currentPosition.first);
            moveInterpolationX.changeEnd(bigSlime.getCol());
            moveInterpolationX.changeDuration(duration);
            moveInterpolationX.restart();
            moveInterpolationY.changeStart(currentPosition.second);
            moveInterpolationY.changeEnd(bigSlime.getRow());
            moveInterpolationY.changeDuration(duration);
            moveInterpolationY.restart();
            firstTimeInState = false;
        }

        currentPosition.changeFirst(moveInterpolationX.getValue());
        currentPosition.changeSecond(moveInterpolationY.getValue());
        return currentPosition;
    }

    @Override
    public void wasRemoved() {
        moveInterpolationX.delete();
        moveInterpolationY.delete();
        deleteAnimations();
    }
}