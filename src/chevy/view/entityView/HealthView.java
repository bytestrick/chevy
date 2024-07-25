package chevy.view.entityView;

import chevy.model.entity.collectable.Health;
import chevy.utils.Vector2;
import chevy.view.Image;
import chevy.view.animation.Interpolate;
import chevy.view.animation.InterpolationTypes;

import java.awt.image.BufferedImage;

public class HealthView extends EntityView {
    private static final String HEALT_PATH = "/assets/collectable/health/0.png";
    private final BufferedImage frame;
    private final Interpolate moveInterpolationY;
    private double end;
    private double start;


    public HealthView(Health health) {
        this.frame = Image.load(HEALT_PATH);
        this.currentViewPosition = new Vector2<>((double) health.getCol(), (double) health.getRow());
        float offsetY = 0.04f;
        float duration = 0.7f;

        this.start = currentViewPosition.second - offsetY;
        this.end = currentViewPosition.second + offsetY;
        this.moveInterpolationY = new Interpolate(start, end,
                duration, InterpolationTypes.EASE_IN_OUT_BACK);
    }


    @Override
    public BufferedImage getCurrentFrame() {
        return frame;
    }

    @Override
    public Vector2<Double> getCurrentViewPosition() {
        if (!moveInterpolationY.isRunning()) {
            moveInterpolationY.changeStart(end);
            moveInterpolationY.changeEnd(start);

            double tempEnd = end;
            end = start;
            start = tempEnd;

            moveInterpolationY.restart();
        }
        currentViewPosition.changeSecond(moveInterpolationY.getValue());
        return currentViewPosition;
    }

    public void wasRemoved() {
        moveInterpolationY.delete();
    }
}
