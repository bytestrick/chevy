package chevy.model.chamber;

import chevy.model.entity.Entity;
import chevy.utilz.Vector2;

public class TileFromEntity {

    public static Vector2<Integer> get(Entity entity) {
        System.out.println(entity.getSpecificType().toString() + "  " + entity.getGenericType().toString());
//        return switch (entity) {
//            default -> null;
//        };
        return null;
    }

}
