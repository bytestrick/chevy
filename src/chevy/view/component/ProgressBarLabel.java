package chevy.view.component;

import chevy.settings.WindowSettings;
import chevy.utils.Fontt;

import javax.swing.*;
import java.awt.*;

public class ProgressBarLabel extends JPanel {
    private JLabel label = new JLabel();
    private final Color COLOR_TEXT = new Color(228, 166, 114, 255);
    private final ProgressBar progressBar;
    private Font font;
    private int fontSize = 13;

    public  ProgressBarLabel(int maxValue) {
        this(maxValue, maxValue, 1f);
    }

    public ProgressBarLabel(int maxValue, float scale) {
        this(maxValue, maxValue, scale);
    }

    public ProgressBarLabel(int value, int maxValue, float scale) {
        setOpaque(false);
        progressBar = new ProgressBar(value, maxValue, scale);

        font = label.getFont();
        label.setForeground(COLOR_TEXT);
        label.setText(String.valueOf(value));

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(progressBar);
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(label);
    }

    public void setValue(int value) {
        progressBar.setValue(value);
    }

    public void setTexture(int i, String path) {
        progressBar.setTexture(i, path);
    }

    public void setFontSize(int value) {
        fontSize = value;
        resizeFont();
    }

    public void setFont(String path) {
        font = Fontt.load(path);
        resizeFont();
    }

    private void resizeFont() {
        font = font.deriveFont(fontSize * WindowSettings.scale);
        label.setFont(font);
    }

    public void setStepTexture(String path) {
        progressBar.setStepTexture(path);
    }

    public void setMaxValue(int maxValue) {
        progressBar.setMaxValue(maxValue);
    }

    public int getMaxValue() {
        return progressBar.getMaxValue();
    }

    public void setText(String text) {
        label.setText(text);
    }

    public void windowResized(float scale) {
        progressBar.windowResized(scale);
        resizeFont();
    }
}
