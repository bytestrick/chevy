package chevy.view.entityView.entityViewAnimated.enemy;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.model.entity.dinamicEntity.stateMachine.State;
import chevy.utils.Pair;
import chevy.utils.Vector2;
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
    private State currentState;
    private boolean firstTimeInState = false;


    public SlimeView(Slime slime) {
        super();
        this.slime = slime;
        this.currentPosition = new Vector2<>(
                (double) slime.getCol(),
                (double) slime.getRow()
        );
        currentState = slime.getState(slime.getCurrentEumState());
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
        // --- IDLE

        createAnimation(Slime.EnumState.IDLE, 0,
                4, true, 3,
                SLIME_RESOURCES + "idle", ".png");


        // --- MOVE

        createAnimation(Slime.EnumState.MOVE, 0,
                4, false, 1,
                SLIME_RESOURCES + "move", ".png");


        // --- ATTACK

        createAnimation(Slime.EnumState.ATTACK, 0,
                4, false, 1,
                SLIME_RESOURCES + "attack/up", ".png");

        createAnimation(Slime.EnumState.ATTACK, 1,
                4, false, 1,
                SLIME_RESOURCES + "attack/down", ".png");

        createAnimation(Slime.EnumState.ATTACK, 2,
                4, false, 1,
                SLIME_RESOURCES + "attack/right", ".png");

        createAnimation(Slime.EnumState.ATTACK, 3,
                4, false, 1,
                SLIME_RESOURCES + "attack/left", ".png");

        // --- HIT

        createAnimation(Slime.EnumState.HIT, 0,
                1, false, 1,
                SLIME_RESOURCES + "hit", ".png");

        // --- DEAD

        createAnimation(Slime.EnumState.DEAD, 0,
                4, false, 1,
                SLIME_RESOURCES + "dead", ".png");
    }

    private void createAnimation(CommonEnumStates enumStates, int type,
                                 int nFrame, boolean loop, int times,
                                 String folderPath, String extension) {
        if (!loop)
            times = 1;
        float durationFrame = slime.getState(enumStates).getDuration() / (nFrame * times);
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
        CommonEnumStates currentState = slime.getCurrentEumState();
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
        DirectionsModel currentDirection = slime.getDirection();
        return switch (currentState) {
            case Slime.EnumState.ATTACK ->
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
        if (!currentState.isFinished()) {
            if (firstTimeInState) {
                float duration = currentState.getDuration();
                moveInterpolationX.changeStart(currentPosition.first);
                moveInterpolationX.changeEnd(slime.getCol());
                moveInterpolationX.changeDuration(duration);
                moveInterpolationX.restart();
                moveInterpolationY.changeStart(currentPosition.second);
                moveInterpolationY.changeEnd(slime.getRow());
                moveInterpolationY.changeDuration(duration);
                moveInterpolationY.restart();
                firstTimeInState = false;
            }
        }
        else {
            currentState = slime.getState(slime.getCurrentEumState());
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
