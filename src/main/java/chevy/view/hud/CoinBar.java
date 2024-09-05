package chevy.view.hud;

import chevy.view.component.CounterBar;
import chevy.view.component.MyPanelUI;

import java.awt.Color;
import java.awt.Dimension;

public final class CoinBar extends CounterBar {
    private static final String COIN_BAR_PATH = "/sprites/component/label/";

    public CoinBar(int width, int height, float scale) {
        super(new Dimension(width, height), scale);
        setTexture(MyPanelUI.BAR_L, COIN_BAR_PATH + "coin/leftBar.png");
        setTexture(MyPanelUI.BAR_R, COIN_BAR_PATH + "rightBar.png");
        setTexture(MyPanelUI.CENTER, COIN_BAR_PATH + "centerPanel.png");

        setFont("superstar_2/superstar_memesbruh03");
        setColor(Color.BLACK);
        setOffsetY((int) (2 * scale));
        setFontSize((int) (8 * scale));
    }

    public void setCoin(int value) {
        setText(String.valueOf(value));
    }
}
