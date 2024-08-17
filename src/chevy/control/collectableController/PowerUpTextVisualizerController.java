package chevy.control.collectableController;

import chevy.Sound;
import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.view.entities.animated.collectable.powerUp.PowerUpTextVisualizerView;

public class PowerUpTextVisualizerController {
    private final PowerUpTextVisualizerView powerUpTextVisualizerView;


    public PowerUpTextVisualizerController(PowerUpTextVisualizerView powerUpTextVisualizerView) {
        this.powerUpTextVisualizerView = powerUpTextVisualizerView;
    }


    public void show(PowerUp powerUp) {
        Sound.getInstance().play(Sound.Effect.POWER_UP_TEXT_VISUALIZER);
        powerUpTextVisualizerView.setPowerUpName(powerUp.getName());
        powerUpTextVisualizerView.setPowerUpDescription(powerUp.getDescription());
        powerUpTextVisualizerView.repaint();
        powerUpTextVisualizerView.setVisible(true);
    }

    public void hide() {
        powerUpTextVisualizerView.setVisible(false);
    }
}
