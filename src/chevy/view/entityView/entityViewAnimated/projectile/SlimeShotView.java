package chevy.view.entityView.entityViewAnimated.projectile;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Pair;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolate;
import chevy.view.animation.InterpolationTypes;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.image.BufferedImage;

public class SlimeShotView extends EntityViewAnimated {
    private static final String SLIME_SHOT_RESOURCES = "/assets/projectile/slimeShot/";
    private final Projectile slimeShot;
    private final Vector2<Double> currentPosition;
    private final Interpolate moveInterpolationX;
    private final Interpolate moveInterpolationY;
    private State currentState;
    private boolean firstTimeInState = false;


    public SlimeShotView(Projectile slimeShot) {
        super();
        this.slimeShot = slimeShot;
        this.currentPosition = new Vector2<>(
                (double) slimeShot.getCol(),
                (double) slimeShot.getRow()
        );
        currentState = slimeShot.getState(slimeShot.getCurrentEumState());
        moveInterpolationX = new Interpolate(currentPosition.first,
                slimeShot.getCol(),
                slimeShot.getState(slimeShot.getCurrentEumState()).getDuration(),
                InterpolationTypes.LINEAR
        );
        moveInterpolationY = new Interpolate(currentPosition.second,
                slimeShot.getRow(),
                slimeShot.getState(slimeShot.getCurrentEumState()).getDuration(),
                InterpolationTypes.LINEAR
        );

        initAnimation();
    }


    private void initAnimation() {
        // --- START
        Vector2<Integer> startOffsetUp = new Vector2<>(1, -16);
        Vector2<Integer> startOffsetDown = new Vector2<>(1, 16);
        Vector2<Integer> startOffsetRight = new Vector2<>(18, 2);
        Vector2<Integer> startOffsetLeft = new Vector2<>(-16, 2);

        createAnimation(Projectile.EnumState.START, 0,
                4, false, 1,
                startOffsetUp, 1,
                SLIME_SHOT_RESOURCES + "start/up", ".png");

        createAnimation(Projectile.EnumState.START, 1,
                4, false, 1,
                startOffsetDown, 1,
                SLIME_SHOT_RESOURCES + "start/down", ".png");

        createAnimation(Projectile.EnumState.START, 2,
                4, false, 1,
                startOffsetRight, 1,
                SLIME_SHOT_RESOURCES + "start/right", ".png");

        createAnimation(Projectile.EnumState.START, 3,
                4, false, 1,
                startOffsetLeft, 1,
                SLIME_SHOT_RESOURCES + "start/left", ".png");

        // --- LOOP
        Vector2<Integer> loopEndOffsetUp = new Vector2<>(2, 0);
        Vector2<Integer> loopEndOffsetDown = new Vector2<>(2, 4);
        Vector2<Integer> loopEndOffsetRight = new Vector2<>(4, 2);
        Vector2<Integer> loopEndOffsetLeft = new Vector2<>(0, 2);

        createAnimation(Projectile.EnumState.LOOP, 0,
                4, true, 3,
                loopEndOffsetUp, 1,
                SLIME_SHOT_RESOURCES + "loop/up", ".png");

        createAnimation(Projectile.EnumState.LOOP, 1,
                4, true, 3,
                loopEndOffsetDown, 1,
                SLIME_SHOT_RESOURCES + "loop/down", ".png");

        createAnimation(Projectile.EnumState.LOOP, 2,
                4, true, 3,
                loopEndOffsetRight, 1,
                SLIME_SHOT_RESOURCES + "loop/right", ".png");

        createAnimation(Projectile.EnumState.LOOP, 3,
                4, true, 3,
                loopEndOffsetLeft, 1,
                SLIME_SHOT_RESOURCES + "loop/left", ".png");

        // --- END

        createAnimation(Projectile.EnumState.END, 0,
                5, false, 1,
                loopEndOffsetUp, 1,
                SLIME_SHOT_RESOURCES + "end", ".png");

        createAnimation(Projectile.EnumState.END, 1,
                5, false, 1,
                loopEndOffsetDown, 1,
                SLIME_SHOT_RESOURCES + "end", ".png");

        createAnimation(Projectile.EnumState.END, 2,
                5, false, 1,
                loopEndOffsetRight, 1,
                SLIME_SHOT_RESOURCES + "end", ".png");

        createAnimation(Projectile.EnumState.END, 3,
                5, false, 1,
                loopEndOffsetLeft, 1,
                SLIME_SHOT_RESOURCES + "end", ".png");
    }

    private void createAnimation(CommonEnumStates enumStates, int type,
                                 int nFrame, boolean loop, int times,
                                 Vector2<Integer> offset, float scale,
                                 String folderPath, String extension) {
        if (!loop)
            times = 1;
        float durationFrame = slimeShot.getState(enumStates).getDuration() / (nFrame * times);
        AnimatedSprite animatedSprite = new AnimatedSprite(
                new Pair<>(enumStates, type),
                nFrame,
                durationFrame,
                loop,
                offset,
                scale
        );
        super.initAnimation(animatedSprite, folderPath, extension);
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonEnumStates currentEnumState = slimeShot.getCurrentEumState();
        int type = getAnimationType(currentEnumState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentEnumState, type);

        if (currentAnimatedSprite != null) {
            if (currentEnumState == Projectile.EnumState.END && currentState.isFinished()) {
                slimeShot.setToDraw(false);
            }
            else if (!currentAnimatedSprite.isRunning()) {
                currentAnimatedSprite.start();
            }
            return currentAnimatedSprite.getCurrentFrame();
        }
        return null;
    }

    public Vector2<Integer> getOffset() {
        CommonEnumStates currentState = slimeShot.getCurrentEumState();
        int type = getAnimationType(currentState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, type);
        return currentAnimatedSprite.getOffset();
    }

    public float getScale() {
        CommonEnumStates currentState = slimeShot.getCurrentEumState();
        int type = getAnimationType(currentState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, type);
        return currentAnimatedSprite.getScale();
    }

    private int getAnimationType(CommonEnumStates currentState) {
        DirectionsModel currentDirection = slimeShot.getDirection();
        return switch (currentState) {
            case Projectile.EnumState.START, Projectile.EnumState.LOOP, Projectile.EnumState.END ->
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
        if (slimeShot.isCollide()) {
            return currentPosition;
        }

        if (!currentState.isFinished()) {
            if (firstTimeInState) {
                float duration = currentState.getDuration();
                moveInterpolationX.changeStart(currentPosition.first);
                moveInterpolationX.changeEnd(slimeShot.getCol());
                moveInterpolationX.changeDuration(duration);
                moveInterpolationX.restart();
                moveInterpolationY.changeStart(currentPosition.second);
                moveInterpolationY.changeEnd(slimeShot.getRow());
                moveInterpolationY.changeDuration(duration);
                moveInterpolationY.restart();
                firstTimeInState = false;
            }
            currentPosition.changeFirst(moveInterpolationX.getValue());
            currentPosition.changeSecond(moveInterpolationY.getValue());
        }
        else {
            currentState = slimeShot.getState(slimeShot.getCurrentEumState());
            firstTimeInState = true;
        }
        return currentPosition;
    }

    @Override
    public void wasRemoved() {
        moveInterpolationX.delete();
        moveInterpolationY.delete();
        deleteAnimations();
    }
}
