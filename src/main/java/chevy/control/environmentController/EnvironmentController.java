package chevy.control.environmentController;

import chevy.control.HUDController;
import chevy.control.Interaction;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.staticEntity.environment.Chest;
import chevy.model.entity.staticEntity.environment.Environment;
import chevy.model.entity.staticEntity.environment.Stair;
import chevy.view.GamePanel;

public final class EnvironmentController {
    private final ChestController chestController;

    public EnvironmentController(Chamber chamber, HUDController hudController,
                                 GamePanel gamePanel) {
        chestController = new ChestController(chamber, hudController);
        StairController.setGamePanel(gamePanel);
        StairController.setHUDController(hudController);
        StairController.onTrapDoor = false;
    }

    private static void playerInInteraction(Environment environment) {
        if (environment.getType() == Environment.Type.STAIR) {
            StairController.playerInInteraction((Stair) environment);
        }
    }

    public synchronized void handleInteraction(Interaction interaction, Entity subject,
                                               Entity object) {
        switch (interaction) {
            case PLAYER_IN -> playerInInteraction((Environment) object);
            case UPDATE -> updateTraps((Environment) subject);
        }
    }

    private void updateTraps(Environment environment) {
        switch (environment.getType()) {
            case Environment.Type.CHEST -> chestController.update((Chest) environment);
            case Environment.Type.STAIR -> StairController.update((Stair) environment);
            default -> {}
        }
    }
}
