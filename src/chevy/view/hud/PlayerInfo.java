package chevy.view.hud;

import javax.swing.*;
import java.awt.*;

public class PlayerInfo extends JPanel {
    private final HealthBar healthBar;
    private final ShieldBar shieldBar;
    private final AttackBar attackBar;
    private final PlayerFrame playerFrame;


    public PlayerInfo(float scale) {
        setOpaque(false);
        JPanel barContainer = new JPanel();
        barContainer.setOpaque(false);

        healthBar = new HealthBar(10, scale);
        shieldBar = new ShieldBar(8, scale);
        attackBar = new AttackBar(5, scale);
        playerFrame = new PlayerFrame(new Dimension(24, 24), scale);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        barContainer.setLayout(new BoxLayout(barContainer, BoxLayout.Y_AXIS));

        healthBar.setAlignmentX(LEFT_ALIGNMENT);
        shieldBar.setAlignmentX(LEFT_ALIGNMENT);
        attackBar.setAlignmentX(LEFT_ALIGNMENT);

        barContainer.add(healthBar);
        barContainer.add(shieldBar);
        barContainer.add(attackBar);
        barContainer.setAlignmentY(CENTER_ALIGNMENT);

        add(playerFrame);
        add(Box.createHorizontalStrut(5));
        add(barContainer);
    }

    public void windowResized(float scale) {
        healthBar.windowResized(scale);
        shieldBar.windowResized(scale);
        attackBar.windowResized(scale);
        playerFrame.windowResized(scale);
    }
}

