package chevy.control.trapsController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.dinamicEntity.projectile.Arrow;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.traps.Totem;
import chevy.utilz.Vector2;

public class TotemController {
    private final Chamber chamber;


    public TotemController(Chamber chamber) {
        this.chamber = chamber;
    }


    public void update(Totem totem) {
        Projectile arrow = new Arrow(new Vector2<>(totem.getRow(), totem.getCol()), totem.getDirectionShot());
        chamber.addProjectile(arrow);
        chamber.addEntityOnTop(arrow);
    }
}
