package chevy.model.entity.dynamicEntity.liveEntity.enemy;

import chevy.utils.Vector2;

public final class Wraith extends Enemy {
    public Wraith(Vector2<Integer> initPosition) {
        super(initPosition, Type.WRAITH, .8f, .5f, .5f, .15f, .3f);
        flying = true;
        health = 3;
        currentHealth = health;
        maxDamage = 2;
        minDamage = 1;
        stateMachine.setName("Wraith");
        initStateMachine();
    }
}