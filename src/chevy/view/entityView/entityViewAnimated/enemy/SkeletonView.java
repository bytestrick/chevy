package chevy.view.entityView.entityViewAnimated.enemy;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Skeleton;
import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.model.entity.dinamicEntity.stateMachine.State;
import chevy.utils.Pair;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolate;
import chevy.view.animation.InterpolationTypes;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.image.BufferedImage;

public class SkeletonView extends EntityViewAnimated {
    private static final String SKELETON_RESOURCES = "/assets/enemy/skeleton/";
    private final Skeleton skeleton;
    private final Vector2<Double> currentPosition;
    private final Interpolate moveInterpolationX;
    private final Interpolate moveInterpolationY;
    private State currentState;
    private boolean firstTimeInState = false;


    public SkeletonView(Skeleton skeleton) {
        super();
        this.skeleton = skeleton;
        this.currentPosition = new Vector2<>(
                (double) skeleton.getCol(),
                (double) skeleton.getRow()
        );
        currentState = skeleton.getState(skeleton.getCurrentEumState());
        moveInterpolationX = new Interpolate(currentPosition.first,
                skeleton.getCol(),
                skeleton.getState(skeleton.getCurrentEumState()).getDuration(),
                InterpolationTypes.EASE_OUT_SINE
        );
        moveInterpolationY = new Interpolate(currentPosition.second,
                skeleton.getRow(),
                skeleton.getState(skeleton.getCurrentEumState()).getDuration(),
                InterpolationTypes.EASE_OUT_SINE
        );

        initAnimation();
    }


    private void initAnimation() {
        // --- IDLE

        createAnimation(Skeleton.EnumState.IDLE, 0,
                4, true, 6,
                SKELETON_RESOURCES + "idle/up", ".png");

        createAnimation(Skeleton.EnumState.IDLE, 1,
                4, true, 6,
                SKELETON_RESOURCES + "idle/down", ".png");

        createAnimation(Skeleton.EnumState.IDLE, 2,
                4, true, 6,
                SKELETON_RESOURCES + "idle/right", ".png");

        createAnimation(Skeleton.EnumState.IDLE, 3,
                4, true, 6,
                SKELETON_RESOURCES + "idle/left", ".png");

        // --- MOVE

        createAnimation(Skeleton.EnumState.MOVE, 0,
                4, false, 1,
                SKELETON_RESOURCES + "move/up", ".png");

        createAnimation(Skeleton.EnumState.MOVE, 1,
                4, false, 1,
                SKELETON_RESOURCES + "move/down", ".png");

        createAnimation(Skeleton.EnumState.MOVE, 2,
                4, false, 1,
                SKELETON_RESOURCES + "move/right", ".png");

        createAnimation(Skeleton.EnumState.MOVE, 3,
                4, false, 1,
                SKELETON_RESOURCES + "move/left", ".png");

        // --- ATTACK

        createAnimation(Skeleton.EnumState.ATTACK, 0,
                4, false, 1,
                SKELETON_RESOURCES + "attack/up", ".png");

        createAnimation(Skeleton.EnumState.ATTACK, 1,
                4, false, 1,
                SKELETON_RESOURCES + "attack/down", ".png");

        createAnimation(Skeleton.EnumState.ATTACK, 2,
                4, false, 1,
                SKELETON_RESOURCES + "attack/right", ".png");

        createAnimation(Skeleton.EnumState.ATTACK, 3,
                4, false, 1,
                SKELETON_RESOURCES + "attack/left", ".png");

        // --- HIT

        createAnimation(Skeleton.EnumState.HIT, 0,
                1, false, 1,
                SKELETON_RESOURCES + "dead/up", ".png");

        createAnimation(Skeleton.EnumState.HIT, 1,
                1, false, 1,
                SKELETON_RESOURCES + "dead/down", ".png");

        createAnimation(Skeleton.EnumState.HIT, 2,
                1, false, 1,
                SKELETON_RESOURCES + "dead/right", ".png");

        createAnimation(Skeleton.EnumState.HIT, 3,
                1, false, 1,
                SKELETON_RESOURCES + "dead/left", ".png");

        // --- DEAD

        createAnimation(Skeleton.EnumState.DEAD, 0,
                4, false, 1,
                SKELETON_RESOURCES + "dead/left", ".png");
        createAnimation(Skeleton.EnumState.DEAD, 1,
                4, false, 1,
                SKELETON_RESOURCES + "dead/right", ".png");
    }

    private void createAnimation(CommonEnumStates enumStates, int type,
                                 int nFrame, boolean loop, int times,
                                 String folderPath, String extension) {
        if (!loop)
            times = 1;
        float durationFrame = skeleton.getState(enumStates).getDuration() / (nFrame * times);
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
        CommonEnumStates currentState = skeleton.getCurrentEumState();
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
        DirectionsModel currentDirection = skeleton.getDirection();
        return switch (currentState) {
            case Skeleton.EnumState.ATTACK, Skeleton.EnumState.IDLE, Skeleton.EnumState.MOVE ->
                    switch (currentDirection) {
                        case UP -> 0;
                        case DOWN -> 1;
                        case RIGHT -> 2;
                        case LEFT -> 3;
                    };
            case Skeleton.EnumState.DEAD -> {
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
                moveInterpolationX.changeEnd(skeleton.getCol());
                moveInterpolationX.changeDuration(duration);
                moveInterpolationX.restart();
                moveInterpolationY.changeStart(currentPosition.second);
                moveInterpolationY.changeEnd(skeleton.getRow());
                moveInterpolationY.changeDuration(duration);
                moveInterpolationY.restart();
                firstTimeInState = false;
            }
        }
        else {
            currentState = skeleton.getState(skeleton.getCurrentEumState());
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
