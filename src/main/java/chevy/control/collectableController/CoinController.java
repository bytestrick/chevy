package chevy.control.collectableController;

import chevy.control.HUDController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.collectable.Coin;
import chevy.service.Data;
import chevy.service.Sound;

public final class CoinController {
    private final Chamber chamber;
    private final HUDController hudController;

    CoinController(Chamber chamber, HUDController hudController) {
        this.chamber = chamber;
        this.hudController = hudController;
    }

    public void playerInInteraction(Coin coin) {
        if (coin.changeState(Coin.State.COLLECTED)) {
            int value = Coin.getValue();
            coin.collect();
            Sound.play(Sound.Effect.COIN);
            Data.increase("stats.collectable.total.count", value);
            Data.increase("stats.collectable.commons.coins.count", value);
            hudController.addCoins(value);
            chamber.findAndRemoveEntity(coin);
        }
    }

    public static void update(Coin coin) {
        if (coin.isCollected()) {
            if (coin.getState(Coin.State.COLLECTED).isFinished()) {
                coin.setToDraw(false);
                coin.removeFromUpdate();
            }
        }
    }
}