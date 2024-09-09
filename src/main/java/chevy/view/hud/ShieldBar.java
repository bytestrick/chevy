package chevy.view.hud;

import chevy.view.component.MyPanelUI;
import chevy.view.component.ProgressBar;

public final class ShieldBar extends ProgressBar {
    private static final String PROGRESS_BAR_PATH = "/sprites/component/progressBar/";
    private static final String SHIELD_BAR_PATH = PROGRESS_BAR_PATH + "shieldBar/";

    ShieldBar(float scale) {
        super(0, 0, scale);

        setTexture(MyPanelUI.BAR_L, SHIELD_BAR_PATH + "leftBar.png");
        setTexture(MyPanelUI.BAR_R, PROGRESS_BAR_PATH + "rightBar.png");
        setTexture(MyPanelUI.CENTER, PROGRESS_BAR_PATH + "centerPanel.png");
        setStepTexture(SHIELD_BAR_PATH + "step.png");
    }
}