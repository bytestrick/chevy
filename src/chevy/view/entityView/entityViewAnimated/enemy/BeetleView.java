package chevy.view.entityView.entityViewAnimated.enemy;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Beetle;
import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.stateMachine.State;
import chevy.model.entity.staticEntity.environment.traps.SpikedFloor;
import chevy.utils.Pair;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolate;
import chevy.view.animation.InterpolationTypes;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.image.BufferedImage;

public class BeetleView extends EntityViewAnimated {
    private static final String BEETLE_RESOURCES = "/assets/enemy/beetle/";
    private final Beetle beetle;
    private final Vector2<Double> currentPosition;
    private final Interpolate moveInterpolationX;
    private final Interpolate moveInterpolationY;
    private State currentState;
    private boolean firstTimeInState = false;


    public BeetleView(Beetle beetle) {
        super();
        this.beetle = beetle;
        this.currentPosition = new Vector2<>(
                (double) beetle.getCol(),
                (double) beetle.getRow()
        );
        currentState = beetle.getState(beetle.getCurrentEumState());
        moveInterpolationX = new Interpolate(currentPosition.first,
                beetle.getCol(),
                currentState.getDuration(),
                InterpolationTypes.EASE_OUT_SINE
        );
        moveInterpolationY = new Interpolate(currentPosition.second,
                beetle.getRow(),
                currentState.getDuration(),
                InterpolationTypes.EASE_OUT_SINE
        );

        initAnimation();
    }


    private void initAnimation() {
        float idleDuration = beetle.getState(Beetle.EnumState.IDLE).getDuration();
        float moveDuration = beetle.getState(Beetle.EnumState.MOVE).getDuration();
        float hitDuration = beetle.getState(Beetle.EnumState.HIT).getDuration();
        float attackDuration = beetle.getState(Beetle.EnumState.ATTACK).getDuration();
        float deadDuration = beetle.getState(Beetle.EnumState.DEAD).getDuration();
        // --- IDLE

        createAnimation(Beetle.EnumState.IDLE, 0,
                4, idleDuration,
                BEETLE_RESOURCES + "idle/up", ".png");

        createAnimation(Beetle.EnumState.IDLE, 1,
                4,  idleDuration,
                BEETLE_RESOURCES + "idle/down", ".png");

        createAnimation(Beetle.EnumState.IDLE, 2,
                4,  idleDuration,
                BEETLE_RESOURCES + "idle/right", ".png");

        createAnimation(Beetle.EnumState.IDLE, 3,
                4,  idleDuration,
                BEETLE_RESOURCES + "idle/left", ".png");

        // --- MOVE

        createAnimation(Beetle.EnumState.MOVE, 0,
                4,  moveDuration,
                BEETLE_RESOURCES + "move/up", ".png");

        createAnimation(Beetle.EnumState.MOVE, 1,
                4,  moveDuration,
                BEETLE_RESOURCES + "move/down", ".png");

        createAnimation(Beetle.EnumState.MOVE, 2,
                4,  moveDuration,
                BEETLE_RESOURCES + "move/right", ".png");

        createAnimation(Beetle.EnumState.MOVE, 3,
                4,  moveDuration,
                BEETLE_RESOURCES + "move/left", ".png");

        // --- ATTACK
        Vector2<Integer> offsetAttack = new Vector2<>(-2, -4);
        float scaleAttack = 1;
        createAnimation(Beetle.EnumState.ATTACK, 0,
                4,  attackDuration,
                offsetAttack, scaleAttack,
                BEETLE_RESOURCES + "attack/up", ".png");

        createAnimation(Beetle.EnumState.ATTACK, 1,
                4,  attackDuration,
                offsetAttack, scaleAttack,
                BEETLE_RESOURCES + "attack/down", ".png");

        createAnimation(Beetle.EnumState.ATTACK, 2,
                4,  attackDuration,
                offsetAttack, scaleAttack,
                BEETLE_RESOURCES + "attack/right", ".png");

        createAnimation(Beetle.EnumState.ATTACK, 3,
                4,  attackDuration,
                offsetAttack, scaleAttack,
                BEETLE_RESOURCES + "attack/left", ".png");

        // --- HIT

        createAnimation(Beetle.EnumState.HIT, 0,
                1,  hitDuration,
                BEETLE_RESOURCES + "hit/up", ".png");

        createAnimation(Beetle.EnumState.HIT, 1,
                1,  hitDuration,
                BEETLE_RESOURCES + "hit/down", ".png");

        createAnimation(Beetle.EnumState.HIT, 2,
                1,  hitDuration,
                BEETLE_RESOURCES + "hit/right", ".png");

        createAnimation(Beetle.EnumState.HIT, 3,
                1,  hitDuration,
                BEETLE_RESOURCES + "hit/left", ".png");

        // --- DEAD

        createAnimation(Beetle.EnumState.DEAD, 0,
                4,  deadDuration,
                BEETLE_RESOURCES + "dead/left", ".png");
        createAnimation(Beetle.EnumState.DEAD, 1,
                4,  deadDuration,
                BEETLE_RESOURCES + "dead/right", ".png");
    }

    public Vector2<Integer> getOffset() {
        CommonEnumStates currentState = beetle.getCurrentEumState();
        int type = getAnimationType(currentState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, type);
        return currentAnimatedSprite.getOffset();
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonEnumStates currentStateState = beetle.getCurrentEumState();
        int type = getAnimationType(currentStateState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentStateState, type);

        if (currentAnimatedSprite != null) {
            if (!currentAnimatedSprite.isRunning()) {
                currentAnimatedSprite.restart();
            }
            return currentAnimatedSprite.getCurrentFrame();
        }
        return null;
    }

    private int getAnimationType(CommonEnumStates currentState) {
        DirectionsModel currentDirection = beetle.getDirection();
        return switch (currentState) {
            case Beetle.EnumState.ATTACK, Beetle.EnumState.IDLE, Beetle.EnumState.MOVE, Beetle.EnumState.HIT ->
                    switch (currentDirection) {
                        case UP -> 0;
                        case DOWN -> 1;
                        case RIGHT -> 2;
                        case LEFT -> 3;
                    };
            case Beetle.EnumState.DEAD -> {
                if (currentDirection == DirectionsModel.RIGHT)
                    yield 1;
                else
                    yield 0;
            }
            default -> 0;
        };
    }

    @Override
    public Vector2<Double> getCurrentPosition()  {
        if (!currentState.isFinished()) {
            if (firstTimeInState) {
                float duration = currentState.getDuration();
                moveInterpolationX.changeStart(currentPosition.first);
                moveInterpolationX.changeEnd(beetle.getCol());
                moveInterpolationX.changeDuration(duration);
                moveInterpolationX.restart();
                moveInterpolationY.changeStart(currentPosition.second);
                moveInterpolationY.changeEnd(beetle.getRow());
                moveInterpolationY.changeDuration(duration);
                moveInterpolationY.restart();
                firstTimeInState = false;
            }
        }
        else {
            currentState = beetle.getState(beetle.getCurrentEumState());
            firstTimeInState = true;
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
