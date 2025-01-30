package chevy.view.hud;

import javax.swing.*;
import java.awt.*;

public final class PlayerInfo extends JPanel {
    private final HealthBar healthBar;
    private final ShieldBar shieldBar;
    private final AttackBar attackBar;
    private final PlayerFrame playerFrame;
    private final PowerUpEquippedView powerUpEquippedView;

    PlayerInfo(float scale) {
        JPanel barContainer = new JPanel();
        JPanel frameAndBarContainer = new JPanel();

        setOpaque(false);
        barContainer.setOpaque(false);
        frameAndBarContainer.setOpaque(false);

        // Creation, arrangement and addition of components in the barContainer
        healthBar = new HealthBar(scale);
        shieldBar = new ShieldBar(scale);
        attackBar = new AttackBar(scale);
        playerFrame = new PlayerFrame(new Dimension(42, 42), scale);

        barContainer.setLayout(new BoxLayout(barContainer, BoxLayout.Y_AXIS));
        healthBar.setAlignmentX(LEFT_ALIGNMENT);
        shieldBar.setAlignmentX(LEFT_ALIGNMENT);
        attackBar.setAlignmentX(LEFT_ALIGNMENT);

        barContainer.add(healthBar);
        barContainer.add(shieldBar);
        barContainer.add(attackBar);
        barContainer.setAlignmentY(CENTER_ALIGNMENT);

        // Creation, arrangement and addition of components in the frameAndBarContainer
        powerUpEquippedView = new PowerUpEquippedView(scale / 1.4f);

        frameAndBarContainer.setLayout(new BoxLayout(frameAndBarContainer, BoxLayout.X_AXIS));
        frameAndBarContainer.add(playerFrame);
        frameAndBarContainer.add(Box.createHorizontalStrut(5));
        frameAndBarContainer.add(barContainer);
        frameAndBarContainer.setAlignmentX(LEFT_ALIGNMENT);

        // Arrangement and addition of components in the main container
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        powerUpEquippedView.setAlignmentX(LEFT_ALIGNMENT);

        add(frameAndBarContainer);
        add(Box.createVerticalStrut(5));
        add(powerUpEquippedView);
    }

    public void windowResized(float scale) {
        healthBar.windowResized(scale);
        shieldBar.windowResized(scale);
        attackBar.windowResized(scale);
        playerFrame.windowResized(scale);
        powerUpEquippedView.windowResized(scale);
    }

    public HealthBar getHealthBar() {
        return healthBar;
    }

    public ShieldBar getShieldBar() {
        return shieldBar;
    }

    public AttackBar getAttackBar() {
        return attackBar;
    }

    public PowerUpEquippedView getPowerUpEquippedView() {
        return powerUpEquippedView;
    }

    public PlayerFrame getPlayerFrame() {
        return playerFrame;
    }
}
