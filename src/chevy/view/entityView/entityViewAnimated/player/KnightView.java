package chevy.view.entityView.entityViewAnimated.player;

import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.liveEntity.player.Knight;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.utilz.Vector2;
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
    private CommonEnumStates previousEnumState = null;


    public KnightView(Knight knight) {
        this.knight = knight;
        this.currentPosition = new Vector2<>(
                (double) knight.getCol(),
                (double) knight.getRow()
        );
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
        CommonEnumStates currentEnumState = knight.getCurrentEumState();
        if (currentEnumState != previousEnumState) {
            if (previousEnumState == null) {
                previousEnumState = currentEnumState;
                return currentPosition;
            }

            float duration = knight.getState(knight.getCurrentEumState()).getDuration();

            moveInterpolationX.changeStart(currentPosition.first);
            moveInterpolationX.changeEnd(knight.getCol());
            moveInterpolationX.changeDuration(duration);

            moveInterpolationY.changeStart(currentPosition.second);
            moveInterpolationY.changeEnd(knight.getRow());
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
