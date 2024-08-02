package chevy.view.entityView.entityViewAnimated.trap;

import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.staticEntity.environment.traps.SpikedFloor;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.image.BufferedImage;

public class SpikedFloorView extends EntityViewAnimated {
    private static final String SPIKED_FLOOR_PATH = "/assets/traps/spikedFloor/";
    private final SpikedFloor spikedFloor;


    public SpikedFloorView(SpikedFloor spikedFloor) {
        this.spikedFloor = spikedFloor;
        this.currentViewPosition = new Vector2<>(
                (double) spikedFloor.getCol(),
                (double) spikedFloor.getRow()
        );

        initAnimation();
    }


    private void initAnimation() {
        Vector2<Integer> offset = new Vector2<>(0, -7);
        createAnimation(SpikedFloor.EnumState.DISABLED, 0,
                1, spikedFloor.getState(SpikedFloor.EnumState.DISABLED).getDuration(),
                offset, 1,
                SPIKED_FLOOR_PATH + "disabled", ".png");

        createAnimation(SpikedFloor.EnumState.ACTIVATED, 0,
                2, spikedFloor.getState(SpikedFloor.EnumState.ACTIVATED).getDuration(),
                offset, 1,
                SPIKED_FLOOR_PATH + "active", ".png");

        createAnimation(SpikedFloor.EnumState.DAMAGE, 0,
                1, spikedFloor.getState(SpikedFloor.EnumState.DAMAGE).getDuration(),
                offset, 1,
                SPIKED_FLOOR_PATH + "damage", ".png");
    }

    public Vector2<Integer> getOffset() {
        CommonEnumStates currentState = spikedFloor.getCurrentEumState();
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, 0);
        return currentAnimatedSprite.getOffset();
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonEnumStates currentEnumState = spikedFloor.getCurrentEumState();
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
