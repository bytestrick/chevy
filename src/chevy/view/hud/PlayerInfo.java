package chevy.view.hud;

import javax.swing.*;
import java.awt.*;

public class PlayerInfo extends JPanel {
    private float scale = 3f;
    private HealthBar healthBar = new HealthBar(10, scale);
    private ShieldBar shieldBar = new ShieldBar(8, scale);
    private AttackBar attackBar = new AttackBar(5, scale);
    private JPanel barContainer = new JPanel();


    public PlayerInfo() {
        setOpaque(false);
        barContainer.setOpaque(false);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        barContainer.setLayout(new BoxLayout(barContainer, BoxLayout.Y_AXIS));

        healthBar.setAlignmentX(LEFT_ALIGNMENT);
        shieldBar.setAlignmentX(LEFT_ALIGNMENT);
        attackBar.setAlignmentX(LEFT_ALIGNMENT);

        barContainer.add(healthBar);
        barContainer.add(shieldBar);
        barContainer.add(attackBar);
        barContainer.setAlignmentY(CENTER_ALIGNMENT);

        add(new PlayerFrame(new Dimension(112, 112)));
        add(Box.createHorizontalStrut(5));
        add(barContainer);
    }
}

