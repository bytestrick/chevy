package chevy.model.dinamicEntity.enemy;

import chevy.utilz.Vector2;

public class Frog extends Enemy {
    public Frog(Vector2<Integer> initVelocity) {
        super(initVelocity, EnemyTypes.FROG);
    }
}
