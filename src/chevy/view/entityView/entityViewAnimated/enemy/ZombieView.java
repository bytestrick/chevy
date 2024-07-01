package chevy.view.entityView.entityViewAnimated.enemy;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Zombie;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Zombie;
import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Pair;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolate;
import chevy.view.animation.InterpolationTypes;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.image.BufferedImage;

public class ZombieView extends EntityViewAnimated {
    private static final String ZOMBIE_RESOURCES = "/assets/enemy/zombie/";
    private final Zombie zombie;
    private final Vector2<Double> currentPosition;
    private final Interpolate moveInterpolationX;
    private final Interpolate moveInterpolationY;
    private State currentState;
    private boolean firstTimeInState = false;


    public ZombieView(Zombie zombie) {
        super();
        this.zombie = zombie;
        this.currentPosition = new Vector2<>(
                (double) zombie.getCol(),
                (double) zombie.getRow()
        );
        currentState = zombie.getState(zombie.getCurrentEumState());
        moveInterpolationX = new Interpolate(currentPosition.first,
                zombie.getCol(),
                zombie.getState(zombie.getCurrentEumState()).getDuration(),
                InterpolationTypes.EASE_OUT_SINE
        );
        moveInterpolationY = new Interpolate(currentPosition.second,
                zombie.getRow(),
                zombie.getState(zombie.getCurrentEumState()).getDuration(),
                InterpolationTypes.EASE_OUT_SINE
        );

        initAnimation();
    }


    private void initAnimation() {
        float idleDuration = zombie.getState(Zombie.EnumState.IDLE).getDuration();
        float moveDuration = zombie.getState(Zombie.EnumState.MOVE).getDuration();
        float attackDuration = zombie.getState(Zombie.EnumState.ATTACK).getDuration();
        float hitDuration = zombie.getState(Zombie.EnumState.HIT).getDuration();
        float deadDuration = zombie.getState(Zombie.EnumState.DEAD).getDuration();
        // --- IDLE

        createAnimation(Zombie.EnumState.IDLE, 0,
                4, true, 3, idleDuration,
                ZOMBIE_RESOURCES + "idle/up", ".png");

        createAnimation(Zombie.EnumState.IDLE, 1,
                4, true, 3, idleDuration,
                ZOMBIE_RESOURCES + "idle/down", ".png");

        createAnimation(Zombie.EnumState.IDLE, 2,
                4, true, 3, idleDuration,
                ZOMBIE_RESOURCES + "idle/right", ".png");

        createAnimation(Zombie.EnumState.IDLE, 3,
                4, true, 3, idleDuration,
                ZOMBIE_RESOURCES + "idle/left", ".png");

        // --- MOVE

        createAnimation(Zombie.EnumState.MOVE, 0,
                4, moveDuration,
                ZOMBIE_RESOURCES + "move/up", ".png");

        createAnimation(Zombie.EnumState.MOVE, 1,
                4, moveDuration,
                ZOMBIE_RESOURCES + "move/down", ".png");

        createAnimation(Zombie.EnumState.MOVE, 2,
                4, moveDuration,
                ZOMBIE_RESOURCES + "move/right", ".png");

        createAnimation(Zombie.EnumState.MOVE, 3,
                4, moveDuration,
                ZOMBIE_RESOURCES + "move/left", ".png");

        // --- ATTACK

        createAnimation(Zombie.EnumState.ATTACK, 0,
                4, attackDuration,
                ZOMBIE_RESOURCES + "attack/up", ".png");

        createAnimation(Zombie.EnumState.ATTACK, 1,
                4, attackDuration,
                ZOMBIE_RESOURCES + "attack/down", ".png");

        createAnimation(Zombie.EnumState.ATTACK, 2,
                4, attackDuration,
                ZOMBIE_RESOURCES + "attack/right", ".png");

        createAnimation(Zombie.EnumState.ATTACK, 3,
                4, attackDuration,
                ZOMBIE_RESOURCES + "attack/left", ".png");

        // --- HIT

        createAnimation(Zombie.EnumState.HIT, 0,
                1, hitDuration,
                ZOMBIE_RESOURCES + "hit/up", ".png");

        createAnimation(Zombie.EnumState.HIT, 1,
                1, hitDuration,
                ZOMBIE_RESOURCES + "hit/down", ".png");

        createAnimation(Zombie.EnumState.HIT, 2,
                1, hitDuration,
                ZOMBIE_RESOURCES + "hit/left", ".png");

        createAnimation(Zombie.EnumState.HIT, 3,
                1, hitDuration,
                ZOMBIE_RESOURCES + "hit/right", ".png");

        // --- DEAD

        createAnimation(Zombie.EnumState.DEAD, 0,
                4, deadDuration,
                ZOMBIE_RESOURCES + "dead/left", ".png");
        createAnimation(Zombie.EnumState.DEAD, 1,
                4, deadDuration,
                ZOMBIE_RESOURCES + "dead/right", ".png");
    }


    @Override
    public BufferedImage getCurrentFrame() {
        CommonEnumStates currentState = zombie.getCurrentEumState();
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
        DirectionsModel currentDirection = zombie.getDirection();
        return switch (currentState) {
            case Zombie.EnumState.ATTACK, Zombie.EnumState.IDLE, Zombie.EnumState.MOVE ->
                    switch (currentDirection) {
                        case UP -> 0;
                        case DOWN -> 1;
                        case RIGHT -> 2;
                        case LEFT -> 3;
                    };
            case Zombie.EnumState.DEAD -> {
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
                moveInterpolationX.changeEnd(zombie.getCol());
                moveInterpolationX.changeDuration(duration);
                moveInterpolationX.restart();
                moveInterpolationY.changeStart(currentPosition.second);
                moveInterpolationY.changeEnd(zombie.getRow());
                moveInterpolationY.changeDuration(duration);
                moveInterpolationY.restart();
                firstTimeInState = false;
            }
        }
        else {
            currentState = zombie.getState(zombie.getCurrentEumState());
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
