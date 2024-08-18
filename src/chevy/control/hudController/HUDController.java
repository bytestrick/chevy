package chevy.control.hudController;

import chevy.model.HUD;
import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.view.hud.HUDView;
import chevy.view.hud.HealthBar;
import chevy.view.hud.ShieldBar;

public class HUDController {
    private final HUDView hudView;
    private final HUD hud;

    public HUDController(HUD hud, HUDView hudView) {
        this.hudView = hudView;
        this.hud = hud;
    }

    public void changeHealth(int health) {
        HealthBar healthBar = hudView.getPlayerInfo().getHealthBar();
        if (healthBar.getValue() != health)
            healthBar.setValue(health);
    }

    public void changeShield(int shield) {
        ShieldBar shieldBar = hudView.getPlayerInfo().getShieldBar();
        if (shieldBar.getValue() != shield)
            shieldBar.setValue(shield);
    }

    public void addCoin(int value) {
        hud.addCoin(value);
        hudView.getCoinBar().setCoin(hud.getCoin());
    }

    public void addKey(int value) {
        hud.addKey(value);
        hudView.getKeyBar().setKey(hud.getCoin());
    }

    public void hidePowerUpText() {
        hudView.getPowerUpText().mHide();
    }

    public void PowerUpText(PowerUp powerUp) {
        hudView.getPowerUpText().show(powerUp);
    }
}
