package chevy.view.entityView.entityViewAnimated.enemy;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Zombie;
import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.model.entity.dinamicEntity.stateMachine.State;
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
        // --- IDLE

        createAnimation(Zombie.EnumState.IDLE, 0,
                4, true, 3,
                ZOMBIE_RESOURCES + "idle/up", ".png");

        createAnimation(Zombie.EnumState.IDLE, 1,
                4, true, 3,
                ZOMBIE_RESOURCES + "idle/down", ".png");

        createAnimation(Zombie.EnumState.IDLE, 2,
                4, true, 3,
                ZOMBIE_RESOURCES + "idle/right", ".png");

        createAnimation(Zombie.EnumState.IDLE, 3,
                4, true, 3,
                ZOMBIE_RESOURCES + "idle/left", ".png");

        // --- MOVE

        createAnimation(Zombie.EnumState.MOVE, 0,
                4, false, 1,
                ZOMBIE_RESOURCES + "move/up", ".png");

        createAnimation(Zombie.EnumState.MOVE, 1,
                4, false, 1,
                ZOMBIE_RESOURCES + "move/down", ".png");

        createAnimation(Zombie.EnumState.MOVE, 2,
                4, false, 1,
                ZOMBIE_RESOURCES + "move/right", ".png");

        createAnimation(Zombie.EnumState.MOVE, 3,
                4, false, 1,
                ZOMBIE_RESOURCES + "move/left", ".png");

        // --- ATTACK

        createAnimation(Zombie.EnumState.ATTACK, 0,
                4, false, 1,
                ZOMBIE_RESOURCES + "attack/up", ".png");

        createAnimation(Zombie.EnumState.ATTACK, 1,
                4, false, 1,
                ZOMBIE_RESOURCES + "attack/down", ".png");

        createAnimation(Zombie.EnumState.ATTACK, 2,
                4, false, 1,
                ZOMBIE_RESOURCES + "attack/right", ".png");

        createAnimation(Zombie.EnumState.ATTACK, 3,
                4, false, 1,
                ZOMBIE_RESOURCES + "attack/left", ".png");

        // --- HIT

        createAnimation(Zombie.EnumState.DEAD, 0,
                1, false, 1,
                ZOMBIE_RESOURCES + "dead/up", ".png");

        createAnimation(Zombie.EnumState.DEAD, 1,
                1, false, 1,
                ZOMBIE_RESOURCES + "dead/down", ".png");

        createAnimation(Zombie.EnumState.DEAD, 2,
                1, false, 1,
                ZOMBIE_RESOURCES + "dead/left", ".png");

        createAnimation(Zombie.EnumState.DEAD, 3,
                1, false, 1,
                ZOMBIE_RESOURCES + "dead/right", ".png");

        // --- DEAD

        createAnimation(Zombie.EnumState.DEAD, 0,
                4, false, 1,
                ZOMBIE_RESOURCES + "dead/left", ".png");
        createAnimation(Zombie.EnumState.DEAD, 1,
                4, false, 1,
                ZOMBIE_RESOURCES + "dead/right", ".png");
    }

    private void createAnimation(CommonEnumStates enumStates, int type,
                                 int nFrame, boolean loop, int times,
                                 String folderPath, String extension) {
        if (!loop)
            times = 1;
        float durationFrame = zombie.getState(enumStates).getDuration() / (nFrame * times);
        AnimatedSprite animatedSprite = new AnimatedSprite(
                new Pair<>(enumStates, type),
                nFrame,
                durationFrame,
                loop
        );
        super.initAnimation(animatedSprite, folderPath, extension);
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
