package chevy.view.entities.animated.trap;

import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.staticEntity.environment.traps.Sludge;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class SludgeView extends AnimatedEntityView {
    private static final String SLUDGE_FLOOR_PATH = "/assets/traps/sludge/";
    private final Vector2<Double> position;
    private final Sludge sludge;

    public SludgeView(Sludge sludge) {
        this.sludge = sludge;
        position = new Vector2<>((double) sludge.getCol(), (double) sludge.getRow());

        initAnimation();
    }

    private void initAnimation() {
        createAnimation(Sludge.EnumState.SLUDGE_BUBBLES, 0, 4,
                sludge.getState(Sludge.EnumState.SLUDGE_BUBBLES).getDuration(), SLUDGE_FLOOR_PATH, ".png");
    }

    public Vector2<Integer> getOffset() {
        CommonEnumStates currentState = sludge.getCurrentEumState();
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, 0);
        return currentAnimatedSprite.getOffset();
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonEnumStates currentEnumState = sludge.getCurrentEumState();
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentEnumState, 0);

        if (currentAnimatedSprite != null) {
            if (!currentAnimatedSprite.isRunning()) {
                currentAnimatedSprite.restart();
            }
            return currentAnimatedSprite.getCurrentFrame();
        }
        return null;
    }

    @Override
    public Vector2<Double> getCurrentPosition() {
        return position;
    }

    @Override
    public void wasRemoved() {
        deleteAnimations();
    }
}