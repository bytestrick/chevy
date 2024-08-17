package chevy.view.component;

import javax.swing.*;
import java.awt.*;

public class ProgressBarLabel extends JPanel {
    private JLabel label = new JLabel();
    private final ProgressBar progressBar;

    public  ProgressBarLabel(int maxValue) {
        this(maxValue, maxValue, 1f);
    }

    public ProgressBarLabel(int maxValue, float scale) {
        this(maxValue, maxValue, scale);
    }

    public ProgressBarLabel(int value, int maxValue, float scale) {
        setOpaque(false);
        progressBar = new ProgressBar(value, maxValue, scale);

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

    public void setStepTexture(String path) {
        progressBar.setStepTexture(path);
    }

    public void setText(String text) {
        label.setText(text);
    }

    public void windowResized(float scale) {
        progressBar.windowResized(scale);
    }
}
