package chevy.view.hud;

import chevy.view.component.MyPanelUI;
import chevy.view.component.ProgressBarLabel;

public final class AttackBar extends ProgressBarLabel {
    private static final String PROGRESS_BAR_PATH = "/sprites/component/progressBar/";
    private static final String ATTACK_BAR_PATH = PROGRESS_BAR_PATH + "attackBar/";

    AttackBar(float scale) {
        super(0, 0, scale);
        initUI();

        setTexture(MyPanelUI.BAR_L, ATTACK_BAR_PATH + "leftBar.png");
        setTexture(MyPanelUI.BAR_R, PROGRESS_BAR_PATH + "rightBar.png");
        setTexture(MyPanelUI.CENTER, PROGRESS_BAR_PATH + "centerPanel.png");
        setStepTexture();

        setFont();
        setFontSize((int) (8 * scale));
    }
}