package chevy.view.component;

import javax.swing.*;
import java.awt.*;

public class CounterBar extends JPanel {
    private int counter = 0;
    private final JLabel text = new JLabel(String.valueOf(counter), SwingConstants.RIGHT);
    private final String[] texture = new String[9];
    private final MyPanelUI ui;
    private Component rightSpace = Box.createHorizontalStrut(0);
    private float scale;

    public CounterBar(Dimension dimension, float scale) {
        this.scale = scale;

        setOpaque(false);
        ui = new MyPanelUI(null, scale);
        setUI(ui);

        setMaximumSize(dimension);
        setPreferredSize(dimension);
        setMinimumSize(dimension);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        text.setMaximumSize(getMaximumSize());
        text.setAlignmentX(Component.RIGHT_ALIGNMENT);

        add(Box.createHorizontalGlue());
        add(text);
        add(rightSpace);
    }

    public void setTexture(int i, String path) {
        ui.setTexture(i, path);
        revalidate();
        repaint();
    }

    public void setRightSpace(int with) {
        rightSpace = Box.createHorizontalStrut(with);
        add(rightSpace, getComponentCount() - 1);
        revalidate();
        repaint();
    }
}
