package chevy.view.entities.animated.environmet;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.staticEntity.environment.Chest;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class ChestView extends AnimatedEntityView {
    private static final String CHEST_RESOURCES = "/sprites/chest/";
    private final Chest chest;
    private CommonState previousAnimationState;

    public ChestView(Chest chest) {
        this.chest = chest;
        currentViewPosition = new Vector2<>((double) chest.getCol(), (double) chest.getRow());
        initAnimation();
    }

    private void initAnimation() {
        float idleDuration = 0f;
        createAnimation(Chest.State.IDLE_LOCKED, 0, 1, idleDuration, CHEST_RESOURCES + "idle/locked", ".png");

        createAnimation(Chest.State.IDLE_UNLOCKED, 0, 1, idleDuration, CHEST_RESOURCES + "idle/unlocked", ".png");

        createAnimation(Chest.State.CLOSE, 0, 5, chest.getState(Chest.State.CLOSE).getDuration(), CHEST_RESOURCES +
                "close", ".png");

        createAnimation(Chest.State.OPEN, 0, 6, chest.getState(Chest.State.OPEN).getDuration(), CHEST_RESOURCES +
                "open", ".png");

        createAnimation(Chest.State.UNLOCK, 0, 3, chest.getState(Chest.State.UNLOCK).getDuration(),
                CHEST_RESOURCES + "unlock", ".png");
    }

    @Override
    public Vector2<Double> getCurrentViewPosition() {
        return currentViewPosition;
    }

    @Override
    public BufferedImage getCurrentFrame() {
        CommonState currentState = chest.getCurrentState();
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, 0);

        if (currentAnimatedSprite != null) {
            if (!currentAnimatedSprite.isRunning()) {
                if (previousAnimationState != Chest.State.OPEN) {
                    currentAnimatedSprite.restart();
                } else if (currentState != Chest.State.OPEN) {
                    currentAnimatedSprite.restart();
                }
            }
            previousAnimationState = currentState;
            return currentAnimatedSprite.getCurrentFrame();
        }
        return null;
    }
}
