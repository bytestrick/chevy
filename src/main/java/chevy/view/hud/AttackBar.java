package chevy.view.hud;

import chevy.view.component.MyPanelUI;
import chevy.view.component.ProgressBarLabel;

public final class AttackBar extends ProgressBarLabel {
    private static final String PROGRESS_BAR_PATH = "/sprites/component/progressBar/";
    private static final String ATTACK_BAR_PATH = PROGRESS_BAR_PATH + "attackBar/";

    public AttackBar(int maxValue) {
        this(maxValue, maxValue, 1f);
    }

    public AttackBar(int maxValue, float scale) {
        this(maxValue, maxValue, scale);
    }

    public AttackBar(int value, int maxValue, float scale) {
        super(value, maxValue, scale);

        setTexture(MyPanelUI.BAR_L, ATTACK_BAR_PATH + "leftBar.png");
        setTexture(MyPanelUI.BAR_R, PROGRESS_BAR_PATH + "rightBar.png");
        setTexture(MyPanelUI.CENTER, PROGRESS_BAR_PATH + "centerPanel.png");
        setStepTexture(ATTACK_BAR_PATH + "step.png");

        setFont("superstar_2/superstar_memesbruh03");
        setFontSize((int) (8 * scale));
    }
}
