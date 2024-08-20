package chevy.view.component;

import chevy.settings.WindowSettings;
import chevy.utils.Fontt;

import javax.swing.*;
import java.awt.*;

public class CounterBar extends JPanel {
    private int counter = 0;
    private final JLabel text = new JLabel(String.valueOf(counter), SwingConstants.RIGHT);
    private final MyPanelUI ui;
    private int rightSpace = 0;
    private final Dimension dimension;
    private final SpringLayout springLayout;
    private Font font;
    private int fontSize = 13;
    private float scale = 1f;


    public CounterBar(Dimension dimension) {
        this(dimension, 1f);
    }

    public CounterBar(Dimension dimension, float scale) {
        this.scale = scale;
        this.dimension = dimension;

        setOpaque(false);
        ui = new MyPanelUI(null, scale * WindowSettings.scale);
        setUI(ui);

        setDimension(WindowSettings.scale);

        springLayout = new SpringLayout();
        setLayout(springLayout);
        setConstraints(WindowSettings.scale);

        font = text.getFont();
        font = font.deriveFont(fontSize * WindowSettings.scale);
        text.setMaximumSize(getMaximumSize());
        text.setAlignmentX(Component.RIGHT_ALIGNMENT);
        text.setAlignmentY(Component.CENTER_ALIGNMENT);

        add(Box.createHorizontalGlue());
        add(text);
    }

    public void setRightSpace(int rightSpace) {
        this.rightSpace = rightSpace;
    }

    public void setTexture(int i, String path) {
        ui.setTexture(i, path);
        revalidate();
        repaint();
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
        text.setFont(font);
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    private void setDimension(float scale) {
        Dimension dimensionScaled = new Dimension(
                (int) (dimension.getWidth() * scale),
                (int) (dimension.height * scale)
        );
        setMaximumSize(dimensionScaled);
        setPreferredSize(dimensionScaled);
        setMinimumSize(dimensionScaled);
    }

    private void setConstraints(float scale) {
        springLayout.putConstraint(SpringLayout.EAST, text, -((int) (rightSpace * scale) + ui.getWidth(MyPanelUI.BAR_R)), SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, text, 0, SpringLayout.VERTICAL_CENTER, this);
    }

    public void windowResized(float scale) {
        scale = scale * this.scale;

        ui.setScale(scale);
        resizeFont();
        setConstraints(scale);
        setDimension(scale);
    }
}
