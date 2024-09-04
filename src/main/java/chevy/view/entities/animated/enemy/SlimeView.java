package chevy.view.entities.animated.enemy;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.stateMachine.CommonState;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class SlimeView extends AnimatedEntityView {
    private static final String SLIME_RESOURCES = "/sprites/enemy/slime/";
    private final Slime slime;

    public SlimeView(Slime slime) {
        super();
        this.slime = slime;
        this.currentViewPosition = new Vector2<>((double) slime.getCol(), (double) slime.getRow());
        currentVertex = slime.getState(slime.getCurrentState());

        float duration = slime.getState(slime.getCurrentState()).getDuration();
        moveInterpolationX = new Interpolation(currentViewPosition.first, slime.getCol(), duration,
                Interpolation.Type.EASE_OUT_SINE);
        moveInterpolationY = new Interpolation(currentViewPosition.second, slime.getRow(), duration,
                Interpolation.Type.EASE_OUT_SINE);

        initAnimation();
    }

    private void initAnimation() {
        // Idle
        float idleDuration = slime.getState(Slime.State.IDLE).getDuration();
        createAnimation(Slime.State.IDLE, 0, 4, true, 3, idleDuration, SLIME_RESOURCES + "idle", ".png");

        // Move
        float moveDuration = slime.getState(Slime.State.MOVE).getDuration();
        createAnimation(Slime.State.MOVE, 0, 4, moveDuration, SLIME_RESOURCES + "move", ".png");

        // Attack
        float attackDuration = slime.getState(Slime.State.ATTACK).getDuration();
        createAnimation(Slime.State.ATTACK, 0, 4, attackDuration, SLIME_RESOURCES + "attack/up", ".png");
        createAnimation(Slime.State.ATTACK, 1, 4, attackDuration, SLIME_RESOURCES + "attack/down", ".png");
        createAnimation(Slime.State.ATTACK, 2, 4, attackDuration, SLIME_RESOURCES + "attack/right", ".png");
        createAnimation(Slime.State.ATTACK, 3, 4, attackDuration, SLIME_RESOURCES + "attack/left", ".png");

        // Hit
        float hitDuration = slime.getState(Slime.State.HIT).getDuration();
        createAnimation(Slime.State.HIT, 0, 1, hitDuration, SLIME_RESOURCES + "hit", ".png");

        // Dead
        float deadDuration = slime.getState(Slime.State.DEAD).getDuration();
        createAnimation(Slime.State.DEAD, 0, 4, deadDuration, SLIME_RESOURCES + "dead", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonState currentState = slime.getCurrentState();
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
        Direction currentDirection = slime.getDirection();
        return switch (currentState) {
            case Slime.State.ATTACK -> switch (currentDirection) {
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
        if (currentVertex.isFinished()) {
            currentVertex = slime.getState(slime.getCurrentState());
            firstTimeInState = true;
        } else if (firstTimeInState) {
            float duration = currentVertex.getDuration();
            moveInterpolationX.changeStart(currentViewPosition.first);
            moveInterpolationX.changeEnd(slime.getCol());
            moveInterpolationX.changeDuration(duration);
            moveInterpolationX.restart();
            moveInterpolationY.changeStart(currentViewPosition.second);
            moveInterpolationY.changeEnd(slime.getRow());
            moveInterpolationY.changeDuration(duration);
            moveInterpolationY.restart();
            firstTimeInState = false;
        }

        currentViewPosition.changeFirst(moveInterpolationX.getValue());
        currentViewPosition.changeSecond(moveInterpolationY.getValue());
        return currentViewPosition;
    }
}
