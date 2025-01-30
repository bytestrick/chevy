package chevy.view.component;

import chevy.utils.Load;
import chevy.view.Window;

import javax.swing.*;
import java.awt.*;

public class CounterBar extends JPanel {
    private static final int counter = 0;
    private final JLabel text = new JLabel(String.valueOf(counter), SwingConstants.RIGHT);
    private final MyPanelUI ui = new MyPanelUI();
    private final Dimension dimension;
    private final SpringLayout springLayout = new SpringLayout();
    private final float scale;
    private int fontSize = 13;
    private Font font = text.getFont().deriveFont(fontSize * Window.scale);
    private int offsetY;

    public CounterBar(Dimension dimension, float scale) {
        this.scale = scale;
        this.dimension = dimension;

        text.setAlignmentX(Component.RIGHT_ALIGNMENT);
        text.setAlignmentY(Component.CENTER_ALIGNMENT);
    }

    protected void initUI() {
        text.setMaximumSize(getMaximumSize());
        setOpaque(false);
        setUI(ui);
        setDimension(Window.scale);
        setLayout(springLayout);
        setConstraints(Window.scale);
        add(Box.createHorizontalGlue());
        add(text);
    }

    protected void setTexture(int i, String path) {
        ui.setTexture(i, path);
        revalidate();
        repaint();
    }

    protected void setFontSize(int value) {
        fontSize = value;
        resizeFont();
    }

    protected void setFont() {
        font = Load.font("superstar_2/superstar_memesbruh03");
        resizeFont();
    }

    protected void setColor() {
        text.setForeground(Color.BLACK);
    }

    private void resizeFont() {
        font = font.deriveFont(fontSize * Window.scale);
        text.setFont(font);
    }

    protected void setText(String text) {
        this.text.setText(text);
    }

    private void setDimension(float scale) {
        Dimension dimensionScaled = new Dimension((int) (dimension.getWidth() * scale),
                (int) (dimension.height * scale));

        setMaximumSize(dimensionScaled);
        setPreferredSize(dimensionScaled);
        setMinimumSize(dimensionScaled);
    }

    private void setConstraints(float scale) {
        final int rightSpace = 0;
        springLayout.putConstraint(SpringLayout.EAST, text,
                -((int) (rightSpace * scale) + ui.getWidth(MyPanelUI.BAR_R)), SpringLayout.EAST,
                this);
        springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, text, offsetY,
                SpringLayout.VERTICAL_CENTER, this);
    }

    protected void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public void windowResized(float scale) {
        scale = scale * this.scale;

        ui.setScale(scale);
        resizeFont();
        setConstraints(scale);
        setDimension(scale);
    }
}
