package chevy.control.collectableController;

import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.view.entityView.entityViewAnimated.collectable.powerUp.PowerUpTextVisualizerView;

public class PowerUpTextVisualizerController {
    private final PowerUpTextVisualizerView powerUpTextVisualizerView;


    public PowerUpTextVisualizerController(PowerUpTextVisualizerView powerUpTextVisualizerView) {
        this.powerUpTextVisualizerView = powerUpTextVisualizerView;
    }


    public void show(PowerUp powerUp) {
        powerUpTextVisualizerView.setPowerUpName(powerUp.getName());
        powerUpTextVisualizerView.setPowerUpDescription(powerUp.getDescription());
        powerUpTextVisualizerView.repaint();
        powerUpTextVisualizerView.setVisible(true);
    }

    public void hide() {
        powerUpTextVisualizerView.setVisible(false);
    }
}
