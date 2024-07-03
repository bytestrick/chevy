package chevy.view.entities.animated.enemy;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Wraith;
import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class WraithView extends AnimatedEntityView {
    private static final String WRAITH_RESOURCES = "/assets/enemy/wraith/";
    private final Wraith wraith;
    private final Vector2<Double> currentPosition;
    private final Interpolation moveInterpolationX;
    private final Interpolation moveInterpolationY;
    private State currentState;
    private boolean firstTimeInState = false;

    public WraithView(Wraith wraith) {
        super();
        this.wraith = wraith;
        this.currentPosition = new Vector2<>((double) wraith.getCol(), (double) wraith.getRow());
        currentState = wraith.getState(wraith.getCurrentEumState());

        float duration = wraith.getState(wraith.getCurrentEumState()).getDuration();
        moveInterpolationX = new Interpolation(currentPosition.first, wraith.getCol(), duration,
                Interpolation.Types.EASE_OUT_SINE);
        moveInterpolationY = new Interpolation(currentPosition.second, wraith.getRow(), duration,
                Interpolation.Types.EASE_OUT_SINE);

        initAnimation();
    }

    private void initAnimation() {
        // Idle
        float idleDuration = wraith.getState(Wraith.States.IDLE).getDuration();
        createAnimation(Wraith.States.IDLE, 0, 4, true, 3, idleDuration, WRAITH_RESOURCES + "idle/up", ".png");
        createAnimation(Wraith.States.IDLE, 1, 4, true, 3, idleDuration, WRAITH_RESOURCES + "idle/down", ".png");
        createAnimation(Wraith.States.IDLE, 2, 4, true, 3, idleDuration, WRAITH_RESOURCES + "idle/right", ".png");
        createAnimation(Wraith.States.IDLE, 3, 4, true, 3, idleDuration, WRAITH_RESOURCES + "idle/left", ".png");

        // Move
        float moveDuration = wraith.getState(Wraith.States.MOVE).getDuration();
        createAnimation(Wraith.States.MOVE, 0, 4, moveDuration, WRAITH_RESOURCES + "move/up", ".png");
        createAnimation(Wraith.States.MOVE, 1, 4, moveDuration, WRAITH_RESOURCES + "move/down", ".png");
        createAnimation(Wraith.States.MOVE, 2, 4, moveDuration, WRAITH_RESOURCES + "move/right", ".png");
        createAnimation(Wraith.States.MOVE, 3, 4, moveDuration, WRAITH_RESOURCES + "move/left", ".png");

        // Attack
        float attackDuration = wraith.getState(Wraith.States.ATTACK).getDuration();
        createAnimation(Wraith.States.ATTACK, 0, 4, attackDuration, WRAITH_RESOURCES + "attack/up", ".png");
        createAnimation(Wraith.States.ATTACK, 1, 4, attackDuration, WRAITH_RESOURCES + "attack/down", ".png");
        createAnimation(Wraith.States.ATTACK, 2, 4, attackDuration, WRAITH_RESOURCES + "attack/right", ".png");
        createAnimation(Wraith.States.ATTACK, 3, 4, attackDuration, WRAITH_RESOURCES + "attack/left", ".png");

        // Hit
        float hitDuration = wraith.getState(Wraith.States.HIT).getDuration();
        createAnimation(Wraith.States.HIT, 0, 1, hitDuration, WRAITH_RESOURCES + "hit/up", ".png");
        createAnimation(Wraith.States.HIT, 1, 1, hitDuration, WRAITH_RESOURCES + "hit/down", ".png");
        createAnimation(Wraith.States.HIT, 2, 1, hitDuration, WRAITH_RESOURCES + "hit/left", ".png");
        createAnimation(Wraith.States.HIT, 3, 1, hitDuration, WRAITH_RESOURCES + "hit/right", ".png");

        // Dead
        float deadDuration = wraith.getState(Wraith.States.DEAD).getDuration();
        createAnimation(Wraith.States.DEAD, 0, 4, deadDuration, WRAITH_RESOURCES + "dead/left", ".png");
        createAnimation(Wraith.States.DEAD, 1, 4, deadDuration, WRAITH_RESOURCES + "dead/right", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonEnumStates currentEnumState = wraith.getCurrentEumState();
        int type = getAnimationType(currentEnumState);

        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentEnumState, type);

        if (currentAnimatedSprite != null) {
            if (!currentAnimatedSprite.isRunning()) {
                currentAnimatedSprite.restart();
            }
            return currentAnimatedSprite.getCurrentFrame();
        }
        return null;
    }

    private int getAnimationType(CommonEnumStates currentState) {
        DirectionsModel currentDirection = wraith.getDirection();
        return switch (currentState) {
            case Wraith.States.ATTACK, Wraith.States.IDLE, Wraith.States.MOVE -> switch (currentDirection) {
                case UP -> 0;
                case DOWN -> 1;
                case RIGHT -> 2;
                case LEFT -> 3;
            };
            case Wraith.States.DEAD -> {
                if (currentDirection == DirectionsModel.RIGHT) yield 1;
                else yield 0;
            }
            default -> 0;
        };
    }

    @Override
    public Vector2<Double> getCurrentPosition() {
        if (currentState.isFinished()) {
            currentState = wraith.getState(wraith.getCurrentEumState());
            firstTimeInState = true;
        } else if (firstTimeInState) {
            float duration = currentState.getDuration();
            moveInterpolationX.changeStart(currentPosition.first);
            moveInterpolationX.changeEnd(wraith.getCol());
            moveInterpolationX.changeDuration(duration);
            moveInterpolationX.restart();
            moveInterpolationY.changeStart(currentPosition.second);
            moveInterpolationY.changeEnd(wraith.getRow());
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