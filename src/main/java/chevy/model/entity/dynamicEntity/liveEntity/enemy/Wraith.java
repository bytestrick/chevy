package chevy.model.entity.dynamicEntity.liveEntity.enemy;

import chevy.utils.Vector2;

public final class Wraith extends Enemy {
    public Wraith(Vector2<Integer> initPosition) {
        super(initPosition, Type.WRAITH, .8f, .15f);
        health = 3;
        currentHealth = health;
        maxDamage = 2;
        minDamage = 1;
        stateMachine.setName("Wraith");
    }
}