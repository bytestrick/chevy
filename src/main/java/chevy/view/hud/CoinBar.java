package chevy.view.hud;

import chevy.view.component.CounterBar;
import chevy.view.component.MyPanelUI;

import java.awt.Dimension;

public final class CoinBar extends CounterBar {
    private static final String COIN_BAR_PATH = "/sprites/component/label/";

    CoinBar(float scale) {
        super(new Dimension(64, 12), scale);
        setTexture(MyPanelUI.BAR_L, COIN_BAR_PATH + "coin/leftBar.png");
        setTexture(MyPanelUI.BAR_R, COIN_BAR_PATH + "rightBar.png");
        setTexture(MyPanelUI.CENTER, COIN_BAR_PATH + "centerPanel.png");

        setFont();
        setColor();
        setOffsetY((int) (2 * scale));
        setFontSize((int) (8 * scale));
    }

    public void setCoin(int value) {setText(String.valueOf(value));}
}
