package chevy.model.entity.dynamicEntity.liveEntity.enemy;

import chevy.model.entity.stateMachine.Vertex;

import java.awt.Point;

public final class Zombie extends Enemy {
    public Zombie(Point position) {
        super(position, Type.ZOMBIE, 1f, .15f);
        idle = new Vertex(State.IDLE, 1f, true);

        health = 8;
        currentHealth = health;
        maxDamage = 3;
        minDamage = 2;

        stateMachine.setName("Zombie");
    }
}
