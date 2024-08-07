package chevy.view.entities.animated.trap;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.staticEntity.environment.traps.SpikedFloor;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class SpikedFloorView extends AnimatedEntityView {
    private static final String SPIKED_FLOOR_PATH = "/assets/traps/spikedFloor/";
    private final SpikedFloor spikedFloor;

    public SpikedFloorView(SpikedFloor spikedFloor) {
        this.spikedFloor = spikedFloor;
        this.currentViewPosition = new Vector2<>((double) spikedFloor.getCol(), (double) spikedFloor.getRow());

        initAnimation();
    }

    private void initAnimation() {
        Vector2<Integer> offset = new Vector2<>(0, -7);
        createAnimation(SpikedFloor.EnumState.DISABLED, 0, 1,
                spikedFloor.getState(SpikedFloor.EnumState.DISABLED).getDuration(), offset, 1, SPIKED_FLOOR_PATH +
                        "disabled", ".png");

        createAnimation(SpikedFloor.EnumState.ACTIVATED, 0, 2,
                spikedFloor.getState(SpikedFloor.EnumState.ACTIVATED).getDuration(), offset, 1, SPIKED_FLOOR_PATH +
                        "active", ".png");

        createAnimation(SpikedFloor.EnumState.DAMAGE, 0, 1,
                spikedFloor.getState(SpikedFloor.EnumState.DAMAGE).getDuration(), offset, 1, SPIKED_FLOOR_PATH +
                        "damage", ".png");
    }

    public Vector2<Integer> getOffset() {
        CommonState currentState = spikedFloor.getCurrentState();
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, 0);
        return currentAnimatedSprite.getOffset();
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonState currentEnumState = spikedFloor.getCurrentState();
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
    public Vector2<Double> getCurrentViewPosition() {
        return currentViewPosition;
    }

    @Override
    public void wasRemoved() {
        super.deleteAnimations();
    }
}