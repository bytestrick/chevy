package chevy.model.entity.dynamicEntity.liveEntity.enemy;

import java.awt.Point;

public final class Beetle extends Enemy {
    public Beetle(Point position) {
        super(position, Type.BEETLE, 1.5f, .15f);
        health = 10;
        currentHealth = health;
        maxDamage = 5;
        minDamage = 3;
        stateMachine.setName("Beetle");
    }
}