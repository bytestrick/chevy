package chevy.view.entities.animated.collectable.powerUp;

import chevy.settings.WindowSettings;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Graphics;

public class PowerUpTextVisualizerView extends JPanel {
    private static final String FONT_PATH = "src/res/fonts/PixelatedPusab.ttf";
    private final float fontSizeName = 24f;
    private final float fontSizeDescription = 16f;
    private final float fontSizeInfoEscape = 10f;
    private final Color BGColor = new Color(0, 0, 0, 153);
    private final Color shadowColor = new Color(105, 127, 144, 255);
    private final Color textColor = new Color(228, 236, 238, 255);
    private final Label name;
    private final Label description;
    private final Label infoEscape;

    public PowerUpTextVisualizerView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        // Inizializzazione dei componenti
        name = new Label("No name", SwingConstants.CENTER);
        description = new Label("No description", SwingConstants.CENTER);
        infoEscape = new Label("(Move to escape)", SwingConstants.CENTER);

        name.setFont(FONT_PATH);
        name.setTextColor(textColor);
        name.setShadowColor(shadowColor);
        name.setSizeFont(fontSizeName);
        name.setOffsetShadow(0, 2);
        name.setAlignmentX(Label.CENTER);
        name.setAlignmentY(Label.BOTTOM);

        description.setFont(FONT_PATH);
        description.setTextColor(textColor);
        description.setShadowColor(shadowColor);
        description.setSizeFont(fontSizeDescription);
        description.setOffsetShadow(0, 2);
        description.setAlignmentX(Label.CENTER);

        infoEscape.setFont(FONT_PATH);
        infoEscape.setTextColor(textColor);
        infoEscape.setShadowColor(shadowColor);
        infoEscape.setSizeFont(fontSizeInfoEscape);
        infoEscape.setOffsetShadow(0, 1);
        infoEscape.setAlignmentX(Label.CENTER);

        // aggiunta componenti al panel
        add(name);
        add(description);
        add(infoEscape);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // ridisegna automaticamente quando la finestra viene ridimensionata
        g.setColor(BGColor);
        g.fillRect(0, 0, WindowSettings.WINDOW_WIDTH, WindowSettings.WINDOW_HEIGHT);
    }

    public void setPowerUpName(String name) {
        this.name.setText(name);
    }

    public void setPowerUpDescription(String description) {
        this.description.setText(description);
    }

    // Adatta la dimensione del font in base alla sua dimensione in relazione a quella della finestra
    public void resizeFont(float scale) {
        name.setSizeFont(fontSizeName, scale);
        description.setSizeFont(fontSizeDescription, scale);
        infoEscape.setSizeFont(fontSizeInfoEscape, scale);
    }
}