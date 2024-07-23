package chevy.model.entity.collectable;

import chevy.utils.Vector2;

public class Health extends Collectable {
    private int recoverHealth = 2;


    public Health(Vector2<Integer> initPosition) {
        super(initPosition, Type.HEALTH);
    }


    public void changeRecoverHealth(int newRecoverHealth) {
        this.recoverHealth = newRecoverHealth;
    }

    public int getRecoverHealth() {
        if (isCollected())
            return 0;

        return recoverHealth;
    }
}
