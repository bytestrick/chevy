package chevy.view.entities.animated.environmet.trap;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.staticEntity.environment.traps.Trapdoor;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class TrapdoorView extends AnimatedEntityView {
    private static final String TRAPDOOR_PATH = "/sprites/traps/trapdoor/";
    private final Trapdoor trapdoor;

    public TrapdoorView(Trapdoor trapdoor) {
        this.trapdoor = trapdoor;
        this.currentViewPosition = new Vector2<>((double) trapdoor.getCol(), (double) trapdoor.getRow());

        initAnimation();
    }

    private void initAnimation() {
        createAnimation(Trapdoor.EnumState.IDLE, 0, 1, trapdoor.getState(Trapdoor.EnumState.IDLE).getDuration(),
                TRAPDOOR_PATH + "idle", ".png");

        createAnimation(Trapdoor.EnumState.OPEN, 0, 3, trapdoor.getState(Trapdoor.EnumState.OPEN).getDuration(),
                TRAPDOOR_PATH + "open", ".png");

        createAnimation(Trapdoor.EnumState.DAMAGE, 0, 1, trapdoor.getState(Trapdoor.EnumState.DAMAGE).getDuration(),
                TRAPDOOR_PATH + "damage", ".png");
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonState currentEnumState = trapdoor.getCurrentState();
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentEnumState, 0);

        if (currentAnimatedSprite != null) {
            if (!currentAnimatedSprite.isRunning()) {
                currentAnimatedSprite.start();
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