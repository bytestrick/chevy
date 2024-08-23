package chevy.control.environmentController;

import chevy.control.HUDController;
import chevy.control.InteractionType;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.staticEntity.environment.Chest;
import chevy.model.entity.staticEntity.environment.Environment;
import chevy.model.entity.staticEntity.environment.Stair;

public class EnvironmentController {
    private final ChestController chestController;
    private final StairController stairController;

    public EnvironmentController(Chamber chamber, HUDController hudController) {
        this.chestController = new ChestController(chamber, hudController);
        this.stairController = new StairController(chamber);
    }


    public synchronized void handleInteraction(InteractionType interaction, Entity subject, Entity object) {
        switch (interaction) {
            case UPDATE -> updateTraps((Environment) subject);
        }
    }

    private void updateTraps(Environment environment) {
        switch (environment.getSpecificType()) {
            case Environment.Type.CHEST -> chestController.update((Chest) environment);
            case Environment.Type.STAIR -> stairController.update((Stair) environment);
            default -> { }
        }
    }
}
