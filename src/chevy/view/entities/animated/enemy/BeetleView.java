package chevy.view.entities.animated.enemy;

import chevy.model.entity.dynamicEntity.DirectionsModel;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Beetle;
import chevy.model.entity.stateMachine.CommonState;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class BeetleView extends AnimatedEntityView {
    private static final String BEETLE_RESOURCES = "/assets/enemy/beetle/";
    private final Beetle beetle;

    public BeetleView(Beetle beetle) {
        super();
        this.beetle = beetle;
        this.currentViewPosition = new Vector2<>((double) beetle.getCol(), (double) beetle.getRow());
        currentGlobalState = beetle.getState(beetle.getCurrentState());

        float duration = currentGlobalState.getDuration();
        moveInterpolationX = new Interpolation(currentViewPosition.first, beetle.getCol(), duration,
                Interpolation.Type.EASE_OUT_SINE);
        moveInterpolationY = new Interpolation(currentViewPosition.second, beetle.getRow(), duration,
                Interpolation.Type.EASE_OUT_SINE);

        initAnimation();
    }

    private void initAnimation() {
        // Idle
        float idleDuration = beetle.getState(Beetle.State.IDLE).getDuration();
        createAnimation(Beetle.State.IDLE, 0, 4, true, 4, idleDuration, BEETLE_RESOURCES + "idle/up", ".png");
        createAnimation(Beetle.State.IDLE, 1, 4, true, 4, idleDuration, BEETLE_RESOURCES + "idle/down", ".png");
        createAnimation(Beetle.State.IDLE, 2, 4, true, 4, idleDuration, BEETLE_RESOURCES + "idle/right", ".png");
        createAnimation(Beetle.State.IDLE, 3, 4, true, 4, idleDuration, BEETLE_RESOURCES + "idle/left", ".png");

        // Move
        float moveDuration = beetle.getState(Beetle.State.MOVE).getDuration();
        createAnimation(Beetle.State.MOVE, 0, 4, moveDuration, BEETLE_RESOURCES + "move/up", ".png");
        createAnimation(Beetle.State.MOVE, 1, 4, moveDuration, BEETLE_RESOURCES + "move/down", ".png");
        createAnimation(Beetle.State.MOVE, 2, 4, moveDuration, BEETLE_RESOURCES + "move/right", ".png");
        createAnimation(Beetle.State.MOVE, 3, 4, moveDuration, BEETLE_RESOURCES + "move/left", ".png");

        // Attack
        float attackDuration = beetle.getState(Beetle.State.ATTACK).getDuration();
        Vector2<Integer> offsetAttack = new Vector2<>(-1, -3);
        float scaleAttack = 1;
        createAnimation(Beetle.State.ATTACK, 0, 4, attackDuration, offsetAttack, scaleAttack, BEETLE_RESOURCES +
                "attack/up", ".png");
        createAnimation(Beetle.State.ATTACK, 1, 4, attackDuration, offsetAttack, scaleAttack, BEETLE_RESOURCES +
                "attack/down", ".png");
        createAnimation(Beetle.State.ATTACK, 2, 4, attackDuration, offsetAttack, scaleAttack, BEETLE_RESOURCES +
                "attack/right", ".png");
        createAnimation(Beetle.State.ATTACK, 3, 4, attackDuration, offsetAttack, scaleAttack, BEETLE_RESOURCES +
                "attack/left", ".png");

        // Hit
        float hitDuration = beetle.getState(Beetle.State.HIT).getDuration();
        createAnimation(Beetle.State.HIT, 0, 1, hitDuration, BEETLE_RESOURCES + "hit/up", ".png");
        createAnimation(Beetle.State.HIT, 1, 1, hitDuration, BEETLE_RESOURCES + "hit/down", ".png");
        createAnimation(Beetle.State.HIT, 2, 1, hitDuration, BEETLE_RESOURCES + "hit/right", ".png");
        createAnimation(Beetle.State.HIT, 3, 1, hitDuration, BEETLE_RESOURCES + "hit/left", ".png");

        // Dead
        float deadDuration = beetle.getState(Beetle.State.DEAD).getDuration();
        createAnimation(Beetle.State.DEAD, 0, 4, deadDuration, BEETLE_RESOURCES + "dead/left", ".png");
        createAnimation(Beetle.State.DEAD, 1, 4, deadDuration, BEETLE_RESOURCES + "dead/right", ".png");
    }

    public Vector2<Integer> getOffset() {
        CommonState currentState = beetle.getCurrentState();
        int type = getAnimationType(currentState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, type);
        return currentAnimatedSprite.getOffset();
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonState currentState = beetle.getCurrentState();
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
        DirectionsModel currentDirection = beetle.getDirection();
        return switch (currentState) {
            case Beetle.State.ATTACK, Beetle.State.IDLE, Beetle.State.MOVE, Beetle.State.HIT ->
                    switch (currentDirection) {
                        case UP -> 0;
                        case DOWN -> 1;
                        case RIGHT -> 2;
                        case LEFT -> 3;
                    };
            case Beetle.State.DEAD -> {
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
            currentGlobalState = beetle.getState(beetle.getCurrentState());
            firstTimeInState = true;
        } else if (firstTimeInState) {
            float duration = currentGlobalState.getDuration();
            moveInterpolationX.changeStart(currentViewPosition.first);
            moveInterpolationX.changeEnd(beetle.getCol());
            moveInterpolationX.changeDuration(duration);
            moveInterpolationX.restart();
            moveInterpolationY.changeStart(currentViewPosition.second);
            moveInterpolationY.changeEnd(beetle.getRow());
            moveInterpolationY.changeDuration(duration);
            moveInterpolationY.restart();
            firstTimeInState = false;
        }

        currentViewPosition.changeFirst(moveInterpolationX.getValue());
        currentViewPosition.changeSecond(moveInterpolationY.getValue());
        return currentViewPosition;
    }
}
