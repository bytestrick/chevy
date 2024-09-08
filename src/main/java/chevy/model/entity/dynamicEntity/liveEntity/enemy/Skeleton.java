package chevy.model.entity.dynamicEntity.liveEntity.enemy;

import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Log;
import chevy.utils.Vector2;

public final class Skeleton extends Enemy {
    private boolean invincible = true;

    public Skeleton(Vector2<Integer> initPosition) {
        super(initPosition, Type.SKELETON, 1.8f, 0.5f, 0.5f, 0.15f, 0.3f);
        invincibility = new Vertex(State.INVINCIBILITY);

        health = 5;
        currentHealth = health;
        maxDamage = 4;
        minDamage = 1;

        initStateMachine();
    }

    @Override
    protected void initStateMachine() {
        stateMachine.setName("Skeleton");
        stateMachine.setInitialState(idle);

        idle.linkVertex(move);
        idle.linkVertex(attack);
        idle.linkVertex(hit);
        idle.linkVertex(invincibility);
        invincibility.linkVertex(idle);
        move.linkVertex(idle);
        move.linkVertex(hit);
        attack.linkVertex(idle);
        attack.linkVertex(hit);
        hit.linkVertex(idle);
        hit.linkVertex(dead);
    }

    @Override
    public synchronized void decreaseHealthShield(int value) {
        if (invincible) {
            invincible = false;
            Log.info("Skeleton ha perso la sua invincibilità");
            return;
        }
        super.decreaseHealthShield(value);
    }
}