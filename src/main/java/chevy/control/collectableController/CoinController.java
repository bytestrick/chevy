package chevy.control.collectableController;

import chevy.control.HUDController;
import chevy.model.Statistics;
import chevy.model.chamber.Chamber;
import chevy.model.entity.collectable.Coin;
import chevy.service.Sound;

public class CoinController {
    private final Chamber chamber;
    private final HUDController hudController;

    public CoinController(Chamber chamber, HUDController hudController) {
        this.chamber = chamber;
        this.hudController = hudController;
    }

    public void playerInInteraction(Coin coin) {
        if (coin.changeState(Coin.State.COLLECTED)) {
            int value = coin.getValue();
            coin.collect();
            Sound.play(Sound.Effect.COIN);
            Statistics.increase(Statistics.COLLECTED_COLLECTABLE, value);
            Statistics.increase(Statistics.COLLECTED_COIN, value);
            hudController.addCoin(value);
            chamber.findAndRemoveEntity(coin);
        }
    }

    public void update(Coin coin) {
        if (coin.isCollected()) {
            if (coin.getState(Coin.State.COLLECTED).isFinished()) {
                coin.setToDraw(false);
                coin.removeFromUpdate();
            }
        }
    }
}
