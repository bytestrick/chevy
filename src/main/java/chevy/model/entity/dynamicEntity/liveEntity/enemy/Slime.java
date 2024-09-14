package chevy.model.entity.dynamicEntity.liveEntity.enemy;

import chevy.model.entity.stateMachine.Vertex;

import java.awt.Point;

public final class Slime extends Enemy {
    public Slime(Point position) {
        super(position, Type.SLIME, 1f, .15f);
        idle = new Vertex(State.IDLE, 1f, true);

        health = 3;
        currentHealth = health;
        maxDamage = 2;
        minDamage = 1;

        stateMachine.setName("Slime");
    }
}