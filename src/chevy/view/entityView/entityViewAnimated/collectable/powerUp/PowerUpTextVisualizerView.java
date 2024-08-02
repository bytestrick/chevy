package chevy.view.entityView.entityViewAnimated.collectable.powerUp;

import chevy.settings.WindowSettings;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PowerUpTextVisualizerView extends JPanel {
    private static final String FONT_PATH = "src/res/assets/Silver.ttf";
    private float fontSizeName = 42f;
    private float fontSizeDescription = 32f;
    private final Color BGColor = new Color(0, 0, 0, 153);
    private final Color shadowColor = new Color(67, 97, 116, 255);
    private final Color textColor = new Color(255, 255, 255, 255);
    private Label name;
    private Label description;

    public PowerUpTextVisualizerView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        // Inizializzazione dei componenti
        this.name = new Label("No name", SwingConstants.CENTER);
        this.description = new Label("No description", SwingConstants.CENTER);

        name.setFont(FONT_PATH);
        name.setTextColor(textColor);
        name.setShadowColor(shadowColor);
        name.setSizeFont(fontSizeName);
        name.setOffsetShadow(0, 6);
        name.setAlignmentX(Label.CENTER);
        name.setAlignmentY(Label.BOTTOM);

        description.setFont(FONT_PATH);
        description.setTextColor(textColor);
        description.setShadowColor(shadowColor);
        description.setSizeFont(fontSizeDescription);
        description.setOffsetShadow(0, 5);
        description.setAlignmentX(Label.CENTER);

        add(name);
        add(description);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(BGColor);
        g.fillRect(0, 0, WindowSettings.WINDOW_WIDTH, WindowSettings.WINDOW_HEIGHT);
    }

    public void setPowerUpName(String name) {
        this.name.setText(name);
    }

    public void setPowerUpDescription(String description) {
        this.description.setText(description);
    }

    public void resizeFont(float scale) {
        name.setSizeFont(fontSizeName, scale);
        description.setSizeFont(fontSizeDescription, scale);
    }
}
