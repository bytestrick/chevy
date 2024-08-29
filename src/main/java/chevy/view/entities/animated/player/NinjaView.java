package chevy.view.entities.animated.player;

import chevy.model.entity.dynamicEntity.DirectionsModel;
import chevy.model.entity.dynamicEntity.liveEntity.player.Ninja;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.stateMachine.CommonState;
import chevy.utils.Log;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class NinjaView extends AnimatedEntityView {
    private static final String NINJA_RESOURCES = "/sprites/player/ninja/";
    private final Ninja ninja;

    public NinjaView(Ninja ninja) {
        super();
        this.ninja = ninja;
        currentViewPosition = new Vector2<>((double) ninja.getCol(), (double) ninja.getRow());
        currentGlobalState = ninja.getState(ninja.getCurrentState());

        float duration = ninja.getState(ninja.getCurrentState()).getDuration();
        moveInterpolationX = new Interpolation(currentViewPosition.first, ninja.getCol(), duration,
                Interpolation.Type.EASE_OUT_SINE);
        moveInterpolationY = new Interpolation(currentViewPosition.second, ninja.getRow(), duration,
                Interpolation.Type.EASE_OUT_SINE);

        initAnimation();
    }

    private void initAnimation() {
        final float idleDuration = 1f;
        createAnimation(Player.State.IDLE, 0, 2, true, 2, idleDuration, NINJA_RESOURCES + "idle/up", ".png");
        createAnimation(Player.State.IDLE, 1, 2, true, 2, idleDuration, NINJA_RESOURCES + "idle/down", ".png");
        createAnimation(Player.State.IDLE, 2, 2, true, 2, idleDuration, NINJA_RESOURCES + "idle/right", ".png");
        createAnimation(Player.State.IDLE, 3, 2, true, 2, idleDuration, NINJA_RESOURCES + "idle/left", ".png");

        final float moveDuration = ninja.getState(Player.State.MOVE).getDuration();
        createAnimation(Player.State.MOVE, 0, 3, moveDuration, NINJA_RESOURCES + "move/up", ".png");
        createAnimation(Player.State.MOVE, 1, 3, moveDuration, NINJA_RESOURCES + "move/down", ".png");
        createAnimation(Player.State.MOVE, 2, 3, moveDuration, NINJA_RESOURCES + "move/right", ".png");
        createAnimation(Player.State.MOVE, 3, 3, moveDuration, NINJA_RESOURCES + "move/left", ".png");

        final float attackDuration = ninja.getState(Player.State.ATTACK).getDuration();
        createAnimation(Player.State.ATTACK, 0, 2, attackDuration, NINJA_RESOURCES + "attack/up", ".png");
        createAnimation(Player.State.ATTACK, 1, 3, attackDuration, NINJA_RESOURCES + "attack/down", ".png");
        createAnimation(Player.State.ATTACK, 2, 3, attackDuration, NINJA_RESOURCES + "attack/right", ".png");
        createAnimation(Player.State.ATTACK, 3, 3, attackDuration, NINJA_RESOURCES + "attack/left", ".png");

        final float hitDuration = ninja.getState(Player.State.HIT).getDuration();
        createAnimation(Player.State.HIT, 0, 1, hitDuration, NINJA_RESOURCES + "hit/up", ".png");
        createAnimation(Player.State.HIT, 1, 1, hitDuration, NINJA_RESOURCES + "hit/down", ".png");
        createAnimation(Player.State.HIT, 2, 1, hitDuration, NINJA_RESOURCES + "hit/right", ".png");
        createAnimation(Player.State.HIT, 3, 1, hitDuration, NINJA_RESOURCES + "hit/left", ".png");

        final float deadDuration = ninja.getState(Player.State.DEAD).getDuration();
        createAnimation(Player.State.DEAD, 0, 1, deadDuration, NINJA_RESOURCES + "dead", ".png");
        createAnimation(Player.State.DEAD, 1, 1, deadDuration, NINJA_RESOURCES + "dead", ".png");

        final float sludgeDuration = ninja.getState(Player.State.SLUDGE).getDuration();
        createAnimation(Player.State.SLUDGE, 0, 1, sludgeDuration, NINJA_RESOURCES + "idle/up", ".png");
        createAnimation(Player.State.SLUDGE, 1, 1, sludgeDuration, NINJA_RESOURCES + "idle/down", ".png");
        createAnimation(Player.State.SLUDGE, 2, 1, sludgeDuration, NINJA_RESOURCES + "idle/right", ".png");
        createAnimation(Player.State.SLUDGE, 3, 1, sludgeDuration, NINJA_RESOURCES + "idle/left", ".png");

        float fallDuration = ninja.getState(Player.State.FALL).getDuration();
        createAnimation(Player.State.FALL, 0, 1, fallDuration, NINJA_RESOURCES + "dead", ".png");
        createAnimation(Player.State.FALL, 1, 1, fallDuration, NINJA_RESOURCES + "dead", ".png");

        float glideDuration = ninja.getState(Player.State.GLIDE).getDuration();
        createAnimation(Player.State.GLIDE, 0, 1, glideDuration, NINJA_RESOURCES + "glide/up", ".png");
        createAnimation(Player.State.GLIDE, 1, 1, glideDuration, NINJA_RESOURCES + "glide/down", ".png");
        createAnimation(Player.State.GLIDE, 2, 1, glideDuration, NINJA_RESOURCES + "glide/right", ".png");
        createAnimation(Player.State.GLIDE, 3, 1, glideDuration, NINJA_RESOURCES + "glide/left", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonState currentState = ninja.getCurrentState();
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

    private int getAnimationType(CommonState currentState) {
        DirectionsModel currentDirection = ninja.getDirection();
        return switch (currentState) {
            case Player.State.ATTACK, Player.State.IDLE, Player.State.MOVE, Player.State.HIT, Player.State.GLIDE,
                 Player.State.SLUDGE -> switch (currentDirection) {
                case UP -> 0;
                case DOWN -> 1;
                case RIGHT -> 2;
                case LEFT -> 3;
            };
            case Player.State.DEAD, Player.State.FALL -> {
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
    public Vector2<Double> getCurrentViewPosition() {
        if (currentGlobalState.isFinished()) {
            currentGlobalState = ninja.getState(ninja.getCurrentState());
            firstTimeInState = true;
        } else if (firstTimeInState) {
            float duration = currentGlobalState.getDuration();
            moveInterpolationX.changeStart(currentViewPosition.first);
            moveInterpolationX.changeEnd(ninja.getCol());
            moveInterpolationX.changeDuration(duration);
            moveInterpolationX.restart();
            moveInterpolationY.changeStart(currentViewPosition.second);
            moveInterpolationY.changeEnd(ninja.getRow());
            moveInterpolationY.changeDuration(duration);
            moveInterpolationY.restart();
            firstTimeInState = false;
        }

        currentViewPosition.changeFirst(moveInterpolationX.getValue());
        currentViewPosition.changeSecond(moveInterpolationY.getValue());
        return currentViewPosition;
    }
}