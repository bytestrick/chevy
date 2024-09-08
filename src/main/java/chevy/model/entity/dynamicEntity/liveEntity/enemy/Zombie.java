package chevy.model.entity.dynamicEntity.liveEntity.enemy;

import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Vector2;

public final class Zombie extends Enemy {
    public Zombie(Vector2<Integer> initPosition) {
        super(initPosition, Type.ZOMBIE, 1f, .5f, .5f, .15f, .3f);
        idle = new Vertex(State.IDLE, 1f, true);

        health = 8;
        currentHealth = health;
        maxDamage = 3;
        minDamage = 2;

        stateMachine.setName("Zombie");
        initStateMachine();
    }
}