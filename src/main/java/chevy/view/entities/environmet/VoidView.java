package chevy.view.entities.environmet;

import chevy.model.entity.staticEntity.environment.traps.Void;
import chevy.utils.Load;
import chevy.view.entities.EntityView;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public final class VoidView extends EntityView {
    private static final String VOID_RESOURCES = "/sprites/chamberTiles/trapTiles/void.png";
    private static final BufferedImage frame = Load.image(VOID_RESOURCES);

    public VoidView(Void _void) {
        viewPosition = new Point2D.Double(_void.getCol(), _void.getRow());
    }

    @Override
    public BufferedImage getFrame() {return frame;}
}
