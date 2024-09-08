package chevy.model.entity.dynamicEntity.liveEntity.enemy;

import chevy.utils.Vector2;

public final class Beetle extends Enemy {
    public Beetle(Vector2<Integer> initPosition) {
        super(initPosition, Type.BEETLE, 1.5f, .5f, .5f, .15f, .3f);
        health = 10;
        currentHealth = health;
        maxDamage = 5;
        minDamage = 3;
        drawLayer = 3;

        stateMachine.setName("Beetle");
        initStateMachine();
    }
}