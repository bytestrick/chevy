package chevy.view.entityView.entityViewAnimated.enemy;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Wraith;
import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.model.entity.dinamicEntity.stateMachine.State;
import chevy.utilz.Pair;
import chevy.utilz.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolate;
import chevy.view.animation.InterpolationTypes;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.image.BufferedImage;

public class WraithView extends EntityViewAnimated {
    private static final String WRAITH_RESOURCES = "/assets/enemy/wraith/";
    private final Wraith wraith;
    private final Vector2<Double> currentPosition;
    private final Interpolate moveInterpolationX;
    private final Interpolate moveInterpolationY;
    private State currentState;
    private boolean firstTimeInState = false;


    public WraithView(Wraith wraith) {
        super();
        this.wraith = wraith;
        this.currentPosition = new Vector2<>(
                (double) wraith.getCol(),
                (double) wraith.getRow()
        );
        currentState = wraith.getState(wraith.getCurrentEumState());
        moveInterpolationX = new Interpolate(currentPosition.first,
                wraith.getCol(),
                wraith.getState(wraith.getCurrentEumState()).getDuration(),
                InterpolationTypes.EASE_OUT_SINE
        );
        moveInterpolationY = new Interpolate(currentPosition.second,
                wraith.getRow(),
                wraith.getState(wraith.getCurrentEumState()).getDuration(),
                InterpolationTypes.EASE_OUT_SINE
        );

        initAnimation();
    }


    private void initAnimation() {
        // --- IDLE

        createAnimation(Wraith.EnumState.IDLE, 0,
                4, true, 3,
                WRAITH_RESOURCES + "idle/up", ".png");

        createAnimation(Wraith.EnumState.IDLE, 1,
                4, true, 3,
                WRAITH_RESOURCES + "idle/down", ".png");

        createAnimation(Wraith.EnumState.IDLE, 2,
                4, true, 3,
                WRAITH_RESOURCES + "idle/right", ".png");

        createAnimation(Wraith.EnumState.IDLE, 3,
                4, true, 3,
                WRAITH_RESOURCES + "idle/left", ".png");

        // --- MOVE

        createAnimation(Wraith.EnumState.MOVE, 0,
                4, false, 1,
                WRAITH_RESOURCES + "move/up", ".png");

        createAnimation(Wraith.EnumState.MOVE, 1,
                4, false, 1,
                WRAITH_RESOURCES + "move/down", ".png");

        createAnimation(Wraith.EnumState.MOVE, 2,
                4, false, 1,
                WRAITH_RESOURCES + "move/right", ".png");

        createAnimation(Wraith.EnumState.MOVE, 3,
                4, false, 1,
                WRAITH_RESOURCES + "move/left", ".png");

        // --- ATTACK

        createAnimation(Wraith.EnumState.ATTACK, 0,
                4, false, 1,
                WRAITH_RESOURCES + "attack/up", ".png");

        createAnimation(Wraith.EnumState.ATTACK, 1,
                4, false, 1,
                WRAITH_RESOURCES + "attack/down", ".png");

        createAnimation(Wraith.EnumState.ATTACK, 2,
                4, false, 1,
                WRAITH_RESOURCES + "attack/right", ".png");

        createAnimation(Wraith.EnumState.ATTACK, 3,
                4, false, 1,
                WRAITH_RESOURCES + "attack/left", ".png");

        // --- DEAD

        createAnimation(Wraith.EnumState.DEAD, 0,
                4, false, 1,
                WRAITH_RESOURCES + "dead/left", ".png");
        createAnimation(Wraith.EnumState.DEAD, 1,
                4, false, 1,
                WRAITH_RESOURCES + "dead/right", ".png");
    }

    private void createAnimation(CommonEnumStates enumStates, int type,
                                 int nFrame, boolean loop, int times,
                                 String folderPath, String extension) {
        if (!loop)
            times = 1;
        float durationFrame = wraith.getState(enumStates).getDuration() / (nFrame * times);
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
        CommonEnumStates currentState = wraith.getCurrentEumState();
        int type = getAnimationType(currentState);

        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, type);

        if (currentAnimatedSprite != null) {
            if (!currentAnimatedSprite.isRunning()) {
                currentAnimatedSprite.start();
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
    public Vector2<Double> getCurrentPosition()  {
        if (!currentState.isFinished()) {
            if (firstTimeInState) {
                float duration = currentState.getDuration();
                moveInterpolationX.changeStart(currentPosition.first);
                moveInterpolationX.changeEnd(wraith.getCol());
                moveInterpolationX.changeDuration(duration);
                moveInterpolationX.restart();
                moveInterpolationY.changeStart(currentPosition.second);
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
