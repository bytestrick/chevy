package chevy.model.world;

import chevy.model.staticEntity.Entity;
import chevy.model.staticEntity.environment.Ground;
import chevy.utilz.Vector2;

public class ColorToEntity {
    public static final int GROUND = 255;

    public static Entity get(int r, int row, int col) {
        switch (r) {
            case GROUND -> new Ground(new Vector2<>(row, col));
        }
        return null;
    }
}
