package chevy.view.entities.animated.player;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.player.Ninja;
import chevy.model.entity.stateMachine.CommonStates;
import chevy.utils.Log;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class NinjaView extends AnimatedEntityView {
    private static final String NINJA_RESOURCES = "/assets/player/ninja/";
    private final Ninja ninja;

    public NinjaView(Ninja ninja) {
        super();
        this.ninja = ninja;
        currentPosition = new Vector2<>((double) ninja.getCol(), (double) ninja.getRow());
        currentState = ninja.getState(ninja.getCurrentState());

        float duration = ninja.getState(ninja.getCurrentState()).getDuration();
        moveInterpolationX = new Interpolation(currentPosition.first, ninja.getCol(), duration,
                Interpolation.Types.EASE_OUT_SINE);
        moveInterpolationY = new Interpolation(currentPosition.second, ninja.getRow(), duration,
                Interpolation.Types.EASE_OUT_SINE);

        initAnimation();
    }

    private void initAnimation() {
        final float idleDuration = 1f;
        createAnimation(Ninja.States.IDLE, 0, 2, true, 2, idleDuration, NINJA_RESOURCES + "idle/up", ".png");
        createAnimation(Ninja.States.IDLE, 1, 2, true, 2, idleDuration, NINJA_RESOURCES + "idle/down", ".png");
        createAnimation(Ninja.States.IDLE, 2, 2, true, 2, idleDuration, NINJA_RESOURCES + "idle/right", ".png");
        createAnimation(Ninja.States.IDLE, 3, 2, true, 2, idleDuration, NINJA_RESOURCES + "idle/left", ".png");

        final float moveDuration = ninja.getState(Ninja.States.MOVE).getDuration();
        createAnimation(Ninja.States.MOVE, 0, 3, moveDuration, NINJA_RESOURCES + "move/up", ".png");
        createAnimation(Ninja.States.MOVE, 1, 3, moveDuration, NINJA_RESOURCES + "move/down", ".png");
        createAnimation(Ninja.States.MOVE, 2, 3, moveDuration, NINJA_RESOURCES + "move/right", ".png");
        createAnimation(Ninja.States.MOVE, 3, 3, moveDuration, NINJA_RESOURCES + "move/left", ".png");

        final float attackDuration = ninja.getState(Ninja.States.ATTACK).getDuration();
        createAnimation(Ninja.States.ATTACK, 0, 2, attackDuration, NINJA_RESOURCES + "attack/up", ".png");
        createAnimation(Ninja.States.ATTACK, 1, 3, attackDuration, NINJA_RESOURCES + "attack/down", ".png");
        createAnimation(Ninja.States.ATTACK, 2, 3, attackDuration, NINJA_RESOURCES + "attack/right", ".png");
        createAnimation(Ninja.States.ATTACK, 3, 3, attackDuration, NINJA_RESOURCES + "attack/left", ".png");

        final float hitDuration = ninja.getState(Ninja.States.HIT).getDuration();
        createAnimation(Ninja.States.HIT, 0, 1, hitDuration, NINJA_RESOURCES + "hit/up", ".png");
        createAnimation(Ninja.States.HIT, 1, 1, hitDuration, NINJA_RESOURCES + "hit/down", ".png");
        createAnimation(Ninja.States.HIT, 2, 1, hitDuration, NINJA_RESOURCES + "hit/right", ".png");
        createAnimation(Ninja.States.HIT, 3, 1, hitDuration, NINJA_RESOURCES + "hit/left", ".png");

        final float deadDuration = ninja.getState(Ninja.States.DEAD).getDuration();
        createAnimation(Ninja.States.DEAD, 0, 1, deadDuration, NINJA_RESOURCES + "dead", ".png");
        createAnimation(Ninja.States.DEAD, 1, 1, deadDuration, NINJA_RESOURCES + "dead", ".png");

        final float sludgeDuration = ninja.getState(Ninja.States.SLUDGE).getDuration();
        createAnimation(Ninja.States.SLUDGE, 0, 1, sludgeDuration, NINJA_RESOURCES + "idle/up", ".png");
        createAnimation(Ninja.States.SLUDGE, 1, 1, sludgeDuration, NINJA_RESOURCES + "idle/down", ".png");
        createAnimation(Ninja.States.SLUDGE, 2, 1, sludgeDuration, NINJA_RESOURCES + "idle/right", ".png");
        createAnimation(Ninja.States.SLUDGE, 3, 1, sludgeDuration, NINJA_RESOURCES + "idle/left", ".png");

        float fallDuration = ninja.getState(Ninja.States.FALL).getDuration();
        createAnimation(Ninja.States.FALL, 0, 1, fallDuration, NINJA_RESOURCES + "dead", ".png");
        createAnimation(Ninja.States.FALL, 1, 1, fallDuration, NINJA_RESOURCES + "dead", ".png");

        float glideDuration = ninja.getState(Ninja.States.GLIDE).getDuration();
        createAnimation(Ninja.States.GLIDE, 0, 1, glideDuration, NINJA_RESOURCES + "glide/up", ".png");
        createAnimation(Ninja.States.GLIDE, 1, 1, glideDuration, NINJA_RESOURCES + "glide/down", ".png");
        createAnimation(Ninja.States.GLIDE, 2, 1, glideDuration, NINJA_RESOURCES + "glide/right", ".png");
        createAnimation(Ninja.States.GLIDE, 3, 1, glideDuration, NINJA_RESOURCES + "glide/left", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonStates currentState = ninja.getCurrentState();
        int type = getAnimationType(currentState);
        AnimatedSprite animatedSprite = this.getAnimatedSprite(currentState, type);

        if (animatedSprite != null) {
            if (!animatedSprite.isRunning()) {
                animatedSprite.restart();
            }
            return animatedSprite.getCurrentFrame();
        }
        Log.error("NinjaView: il frame corrente è nullo, " + currentState + " " + type);
        System.exit(1);
        return null;
    }

    private int getAnimationType(CommonStates currentState) {
        DirectionsModel currentDirection = ninja.getDirection();
        return switch (currentState) {
            case Ninja.States.ATTACK, Ninja.States.IDLE, Ninja.States.MOVE, Ninja.States.HIT , Ninja.States.GLIDE,Ninja.States.SLUDGE->
                    switch (currentDirection) {
                        case UP -> 0;
                        case DOWN -> 1;
                        case RIGHT -> 2;
                        case LEFT -> 3;
                    };
            case  Ninja.States.DEAD, Ninja.States.FALL -> {
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
            currentState = ninja.getState(ninja.getCurrentState());
            firstTimeInState = true;
        } else if (firstTimeInState) {
            float duration = currentState.getDuration();
            moveInterpolationX.changeStart(currentPosition.first);
            moveInterpolationX.changeEnd(ninja.getCol());
            moveInterpolationX.changeDuration(duration);
            moveInterpolationX.restart();
            moveInterpolationY.changeStart(currentPosition.second);
            moveInterpolationY.changeEnd(ninja.getRow());
            moveInterpolationY.changeDuration(duration);
            moveInterpolationY.restart();
            firstTimeInState = false;
        }

        currentPosition.changeFirst(moveInterpolationX.getValue());
        currentPosition.changeSecond(moveInterpolationY.getValue());
        return currentPosition;
    }
}