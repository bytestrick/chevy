package chevy.view.entityView.entityViewAnimated.enemy;

import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.utilz.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolate;
import chevy.view.animation.InterpolationTypes;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.image.BufferedImage;

public class SlimeView extends EntityViewAnimated {
    private static final String SLIME_RESOURCES = "/assets/enemy/slime/";
    private final Slime slime;
    private final Vector2<Double> currentPosition;
    private final Interpolate moveInterpolationX;
    private final Interpolate moveInterpolationY;
    private CommonEnumStates previousEnumState = null;


    public SlimeView(Slime slime) {
        super();
        this.slime = slime;
        this.currentPosition = new Vector2<>(
                (double) slime.getCol(),
                (double) slime.getRow()
        );
        moveInterpolationX = new Interpolate(currentPosition.first,
                slime.getCol(),
                slime.getState(slime.getCurrentEumState()).getDuration(),
                InterpolationTypes.EASE_OUT_SINE
        );
        moveInterpolationY = new Interpolate(currentPosition.second,
                slime.getRow(),
                slime.getState(slime.getCurrentEumState()).getDuration(),
                InterpolationTypes.EASE_OUT_SINE
        );

        initAnimation();
    }


    private void initAnimation() {
        createAnimation(Slime.EnumState.IDLE, 6, true, 4,
                SLIME_RESOURCES + "idle", ".png");

        createAnimation(Slime.EnumState.MOVE, 6, false, 1,
                SLIME_RESOURCES + "move", ".png");

        createAnimation(Slime.EnumState.ATTACK, 5, false, 1,
                SLIME_RESOURCES + "attack/up", ".png");

        createAnimation(Slime.EnumState.HIT, 1, false, 1,
                SLIME_RESOURCES + "hit", ".png");

        createAnimation(Slime.EnumState.DEAD, 2, false, 1,
                SLIME_RESOURCES + "dead", ".png");
    }

    private void createAnimation(CommonEnumStates enumStates, int nFrame, boolean loop, int times,
                                 String folderPath, String extension) {
        if (!loop)
            times = 1;
        float durationFrame = slime.getState(enumStates).getDuration() / (nFrame * times);
        AnimatedSprite animatedSprite = new AnimatedSprite(
                enumStates,
                nFrame,
                durationFrame,
                loop
        );
        initAnimation(animatedSprite, folderPath, extension);
    }


    @Override
    public BufferedImage getCurrentFrame() {
        CommonEnumStates currentEnumState = slime.getCurrentEumState();
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentEnumState);
        if (currentAnimatedSprite != null) {
            if (!currentAnimatedSprite.isRunning()) {
                currentAnimatedSprite.start();
            }
            return currentAnimatedSprite.getCurrentFrame();
        }
        return null;
    }

    @Override
    public Vector2<Double> getCurrentPosition()  {
        CommonEnumStates currentEnumState = slime.getCurrentEumState();
        if (currentEnumState != previousEnumState) {
            if (previousEnumState == null) {
                previousEnumState = currentEnumState;
                return currentPosition;
            }

            float duration = slime.getState(slime.getCurrentEumState()).getDuration();

            moveInterpolationX.changeStart(currentPosition.first);
            moveInterpolationX.changeEnd(slime.getCol());
            moveInterpolationX.changeDuration(duration);

            moveInterpolationY.changeStart(currentPosition.second);
            moveInterpolationY.changeEnd(slime.getRow());
            moveInterpolationY.changeDuration(duration);

            if (!moveInterpolationX.isRunning()) {
                moveInterpolationX.restart();
            }
            if (!moveInterpolationY.isRunning()) {
                moveInterpolationY.restart();
            }

            previousEnumState = currentEnumState;
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
