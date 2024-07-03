package chevy.view.entities.animated.player;

import chevy.model.entity.dinamicEntity.liveEntity.player.Knight;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Image;
import chevy.utils.Vector2;
import chevy.view.animation.Interpolation;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class KnightView extends AnimatedEntityView {
    private final Knight knight;
    private final Vector2<Double> currentPosition;
    private final Interpolation moveInterpolationX;
    private final Interpolation moveInterpolationY;
    private State currentState;
    private boolean firstTimeInState = false;

    public KnightView(Knight knight) {
        this.knight = knight;
        this.currentPosition = new Vector2<>((double) knight.getCol(), (double) knight.getRow());
        currentState = knight.getState(knight.getCurrentEumState());
        moveInterpolationX = new Interpolation(currentPosition.first, knight.getCol(),
                knight.getState(knight.getCurrentEumState()).getDuration(), Interpolation.Types.EASE_OUT_SINE);
        moveInterpolationY = new Interpolation(currentPosition.second, knight.getRow(),
                knight.getState(knight.getCurrentEumState()).getDuration(), Interpolation.Types.EASE_OUT_SINE);
    }

    @Override
    public BufferedImage getCurrentFrame() {
        return Image.load("/assets/chamberTiles/trapTiles/void.png");
    }

    @Override
    public Vector2<Double> getCurrentPosition() {
        if (currentState.isFinished()) {
            currentState = knight.getState(knight.getCurrentEumState());
            firstTimeInState = true;
        } else if (firstTimeInState) {
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