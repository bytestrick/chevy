package chevy.view.entityView.entityViewAnimated.player;

import chevy.model.entity.dinamicEntity.liveEntity.player.Knight;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;
import chevy.view.Image;
import chevy.view.animation.Interpolate;
import chevy.view.animation.InterpolationTypes;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.image.BufferedImage;

public class KnightView extends EntityViewAnimated {
    private final Knight knight;
    private final Vector2<Double> currentPosition;
    private final Interpolate moveInterpolationX;
    private final Interpolate moveInterpolationY;
    private State currentState;
    private boolean firstTimeInState = false;


    public KnightView(Knight knight) {
        this.knight = knight;
        this.currentPosition = new Vector2<>(
                (double) knight.getCol(),
                (double) knight.getRow()
        );
        currentState = knight.getState(knight.getCurrentEumState());
        moveInterpolationX = new Interpolate(currentPosition.first,
                knight.getCol(),
                knight.getState(knight.getCurrentEumState()).getDuration(),
                InterpolationTypes.EASE_OUT_SINE
        );
        moveInterpolationY = new Interpolate(currentPosition.second,
                knight.getRow(),
                knight.getState(knight.getCurrentEumState()).getDuration(),
                InterpolationTypes.EASE_OUT_SINE
        );
    }


    @Override
    public BufferedImage getCurrentFrame() {
        return Image.load("/assets/chamberTiles/trapTiles/void.png");
    }

    @Override
    public Vector2<Double> getCurrentPosition() {
        if (!currentState.isFinished()) {
            if (firstTimeInState) {
                float duration = currentState.getDuration();
                moveInterpolationX.changeStart(currentPosition.first);
                moveInterpolationX.changeEnd(knight.getCol());
                moveInterpolationX.changeDuration(duration);
                moveInterpolationX.restart();
                moveInterpolationY.changeStart(currentPosition.second);
                moveInterpolationY.changeEnd(knight.getRow());
                moveInterpolationY.changeDuration(duration);
                moveInterpolationY.restart();
                firstTimeInState = false;
            }
        }
        else {
            currentState = knight.getState(knight.getCurrentEumState());
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
