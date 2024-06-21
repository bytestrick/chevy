package chevy.view.entityView.entityViewAnimated.enemy;

import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.settings.GameSettings;
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
    private Interpolate moveInterpolationX = null;
    private Interpolate moveInterpolationY = null;
    private CommonEnumStates previousState = null;


    public SlimeView(Slime slime) {
        super();
        this.slime = slime;
        this.currentPosition = new Vector2<>(
                (double) slime.getCol(),
                (double) slime.getRow()
        );

        initAnimation();
    }


    private void initAnimation() {
        int nFrame = 6;
        int times = 4; // ripete l'animazione times volte
        float durationFrame = slime.getState(Slime.EnumState.IDLE).getDuration() / (nFrame * times);
        AnimatedSprite idle = new AnimatedSprite(
                Slime.EnumState.IDLE,
                nFrame,
                durationFrame,
                true
        );
        initAnimation(idle, SLIME_RESOURCES + "idle", ".png");

        // -----
        nFrame = 6;
        times = 1;
        durationFrame = slime.getState(Slime.EnumState.MOVE).getDuration() / (nFrame * times);
        AnimatedSprite move = new AnimatedSprite(
                Slime.EnumState.MOVE,
                nFrame,
                durationFrame
        );
        initAnimation(move, SLIME_RESOURCES + "move", ".png");

        // -----
        nFrame = 5;
        times = 1;
        durationFrame = slime.getState(Slime.EnumState.ATTACK).getDuration() / (nFrame * times);
        AnimatedSprite attackUp = new AnimatedSprite(
                Slime.EnumState.ATTACK,
                nFrame,
                durationFrame
        );
        initAnimation(attackUp, SLIME_RESOURCES + "attack/up", ".png");
    }


    @Override
    public BufferedImage getCurrentFrame() {
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(slime.getCurrentEumState());
        if (currentAnimatedSprite != null) {
            if (!currentAnimatedSprite.isRunning())
                currentAnimatedSprite.start();
            return currentAnimatedSprite.getCurrentFrame();
        }
        return null;
    }

    @Override
    public Vector2<Double> getCurrentPosition() {
        moveInterpolationX = createInterpolation(moveInterpolationX,
                currentPosition.first,
                slime.getCol(),
                slime.getState(Slime.EnumState.MOVE).getDuration(),
                InterpolationTypes.EASE_OUT_SINE
        );
        moveInterpolationY = createInterpolation(moveInterpolationY,
                currentPosition.second,
                slime.getRow(),
                slime.getState(Slime.EnumState.MOVE).getDuration(),
                InterpolationTypes.EASE_OUT_SINE
        );

        if (moveInterpolationX != null) {
            currentPosition.changeFirst(moveInterpolationX.getValue());
            if (!moveInterpolationX.isRunning())
                moveInterpolationX = null;
        }
        if (moveInterpolationY != null) {
            currentPosition.changeSecond(moveInterpolationY.getValue());
            if (!moveInterpolationY.isRunning())
                moveInterpolationY = null;
        }

        return currentPosition;
    }

    private Interpolate createInterpolation(Interpolate interpolate, double start, double end, float duration, InterpolationTypes interpolationTypes) {
        if (interpolate == null) {
            interpolate = new Interpolate(start, end, duration, interpolationTypes);
            interpolate.start();
        }
        else if (slime.getCurrentEumState() != previousState) {
            previousState = slime.getCurrentEumState();
            interpolate.stop();
            interpolate = new Interpolate(start, end, duration, interpolationTypes);
            interpolate.start();
        }
        return interpolate;
    }
}
