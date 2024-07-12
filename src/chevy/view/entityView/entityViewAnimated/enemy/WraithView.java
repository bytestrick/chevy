package chevy.view.entityView.entityViewAnimated.enemy;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Wraith;
import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolate;
import chevy.view.animation.InterpolationTypes;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.image.BufferedImage;

public class WraithView extends EntityViewAnimated {
    private static final String WRAITH_RESOURCES = "/assets/enemy/wraith/";
    private final Wraith wraith;
    private final Interpolate moveInterpolationX;
    private final Interpolate moveInterpolationY;
    private State currentState;
    private boolean firstTimeInState = false;


    public WraithView(Wraith wraith) {
        super();
        this.wraith = wraith;
        this.currentViewPosition = new Vector2<>(
                (double) wraith.getCol(),
                (double) wraith.getRow()
        );
        currentState = wraith.getState(wraith.getCurrentEumState());
        moveInterpolationX = new Interpolate(currentViewPosition.first,
                wraith.getCol(),
                wraith.getState(wraith.getCurrentEumState()).getDuration(),
                InterpolationTypes.EASE_OUT_SINE
        );
        moveInterpolationY = new Interpolate(currentViewPosition.second,
                wraith.getRow(),
                wraith.getState(wraith.getCurrentEumState()).getDuration(),
                InterpolationTypes.EASE_OUT_SINE
        );

        initAnimation();
    }


    private void initAnimation() {
        float idleDuration = wraith.getState(Wraith.EnumState.IDLE).getDuration();
        float moveDuration = wraith.getState(Wraith.EnumState.MOVE).getDuration();
        float attackDuration = wraith.getState(Wraith.EnumState.ATTACK).getDuration();
        float hitDuration = wraith.getState(Wraith.EnumState.HIT).getDuration();
        float deadDuration = wraith.getState(Wraith.EnumState.DEAD).getDuration();

        // --- IDLE
        createAnimation(Wraith.EnumState.IDLE, 0,
                4, true, 3, idleDuration,
                WRAITH_RESOURCES + "idle/up", ".png");

        createAnimation(Wraith.EnumState.IDLE, 1,
                4, true, 3, idleDuration,
                WRAITH_RESOURCES + "idle/down", ".png");

        createAnimation(Wraith.EnumState.IDLE, 2,
                4, true, 3, idleDuration,
                WRAITH_RESOURCES + "idle/right", ".png");

        createAnimation(Wraith.EnumState.IDLE, 3,
                4, true, 3, idleDuration,
                WRAITH_RESOURCES + "idle/left", ".png");

        // --- MOVE
        createAnimation(Wraith.EnumState.MOVE, 0,
                4, moveDuration,
                WRAITH_RESOURCES + "move/up", ".png");

        createAnimation(Wraith.EnumState.MOVE, 1,
                4, moveDuration,
                WRAITH_RESOURCES + "move/down", ".png");

        createAnimation(Wraith.EnumState.MOVE, 2,
                4, moveDuration,
                WRAITH_RESOURCES + "move/right", ".png");

        createAnimation(Wraith.EnumState.MOVE, 3,
                4, moveDuration,
                WRAITH_RESOURCES + "move/left", ".png");

        // --- ATTACK
        createAnimation(Wraith.EnumState.ATTACK, 0,
                4, attackDuration,
                WRAITH_RESOURCES + "attack/up", ".png");

        createAnimation(Wraith.EnumState.ATTACK, 1,
                4, attackDuration,
                WRAITH_RESOURCES + "attack/down", ".png");

        createAnimation(Wraith.EnumState.ATTACK, 2,
                4, attackDuration,
                WRAITH_RESOURCES + "attack/right", ".png");

        createAnimation(Wraith.EnumState.ATTACK, 3,
                4, attackDuration,
                WRAITH_RESOURCES + "attack/left", ".png");

        // --- HIT
        createAnimation(Wraith.EnumState.HIT, 0,
                1, hitDuration,
                WRAITH_RESOURCES + "hit/up", ".png");

        createAnimation(Wraith.EnumState.HIT, 1,
                1, hitDuration,
                WRAITH_RESOURCES + "hit/down", ".png");

        createAnimation(Wraith.EnumState.HIT, 2,
                1, hitDuration,
                WRAITH_RESOURCES + "hit/left", ".png");

        createAnimation(Wraith.EnumState.HIT, 3,
                1, hitDuration,
                WRAITH_RESOURCES + "hit/right", ".png");

        // --- DEAD
        createAnimation(Wraith.EnumState.DEAD, 0,
                4, deadDuration,
                WRAITH_RESOURCES + "dead/left", ".png");
        createAnimation(Wraith.EnumState.DEAD, 1,
                4, deadDuration,
                WRAITH_RESOURCES + "dead/right", ".png");
    }


    @Override
    public BufferedImage getCurrentFrame() {
        CommonEnumStates currentEnumState = wraith.getCurrentEumState();
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

    private int getAnimationType(CommonEnumStates currentState) {
        DirectionsModel currentDirection = wraith.getDirection();
        return switch (currentState) {
            case Wraith.EnumState.ATTACK, Wraith.EnumState.IDLE, Wraith.EnumState.MOVE ->
                    switch (currentDirection) {
                        case UP -> 0;
                        case DOWN -> 1;
                        case RIGHT -> 2;
                        case LEFT -> 3;
                    };
            case Wraith.EnumState.DEAD -> {
                if (currentDirection == DirectionsModel.RIGHT)
                    yield 1;
                else
                    yield 0;
            }
            default -> 0;
        };
    }

    @Override
    public Vector2<Double> getCurrentViewPosition()  {
        if (!currentState.isFinished()) {
            if (firstTimeInState) {
                float duration = currentState.getDuration();
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
        }
        else {
            currentState = wraith.getState(wraith.getCurrentEumState());
            firstTimeInState = true;
        }

        currentViewPosition.changeFirst(moveInterpolationX.getValue());
        currentViewPosition.changeSecond(moveInterpolationY.getValue());
        return currentViewPosition;
    }

    @Override
    public void wasRemoved() {
        moveInterpolationX.delete();
        moveInterpolationY.delete();
        deleteAnimations();
    }
}
