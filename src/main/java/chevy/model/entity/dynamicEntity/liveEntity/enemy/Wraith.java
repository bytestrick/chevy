package chevy.model.entity.dynamicEntity.liveEntity.enemy;

import java.awt.Point;

public final class Wraith extends Enemy {
    public Wraith(Point position) {
        super(position, Type.WRAITH, .8f, .15f);
        health = 3;
        currentHealth = health;
        maxDamage = 2;
        minDamage = 1;
        stateMachine.setName("Wraith");
    }
}