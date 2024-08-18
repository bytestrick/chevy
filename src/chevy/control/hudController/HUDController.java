package chevy.control.hudController;

import chevy.view.hud.HUD;
import chevy.view.hud.HealthBar;
import chevy.view.hud.ShieldBar;

public class HUDController {
    private final HUD hud;

    public HUDController(HUD hud) {
        this.hud = hud;
    }

    public void changeHealth(int health) {
        HealthBar healthBar = hud.getPlayerInfo().getHealthBar();
        if (healthBar.getValue() != health)
            healthBar.setValue(health);
    }

    public void changeShield(int shield) {
        ShieldBar shieldBar = hud.getPlayerInfo().getShieldBar();
        if (shieldBar.getValue() != shield)
            shieldBar.setValue(shield);
    }
}
