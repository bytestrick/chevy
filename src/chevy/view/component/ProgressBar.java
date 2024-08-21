package chevy.view.component;


import chevy.settings.WindowSettings;
import chevy.utils.Image;
import chevy.utils.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ProgressBar extends JPanel {
    private BufferedImage stepTexture;
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
        this.scale = scale;

        setOpaque(false);
        containerImage.setOpaque(false);
        ui = new MyPanelUI(null, scale * WindowSettings.scale);
        setUI(ui);

        setLayout(springLayout);
        containerImage.setLayout(new BoxLayout(containerImage, BoxLayout.X_AXIS));

        setMaxValue(maxValue);
        add(containerImage);
    }

    public void setMaxValue(int maxValue) {
        setMaxValue(maxValue, maxValue);
    }

    public void setMaxValue(int value, int maxValue) {
        if (maxValue <= 0) {
            return;
        }

        this.maxValue = maxValue;
        this.value = value;

        addStep();
        setDimension(scale * WindowSettings.scale);
    }

    private void setConstraints() {
        springLayout.putConstraint(SpringLayout.NORTH, containerImage, ui.getHeight(MyPanelUI.BAR_T), SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.SOUTH, containerImage, ui.getHeight(MyPanelUI.BAR_B), SpringLayout.SOUTH, this);
        springLayout.putConstraint(SpringLayout.WEST, containerImage, ui.getWidth(MyPanelUI.BAR_L), SpringLayout.WEST, this);
    }

    private void setDimension(float scale) {
        int width = 0;
        int height = ui.getHeight(MyPanelUI.BAR_T) + ui.getHeight(MyPanelUI.BAR_B) + ui.getHeight(MyPanelUI.CENTER);

        if (stepTexture == null) {
            width = ui.getWidth(MyPanelUI.BAR_L) + ui.getWidth(MyPanelUI.BAR_R);
        } else {
            width = (int) (maxValue * stepTexture.getWidth() * scale) + ui.getWidth(MyPanelUI.BAR_L) + ui.getWidth(MyPanelUI.BAR_R);
        }

        Dimension newDimension = new Dimension(width, height);
        setMaximumSize(newDimension);
        setPreferredSize(newDimension);
        setMinimumSize(newDimension);
        setConstraints();

        revalidate();
        repaint();
    }

    public void setValue(int value) {
        int increment = value - this.value;
        if (increment > containerImage.getComponentCount()) {
            return;
        }

        SwingUtilities.invokeLater(() -> {
            if (increment > 0) {
                for (int i = 0; i < increment; ++i) {
                    containerImage.getComponent(this.value + i).setVisible(true);
                }
            } else {
                for (int i = increment; i < 0; ++i) {
                    containerImage.getComponent(this.value + i).setVisible(false);
                }
            }
            this.value = value;
        });
    }

    public void setTexture(int i, String path) {
        ui.setTexture(i, path);
        setDimension(scale * WindowSettings.scale);
    }

    public void setStepTexture(String path) {
        stepTexture = Image.load(path);
    }

    private void addStep() {
        if (stepTexture == null) {
            Log.warn("Non è presente nessuna texture per lo step");
            return;
        }

        int nComponents = containerImage.getComponentCount();
        for (int i = 0; i < maxValue; ++i) {
            if (i < nComponents) {
                containerImage.remove(i);
            }
            containerImage.add(new ImageVisualizer(stepTexture, scale), i);
            if (i >= value) {
                containerImage.getComponent(i).setVisible(false);
            }
        }

        setDimension(scale * WindowSettings.scale);
    }

    public int getValue() {
        return value;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void windowResized(float scale) {
        float scale2 = scale;
        scale *= this.scale;

        ui.setScale(scale);
        for (int i = 0; i < containerImage.getComponentCount(); ++i) {
            ImageVisualizer im = (ImageVisualizer) containerImage.getComponent(i);
            im.windowResized(scale2); // lo scale del componente è già applicato all'inserimento (riga 114)
        }
        setDimension(scale);
    }
}

