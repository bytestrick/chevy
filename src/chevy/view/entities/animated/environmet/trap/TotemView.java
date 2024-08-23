package chevy.view.entities.animated.environmet.trap;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.staticEntity.environment.traps.Totem;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.image.BufferedImage;

public class TotemView extends AnimatedEntityView {
    private static final String TOTEM_PATH = "/assets/traps/totem/";
    private final Totem totem;

    public TotemView(Totem totem) {
        this.totem = totem;
        this.currentViewPosition = new Vector2<>((double) totem.getCol(), (double) totem.getRow());

        initAnimation();
    }

    private void initAnimation() {
        Vector2<Integer> offset = new Vector2<>(0, -6);

        float shotDuration = totem.getState(Totem.EnumState.SHOT).getDuration();
        createAnimation(Totem.EnumState.SHOT, 0, 1, shotDuration, offset, 1, TOTEM_PATH + "up", ".png");
        createAnimation(Totem.EnumState.SHOT, 1, 1, shotDuration, offset, 1, TOTEM_PATH + "down", ".png");
        createAnimation(Totem.EnumState.SHOT, 2, 1, shotDuration, offset, 1, TOTEM_PATH + "right", ".png");
        createAnimation(Totem.EnumState.SHOT, 3, 1, shotDuration, offset, 1, TOTEM_PATH + "left", ".png");

        // Reload
        float reloadDuration = totem.getState(Totem.EnumState.RELOAD).getDuration();
        createAnimation(Totem.EnumState.RELOAD, 0, 1, reloadDuration, offset, 1, TOTEM_PATH + "up", ".png");
        createAnimation(Totem.EnumState.RELOAD, 1, 1, reloadDuration, offset, 1, TOTEM_PATH + "down", ".png");
        createAnimation(Totem.EnumState.RELOAD, 2, 1, reloadDuration, offset, 1, TOTEM_PATH + "right", ".png");
        createAnimation(Totem.EnumState.RELOAD, 3, 1, reloadDuration, offset, 1, TOTEM_PATH + "left", ".png");
    }

    private int getAnimationType(CommonState currentState) {
        DirectionsModel currentDirection = totem.getDirectionShot();
        return switch (currentState) {
            case Totem.EnumState.SHOT, Totem.EnumState.RELOAD -> switch (currentDirection) {
                case UP -> 0;
                case DOWN -> 1;
                case RIGHT -> 2;
                case LEFT -> 3;
            };
            default -> 0;
        };
    }

    public BufferedImage getCurrentFrame() {
        CommonState commonState = totem.getCurrentState();
        int type = getAnimationType(commonState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(commonState, type);
        return currentAnimatedSprite.getCurrentFrame();
    }

    public Vector2<Integer> getOffset() {
        CommonState currentState = totem.getCurrentState();
        int type = getAnimationType(currentState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, type);
        return currentAnimatedSprite.getOffset();
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