package chevy.view.entities.animated.trap;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.staticEntity.environment.traps.IcyFloor;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class IcyFloorView extends AnimatedEntityView {
    private static final String ICY_FLOOR_PATH = "/assets/traps/icyFloor/";
    private final IcyFloor icyFloor;

    public IcyFloorView(IcyFloor icyFloor) {
        this.icyFloor = icyFloor;
        this.currentViewPosition = new Vector2<>( (double) icyFloor.getCol(), (double) icyFloor.getRow());

        initAnimation();
    }

    private void initAnimation() {
        createAnimation(IcyFloor.EnumState.ICY_FLOOR, 0,
                1, icyFloor.getState(IcyFloor.EnumState.ICY_FLOOR).getDuration(),
                ICY_FLOOR_PATH + "base", ".png");

        createAnimation(IcyFloor.EnumState.ICY_FLOOR_SPARKLING, 0,
                8, icyFloor.getState(IcyFloor.EnumState.ICY_FLOOR_SPARKLING).getDuration(),
                ICY_FLOOR_PATH + "sparkling", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonState currentState = icyFloor.getCurrentState();
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, 0);

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