package chevy.view.entities.environmet;

import chevy.model.entity.staticEntity.environment.traps.Void;
import chevy.utils.Load;
import chevy.utils.Vector2;
import chevy.view.entities.EntityView;

import java.awt.image.BufferedImage;

public final class VoidView extends EntityView {
    private static final String VOID_RESOURCES = "/sprites/chamberTiles/trapTiles/void.png";
    private static final BufferedImage frame = Load.image(VOID_RESOURCES);

    public VoidView(Void _void) {
        viewPosition = new Vector2<>((double) _void.getCol(), (double) _void.getRow());
    }

    @Override
    public BufferedImage getFrame() { return frame; }
}