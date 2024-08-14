package chevy.view.hud;


import chevy.view.component.CounterBar;
import chevy.view.component.MyPanelUI;

import java.awt.*;

public class CoinBar extends CounterBar {
    private static final String COIN_BAR_PATH = "/assets/component/label/";

    public CoinBar() {
        super(new Dimension(128 + 64, 36), 3f);
        setTexture(MyPanelUI.BAR_L, COIN_BAR_PATH + "coin/leftBar.png");
        setTexture(MyPanelUI.BAR_R, COIN_BAR_PATH + "rightBar.png");
        setTexture(MyPanelUI.CENTER, COIN_BAR_PATH + "centerPanel.png");

        setRightSpace(25);
    }

}
