package chevy.view.entities.animated.player;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.player.Knight;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.stateMachine.CommonState;
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
        this.currentViewPosition = new Vector2<>((double) knight.getCol(), (double) knight.getRow());

        currentGlobalState = knight.getState(knight.getCurrentState());
        float duration = knight.getState(knight.getCurrentState()).getDuration();
        moveInterpolationX = new Interpolation(currentViewPosition.first, knight.getCol(), duration,
                Interpolation.Type.EASE_OUT_SINE);
        moveInterpolationY = new Interpolation(currentViewPosition.second, knight.getRow(), duration,
                Interpolation.Type.EASE_OUT_SINE);

        initAnimation();
    }

    private void initAnimation() {
        Vector2<Integer> offset = new Vector2<>(-8, -8);

        final float idleDuration = 1f;
        createAnimation(Player.State.IDLE, 0, 2, true, 2, idleDuration, offset, 1, KNIGHT_RESOURCES + "idle/up", ".png");
        createAnimation(Player.State.IDLE, 1, 2, true, 2, idleDuration, offset, 1, KNIGHT_RESOURCES + "idle/down", ".png");
        createAnimation(Player.State.IDLE, 2, 2, true, 2, idleDuration, offset, 1, KNIGHT_RESOURCES + "idle/right", ".png");
        createAnimation(Player.State.IDLE, 3, 2, true, 2, idleDuration, offset, 1, KNIGHT_RESOURCES + "idle/left", ".png");

        final float moveDuration = knight.getState(Player.State.MOVE).getDuration();
        createAnimation(Player.State.MOVE, 0, 8, moveDuration, offset, 1, KNIGHT_RESOURCES + "move/up", ".png");
        createAnimation(Player.State.MOVE, 1, 8, moveDuration, offset, 1, KNIGHT_RESOURCES + "move/down", ".png");
        createAnimation(Player.State.MOVE, 2, 8, moveDuration, offset, 1, KNIGHT_RESOURCES + "move/right", ".png");
        createAnimation(Player.State.MOVE, 3, 8, moveDuration, offset, 1, KNIGHT_RESOURCES + "move/left", ".png");

        final float attackDuration = knight.getState(Player.State.ATTACK).getDuration();
        createAnimation(Player.State.ATTACK, 0, 6, attackDuration, offset, 1, KNIGHT_RESOURCES + "attack/up", ".png");
        createAnimation(Player.State.ATTACK, 1, 6, attackDuration, offset, 1, KNIGHT_RESOURCES + "attack/down", ".png");
        createAnimation(Player.State.ATTACK, 2, 6, attackDuration, offset, 1, KNIGHT_RESOURCES + "attack/right", ".png");
        createAnimation(Player.State.ATTACK, 3, 6, attackDuration, offset, 1, KNIGHT_RESOURCES + "attack/left", ".png");

        final float hitDuration = knight.getState(Player.State.HIT).getDuration();
        createAnimation(Player.State.HIT, 0, 1, hitDuration, offset, 1, KNIGHT_RESOURCES + "hit/up", ".png");
        createAnimation(Player.State.HIT, 1, 1, hitDuration, offset, 1, KNIGHT_RESOURCES + "hit/down", ".png");
        createAnimation(Player.State.HIT, 2, 1, hitDuration, offset, 1, KNIGHT_RESOURCES + "hit/right", ".png");
        createAnimation(Player.State.HIT, 3, 1, hitDuration, offset, 1, KNIGHT_RESOURCES + "hit/left", ".png");

        final float deadDuration = knight.getState(Player.State.DEAD).getDuration();
        createAnimation(Player.State.DEAD, 0, 8, deadDuration, offset, 1, KNIGHT_RESOURCES + "dead/right", ".png");
        createAnimation(Player.State.DEAD, 1, 8, deadDuration, offset, 1, KNIGHT_RESOURCES + "dead/left", ".png");

        final float sludgeDuration = knight.getState(Player.State.SLUDGE).getDuration();
        createAnimation(Player.State.SLUDGE, 0, 1, sludgeDuration, offset, 1, KNIGHT_RESOURCES + "idle/right", ".png");
        createAnimation(Player.State.SLUDGE, 1, 1, sludgeDuration, offset, 1, KNIGHT_RESOURCES + "idle/left", ".png");

        float fallDuration = knight.getState(Player.State.FALL).getDuration();
        createAnimation(Player.State.FALL, 0, 2, fallDuration, offset, 1, KNIGHT_RESOURCES + "dead/right", ".png");
        createAnimation(Player.State.FALL, 1, 2, fallDuration, offset, 1, KNIGHT_RESOURCES + "dead/left", ".png");

        float glideDuration = knight.getState(Player.State.GLIDE).getDuration();
        createAnimation(Player.State.GLIDE, 0, 1, glideDuration, offset, 1, KNIGHT_RESOURCES + "idle/right", ".png");
        createAnimation(Player.State.GLIDE, 1, 1, glideDuration, offset, 1, KNIGHT_RESOURCES + "idle/left", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonState currentState = knight.getCurrentState();
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

    private int getAnimationType(CommonState currentState) {
        DirectionsModel currentDirection = knight.getDirection();
        return switch (currentState) {
            case Player.State.ATTACK, Player.State.IDLE, Player.State.MOVE, Player.State.HIT ->
                    switch (currentDirection) {
                        case UP -> 0;
                        case DOWN -> 1;
                        case RIGHT -> 2;
                        case LEFT -> 3;
                    };
            case Player.State.GLIDE, Player.State.SLUDGE, Player.State.DEAD, Player.State.FALL -> {
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
    public Vector2<Integer> getOffset() {
        CommonState currentState = knight.getCurrentState();
        int type = getAnimationType(currentState);
        AnimatedSprite animatedSprite = this.getAnimatedSprite(currentState, type);

        if (animatedSprite != null) {
            return animatedSprite.getOffset();
        }
        return null;
    }

    @Override
    public Vector2<Double> getCurrentViewPosition() {
        if (currentGlobalState.isFinished()) {
            currentGlobalState = knight.getState(knight.getCurrentState());
            firstTimeInState = true;
        } else if (firstTimeInState) {
            float duration = currentGlobalState.getDuration();
            moveInterpolationX.changeStart(currentViewPosition.first);
            moveInterpolationX.changeEnd(knight.getCol());
            moveInterpolationX.changeDuration(duration);
            moveInterpolationX.restart();
            moveInterpolationY.changeStart(currentViewPosition.second);
            moveInterpolationY.changeEnd(knight.getRow());
            moveInterpolationY.changeDuration(duration);
            moveInterpolationY.restart();
            firstTimeInState = false;
        }

        currentViewPosition.changeFirst(moveInterpolationX.getValue());
        currentViewPosition.changeSecond(moveInterpolationY.getValue());
        return currentViewPosition;
    }
    @Override
    public void wasRemoved() {
        moveInterpolationX.delete();
        moveInterpolationY.delete();
        super.deleteAnimations();
    }
}
