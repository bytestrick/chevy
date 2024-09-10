package chevy.control;

import chevy.model.HUD;
import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.service.Data;
import chevy.view.hud.AttackBar;
import chevy.view.hud.HUDView;
import chevy.view.hud.HealthBar;
import chevy.view.hud.ShieldBar;

public final class HUDController {
    private final HUDView hudView;
    private final HUD hud;

    public HUDController(HUD hud, HUDView hudView) {
        this.hudView = hudView;
        this.hud = hud;
        addKeys(Data.get("progress.keys"));
        addCoins(Data.get("progress.coins"));
        hudView.getPlayerInfo().getPlayerFrame().setIconFrame();
        hudView.getPlayerInfo().getPowerUpEquippedView().clear();
    }

    public void changeHealth(int health) {
        HealthBar healthBar = hudView.getPlayerInfo().getHealthBar();
        if (healthBar.getValue() != health) {
            healthBar.setValue(health);
        }
    }

    void changeShield(int shield) {
        ShieldBar shieldBar = hudView.getPlayerInfo().getShieldBar();
        if (shieldBar.getValue() != shield) {
            shieldBar.setValue(shield);
        }
    }

    public void changeMaxShield(int value, int maxShield) {
        ShieldBar shieldBar = hudView.getPlayerInfo().getShieldBar();
        if (shieldBar.getMaxValue() != maxShield) {
            shieldBar.setMaxValue(value, maxShield);
        }
    }

    public void changeMaxHealth(int value, int maxHealth) {
        HealthBar healthBar = hudView.getPlayerInfo().getHealthBar();
        if (healthBar.getMaxValue() != maxHealth) {
            healthBar.setMaxValue(value, maxHealth);
        }
    }

    public void changeMaxAttack(int maxDamage) {
        AttackBar attackBar = hudView.getPlayerInfo().getAttackBar();
        if (attackBar.getMaxValue() != maxDamage) {
            attackBar.setMaxValue(maxDamage);
        }
    }

    public void setTextAttackBar(String text) {
        hudView.getPlayerInfo().getAttackBar().setText(text);
    }

    public void addCoins(int value) {
        hud.addCoin(value);
        hudView.getCoinBar().setCoin(hud.getCoins());
    }

    public void addKeys(int value) {
        hud.addKey(value);
        hudView.getKeyBar().setKey(hud.getKeys());
    }

    public int getKeys() {return hud.getKeys();}

    public int getCoins() {return hud.getCoins();}

    public void hidePowerUpText() {hudView.getPowerUpText().mHide();}

    public void PowerUpText(PowerUp powerUp) {hudView.getPowerUpText().show(powerUp);}

    public void addPowerUpIcon(PowerUp powerUp) {
        hudView.getPlayerInfo().getPowerUpEquippedView().add(powerUp);
    }
}