package chevy.view.entityView.entityViewAnimated.enemy;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.utilz.Pair;
import chevy.utilz.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.animation.Interpolate;
import chevy.view.animation.InterpolationTypes;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import javax.swing.*;
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
        createAnimation(Slime.EnumState.IDLE, 0,
                6, true, 4,
                SLIME_RESOURCES + "idle", ".png");

        createAnimation(Slime.EnumState.MOVE, 0,
                6, false, 1,
                SLIME_RESOURCES + "move", ".png");

        createAnimation(Slime.EnumState.ATTACK, 0,
                5, false, 1,
                SLIME_RESOURCES + "attack/up", ".png");

        createAnimation(Slime.EnumState.ATTACK, 1,
                5, false, 1,
                SLIME_RESOURCES + "attack/down", ".png");

        createAnimation(Slime.EnumState.ATTACK, 2,
                5, false, 1,
                SLIME_RESOURCES + "attack/right", ".png");

        createAnimation(Slime.EnumState.ATTACK, 3,
                5, false, 1,
                SLIME_RESOURCES + "attack/left", ".png");

        createAnimation(Slime.EnumState.HIT, 0,
                1, false, 1,
                SLIME_RESOURCES + "hit/right", ".png");

        createAnimation(Slime.EnumState.HIT, 1,
                1, false, 1,
                SLIME_RESOURCES + "hit/left", ".png");

        createAnimation(Slime.EnumState.DEAD, 0,
                2, false, 1,
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
        initAnimation(animatedSprite, folderPath, extension);
    }


    @Override
    public BufferedImage getCurrentFrame() {
        CommonEnumStates currentState = slime.getCurrentEumState();
        int type = getType(currentState);

        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, type);

        if (currentAnimatedSprite != null) {
            if (!currentAnimatedSprite.isRunning()) {
                currentAnimatedSprite.start();
            }
            return currentAnimatedSprite.getCurrentFrame();
        }
        return null;
    }

    private int getType(CommonEnumStates currentState) {
        DirectionsModel currentDirection = slime.getDirection();

        int type = switch (currentState) {
            case Slime.EnumState.ATTACK ->
                switch (currentDirection) {
                    case UP -> 0;
                    case DOWN -> 1;
                    case RIGHT -> 2;
                    case LEFT -> 3;
                };
            case Slime.EnumState.HIT -> {
                if (currentDirection == DirectionsModel.RIGHT)
                    yield 1;
                else
                    yield 0;
            }
            default -> 0;
        };
        return type;
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

            moveInterpolationX.restart();
            moveInterpolationY.restart();

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
