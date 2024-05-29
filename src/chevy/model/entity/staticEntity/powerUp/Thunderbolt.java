package chevy.model.entity.staticEntity.powerUp;
import chevy.utilz.Vector2;

public class Thunderbolt extends PowerUp {
    private int damage;
    private float cooldown;

    public Thunderbolt(Vector2<Integer> initVelocity) {
        super(initVelocity, PowerUpTypes.THUNDERBOLT);
    }
}
