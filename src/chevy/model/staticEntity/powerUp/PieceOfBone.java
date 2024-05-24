package chevy.model.staticEntity.powerUp;

import chevy.utilz.Vector2;

public class PieceOfBone extends PowerUp {
    public PieceOfBone(Vector2<Integer> initVelocity) {
        super(initVelocity, PowerUpTypes.PIECE_OF_BONE);
    }
}
