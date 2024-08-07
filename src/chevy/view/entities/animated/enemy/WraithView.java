package chevy.view.entities.animated.enemy;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Wraith;
import chevy.model.entity.stateMachine.CommonState;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class WraithView extends AnimatedEntityView {
    private static final String WRAITH_RESOURCES = "/assets/enemy/wraith/";
    private final Wraith wraith;

    public WraithView(Wraith wraith) {
        super();
        this.wraith = wraith;
        this.currentViewPosition = new Vector2<>((double) wraith.getCol(), (double) wraith.getRow());
        currentGlobalState = wraith.getState(wraith.getCurrentState());

        float duration = wraith.getState(wraith.getCurrentState()).getDuration();
        moveInterpolationX = new Interpolation(currentViewPosition.first, wraith.getCol(), duration,
                Interpolation.Type.EASE_OUT_SINE);
        moveInterpolationY = new Interpolation(currentViewPosition.second, wraith.getRow(), duration,
                Interpolation.Type.EASE_OUT_SINE);

        initAnimation();
    }

    private void initAnimation() {
        // Idle
        float idleDuration = wraith.getState(Wraith.State.IDLE).getDuration();
        createAnimation(Wraith.State.IDLE, 0, 4, true, 3, idleDuration, WRAITH_RESOURCES + "idle/up", ".png");
        createAnimation(Wraith.State.IDLE, 1, 4, true, 3, idleDuration, WRAITH_RESOURCES + "idle/down", ".png");
        createAnimation(Wraith.State.IDLE, 2, 4, true, 3, idleDuration, WRAITH_RESOURCES + "idle/right", ".png");
        createAnimation(Wraith.State.IDLE, 3, 4, true, 3, idleDuration, WRAITH_RESOURCES + "idle/left", ".png");

        // Move
        float moveDuration = wraith.getState(Wraith.State.MOVE).getDuration();
        createAnimation(Wraith.State.MOVE, 0, 4, moveDuration, WRAITH_RESOURCES + "move/up", ".png");
        createAnimation(Wraith.State.MOVE, 1, 4, moveDuration, WRAITH_RESOURCES + "move/down", ".png");
        createAnimation(Wraith.State.MOVE, 2, 4, moveDuration, WRAITH_RESOURCES + "move/right", ".png");
        createAnimation(Wraith.State.MOVE, 3, 4, moveDuration, WRAITH_RESOURCES + "move/left", ".png");

        // Attack
        float attackDuration = wraith.getState(Wraith.State.ATTACK).getDuration();
        createAnimation(Wraith.State.ATTACK, 0, 4, attackDuration, WRAITH_RESOURCES + "attack/up", ".png");
        createAnimation(Wraith.State.ATTACK, 1, 4, attackDuration, WRAITH_RESOURCES + "attack/down", ".png");
        createAnimation(Wraith.State.ATTACK, 2, 4, attackDuration, WRAITH_RESOURCES + "attack/right", ".png");
        createAnimation(Wraith.State.ATTACK, 3, 4, attackDuration, WRAITH_RESOURCES + "attack/left", ".png");

        // Hit
        float hitDuration = wraith.getState(Wraith.State.HIT).getDuration();
        createAnimation(Wraith.State.HIT, 0, 1, hitDuration, WRAITH_RESOURCES + "hit/up", ".png");
        createAnimation(Wraith.State.HIT, 1, 1, hitDuration, WRAITH_RESOURCES + "hit/down", ".png");
        createAnimation(Wraith.State.HIT, 2, 1, hitDuration, WRAITH_RESOURCES + "hit/left", ".png");
        createAnimation(Wraith.State.HIT, 3, 1, hitDuration, WRAITH_RESOURCES + "hit/right", ".png");

        // Dead
        float deadDuration = wraith.getState(Wraith.State.DEAD).getDuration();
        createAnimation(Wraith.State.DEAD, 0, 4, deadDuration, WRAITH_RESOURCES + "dead/left", ".png");
        createAnimation(Wraith.State.DEAD, 1, 4, deadDuration, WRAITH_RESOURCES + "dead/right", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonState currentEnumState = wraith.getCurrentState();
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

    private int getAnimationType(CommonState currentState) {
        DirectionsModel currentDirection = wraith.getDirection();
        return switch (currentState) {
            case Wraith.State.ATTACK, Wraith.State.IDLE, Wraith.State.MOVE -> switch (currentDirection) {
                case UP -> 0;
                case DOWN -> 1;
                case RIGHT -> 2;
                case LEFT -> 3;
            };
            case Wraith.State.DEAD -> {
                if (currentDirection == DirectionsModel.RIGHT) yield 1;
                else yield 0;
            }
            default -> 0;
        };
    }

    @Override
    public Vector2<Double> getCurrentViewPosition() {
        if (currentGlobalState.isFinished()) {
            currentGlobalState = wraith.getState(wraith.getCurrentState());
            firstTimeInState = true;
        } else if (firstTimeInState) {
            float duration = currentGlobalState.getDuration();
            moveInterpolationX.changeStart(currentViewPosition.first);
            moveInterpolationX.changeEnd(wraith.getCol());
            moveInterpolationX.changeDuration(duration);
            moveInterpolationX.restart();
            moveInterpolationY.changeStart(currentViewPosition.second);
            moveInterpolationY.changeEnd(wraith.getRow());
            moveInterpolationY.changeDuration(duration);
            moveInterpolationY.restart();
            firstTimeInState = false;
        }

        currentViewPosition.changeFirst(moveInterpolationX.getValue());
        currentViewPosition.changeSecond(moveInterpolationY.getValue());
        return currentViewPosition;
    }
}
