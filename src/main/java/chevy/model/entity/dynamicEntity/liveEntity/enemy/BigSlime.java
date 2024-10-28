package chevy.model.entity.dynamicEntity.liveEntity.enemy;

import java.awt.Point;

public final class BigSlime extends Enemy {
    public BigSlime(Point position) {
        super(position, Type.BIG_SLIME, .7f, .2f);

        health = 10;
        currentHealth = health;
        maxDamage = 3;
        minDamage = 2;

        stateMachine.setName("Big slime");
    }
}
