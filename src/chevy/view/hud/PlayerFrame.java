package chevy.view.hud;


import chevy.view.component.MyPanelUI;

import javax.swing.*;
import java.awt.*;

public class PlayerFrame extends JPanel {
    private static final String PANEL_PATH = "/assets/component/panel/";
    public PlayerFrame(Dimension dimension) {
        setOpaque(false);

        String[] texture = new String[9];
        texture[MyPanelUI.CENTER] = PANEL_PATH + "centerPanel.png";
        texture[MyPanelUI.CORNER_TL] = PANEL_PATH + "topLeftCorner.png";
        texture[MyPanelUI.CORNER_TR] = PANEL_PATH + "topRightCorner.png";
        texture[MyPanelUI.CORNER_BL] = PANEL_PATH + "bottomLeftCorner.png";
        texture[MyPanelUI.CORNER_BR] = PANEL_PATH + "bottomRightCorner.png";
        texture[MyPanelUI.BAR_T] = PANEL_PATH + "topBar.png";
        texture[MyPanelUI.BAR_L] = PANEL_PATH + "leftBar.png";
        texture[MyPanelUI.BAR_B] = PANEL_PATH + "bottomBar.png";
        texture[MyPanelUI.BAR_R] = PANEL_PATH + "rightBar.png";

        MyPanelUI UI = new MyPanelUI(texture, 3f);
        setUI(UI);

        this.setMaximumSize(dimension);
        this.setPreferredSize(dimension);
        this.setMinimumSize(dimension);
    }
}
