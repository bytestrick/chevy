package chevy.control.trapsController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.staticEntity.environment.traps.Trapdoor;
import chevy.model.entity.staticEntity.environment.traps.Void;
import chevy.utilz.Vector2;

public class TrapdoorController {
    private final Chamber chamber;


    public TrapdoorController(Chamber chamber) {
        this.chamber = chamber;
    }


    public void playerOutInteraction(Trapdoor trapdoor) {
        chamber.removeEntityOnTop(trapdoor);
        chamber.addEntityOnTop(new Void(new Vector2<>(
                trapdoor.getRow(),
                trapdoor.getCol()
        )));
    }
}
