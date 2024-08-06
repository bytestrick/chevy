package chevy.view.entityView.entityViewAnimated.collectable.powerUp;

import chevy.settings.WindowSettings;
import chevy.utils.Vector2;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Label extends JPanel {
    public static final int TOP = 0;
    public static final int CENTER = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int BOTTOM = 4;


    // TODO: cambiare la JLabel con un JTextArea, la JLabel non permette l'andata a capo.
    private final JLabel text;
    private final JLabel shadow;
    private float shadowSize = 0;
    private final Vector2<Integer> shadowOffset = new Vector2<>(0, 0);
    private SpringLayout springLayout = new SpringLayout();

    public Label() {
        text = new JLabel();
        shadow = new JLabel();
        initLabel();
    }

    public Label(String text) {
        this.text = new JLabel(text);
        shadow = new JLabel(text);
        initLabel();
    }

    public Label(String text, int horizontalAlignment) {
        this.text = new JLabel(text, horizontalAlignment);
        shadow = new JLabel(text, SwingConstants.CENTER);
        initLabel();
    }

    public Label(String text, float shadowSize, int horizontalAlignment) {
        this.text = new JLabel(text, horizontalAlignment);
        shadow = new JLabel(text, horizontalAlignment);
        this.shadowSize = shadowSize;
        initLabel();
    }

    private void initLabel() {
        setOpaque(false);
        setLayout(springLayout);

        if (shadowSize < 0)
            shadow.setVisible(false);

        setOffsetShadow(0, 0);

        add(text);
        add(shadow);
    }

    public void setAlignmentX(int alignmentX) {
        switch (alignmentX) {
            case CENTER ->
                springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, text, 0,
                        SpringLayout.HORIZONTAL_CENTER, this);
            case RIGHT ->
                    springLayout.putConstraint(SpringLayout.EAST, text, 0,
                            SpringLayout.EAST, this);
            default ->
                    springLayout.putConstraint(SpringLayout.WEST, text, 0,
                            SpringLayout.WEST, this);
        }
    }

    public void setAlignmentY(int alignmentY) {
        switch (alignmentY) {
            case CENTER ->
                    springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, text, 0,
                            SpringLayout.VERTICAL_CENTER, this);
            case BOTTOM ->
                    springLayout.putConstraint(SpringLayout.SOUTH, text, 0,
                            SpringLayout.SOUTH, this);
            default ->
                    springLayout.putConstraint(SpringLayout.NORTH, text, 0,
                            SpringLayout.NORTH, this);
        }
    }

    private void setComponentSize() {
        setMinimumSize(text.getMinimumSize());
        setPreferredSize(text.getPreferredSize());
        setMinimumSize(text.getMaximumSize());
    }

    public void setFont(String fontPath) {
        Font font = loadFont(fontPath);
        if (font != null) {
            text.setFont(font);
            shadow.setFont(font);
        }
        setComponentSize();
    }

    private Font loadFont(String fontPath) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setSizeFont(float fontSize, float scale) {
        text.setFont(text.getFont().deriveFont(fontSize * scale));
        shadow.setFont(shadow.getFont().deriveFont((fontSize + shadowSize) * scale));
        setOffsetShadow(shadowOffset);
        setComponentSize();
    }

    public void setSizeFont(float fontSize) {
        setSizeFont(fontSize, 1f);
    }

    public void setShadowSize(float shadowSize) {
        if (shadowSize < 0) {
            shadow.setVisible(false);
            return;
        }
        shadow.setVisible(true);
        this.shadowSize = shadowSize;
        setSizeFont(text.getFont().getSize2D());
    }

    public void setTextColor(Color textColor) {
        text.setForeground(textColor);
    }

    public void setShadowColor(Color shadowColor) {
        shadow.setForeground(shadowColor);
    }

    public void setText(String text) {
        this.text.setText(text);
        shadow.setText(text);
        setComponentSize();
    }

    public void setOffsetShadow(int offsetX, int offsetY) {
        shadowOffset.change(new Vector2<>(offsetX, offsetY));

        springLayout.putConstraint(SpringLayout.WEST, shadow, (int) (shadowOffset.first * WindowSettings.scaleX),
                SpringLayout.WEST, text);
        springLayout.putConstraint(SpringLayout.NORTH, shadow, (int) (shadowOffset.second * WindowSettings.scaleY),
                SpringLayout.NORTH, text);

        setComponentSize();
    }

    public void setOffsetShadow(Vector2<Integer> offsetShadow) {
        setOffsetShadow(offsetShadow.first, offsetShadow.second);
    }
}
