package chevy.view.hud;

import javax.swing.*;

public class HUD extends JPanel {
    private final CoinBar coinBar = new CoinBar();
    private final KeyBar keyBar = new KeyBar();
    private final PlayerInfo playerInfo = new PlayerInfo();
    private final TextPowerUp textPowerUp = new TextPowerUp();

    public HUD() {
        setOpaque(false);
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        int borderMargin = 10;
        int componentMargin = 5;

        springLayout.putConstraint(SpringLayout.NORTH, coinBar, borderMargin, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, coinBar, -borderMargin, SpringLayout.EAST, this);

        springLayout.putConstraint(SpringLayout.NORTH, keyBar, componentMargin, SpringLayout.SOUTH, coinBar);
        springLayout.putConstraint(SpringLayout.EAST, keyBar, -borderMargin, SpringLayout.EAST, this);

        springLayout.putConstraint(SpringLayout.NORTH, playerInfo, borderMargin, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.WEST, playerInfo, borderMargin, SpringLayout.WEST, this);

        springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, textPowerUp, 0, SpringLayout.HORIZONTAL_CENTER, this);
        springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, textPowerUp, 0, SpringLayout.VERTICAL_CENTER, this);

        add(textPowerUp);
        add(playerInfo);
        add(coinBar);
        add(keyBar);

        textPowerUp.setVisible(false);
    }
}
