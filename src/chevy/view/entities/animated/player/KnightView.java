package chevy.view.entities.animated.player;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.player.Knight;
import chevy.model.entity.stateMachine.CommonStates;
import chevy.utils.Log;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class KnightView extends AnimatedEntityView {
    private static final String KNIGHT_RESOURCES = "/assets/player/knight/";
    private final Knight knight;

    public KnightView(Knight knight) {
        super();
        this.knight = knight;
        currentPosition = new Vector2<>((double) knight.getCol(), (double) knight.getRow());
        currentState = knight.getState(knight.getCurrentState());

        float duration = knight.getState(knight.getCurrentState()).getDuration();
        moveInterpolationX = new Interpolation(currentPosition.first, knight.getCol(), duration,
                Interpolation.Types.EASE_OUT_SINE);
        moveInterpolationY = new Interpolation(currentPosition.second, knight.getRow(), duration,
                Interpolation.Types.EASE_OUT_SINE);

        initAnimation();
    }

    private void initAnimation() {
        final float idleDuration = 1f;
        createAnimation(Knight.States.IDLE, 0, 2, true, 2, idleDuration, KNIGHT_RESOURCES + "idle/up", ".png");
        createAnimation(Knight.States.IDLE, 1, 2, true, 2, idleDuration, KNIGHT_RESOURCES + "idle/down", ".png");
        createAnimation(Knight.States.IDLE, 2, 2, true, 2, idleDuration, KNIGHT_RESOURCES + "idle/right", ".png");
        createAnimation(Knight.States.IDLE, 3, 2, true, 2, idleDuration, KNIGHT_RESOURCES + "idle/left", ".png");

        final float moveDuration = knight.getState(Knight.States.MOVE).getDuration();
        createAnimation(Knight.States.MOVE, 0, 4, moveDuration, KNIGHT_RESOURCES + "move/up", ".png");
        createAnimation(Knight.States.MOVE, 1, 4, moveDuration, KNIGHT_RESOURCES + "move/down", ".png");
        createAnimation(Knight.States.MOVE, 2, 4, moveDuration, KNIGHT_RESOURCES + "move/right", ".png");
        createAnimation(Knight.States.MOVE, 3, 4, moveDuration, KNIGHT_RESOURCES + "move/left", ".png");

        final float attackDuration = knight.getState(Knight.States.ATTACK).getDuration();
        createAnimation(Knight.States.ATTACK, 0, 6, attackDuration, KNIGHT_RESOURCES + "attack/up", ".png");
        createAnimation(Knight.States.ATTACK, 1, 6, attackDuration, KNIGHT_RESOURCES + "attack/down", ".png");
        createAnimation(Knight.States.ATTACK, 2, 6, attackDuration, KNIGHT_RESOURCES + "attack/right", ".png");
        createAnimation(Knight.States.ATTACK, 3, 6, attackDuration, KNIGHT_RESOURCES + "attack/left", ".png");

        final float hitDuration = knight.getState(Knight.States.HIT).getDuration();
        createAnimation(Knight.States.HIT, 0, 1, hitDuration, KNIGHT_RESOURCES + "hit/up", ".png");
        createAnimation(Knight.States.HIT, 1, 1, hitDuration, KNIGHT_RESOURCES + "hit/down", ".png");
        createAnimation(Knight.States.HIT, 2, 1, hitDuration, KNIGHT_RESOURCES + "hit/right", ".png");
        createAnimation(Knight.States.HIT, 3, 1, hitDuration, KNIGHT_RESOURCES + "hit/left", ".png");

        final float deadDuration = knight.getState(Knight.States.DEAD).getDuration();
        createAnimation(Knight.States.DEAD, 0, 2, deadDuration, KNIGHT_RESOURCES + "dead/left", ".png");
        createAnimation(Knight.States.DEAD, 1, 2, deadDuration, KNIGHT_RESOURCES + "dead/right", ".png");

        final float sludgeDuration = knight.getState(Knight.States.SLUDGE).getDuration();
        createAnimation(Knight.States.SLUDGE, 0, 1, sludgeDuration, KNIGHT_RESOURCES + "idle/right", ".png");
        createAnimation(Knight.States.SLUDGE, 1, 1, sludgeDuration, KNIGHT_RESOURCES + "idle/left", ".png");

        float fallDuration = knight.getState(Knight.States.FALL).getDuration();
        createAnimation(Knight.States.FALL, 0, 2, fallDuration, KNIGHT_RESOURCES + "dead/right", ".png");
        createAnimation(Knight.States.FALL, 1, 2, fallDuration, KNIGHT_RESOURCES + "dead/left", ".png");

        float glideDuration = knight.getState(Knight.States.GLIDE).getDuration();
        createAnimation(Knight.States.GLIDE, 0, 1, glideDuration, KNIGHT_RESOURCES + "idle/right", ".png");
        createAnimation(Knight.States.GLIDE, 1, 1, glideDuration, KNIGHT_RESOURCES + "idle/left", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonStates currentState = knight.getCurrentState();
        int type = getAnimationType(currentState);
        AnimatedSprite animatedSprite = this.getAnimatedSprite(currentState, type);

        if (animatedSprite != null) {
            if (!animatedSprite.isRunning()) {
                animatedSprite.restart();
            }
            return animatedSprite.getCurrentFrame();
        }
        Log.error("KnightView: il frame corrente è nullo, " + currentState + " " + type);
        System.exit(1);
        return null;
    }

    private int getAnimationType(CommonStates currentState) {
        DirectionsModel currentDirection = knight.getDirection();
        return switch (currentState) {
            case Knight.States.ATTACK, Knight.States.IDLE, Knight.States.MOVE, Knight.States.HIT ->
                    switch (currentDirection) {
                        case UP -> 0;
                        case DOWN -> 1;
                        case RIGHT -> 2;
                        case LEFT -> 3;
                    };
            case Knight.States.GLIDE, Knight.States.SLUDGE, Knight.States.DEAD, Knight.States.FALL -> {
                if (currentDirection == DirectionsModel.RIGHT) {
                    yield 0;
                } else {
                    yield 1;
                }
            }
            default -> 0;
        };
    }

    @Override
    public Vector2<Double> getCurrentPosition() {
        if (currentState.isFinished()) {
            currentState = knight.getState(knight.getCurrentState());
            firstTimeInState = true;
        } else if (firstTimeInState) {
            float duration = currentState.getDuration();
            moveInterpolationX.changeStart(currentPosition.first);
            moveInterpolationX.changeEnd(knight.getCol());
            moveInterpolationX.changeDuration(duration);
            moveInterpolationX.restart();
            moveInterpolationY.changeStart(currentPosition.second);
            moveInterpolationY.changeEnd(knight.getRow());
            moveInterpolationY.changeDuration(duration);
            moveInterpolationY.restart();
            firstTimeInState = false;
        }

        currentPosition.changeFirst(moveInterpolationX.getValue());
        currentPosition.changeSecond(moveInterpolationY.getValue());
        return currentPosition;
    }
}