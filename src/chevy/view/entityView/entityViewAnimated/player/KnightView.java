package chevy.view.entityView.entityViewAnimated.player;

import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.liveEntity.player.Knight;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.utilz.Vector2;
import chevy.view.Image;
import chevy.view.animation.Interpolate;
import chevy.view.animation.InterpolationTypes;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.image.BufferedImage;

public class KnightView extends EntityViewAnimated {
    private final Knight knight;
    private final Vector2<Double> currentPosition;
    private Interpolate moveInterpolationX = null;
    private Interpolate moveInterpolationY = null;


    public KnightView(Knight knight) {
        this.knight = knight;
        this.currentPosition = new Vector2<>(
                (double) knight.getCol(),
                (double) knight.getRow()
        );
    }


    @Override
    public BufferedImage getCurrentFrame() {
        return Image.load("/assets/chamberTiles/trapTiles/void.png");
    }

    @Override
    public Vector2<Double> getCurrentPosition() {
        if (moveInterpolationX == null) {
            moveInterpolationX = new Interpolate(currentPosition.first,
                    knight.getCol(),
                    knight.getState(Player.EnumState.MOVE).getDuration(),
                    InterpolationTypes.EASE_IN_OUT_SINE);
            moveInterpolationX.start();
        }
        if (moveInterpolationY == null) {
            moveInterpolationY = new Interpolate(currentPosition.second,
                    knight.getRow(),
                    knight.getState(Player.EnumState.MOVE).getDuration(),
                    InterpolationTypes.EASE_IN_OUT_SINE);
            moveInterpolationY.start();
        }

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
}
