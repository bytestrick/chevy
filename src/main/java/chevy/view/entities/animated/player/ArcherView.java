package chevy.view.entities.animated.player;

import chevy.model.entity.dynamicEntity.DirectionsModel;
import chevy.model.entity.dynamicEntity.liveEntity.player.Archer;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.stateMachine.CommonState;
import chevy.utils.Log;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class ArcherView extends AnimatedEntityView {
    private static final String ARCHER_RESOURCES = "/sprites/player/archer/";
    private final Archer archer;

    public ArcherView(Archer archer) {
        super();
        this.archer = archer;
        currentViewPosition = new Vector2<>((double) archer.getCol(), (double) archer.getRow());
        currentGlobalState = archer.getState(archer.getCurrentState());

        float duration = archer.getState(archer.getCurrentState()).getDuration();
        moveInterpolationX = new Interpolation(currentViewPosition.first, archer.getCol(), duration,
                Interpolation.Type.EASE_OUT_SINE);
        moveInterpolationY = new Interpolation(currentViewPosition.second, archer.getRow(), duration,
                Interpolation.Type.EASE_OUT_SINE);
        initAnimation();
    }

    private void initAnimation() {
        final float idleDuration = 1f;
        createAnimation(Player.State.IDLE, 0, 2, true, 2, idleDuration, ARCHER_RESOURCES + "idle/up", ".png");
        createAnimation(Player.State.IDLE, 1, 2, true, 2, idleDuration, ARCHER_RESOURCES + "idle/down", ".png");
        createAnimation(Player.State.IDLE, 2, 4, true, 2, idleDuration, ARCHER_RESOURCES + "idle/right", ".png");
        createAnimation(Player.State.IDLE, 3, 4, true, 2, idleDuration, ARCHER_RESOURCES + "idle/left", ".png");

        //final float moveDuration = archer.getState(Archer.State.MOVE).getDuration();
        final float moveDuration = archer.getState(Player.State.MOVE).getDuration();
        createAnimation(Player.State.MOVE, 0, 2, moveDuration, ARCHER_RESOURCES + "move/up", ".png");
        createAnimation(Player.State.MOVE, 1, 2, moveDuration, ARCHER_RESOURCES + "move/down", ".png");
        createAnimation(Player.State.MOVE, 2, 2, moveDuration, ARCHER_RESOURCES + "move/right", ".png");
        createAnimation(Player.State.MOVE, 3, 2, moveDuration, ARCHER_RESOURCES + "move/left", ".png");

        final float attackDuration = archer.getState(Player.State.ATTACK).getDuration();
        createAnimation(Player.State.ATTACK, 0, 6, attackDuration, ARCHER_RESOURCES + "attack/up", ".png");
        createAnimation(Player.State.ATTACK, 1, 4, attackDuration, ARCHER_RESOURCES + "attack/down", ".png");
        createAnimation(Player.State.ATTACK, 2, 8, attackDuration, ARCHER_RESOURCES + "attack/right", ".png");
        createAnimation(Player.State.ATTACK, 3, 8, attackDuration, ARCHER_RESOURCES + "attack/left", ".png");

        final float hitDuration = archer.getState(Player.State.HIT).getDuration();
        createAnimation(Player.State.HIT, 0, 4, hitDuration, ARCHER_RESOURCES + "hit/up", ".png");
        createAnimation(Player.State.HIT, 1, 4, hitDuration, ARCHER_RESOURCES + "hit/down", ".png");
        createAnimation(Player.State.HIT, 2, 4, hitDuration, ARCHER_RESOURCES + "hit/right", ".png");
        createAnimation(Player.State.HIT, 3, 4, hitDuration, ARCHER_RESOURCES + "hit/left", ".png");

        final float deadDuration = archer.getState(Player.State.DEAD).getDuration();
        createAnimation(Player.State.DEAD, 0, 5, deadDuration, ARCHER_RESOURCES + "dead/left", ".png");
        createAnimation(Player.State.DEAD, 1, 5, deadDuration, ARCHER_RESOURCES + "dead/right", ".png");

        final float sludgeDuration = archer.getState(Player.State.SLUDGE).getDuration();
        createAnimation(Player.State.SLUDGE, 0, 1, sludgeDuration, ARCHER_RESOURCES + "glide/right", ".png");
        createAnimation(Player.State.SLUDGE, 1, 1, sludgeDuration, ARCHER_RESOURCES + "glide/left", ".png");

        float fallDuration = archer.getState(Player.State.FALL).getDuration();
        createAnimation(Player.State.FALL, 0, 2, fallDuration, ARCHER_RESOURCES + "dead/right", ".png");
        createAnimation(Player.State.FALL, 1, 2, fallDuration, ARCHER_RESOURCES + "dead/left", ".png");

        float glideDuration = archer.getState(Player.State.GLIDE).getDuration();
        createAnimation(Player.State.GLIDE, 0, 3, glideDuration, ARCHER_RESOURCES + "glide/right", ".png");
        createAnimation(Player.State.GLIDE, 1, 3, glideDuration, ARCHER_RESOURCES + "glide/left", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonState currentState = archer.getCurrentState();
        int type = getAnimationType(currentState);
        AnimatedSprite animatedSprite = this.getAnimatedSprite(currentState, type);

        if (animatedSprite != null) {
            if (!animatedSprite.isRunning()) {
                animatedSprite.restart();
            }
            return animatedSprite.getCurrentFrame();
        }
        Log.error("ArcherView: il frame corrente è nullo, " + currentState + " " + type);
        System.exit(1);
        return null;
    }

    private int getAnimationType(CommonState currentState) {
        DirectionsModel currentDirection = archer.getDirection();
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
    public Vector2<Double> getCurrentViewPosition() {
        if (currentGlobalState.isFinished()) {
            currentGlobalState = archer.getState(archer.getCurrentState());
            firstTimeInState = true;
        } else if (firstTimeInState) {
            float duration = currentGlobalState.getDuration();
            moveInterpolationX.changeStart(currentViewPosition.first);
            moveInterpolationX.changeEnd(archer.getCol());
            moveInterpolationX.changeDuration(duration);
            moveInterpolationX.restart();
            moveInterpolationY.changeStart(currentViewPosition.second);
            moveInterpolationY.changeEnd(archer.getRow());
            moveInterpolationY.changeDuration(duration);
            moveInterpolationY.restart();
            firstTimeInState = false;
        }

        currentViewPosition.changeFirst(moveInterpolationX.getValue());
        currentViewPosition.changeSecond(moveInterpolationY.getValue());
        return currentViewPosition;
    }
}