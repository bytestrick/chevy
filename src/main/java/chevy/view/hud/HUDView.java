package chevy.view.hud;

import chevy.settings.WindowSettings;

import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class HUDView extends JPanel {
    private final CoinBar coinBar;
    private final KeyBar keyBar;
    private final PlayerInfo playerInfo;
    private final PowerUpText powerUpText;
    private final SpringLayout springLayout;
    private final int borderMargin = 10;
    private final int componentMargin = 5;

    public HUDView(float scale) {
        coinBar = new CoinBar(64, 12, scale);
        keyBar = new KeyBar(48, 12, scale);
        playerInfo = new PlayerInfo(scale);
        powerUpText = new PowerUpText();
        springLayout = new SpringLayout();

        setOpaque(false);
        setLayout(springLayout);
        setConstraints(WindowSettings.scale);

        add(powerUpText);
        add(playerInfo);
        add(coinBar);
        add(keyBar);

        powerUpText.setVisible(false);
    }

    private void setConstraints(float scale) {
        int borderMarginScaled = (int) (borderMargin * scale);
        int componentMarginScaled = (int) (componentMargin * scale);

        springLayout.putConstraint(SpringLayout.NORTH, coinBar, borderMarginScaled, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, coinBar, -borderMarginScaled, SpringLayout.EAST, this);

        springLayout.putConstraint(SpringLayout.NORTH, keyBar, componentMarginScaled, SpringLayout.SOUTH, coinBar);
        springLayout.putConstraint(SpringLayout.EAST, keyBar, -borderMarginScaled, SpringLayout.EAST, this);

        springLayout.putConstraint(SpringLayout.NORTH, playerInfo, borderMarginScaled, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, playerInfo, borderMarginScaled, SpringLayout.WEST, this);

        springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, powerUpText, 0, SpringLayout.HORIZONTAL_CENTER,
                this);
        springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, powerUpText, 0, SpringLayout.VERTICAL_CENTER, this);
    }

    public void windowResized(float scale) {
        coinBar.windowResized(scale);
        keyBar.windowResized(scale);
        playerInfo.windowResized(scale);
        powerUpText.windowResized(scale);
        setConstraints(scale);
    }

    public PowerUpText getPowerUpText() {
        return powerUpText;
    }

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public CoinBar getCoinBar() {
        return coinBar;
    }

    public KeyBar getKeyBar() {
        return keyBar;
    }
}
