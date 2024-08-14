package chevy.view.component;


import chevy.utils.Image;

import javax.swing.*;
import javax.swing.plaf.PanelUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.ParameterizedType;

public class ProgressBar extends JPanel {
    private BufferedImage image;
    private int maxValue;
    private int value;
    private float scale;
    private JPanel containerImage = new JPanel();
    private MyPanelUI ui;
    private SpringLayout springLayout = new SpringLayout();

    public  ProgressBar(int maxValue) {
        this(maxValue, maxValue, 1f);
    }

    public ProgressBar(int maxValue, float scale) {
        this(maxValue, maxValue, scale);
    }

    public ProgressBar(int value, int maxValue, float scale) {
        this.value = value;
        this.maxValue = maxValue;
        this.scale = scale;

        setOpaque(false);
        containerImage.setOpaque(false);
        ui = new MyPanelUI(null, scale);
        setUI(ui);

        setLayout(springLayout);
        containerImage.setLayout(new BoxLayout(containerImage, BoxLayout.X_AXIS));

        setDimension();
        add(containerImage);
    }

    private void setConstraints() {
        springLayout.putConstraint(SpringLayout.NORTH, containerImage, ui.getHeight(MyPanelUI.BAR_T), SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.SOUTH, containerImage, ui.getHeight(MyPanelUI.BAR_B), SpringLayout.SOUTH, this);
        springLayout.putConstraint(SpringLayout.WEST, containerImage, ui.getWidth(MyPanelUI.BAR_L), SpringLayout.WEST, this);
    }

    private void setDimension() {
        int width = 0;
        int height = ui.getHeight(MyPanelUI.BAR_T) + ui.getHeight(MyPanelUI.BAR_B) + ui.getHeight(MyPanelUI.CENTER);;

        if (image == null)
            width = ui.getWidth(MyPanelUI.BAR_L) + ui.getWidth(MyPanelUI.BAR_R);
        else
            width = (int) (maxValue * image.getWidth() * scale) + ui.getWidth(MyPanelUI.BAR_L) + ui.getWidth(MyPanelUI.BAR_R);

        Dimension dimension = new Dimension(width, height);
        setMaximumSize(dimension);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setConstraints();

        revalidate();
        repaint();
    }

    public void setValue(int value) {
        int increment = value - this.value;
        int step = Math.abs(increment);
        if (increment > 0)
            for (int i = 1; i <= step; ++i)
                containerImage.getComponent(this.value + i).setVisible(true);
        else
            for (int i = 1; i <= step; ++i)
                containerImage.getComponent(this.value - i).setVisible(false);
        this.value = value;
    }

    public void setTexture(int i, String path) {
        ui.setTexture(i, path);
        setDimension();
    }

    public void setStepTexture(String path) {
        image = Image.load(path);
        int n = containerImage.getComponentCount();

        for (int i = 0; i < maxValue; ++i) {
            if (i < n)
                containerImage.remove(i);
            containerImage.add(new ImageVisualizer(image, scale), i);
        }

        setDimension();
    }
}

