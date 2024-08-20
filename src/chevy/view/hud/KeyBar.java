package chevy.view.hud;


import chevy.view.component.CounterBar;
import chevy.view.component.MyPanelUI;

import java.awt.*;

public class KeyBar extends CounterBar {
    private static final String KEY_BAR_PATH = "/assets/component/label/";

    public KeyBar(int width, int height, float scale) {
        super(new Dimension((int) (width * scale), (int)(height * scale)), scale);
        setTexture(MyPanelUI.BAR_L, KEY_BAR_PATH + "key/leftBar.png");
        setTexture(MyPanelUI.BAR_R, KEY_BAR_PATH + "rightBar.png");
        setTexture(MyPanelUI.CENTER, KEY_BAR_PATH + "centerPanel.png");
        setFont("fonts/upheaval/upheavtt.ttf");
        setFontSize(8);
    }

    public void setKey(int value) {
        setText(String.valueOf(value));
    }
}
