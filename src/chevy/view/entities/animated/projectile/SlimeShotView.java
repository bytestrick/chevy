package chevy.view.entities.animated.projectile;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.entity.stateMachine.CommonStates;
import chevy.utils.Pair;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class SlimeShotView extends AnimatedEntityView {
    private static final String SLIME_SHOT_RESOURCES = "/assets/projectile/slimeShot/";
    private final Projectile slimeShot;

    public SlimeShotView(Projectile slimeShot) {
        super();
        this.slimeShot = slimeShot;
        this.currentPosition = new Vector2<>((double) slimeShot.getCol(), (double) slimeShot.getRow());
        currentState = slimeShot.getState(slimeShot.getCurrentState());

        float duration = slimeShot.getState(slimeShot.getCurrentState()).getDuration();
        moveInterpolationX = new Interpolation(currentPosition.first, slimeShot.getCol(), duration,
                Interpolation.Types.LINEAR);
        moveInterpolationY = new Interpolation(currentPosition.second, slimeShot.getRow(), duration,
                Interpolation.Types.LINEAR);

        initAnimation();
    }

    private void initAnimation() {
        // Start
        int offset = 16;
        Vector2<Integer> offsetUp = new Vector2<>(0, -16);
        createAnimation(Projectile.States.START, 0, 4, false, 1, offsetUp, 1, SLIME_SHOT_RESOURCES + "start/up",
                ".png");
        createAnimation(Projectile.States.START, 1, 4, false, 1, new Vector2<>(0, offset), 1, SLIME_SHOT_RESOURCES +
                "start/down", ".png");
        createAnimation(Projectile.States.START, 2, 4, false, 1, new Vector2<>(offset, 0), 1, SLIME_SHOT_RESOURCES +
                "start/right", ".png");
        createAnimation(Projectile.States.START, 3, 4, false, 1, new Vector2<>(-1 * offset, 0), 1,
                SLIME_SHOT_RESOURCES + "start/left", ".png");

        // Loop
        createAnimation(Projectile.States.LOOP, 0, 4, true, 3, SLIME_SHOT_RESOURCES + "loop/up", ".png");
        createAnimation(Projectile.States.LOOP, 1, 4, true, 3, SLIME_SHOT_RESOURCES + "loop/down", ".png");
        createAnimation(Projectile.States.LOOP, 2, 4, true, 3, SLIME_SHOT_RESOURCES + "loop/right", ".png");
        createAnimation(Projectile.States.LOOP, 3, 4, true, 3, SLIME_SHOT_RESOURCES + "loop/left", ".png");

        // End
        createAnimation(Projectile.States.END, 0, 5, false, 1, SLIME_SHOT_RESOURCES + "end", ".png");
    }

    private void createAnimation(CommonStates enumStates, int type, int nFrame, boolean loop, int times,
                                 Vector2<Integer> offset, float scale, String folderPath, String extension) {
        if (!loop) times = 1;
        float durationFrame = slimeShot.getState(enumStates).getDuration() / (nFrame * times);
        AnimatedSprite animatedSprite = new AnimatedSprite(new Pair<>(enumStates, type), nFrame, durationFrame, loop,
                offset, scale);
        super.initAnimation(animatedSprite, folderPath, extension);
    }

    private void createAnimation(CommonStates enumStates, int type, int nFrame, boolean loop, int times,
                                 String folderPath, String extension) {
        if (!loop) times = 1;
        float durationFrame = slimeShot.getState(enumStates).getDuration() / (nFrame * times);
        AnimatedSprite animatedSprite = new AnimatedSprite(new Pair<>(enumStates, type), nFrame, durationFrame, loop);
        super.initAnimation(animatedSprite, folderPath, extension);
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonStates currentEnumState = slimeShot.getCurrentState();
        int type = getAnimationType(currentEnumState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentEnumState, type);

        if (currentAnimatedSprite != null) {
            if (currentEnumState == Projectile.States.END && currentState.isFinished()) {
                slimeShot.setToDraw(false);
//                currentAnimatedSprite.stop();
            } else if (!currentAnimatedSprite.isRunning()) {
                currentAnimatedSprite.start();
            }
            return currentAnimatedSprite.getCurrentFrame();
        }
        return null;
    }

    public Vector2<Integer> getOffset() {
        CommonStates currentState = slimeShot.getCurrentState();
        int type = getAnimationType(currentState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, type);
        return currentAnimatedSprite.getOffset();
    }

    public float getScale() {
        CommonStates currentState = slimeShot.getCurrentState();
        int type = getAnimationType(currentState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, type);
        return currentAnimatedSprite.getScale();
    }

    private int getAnimationType(CommonStates currentState) {
        DirectionsModel currentDirection = slimeShot.getDirection();
        return switch (currentState) {
            case Projectile.States.START, Projectile.States.LOOP -> switch (currentDirection) {
                case UP -> 0;
                case DOWN -> 1;
                case RIGHT -> 2;
                case LEFT -> 3;
            };
            default -> 0;
        };
    }

    @Override
    public Vector2<Double> getCurrentPosition() {
        if (slimeShot.isCollision()) {
            return currentPosition;
        }
        if (currentState.isFinished()) {
            currentState = slimeShot.getState(slimeShot.getCurrentState());
            firstTimeInState = true;
        } else {
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
        return currentPosition;
    }
}