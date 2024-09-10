package chevy.control.trapsController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.dynamicEntity.projectile.Arrow;
import chevy.model.entity.staticEntity.environment.traps.Totem;
import chevy.service.Sound;

final class TotemController {
    private final Chamber chamber;

    TotemController(Chamber chamber) {this.chamber = chamber;}

    void update(Totem totem) {
        if (totem.checkAndChangeState(Totem.State.SHOT)) {
            Sound.play(Sound.Effect.ARROW_SWOOSH);
            Arrow arrow = new Arrow(totem.getPosition(), totem.getShotDirection());
            chamber.addProjectile(arrow);
            chamber.addEntityOnTop(arrow);
        }
        totem.checkAndChangeState(Totem.State.RELOAD);
    }
}