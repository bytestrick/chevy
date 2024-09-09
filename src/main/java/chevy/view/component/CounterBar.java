package chevy.view.component;

import chevy.utils.Load;
import chevy.view.Window;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

public class CounterBar extends JPanel {
    private static final int counter = 0;
    private final JLabel text = new JLabel(String.valueOf(counter), SwingConstants.RIGHT);
    private final MyPanelUI ui;
    private final Dimension dimension;
    private final SpringLayout springLayout;
    private final float scale;
    private Font font;
    private int fontSize = 13;
    private int offsetY;

    public CounterBar(Dimension dimension, float scale) {
        this.scale = scale;
        this.dimension = dimension;

        setOpaque(false);
        ui = new MyPanelUI();
        setUI(ui);

        setDimension(Window.scale);

        springLayout = new SpringLayout();
        setLayout(springLayout);
        setConstraints(Window.scale);

        font = text.getFont();
        font = font.deriveFont(fontSize * Window.scale);
        text.setMaximumSize(getMaximumSize());
        text.setAlignmentX(Component.RIGHT_ALIGNMENT);
        text.setAlignmentY(Component.CENTER_ALIGNMENT);

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

    protected void setColor() {text.setForeground(Color.BLACK);}

    private void resizeFont() {
        font = font.deriveFont(fontSize * Window.scale);
        text.setFont(font);
    }

    protected void setText(String text) {this.text.setText(text);}

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

    protected void setOffsetY(int offsetY) {this.offsetY = offsetY;}

    public void windowResized(float scale) {
        scale = scale * this.scale;

        ui.setScale(scale);
        resizeFont();
        setConstraints(scale);
        setDimension(scale);
    }
}