package chevy.view.entities.animated.environmet.trap;

import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.staticEntity.environment.traps.Totem;
import chevy.model.entity.staticEntity.environment.traps.Totem.State;

import java.awt.Point;
import java.awt.image.BufferedImage;

public final class TotemView extends TrapView {
    public TotemView(Totem totem) {
        super(totem);

        final String res = "/sprites/traps/totem/";
        final Point offset = new Point(0, -6);
        final float shotDuration = trap.getState(State.SHOT).getDuration();
        final float reloadDuration = trap.getState(State.RELOAD).getDuration();
        for (Direction direction : Direction.values()) {
            final String path = res + direction.toString().toLowerCase();
            animate(State.SHOT, direction, 1, shotDuration, offset, path);
            animate(State.RELOAD, direction, 1, reloadDuration, offset, path);
        }
    }

    private Direction getAnimationDirection(EntityState currentState) {
        return switch (currentState) {
            case State.SHOT, State.RELOAD -> trap.getShotDirection();
            default -> null;
        };
    }

    @Override
    public BufferedImage getFrame() {
        final EntityState state = trap.getState();
        return getAnimatedSprite(state, getAnimationDirection(state)).getFrame();
    }

    @Override
    public Point getOffset() {
        final EntityState state = trap.getState();
        return getAnimatedSprite(state, getAnimationDirection(state)).getOffset();
    }
}
