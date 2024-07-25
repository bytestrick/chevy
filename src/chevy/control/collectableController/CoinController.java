package chevy.control.collectableController;

import chevy.model.chamber.Chamber;
import chevy.model.entity.collectable.Coin;

public class CoinController {
    private final Chamber chamber;


    public CoinController(Chamber chamber) {
        this.chamber = chamber;
    }


    public void playerInInteraction(Coin coin) {
        if (coin.changeState(Coin.EnumState.COLLECT)) {
            coin.collect();
            chamber.findAndRemoveEntity(coin);
        }
    }

    public void update(Coin coin) {
        if (coin.isCollected()) {
            if (coin.getState(Coin.EnumState.COLLECT).isFinished()) {
                coin.setToDraw(false);
                coin.removeToUpdate();
            }
        }
    }
}
