package chevy.control.environmentController;

import chevy.control.HUDController;
import chevy.control.InteractionType;
import chevy.model.HUD;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.staticEntity.environment.Chest;
import chevy.model.entity.staticEntity.environment.Environment;
import chevy.model.entity.staticEntity.environment.traps.Trap;

import java.util.List;

public class EnvironmentController {
    private final ChestController chestController;

    public EnvironmentController(Chamber chamber, HUDController hudController) {
        this.chestController = new ChestController(chamber, hudController);
    }


    public synchronized void handleInteraction(InteractionType interaction, Entity subject, Entity object) {
        switch (interaction) {
//            case PLAYER_IN -> playerInInteraction((Environment) subject, object);
//            case PLAYER_OUT -> playerOutInteraction((Environment) subject, object);
            case UPDATE -> updateTraps((Environment) subject);
        }
    }

    private void updateTraps(Environment environment) {
        switch (environment.getSpecificType()) {
            case Environment.Type.CHEST -> chestController.update((Chest) environment);
            default -> { }
        }
    }

//    private void playerOutInteraction(Environment environment, Entity entity) {
//        switch (environment.getSpecificType()) {
//            case Environment.Type.CHEST -> chestController.playerOutInteraction((Chest) environment);
//            default -> { }
//        }
//    }
//
//    private void playerInInteraction(Environment environment, Entity entity) {
//        switch (environment.getSpecificType()) {
//            case Environment.Type.CHEST -> chestController.playerInInteraction((Chest) environment);
//            default -> { }
//        }
//    }

}
