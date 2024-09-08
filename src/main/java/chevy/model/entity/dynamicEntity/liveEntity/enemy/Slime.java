package chevy.model.entity.dynamicEntity.liveEntity.enemy;

import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Vector2;

public final class Slime extends Enemy {
    public Slime(Vector2<Integer> initPosition) {
        super(initPosition, Type.SLIME, 1f, .5f, .5f, .15f, .3f);
        idle = new Vertex(State.IDLE, 1f, true);

        health = 3;
        currentHealth = health;
        maxDamage = 2;
        minDamage = 1;

        stateMachine.setName("Slime");
        initStateMachine();
    }
}