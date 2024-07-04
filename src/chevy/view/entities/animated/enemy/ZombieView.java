package chevy.view.entities.animated.enemy;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Zombie;
import chevy.model.entity.stateMachine.CommonStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class ZombieView extends AnimatedEntityView {
    private static final String ZOMBIE_RESOURCES = "/assets/enemy/zombie/";
    private final Zombie zombie;

    public ZombieView(Zombie zombie) {
        super();
        this.zombie = zombie;
        this.currentPosition = new Vector2<>((double) zombie.getCol(), (double) zombie.getRow());
        currentState = zombie.getState(zombie.getCurrentState());

        float duration = zombie.getState(zombie.getCurrentState()).getDuration();
        moveInterpolationX = new Interpolation(currentPosition.first, zombie.getCol(), duration,
                Interpolation.Types.EASE_OUT_SINE);
        moveInterpolationY = new Interpolation(currentPosition.second, zombie.getRow(), duration,
                Interpolation.Types.EASE_OUT_SINE);

        initAnimation();
    }

    private void initAnimation() {
        // Idle
        float idleDuration = zombie.getState(Zombie.States.IDLE).getDuration();
        createAnimation(Zombie.States.IDLE, 0, 4, true, 3, idleDuration, ZOMBIE_RESOURCES + "idle/up", ".png");
        createAnimation(Zombie.States.IDLE, 1, 4, true, 3, idleDuration, ZOMBIE_RESOURCES + "idle/down", ".png");
        createAnimation(Zombie.States.IDLE, 2, 4, true, 3, idleDuration, ZOMBIE_RESOURCES + "idle/right", ".png");
        createAnimation(Zombie.States.IDLE, 3, 4, true, 3, idleDuration, ZOMBIE_RESOURCES + "idle/left", ".png");

        // Move
        float moveDuration = zombie.getState(Zombie.States.MOVE).getDuration();
        createAnimation(Zombie.States.MOVE, 0, 4, moveDuration, ZOMBIE_RESOURCES + "move/up", ".png");
        createAnimation(Zombie.States.MOVE, 1, 4, moveDuration, ZOMBIE_RESOURCES + "move/down", ".png");
        createAnimation(Zombie.States.MOVE, 2, 4, moveDuration, ZOMBIE_RESOURCES + "move/right", ".png");
        createAnimation(Zombie.States.MOVE, 3, 4, moveDuration, ZOMBIE_RESOURCES + "move/left", ".png");

        // Attack
        float attackDuration = zombie.getState(Zombie.States.ATTACK).getDuration();
        createAnimation(Zombie.States.ATTACK, 0, 4, attackDuration, ZOMBIE_RESOURCES + "attack/up", ".png");
        createAnimation(Zombie.States.ATTACK, 1, 4, attackDuration, ZOMBIE_RESOURCES + "attack/down", ".png");
        createAnimation(Zombie.States.ATTACK, 2, 4, attackDuration, ZOMBIE_RESOURCES + "attack/right", ".png");
        createAnimation(Zombie.States.ATTACK, 3, 4, attackDuration, ZOMBIE_RESOURCES + "attack/left", ".png");

        // Hit
        float hitDuration = zombie.getState(Zombie.States.HIT).getDuration();
        createAnimation(Zombie.States.HIT, 0, 1, hitDuration, ZOMBIE_RESOURCES + "hit/up", ".png");
        createAnimation(Zombie.States.HIT, 1, 1, hitDuration, ZOMBIE_RESOURCES + "hit/down", ".png");
        createAnimation(Zombie.States.HIT, 2, 1, hitDuration, ZOMBIE_RESOURCES + "hit/left", ".png");
        createAnimation(Zombie.States.HIT, 3, 1, hitDuration, ZOMBIE_RESOURCES + "hit/right", ".png");

        // Dead
        float deadDuration = zombie.getState(Zombie.States.DEAD).getDuration();
        createAnimation(Zombie.States.DEAD, 0, 4, deadDuration, ZOMBIE_RESOURCES + "dead/left", ".png");
        createAnimation(Zombie.States.DEAD, 1, 4, deadDuration, ZOMBIE_RESOURCES + "dead/right", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonStates currentState = zombie.getCurrentState();
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

    private int getAnimationType(CommonStates currentState) {
        DirectionsModel currentDirection = zombie.getDirection();
        return switch (currentState) {
            case Zombie.States.ATTACK, Zombie.States.IDLE, Zombie.States.MOVE -> switch (currentDirection) {
                case UP -> 0;
                case DOWN -> 1;
                case RIGHT -> 2;
                case LEFT -> 3;
            };
            case Zombie.States.DEAD -> {
                if (currentDirection == DirectionsModel.RIGHT) yield 1;
                else yield 0;
            }
            default -> 0;
        };
    }

    @Override
    public Vector2<Double> getCurrentPosition() {
        if (currentState.isFinished()) {
            currentState = zombie.getState(zombie.getCurrentState());
            firstTimeInState = true;
        } else if (firstTimeInState) {
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

        currentPosition.changeFirst(moveInterpolationX.getValue());
        currentPosition.changeSecond(moveInterpolationY.getValue());
        return currentPosition;
    }
}