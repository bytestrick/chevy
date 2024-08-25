package chevy.view.entities.animated.enemy;

import chevy.model.entity.dynamicEntity.DirectionsModel;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Zombie;
import chevy.model.entity.stateMachine.CommonState;
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
        this.currentViewPosition = new Vector2<>((double) zombie.getCol(), (double) zombie.getRow());
        currentGlobalState = zombie.getState(zombie.getCurrentState());

        float duration = zombie.getState(zombie.getCurrentState()).getDuration();
        moveInterpolationX = new Interpolation(currentViewPosition.first, zombie.getCol(), duration,
                Interpolation.Type.EASE_OUT_SINE);
        moveInterpolationY = new Interpolation(currentViewPosition.second, zombie.getRow(), duration,
                Interpolation.Type.EASE_OUT_SINE);

        initAnimation();
    }

    private void initAnimation() {
        // Idle
        float idleDuration = zombie.getState(Zombie.State.IDLE).getDuration();
        createAnimation(Zombie.State.IDLE, 0, 4, true, 3, idleDuration, ZOMBIE_RESOURCES + "idle/up", ".png");
        createAnimation(Zombie.State.IDLE, 1, 4, true, 3, idleDuration, ZOMBIE_RESOURCES + "idle/down", ".png");
        createAnimation(Zombie.State.IDLE, 2, 4, true, 3, idleDuration, ZOMBIE_RESOURCES + "idle/right", ".png");
        createAnimation(Zombie.State.IDLE, 3, 4, true, 3, idleDuration, ZOMBIE_RESOURCES + "idle/left", ".png");

        // Move
        float moveDuration = zombie.getState(Zombie.State.MOVE).getDuration();
        createAnimation(Zombie.State.MOVE, 0, 4, moveDuration, ZOMBIE_RESOURCES + "move/up", ".png");
        createAnimation(Zombie.State.MOVE, 1, 4, moveDuration, ZOMBIE_RESOURCES + "move/down", ".png");
        createAnimation(Zombie.State.MOVE, 2, 4, moveDuration, ZOMBIE_RESOURCES + "move/right", ".png");
        createAnimation(Zombie.State.MOVE, 3, 4, moveDuration, ZOMBIE_RESOURCES + "move/left", ".png");

        // Attack
        float attackDuration = zombie.getState(Zombie.State.ATTACK).getDuration();
        createAnimation(Zombie.State.ATTACK, 0, 4, attackDuration, ZOMBIE_RESOURCES + "attack/up", ".png");
        createAnimation(Zombie.State.ATTACK, 1, 4, attackDuration, ZOMBIE_RESOURCES + "attack/down", ".png");
        createAnimation(Zombie.State.ATTACK, 2, 4, attackDuration, ZOMBIE_RESOURCES + "attack/right", ".png");
        createAnimation(Zombie.State.ATTACK, 3, 4, attackDuration, ZOMBIE_RESOURCES + "attack/left", ".png");

        // Hit
        float hitDuration = zombie.getState(Zombie.State.HIT).getDuration();
        createAnimation(Zombie.State.HIT, 0, 1, hitDuration, ZOMBIE_RESOURCES + "hit/up", ".png");
        createAnimation(Zombie.State.HIT, 1, 1, hitDuration, ZOMBIE_RESOURCES + "hit/down", ".png");
        createAnimation(Zombie.State.HIT, 2, 1, hitDuration, ZOMBIE_RESOURCES + "hit/left", ".png");
        createAnimation(Zombie.State.HIT, 3, 1, hitDuration, ZOMBIE_RESOURCES + "hit/right", ".png");

        // Dead
        float deadDuration = zombie.getState(Zombie.State.DEAD).getDuration();
        createAnimation(Zombie.State.DEAD, 0, 4, deadDuration, ZOMBIE_RESOURCES + "dead/left", ".png");
        createAnimation(Zombie.State.DEAD, 1, 4, deadDuration, ZOMBIE_RESOURCES + "dead/right", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonState currentState = zombie.getCurrentState();
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

    private int getAnimationType(CommonState currentState) {
        DirectionsModel currentDirection = zombie.getDirection();
        return switch (currentState) {
            case Zombie.State.ATTACK, Zombie.State.IDLE, Zombie.State.MOVE -> switch (currentDirection) {
                case UP -> 0;
                case DOWN -> 1;
                case RIGHT -> 2;
                case LEFT -> 3;
            };
            case Zombie.State.DEAD -> {
                if (currentDirection == DirectionsModel.RIGHT) {
                    yield 1;
                } else {
                    yield 0;
                }
            }
            default -> 0;
        };
    }

    @Override
    public Vector2<Double> getCurrentViewPosition() {
        if (currentGlobalState.isFinished()) {
            currentGlobalState = zombie.getState(zombie.getCurrentState());
            firstTimeInState = true;
        } else if (firstTimeInState) {
            float duration = currentGlobalState.getDuration();
            moveInterpolationX.changeStart(currentViewPosition.first);
            moveInterpolationX.changeEnd(zombie.getCol());
            moveInterpolationX.changeDuration(duration);
            moveInterpolationX.restart();
            moveInterpolationY.changeStart(currentViewPosition.second);
            moveInterpolationY.changeEnd(zombie.getRow());
            moveInterpolationY.changeDuration(duration);
            moveInterpolationY.restart();
            firstTimeInState = false;
        }

        currentViewPosition.changeFirst(moveInterpolationX.getValue());
        currentViewPosition.changeSecond(moveInterpolationY.getValue());
        return currentViewPosition;
    }
}
