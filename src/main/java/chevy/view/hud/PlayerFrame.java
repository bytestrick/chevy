package chevy.view.hud;

import chevy.view.component.MyPanelUI;

import javax.swing.JPanel;
import java.awt.Dimension;

public final class PlayerFrame extends JPanel {
    private static final String PANEL_PATH = "/sprites/component/panel/";
    private final MyPanelUI ui;
    private final float scale;
    private final Dimension dimension;

    public PlayerFrame(Dimension dimension, float scale) {
        setOpaque(false);
        this.scale = scale;
        this.dimension = new Dimension((int) (dimension.getWidth()), (int) (dimension.getHeight()));
        ui = new MyPanelUI(null);

        ui.setTexture(MyPanelUI.CENTER, PANEL_PATH + "centerPanel.png");
        ui.setTexture(MyPanelUI.CORNER_TL, PANEL_PATH + "topLeftCorner.png");
        ui.setTexture(MyPanelUI.CORNER_TR, PANEL_PATH + "topRightCorner.png");
        ui.setTexture(MyPanelUI.CORNER_BL, PANEL_PATH + "bottomLeftCorner.png");
        ui.setTexture(MyPanelUI.CORNER_BR, PANEL_PATH + "bottomRightCorner.png");
        ui.setTexture(MyPanelUI.BAR_T, PANEL_PATH + "topBar.png");
        ui.setTexture(MyPanelUI.BAR_L, PANEL_PATH + "leftBar.png");
        ui.setTexture(MyPanelUI.BAR_B, PANEL_PATH + "bottomBar.png");
        ui.setTexture(MyPanelUI.BAR_R, PANEL_PATH + "rightBar.png");

        setUI(ui);
        setDimension(scale);
    }

    private void setDimension(float scale) {
        Dimension scaledDimension = new Dimension((int) (dimension.getWidth() * scale),
                (int) (dimension.getHeight() * scale));
        this.setMaximumSize(scaledDimension);
        this.setPreferredSize(scaledDimension);
        this.setMinimumSize(scaledDimension);
        revalidate();
        repaint();
    }

    public void windowResized(float scale) {
        scale = this.scale * scale;

        ui.setScale(scale);
        setDimension(scale);
    }
}
