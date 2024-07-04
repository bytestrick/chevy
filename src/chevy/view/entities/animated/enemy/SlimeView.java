package chevy.view.entities.animated.enemy;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.stateMachine.CommonStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class SlimeView extends AnimatedEntityView {
    private static final String SLIME_RESOURCES = "/assets/enemy/slime/";
    private final Slime slime;

    public SlimeView(Slime slime) {
        super();
        this.slime = slime;
        this.currentPosition = new Vector2<>((double) slime.getCol(), (double) slime.getRow());
        currentState = slime.getState(slime.getCurrentState());

        float duration = slime.getState(slime.getCurrentState()).getDuration();
        moveInterpolationX = new Interpolation(currentPosition.first, slime.getCol(), duration,
                Interpolation.Types.EASE_OUT_SINE);
        moveInterpolationY = new Interpolation(currentPosition.second, slime.getRow(), duration,
                Interpolation.Types.EASE_OUT_SINE);

        initAnimation();
    }

    private void initAnimation() {
        // Idle
        float idleDuration = slime.getState(Slime.States.IDLE).getDuration();
        createAnimation(Slime.States.IDLE, 0, 4, true, 3, idleDuration, SLIME_RESOURCES + "idle", ".png");

        // Move
        float moveDuration = slime.getState(Slime.States.MOVE).getDuration();
        createAnimation(Slime.States.MOVE, 0, 4, moveDuration, SLIME_RESOURCES + "move", ".png");

        // Attack
        float attackDuration = slime.getState(Slime.States.ATTACK).getDuration();
        createAnimation(Slime.States.ATTACK, 0, 4, attackDuration, SLIME_RESOURCES + "attack/up", ".png");
        createAnimation(Slime.States.ATTACK, 1, 4, attackDuration, SLIME_RESOURCES + "attack/down", ".png");
        createAnimation(Slime.States.ATTACK, 2, 4, attackDuration, SLIME_RESOURCES + "attack/right", ".png");
        createAnimation(Slime.States.ATTACK, 3, 4, attackDuration, SLIME_RESOURCES + "attack/left", ".png");

        // Hit
        float hitDuration = slime.getState(Slime.States.HIT).getDuration();
        createAnimation(Slime.States.HIT, 0, 1, hitDuration, SLIME_RESOURCES + "hit", ".png");

        // Dead
        float deadDuration = slime.getState(Slime.States.DEAD).getDuration();
        createAnimation(Slime.States.DEAD, 0, 4, deadDuration, SLIME_RESOURCES + "dead", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonStates currentState = slime.getCurrentState();
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
        DirectionsModel currentDirection = slime.getDirection();
        return switch (currentState) {
            case Slime.States.ATTACK -> switch (currentDirection) {
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
            currentState = slime.getState(slime.getCurrentState());
            firstTimeInState = true;
        } else if (firstTimeInState) {
            float duration = currentState.getDuration();
            moveInterpolationX.changeStart(currentPosition.first);
            moveInterpolationX.changeEnd(slime.getCol());
            moveInterpolationX.changeDuration(duration);
            moveInterpolationX.restart();
            moveInterpolationY.changeStart(currentPosition.second);
            moveInterpolationY.changeEnd(slime.getRow());
            moveInterpolationY.changeDuration(duration);
            moveInterpolationY.restart();
            firstTimeInState = false;
        }

        currentPosition.changeFirst(moveInterpolationX.getValue());
        currentPosition.changeSecond(moveInterpolationY.getValue());
        return currentPosition;
    }
}