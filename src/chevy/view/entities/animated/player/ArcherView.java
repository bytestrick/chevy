package chevy.view.entities.animated.player;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.player.Archer;
import chevy.model.entity.stateMachine.CommonStates;
import chevy.utils.Log;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class ArcherView extends AnimatedEntityView {
    private static final String ARCHER_RESOURCES = "/assets/player/archer/";
    private final Archer archer;

    public ArcherView(Archer archer) {
        super();
        this.archer = archer;
        currentPosition = new Vector2<>((double) archer.getCol(), (double) archer.getRow());
        currentState = archer.getState(archer.getCurrentState());

        float duration = archer.getState(archer.getCurrentState()).getDuration();
        moveInterpolationX = new Interpolation(currentPosition.first, archer.getCol(), duration,
                Interpolation.Types.EASE_OUT_SINE);
        moveInterpolationY = new Interpolation(currentPosition.second, archer.getRow(), duration,
                Interpolation.Types.EASE_OUT_SINE);

        initAnimation();
    }

    private void initAnimation() {
        final float idleDuration = 1f;
        createAnimation(Archer.States.IDLE, 0, 2, true, 2, idleDuration, ARCHER_RESOURCES + "idle/up", ".png");
        createAnimation(Archer.States.IDLE, 1, 2, true, 2, idleDuration, ARCHER_RESOURCES + "idle/down", ".png");
        createAnimation(Archer.States.IDLE, 2, 4, true, 2, idleDuration, ARCHER_RESOURCES + "idle/right", ".png");
        createAnimation(Archer.States.IDLE, 3, 4, true, 2, idleDuration, ARCHER_RESOURCES + "idle/left", ".png");

        //final float moveDuration = archer.getState(Archer.States.MOVE).getDuration();
        final float moveDuration = archer.getState(Archer.States.MOVE).getDuration();
        createAnimation(Archer.States.MOVE, 0, 2, moveDuration, ARCHER_RESOURCES + "move/up", ".png");
        createAnimation(Archer.States.MOVE, 1, 2, moveDuration, ARCHER_RESOURCES + "move/down", ".png");
        createAnimation(Archer.States.MOVE, 2, 2, moveDuration, ARCHER_RESOURCES + "move/right", ".png");
        createAnimation(Archer.States.MOVE, 3, 2, moveDuration, ARCHER_RESOURCES + "move/left", ".png");

        final float attackDuration = archer.getState(Archer.States.ATTACK).getDuration();
        createAnimation(Archer.States.ATTACK, 0, 6, attackDuration, ARCHER_RESOURCES + "attack/up", ".png");
        createAnimation(Archer.States.ATTACK, 1, 4, attackDuration, ARCHER_RESOURCES + "attack/down", ".png");
        createAnimation(Archer.States.ATTACK, 2, 8, attackDuration, ARCHER_RESOURCES + "attack/right", ".png");
        createAnimation(Archer.States.ATTACK, 3, 8, attackDuration, ARCHER_RESOURCES + "attack/left", ".png");

        final float hitDuration = archer.getState(Archer.States.HIT).getDuration();
        createAnimation(Archer.States.HIT, 0, 4, hitDuration, ARCHER_RESOURCES + "hit/up", ".png");
        createAnimation(Archer.States.HIT, 1, 4, hitDuration, ARCHER_RESOURCES + "hit/down", ".png");
        createAnimation(Archer.States.HIT, 2, 4, hitDuration, ARCHER_RESOURCES + "hit/right", ".png");
        createAnimation(Archer.States.HIT, 3, 4, hitDuration, ARCHER_RESOURCES + "hit/left", ".png");

        final float deadDuration = archer.getState(Archer.States.DEAD).getDuration();
        createAnimation(Archer.States.DEAD, 0, 5, deadDuration, ARCHER_RESOURCES + "dead/left", ".png");
        createAnimation(Archer.States.DEAD, 1, 5, deadDuration, ARCHER_RESOURCES + "dead/right", ".png");

        final float sludgeDuration = archer.getState(Archer.States.SLUDGE).getDuration();
        createAnimation(Archer.States.SLUDGE, 0, 1, sludgeDuration, ARCHER_RESOURCES + "glide/right", ".png");
        createAnimation(Archer.States.SLUDGE, 1, 1, sludgeDuration, ARCHER_RESOURCES + "glide/left", ".png");

        float fallDuration = archer.getState(Archer.States.FALL).getDuration();
        createAnimation(Archer.States.FALL, 0, 2, fallDuration, ARCHER_RESOURCES + "dead/right", ".png");
        createAnimation(Archer.States.FALL, 1, 2, fallDuration, ARCHER_RESOURCES + "dead/left", ".png");

        float glideDuration = archer.getState(Archer.States.GLIDE).getDuration();
        createAnimation(Archer.States.GLIDE, 0, 3, glideDuration, ARCHER_RESOURCES + "glide/right", ".png");
        createAnimation(Archer.States.GLIDE, 1, 3, glideDuration, ARCHER_RESOURCES + "glide/left", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonStates currentState = archer.getCurrentState();
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

    private int getAnimationType(CommonStates currentState) {
        DirectionsModel currentDirection = archer.getDirection();
        return switch (currentState) {
            case Archer.States.ATTACK, Archer.States.IDLE, Archer.States.MOVE, Archer.States.HIT ->
                    switch (currentDirection) {
                        case UP -> 0;
                        case DOWN -> 1;
                        case RIGHT -> 2;
                        case LEFT -> 3;
                    };
            case Archer.States.GLIDE, Archer.States.SLUDGE, Archer.States.DEAD, Archer.States.FALL -> {
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
            currentState = archer.getState(archer.getCurrentState());
            firstTimeInState = true;
        } else if (firstTimeInState) {
            float duration = currentState.getDuration();
            moveInterpolationX.changeStart(currentPosition.first);
            moveInterpolationX.changeEnd(archer.getCol());
            moveInterpolationX.changeDuration(duration);
            moveInterpolationX.restart();
            moveInterpolationY.changeStart(currentPosition.second);
            moveInterpolationY.changeEnd(archer.getRow());
            moveInterpolationY.changeDuration(duration);
            moveInterpolationY.restart();
            firstTimeInState = false;
        }

        currentPosition.changeFirst(moveInterpolationX.getValue());
        currentPosition.changeSecond(moveInterpolationY.getValue());
        return currentPosition;
    }
}