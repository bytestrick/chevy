package chevy.model.entity.dynamicEntity.liveEntity.enemy;

import chevy.model.entity.stateMachine.Vertex;
import chevy.utils.Log;
import chevy.utils.Vector2;

public final class Skeleton extends Enemy {
    private boolean invincible = true;

    public Skeleton(Vector2<Integer> initPosition) {
        super(initPosition, Type.SKELETON, 1.8f, 0.15f);
        invincibility = new Vertex(State.INVINCIBILITY);

        health = 5;
        currentHealth = health;
        maxDamage = 4;
        minDamage = 1;

        stateMachine.setName("Skeleton");
        idle.linkVertex(invincibility);
        invincibility.linkVertex(idle);
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