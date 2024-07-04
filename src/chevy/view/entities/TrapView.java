package chevy.view.entities;

import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.utils.Image;
import chevy.utils.Vector2;

import java.awt.image.BufferedImage;

public class TrapView extends EntityView {
    private static final String COMMON_PATH = "/assets/chamberTiles/trapTiles/";
    private static BufferedImage TRAP_VOID = null;
    private final Trap trap;
    private final Vector2<Double> position = new Vector2<>(0d, 0d);

    public TrapView(Trap trap) {
        this.trap = trap;
    }

    @Override
    public BufferedImage getCurrentFrame() {
        return switch (trap.getSpecificType()) {
            case Trap.Type.VOID -> {
                if (TRAP_VOID == null) {
                    TRAP_VOID = Image.load(COMMON_PATH + "void.png");
                }
                yield TRAP_VOID;
            }
            default -> null;
        };
    }

    @Override
    public Vector2<Double> getCurrentPosition() {
        position.changeFirst((double) trap.getCol());
        position.changeSecond((double) trap.getRow());
        return position;
    }
}