package chevy.view.entityView.entityViewAnimated.enemy;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Beetle;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.BigSlime;
import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Pair;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolate;
import chevy.view.animation.InterpolationTypes;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.image.BufferedImage;

public class BigSlimeView extends EntityViewAnimated {
    private static final String BIG_SLIME_RESOURCES = "/assets/enemy/bigSlime/";
    private final BigSlime bigSlime;
    private final Vector2<Double> currentPosition;
    private final Interpolate moveInterpolationX;
    private final Interpolate moveInterpolationY;
    private State currentState;
    private boolean firstTimeInState = false;


    public BigSlimeView(BigSlime bigSlime) {
        super();
        this.bigSlime = bigSlime;
        this.currentPosition = new Vector2<>(
                (double) bigSlime.getCol(),
                (double) bigSlime.getRow()
        );
        currentState = bigSlime.getState(bigSlime.getCurrentEumState());
        moveInterpolationX = new Interpolate(currentPosition.first,
                bigSlime.getCol(),
                bigSlime.getState(bigSlime.getCurrentEumState()).getDuration(),
                InterpolationTypes.EASE_OUT_SINE
        );
        moveInterpolationY = new Interpolate(currentPosition.second,
                bigSlime.getRow(),
                bigSlime.getState(bigSlime.getCurrentEumState()).getDuration(),
                InterpolationTypes.EASE_OUT_SINE
        );

        initAnimation();
    }


    private void initAnimation() {
        float idleDuration = bigSlime.getState(BigSlime.EnumState.IDLE).getDuration();
        float moveDuration = bigSlime.getState(BigSlime.EnumState.MOVE).getDuration();
        float hitDuration = bigSlime.getState(BigSlime.EnumState.HIT).getDuration();
        float attackDuration = bigSlime.getState(BigSlime.EnumState.ATTACK).getDuration();
        float deadDuration = bigSlime.getState(BigSlime.EnumState.DEAD).getDuration();
        // --- IDLE

        createAnimation(BigSlime.EnumState.IDLE, 0,
                4, true, 2, idleDuration,
                BIG_SLIME_RESOURCES + "idle", ".png");


        // --- MOVE

        createAnimation(BigSlime.EnumState.MOVE, 0,
                4, moveDuration,
                BIG_SLIME_RESOURCES + "move", ".png");


        // --- ATTACK

        createAnimation(BigSlime.EnumState.ATTACK, 0,
                4, attackDuration,
                BIG_SLIME_RESOURCES + "attack/up", ".png");

        createAnimation(BigSlime.EnumState.ATTACK, 1,
                4, attackDuration,
                BIG_SLIME_RESOURCES + "attack/down", ".png");

        createAnimation(BigSlime.EnumState.ATTACK, 2,
                4, attackDuration,
                BIG_SLIME_RESOURCES + "attack/right", ".png");

        createAnimation(BigSlime.EnumState.ATTACK, 3,
                4, attackDuration,
                BIG_SLIME_RESOURCES + "attack/left", ".png");

        // --- HIT

        createAnimation(BigSlime.EnumState.HIT, 0,
                1, hitDuration,
                BIG_SLIME_RESOURCES + "hit", ".png");

        // --- DEAD

        createAnimation(BigSlime.EnumState.DEAD, 0,
                4, deadDuration,
                BIG_SLIME_RESOURCES + "dead", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonEnumStates currentState = bigSlime.getCurrentEumState();
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

    private int getAnimationType(CommonEnumStates currentState) {
        DirectionsModel currentDirection = bigSlime.getDirection();
        return switch (currentState) {
            case BigSlime.EnumState.ATTACK ->
                    switch (currentDirection) {
                        case UP -> 0;
                        case DOWN -> 1;
                        case RIGHT -> 2;
                        case LEFT -> 3;
                    };
            default -> 0;
        };
    }

    @Override
    public Vector2<Double> getCurrentPosition()  {
        if (!currentState.isFinished()) {
            if (firstTimeInState) {
                float duration = currentState.getDuration();
                moveInterpolationX.changeStart(currentPosition.first);
                moveInterpolationX.changeEnd(bigSlime.getCol());
                moveInterpolationX.changeDuration(duration);
                moveInterpolationX.restart();
                moveInterpolationY.changeStart(currentPosition.second);
                moveInterpolationY.changeEnd(bigSlime.getRow());
                moveInterpolationY.changeDuration(duration);
                moveInterpolationY.restart();
                firstTimeInState = false;
            }
        }
        else {
            currentState = bigSlime.getState(bigSlime.getCurrentEumState());
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
