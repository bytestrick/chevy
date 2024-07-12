package chevy.view.entityView.entityViewAnimated.player;

import chevy.model.entity.dinamicEntity.liveEntity.player.Knight;
import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;
import chevy.view.Image;
import chevy.view.animation.Interpolate;
import chevy.view.animation.InterpolationTypes;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.image.BufferedImage;

public class KnightView extends EntityViewAnimated {
    private final Knight knight;
    private final Interpolate moveInterpolationX;
    private final Interpolate moveInterpolationY;
    private State currentState;
    private CommonEnumStates currentViewEnumState;
    private boolean firstTimeInState = false;


    public KnightView(Knight knight) {
        this.knight = knight;
        this.currentViewEnumState = knight.getCurrentEumState();
        this.currentViewPosition = new Vector2<>(
                (double) knight.getCol(),
                (double) knight.getRow()
        );
        currentState = knight.getState(knight.getCurrentEumState());
        moveInterpolationX = new Interpolate(currentViewPosition.first,
                knight.getCol(),
                knight.getState(knight.getCurrentEumState()).getDuration(),
                InterpolationTypes.EASE_OUT_SINE
        );
        moveInterpolationY = new Interpolate(currentViewPosition.second,
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
    public Vector2<Double> getCurrentViewPosition() {
        CommonEnumStates currentModelEnumState = knight.getCurrentEumState();
        if (!currentState.isFinished()) {
            if (firstTimeInState) {
                updateInterpolateValue();
                firstTimeInState = false;
            }
        }
        else {
            if (currentViewEnumState != currentModelEnumState) {
                updateInterpolateValue();
                currentViewEnumState = currentModelEnumState;
            }
            currentState = knight.getState(knight.getCurrentEumState());
            firstTimeInState = true;
        }

        currentViewPosition.changeFirst(moveInterpolationX.getValue());
        currentViewPosition.changeSecond(moveInterpolationY.getValue());
        return currentViewPosition;
    }

    private void updateInterpolateValue() {
        float duration = currentState.getDuration();
        moveInterpolationX.changeStart(currentViewPosition.first);
        moveInterpolationX.changeEnd(knight.getCol());
        moveInterpolationX.changeDuration(duration);
        moveInterpolationX.restart();
        moveInterpolationY.changeStart(currentViewPosition.second);
        moveInterpolationY.changeEnd(knight.getRow());
        moveInterpolationY.changeDuration(duration);
        moveInterpolationY.restart();
        firstTimeInState = false;
    }

    @Override
    public void wasRemoved() {
        moveInterpolationX.delete();
        moveInterpolationY.delete();
        deleteAnimations();
    }
}
