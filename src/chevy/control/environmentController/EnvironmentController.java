package chevy.control.environmentController;

import chevy.control.ChamberController;
import chevy.control.HUDController;
import chevy.control.InteractionType;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.staticEntity.environment.Chest;
import chevy.model.entity.staticEntity.environment.Environment;
import chevy.model.entity.staticEntity.environment.Stair;

public class EnvironmentController {
    private final ChestController chestController;
    private final StairController stairController;

    public EnvironmentController(Chamber chamber, HUDController hudController, ChamberController chamberController) {
        this.chestController = new ChestController(chamber, hudController);
        this.stairController = new StairController(chamber, chamberController);
    }


    public synchronized void handleInteraction(InteractionType interaction, Entity subject, Entity object) {
        switch (interaction) {
            case PLAYER_IN -> playerInInteraction((Player) subject, (Environment) object);
            case UPDATE -> updateTraps((Environment) subject);
        }
    }

    private void playerInInteraction(Player subject, Environment environment) {
        if (environment.getSpecificType() == Environment.Type.STAIR)
            stairController.playerInInteraction((Stair) environment);
    }

    private void updateTraps(Environment environment) {
        switch (environment.getSpecificType()) {
            case Environment.Type.CHEST -> chestController.update((Chest) environment);
            case Environment.Type.STAIR -> stairController.update((Stair) environment);
            default -> { }
        }
    }
}
