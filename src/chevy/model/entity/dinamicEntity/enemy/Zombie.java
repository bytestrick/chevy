package chevy.model.entity.dinamicEntity.enemy;

import chevy.utilz.Vector2;

public class Zombie extends Enemy {
    public Zombie(Vector2<Integer> initVelocity) {
        super(initVelocity, EnemyTypes.ZOMBIE);
    }
}
