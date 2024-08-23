package chevy.view.entities.environmet;

import chevy.model.entity.staticEntity.environment.traps.Void;
import chevy.utils.Image;
import chevy.utils.Vector2;
import chevy.view.entities.EntityView;

import java.awt.image.BufferedImage;

public class VoidView extends EntityView {
    private static final String VOID_RESOURCES = "/assets/chamberTiles/trapTiles/void.png";
    private static final BufferedImage frame = Image.load(VOID_RESOURCES);

    public VoidView(Void voidd) {
        this.currentViewPosition = new Vector2<>(
                (double) voidd.getCol(),
                (double) voidd.getRow()
        );
    }

    @Override
    public BufferedImage getCurrentFrame() {
        return frame;
    }

    @Override
    public Vector2<Double> getCurrentViewPosition() {
        return currentViewPosition;
    }
}