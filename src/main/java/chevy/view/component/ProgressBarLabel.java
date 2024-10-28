package chevy.view.component;

import chevy.utils.Load;
import chevy.view.Window;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class ProgressBarLabel extends JPanel {
    private final ProgressBar progressBar;
    private final JLabel label = new JLabel();
    private Font font = label.getFont();
    private int fontSize = 13;

    public ProgressBarLabel(int value, int maxValue, float scale) {
        progressBar = new ProgressBar(value, maxValue, scale);
        progressBar.initUI();
        label.setForeground(Color.WHITE);
        label.setText(String.valueOf(value));
    }

    protected void initUI() {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(progressBar);
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(label);
    }

    public void windowResized(float scale) {
        progressBar.windowResized(scale);
        resizeFont();
    }

    protected void setTexture(int i, String path) {progressBar.setTexture(i, path);}

    protected void setFontSize(int value) {
        fontSize = value;
        resizeFont();
    }

    protected void setFont() {
        font = Load.font("superstar_2/superstar_memesbruh03");
        resizeFont();
    }

    private void resizeFont() {
        font = font.deriveFont(fontSize * Window.scale);
        label.setFont(font);
    }

    protected void setStepTexture() {
        progressBar.setStepTexture("/sprites/component/progressBar/attackBar/step.png");
    }

    public int getMaxValue() {return progressBar.getMaxValue();}

    public void setMaxValue(int maxValue) {progressBar.setMaxValue(maxValue);}

    public void setText(String text) {label.setText(text);}
}
