package chevy.view.component;

import chevy.utils.Load;
import chevy.utils.Log;
import chevy.view.Window;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

public class ProgressBar extends JPanel {
    private final float scale;
    private final JPanel containerImage = new JPanel();
    private final MyPanelUI ui = new MyPanelUI();
    private final SpringLayout springLayout = new SpringLayout();
    private BufferedImage stepTexture;
    private int maxValue;
    private int value;

    public ProgressBar(int value, int maxValue, float scale) {
        this.value = value;
        this.scale = scale;
        this.maxValue = maxValue;

        containerImage.setLayout(new BoxLayout(containerImage, BoxLayout.X_AXIS));
        containerImage.setOpaque(false);
    }

    protected void initUI() {
        setUI(ui);
        setOpaque(false);
        setLayout(springLayout);
        setMaxValue(maxValue);
        add(containerImage);
    }

    public void setMaxValue(int value, int maxValue) {
        if (maxValue <= 0) {
            return;
        }

        this.maxValue = maxValue;
        this.value = value;

        addStep();
        setDimension(scale * Window.scale);
    }

    private void setConstraints() {
        springLayout.putConstraint(SpringLayout.NORTH, containerImage,
                ui.getHeight(MyPanelUI.BAR_T), SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.SOUTH, containerImage,
                ui.getHeight(MyPanelUI.BAR_B), SpringLayout.SOUTH, this);
        springLayout.putConstraint(SpringLayout.WEST, containerImage,
                ui.getWidth(MyPanelUI.BAR_L), SpringLayout.WEST, this);
    }

    private void setDimension(float scale) {
        int width;
        int height =
                ui.getHeight(MyPanelUI.BAR_T) + ui.getHeight(MyPanelUI.BAR_B) + ui.getHeight(MyPanelUI.CENTER);

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

    public void setTexture(int i, String path) {
        ui.setTexture(i, path);
        setDimension(scale * Window.scale);
    }

    protected void setStepTexture(String path) {stepTexture = Load.image(path);}

    private void addStep() {
        if (stepTexture == null) {
            Log.error("Non è presente nessuna texture per lo step");
            return;
        }

        int nComponents = containerImage.getComponentCount();
        for (int i = 0; i < maxValue; ++i) {
            if (i < nComponents) {
                containerImage.remove(i);
            }
            containerImage.add(new ImageVisualizer(stepTexture, scale * Window.scale), i);
            if (i >= value) {
                containerImage.getComponent(i).setVisible(false);
            }
        }

        setDimension(scale * Window.scale);
    }

    public int getValue() {return value;}

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

    public int getMaxValue() {return maxValue;}

    void setMaxValue(int maxValue) {setMaxValue(maxValue, maxValue);}

    public void windowResized(float scale) {
        scale *= this.scale;
        ui.setScale(scale);
        final int componentCount = containerImage.getComponentCount();
        for (int i = 0; i < componentCount; ++i) {
            ImageVisualizer im = (ImageVisualizer) containerImage.getComponent(i);
            im.windowResized(scale);
        }
        setDimension(scale);
    }
}
