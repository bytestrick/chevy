package chevy.control.collectableController;

import chevy.control.HUDController;
import chevy.Sound;
import chevy.model.chamber.Chamber;
import chevy.model.entity.collectable.Coin;

public class CoinController {
    private final Chamber chamber;
    private final HUDController hudController;


    public CoinController(Chamber chamber, HUDController hudController) {
        this.chamber = chamber;
        this.hudController = hudController;
    }


    public void playerInInteraction(Coin coin) {
        if (coin.changeState(Coin.State.COLLECTED)) {
            Sound.getInstance().play(Sound.Effect.COIN);
            coin.collect();
            hudController.addCoin(coin.getValue());
            chamber.findAndRemoveEntity(coin);
        }
    }

    public void update(Coin coin) {
        if (coin.isCollected()) {
            if (coin.getState(Coin.State.COLLECTED).isFinished()) {
                coin.setToDraw(false);
                coin.removeToUpdate();
            }
        }
    }
}
