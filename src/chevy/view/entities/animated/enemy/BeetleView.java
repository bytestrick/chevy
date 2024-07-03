package chevy.view.entities.animated.enemy;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Beetle;
import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class BeetleView extends AnimatedEntityView {
    private static final String BEETLE_RESOURCES = "/assets/enemy/beetle/";
    private final Beetle beetle;
    private final Vector2<Double> currentPosition;
    private final Interpolation moveInterpolationX;
    private final Interpolation moveInterpolationY;
    private State currentState;
    private boolean firstTimeInState = false;

    public BeetleView(Beetle beetle) {
        super();
        this.beetle = beetle;
        this.currentPosition = new Vector2<>((double) beetle.getCol(), (double) beetle.getRow());
        currentState = beetle.getState(beetle.getCurrentEumState());

        float duration = currentState.getDuration();
        moveInterpolationX = new Interpolation(currentPosition.first, beetle.getCol(), duration,
                Interpolation.Types.EASE_OUT_SINE);
        moveInterpolationY = new Interpolation(currentPosition.second, beetle.getRow(), duration,
                Interpolation.Types.EASE_OUT_SINE);

        initAnimation();
    }

    private void initAnimation() {
        // Idle
        float idleDuration = beetle.getState(Beetle.States.IDLE).getDuration();
        createAnimation(Beetle.States.IDLE, 0, 4, true, 4, idleDuration, BEETLE_RESOURCES + "idle/up", ".png");
        createAnimation(Beetle.States.IDLE, 1, 4, true, 4, idleDuration, BEETLE_RESOURCES + "idle/down", ".png");
        createAnimation(Beetle.States.IDLE, 2, 4, true, 4, idleDuration, BEETLE_RESOURCES + "idle/right", ".png");
        createAnimation(Beetle.States.IDLE, 3, 4, true, 4, idleDuration, BEETLE_RESOURCES + "idle/left", ".png");

        // Move
        float moveDuration = beetle.getState(Beetle.States.MOVE).getDuration();
        createAnimation(Beetle.States.MOVE, 0, 4, moveDuration, BEETLE_RESOURCES + "move/up", ".png");
        createAnimation(Beetle.States.MOVE, 1, 4, moveDuration, BEETLE_RESOURCES + "move/down", ".png");
        createAnimation(Beetle.States.MOVE, 2, 4, moveDuration, BEETLE_RESOURCES + "move/right", ".png");
        createAnimation(Beetle.States.MOVE, 3, 4, moveDuration, BEETLE_RESOURCES + "move/left", ".png");

        // Attack
        float attackDuration = beetle.getState(Beetle.States.ATTACK).getDuration();
        Vector2<Integer> offsetAttack = new Vector2<>(-2, -4);
        float scaleAttack = 1;
        createAnimation(Beetle.States.ATTACK, 0, 4, attackDuration, offsetAttack, scaleAttack, BEETLE_RESOURCES +
                "attack/up", ".png");
        createAnimation(Beetle.States.ATTACK, 1, 4, attackDuration, offsetAttack, scaleAttack, BEETLE_RESOURCES +
                "attack/down", ".png");
        createAnimation(Beetle.States.ATTACK, 2, 4, attackDuration, offsetAttack, scaleAttack, BEETLE_RESOURCES +
                "attack/right", ".png");
        createAnimation(Beetle.States.ATTACK, 3, 4, attackDuration, offsetAttack, scaleAttack, BEETLE_RESOURCES +
                "attack/left", ".png");

        // Hit
        float hitDuration = beetle.getState(Beetle.States.HIT).getDuration();
        createAnimation(Beetle.States.HIT, 0, 1, hitDuration, BEETLE_RESOURCES + "hit/up", ".png");
        createAnimation(Beetle.States.HIT, 1, 1, hitDuration, BEETLE_RESOURCES + "hit/down", ".png");
        createAnimation(Beetle.States.HIT, 2, 1, hitDuration, BEETLE_RESOURCES + "hit/right", ".png");
        createAnimation(Beetle.States.HIT, 3, 1, hitDuration, BEETLE_RESOURCES + "hit/left", ".png");

        // Dead
        float deadDuration = beetle.getState(Beetle.States.DEAD).getDuration();
        createAnimation(Beetle.States.DEAD, 0, 4, deadDuration, BEETLE_RESOURCES + "dead/left", ".png");
        createAnimation(Beetle.States.DEAD, 1, 4, deadDuration, BEETLE_RESOURCES + "dead/right", ".png");
    }

    public Vector2<Integer> getOffset() {
        CommonEnumStates currentState = beetle.getCurrentEumState();
        int type = getAnimationType(currentState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, type);
        return currentAnimatedSprite.getOffset();
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonEnumStates currentStateState = beetle.getCurrentEumState();
        int type = getAnimationType(currentStateState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentStateState, type);

        if (currentAnimatedSprite != null) {
            if (!currentAnimatedSprite.isRunning()) {
                currentAnimatedSprite.restart();
            }
            return currentAnimatedSprite.getCurrentFrame();
        }
        return null;
    }

    private int getAnimationType(CommonEnumStates currentState) {
        DirectionsModel currentDirection = beetle.getDirection();
        return switch (currentState) {
            case Beetle.States.ATTACK, Beetle.States.IDLE, Beetle.States.MOVE, Beetle.States.HIT ->
                    switch (currentDirection) {
                        case UP -> 0;
                        case DOWN -> 1;
                        case RIGHT -> 2;
                        case LEFT -> 3;
                    };
            case Beetle.States.DEAD -> {
                if (currentDirection == DirectionsModel.RIGHT) yield 1;
                else yield 0;
            }
            default -> 0;
        };
    }

    @Override
    public Vector2<Double> getCurrentPosition() {
        if (currentState.isFinished()) {
            currentState = beetle.getState(beetle.getCurrentEumState());
            firstTimeInState = true;
        } else if (firstTimeInState) {
            float duration = currentState.getDuration();
            moveInterpolationX.changeStart(currentPosition.first);
            moveInterpolationX.changeEnd(beetle.getCol());
            moveInterpolationX.changeDuration(duration);
            moveInterpolationX.restart();
            moveInterpolationY.changeStart(currentPosition.second);
            moveInterpolationY.changeEnd(beetle.getRow());
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