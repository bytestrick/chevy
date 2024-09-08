package chevy.model.entity.dynamicEntity.liveEntity.enemy;

import chevy.utils.Vector2;

public final class BigSlime extends Enemy {
    public BigSlime(Vector2<Integer> initPosition) {
        super(initPosition, Type.BIG_SLIME, .7f, .5f, .5f, .2f, .3f);

        health = 10;
        currentHealth = health;
        maxDamage = 3;
        minDamage = 2;

        stateMachine.setName("Big slime");
        initStateMachine();
    }
}