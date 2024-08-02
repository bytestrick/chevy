package chevy.view.entityView.entityViewAnimated.trap;

import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.staticEntity.environment.traps.Totem;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.image.BufferedImage;

public class TotemView extends EntityViewAnimated {
    private static final String TOTEM_PATH = "/assets/traps/totem/";
    private final Totem totem;


    public TotemView(Totem totem) {
        this.totem = totem;
        this.currentViewPosition = new Vector2<>((double) totem.getCol(), (double) totem.getRow());

        initAnimation();
    }

    private void initAnimation() {
        Vector2<Integer> offset = new Vector2<>(0, -6);
        float durationShot = totem.getState(Totem.EnumState.SHOT).getDuration();
        float durationReload = totem.getState(Totem.EnumState.RELOAD).getDuration();

        createAnimation(Totem.EnumState.SHOT, 0,
                1, durationShot,
                offset, 1,
                TOTEM_PATH + "up", ".png");

        createAnimation(Totem.EnumState.SHOT, 1,
                1, durationShot,
                offset, 1,
                TOTEM_PATH + "down", ".png");

        createAnimation(Totem.EnumState.SHOT, 2,
                1, durationShot,
                offset, 1,
                TOTEM_PATH + "right", ".png");

        createAnimation(Totem.EnumState.SHOT, 3,
                1, durationShot,
                offset, 1,
                TOTEM_PATH + "left", ".png");

        // RELOAD
        createAnimation(Totem.EnumState.RELOAD, 0,
                1, durationReload,
                offset, 1,
                TOTEM_PATH + "up", ".png");

        createAnimation(Totem.EnumState.RELOAD, 1,
                1, durationReload,
                offset, 1,
                TOTEM_PATH + "down", ".png");

        createAnimation(Totem.EnumState.RELOAD, 2,
                1, durationReload,
                offset, 1,
                TOTEM_PATH + "right", ".png");

        createAnimation(Totem.EnumState.RELOAD, 3,
                1, durationReload,
                offset, 1,
                TOTEM_PATH + "left", ".png");
    }


    private int getAnimationType(CommonEnumStates currentState) {
        DirectionsModel currentDirection = totem.getDirectionShot();
        return switch (currentState) {
            case Totem.EnumState.SHOT, Totem.EnumState.RELOAD ->
                    switch (currentDirection) {
                        case UP -> 0;
                        case DOWN -> 1;
                        case RIGHT -> 2;
                        case LEFT -> 3;
                    };
            default -> 0;
        };
    }

    public BufferedImage getCurrentFrame() {
        CommonEnumStates currentEnumState = totem.getCurrentEumState();
        int type = getAnimationType(currentEnumState);
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentEnumState, type);
        return currentAnimatedSprite.getCurrentFrame();
    }

    public Vector2<Integer> getOffset() {
        CommonEnumStates currentState = totem.getCurrentEumState();
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
