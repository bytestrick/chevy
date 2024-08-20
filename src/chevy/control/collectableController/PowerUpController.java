package chevy.control.collectableController;

import chevy.control.hudController.HUDController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.collectable.powerUp.*;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.liveEntity.player.Knight;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Arrow;

public class PowerUpController {
    private final Chamber chamber;
    private final HUDController hudController;


    public PowerUpController(Chamber chamber, HUDController hudController) {
        this.chamber = chamber;
        this.hudController = hudController;
    }


    public void playerInInteraction(Player player, PowerUp powerUp) {
        if (powerUp.changeState(PowerUp.State.COLLECTED)) {
            powerUp.collect();
            chamber.findAndRemoveEntity(powerUp);
            hudController.hidePowerUpText();
            player.acquirePowerUp((PowerUp.Type) powerUp.getSpecificType(), powerUp);

            ColdHeart coldHeath = (ColdHeart) player.getOwnedPowerUp(PowerUp.Type.COLD_HEART);
            if (coldHeath != null && coldHeath.canUse()) {
                player.changeShield(player.getShield() + coldHeath.getIncreaseShield());
                player.increaseCurrentShield(coldHeath.getIncreaseShield());
                hudController.changeMaxShield(player.getCurrentShield(), player.getShield());
            }

            HotHeart hotHeath = (HotHeart) player.getOwnedPowerUp(PowerUp.Type.HOT_HEART);
            if (hotHeath != null && hotHeath.canUse()) {
                player.changeHealth(player.getHealth() + hotHeath.getIncreaseHealth());
                player.increaseCurrentHealth(hotHeath.getIncreaseHealth());
                hudController.changeMaxHealth(player.getCurrentHealth(), player.getHealth());
            }

            GoldArrow goldArrow = (GoldArrow) player.getOwnedPowerUp(PowerUp.Type.GOLD_ARROW);
            if (goldArrow != null && goldArrow.canUse()) {
                Arrow.changeAddDamage(goldArrow.getIncreaseDamage());
            }

            LongSword longSword = (LongSword) player.getOwnedPowerUp(PowerUp.Type.LONG_SWORD);
            if (longSword != null && longSword.canUse() && player instanceof Knight) {
                player.changeMinDamage(player.getMinDamage() + longSword.getIncreaseDamage());
                player.changeMaxDamage(player.getMaxDamage() + longSword.getIncreaseDamage());
                hudController.changeMaxAttack(player.getMaxDamage());
                hudController.setTextAttackBar(player.getMinDamage() + " ~ " + player.getMaxDamage());
            }

            CoinOfGreed coinOfGreed = (CoinOfGreed) player.getOwnedPowerUp(PowerUp.Type.COIN_OF_GREED);
            if (coinOfGreed != null && coinOfGreed.canUse()) {
                int currentPercentage = Enemy.getDropPercentage(0);
                int increaseValue = (int) (currentPercentage * coinOfGreed.getIncreaseDropPercentage());
                Enemy.changeDropPercentage(0, currentPercentage + increaseValue);
            }

            HealingFlood healingFlood = (HealingFlood) player.getOwnedPowerUp(PowerUp.Type.HEALING_FLOOD);
            if (healingFlood != null && healingFlood.canUse()) {
                int currentPercentage = Enemy.getDropPercentage(2);
                int increaseValue = (int) (currentPercentage * healingFlood.getIncreaseDropPercentage());
                Enemy.changeDropPercentage(2, currentPercentage + increaseValue);
            }

            KeySKeeper keySKeeper = (KeySKeeper) player.getOwnedPowerUp(PowerUp.Type.KEY_S_KEEPER);
            if (keySKeeper != null && keySKeeper.canUse()) {
                int currentPercentage = Enemy.getDropPercentage(1);
                int increaseValue = (int) (currentPercentage * keySKeeper.getIncreaseDropPercentage());
                Enemy.changeDropPercentage(1, currentPercentage + increaseValue);
            }

            SlimePiece slimePiece = (SlimePiece) player.getOwnedPowerUp(PowerUp.Type.SLIME_PIECE);
            if (slimePiece != null && slimePiece.canUse()) {
                player.changeMinDamage(player.getMinDamage() + slimePiece.getDamageIncrease());
                player.changeMaxDamage(player.getMaxDamage() + slimePiece.getDamageIncrease());
                hudController.changeMaxAttack(player.getMaxDamage());
                hudController.setTextAttackBar(player.getMinDamage() + " ~ " + player.getMaxDamage());
            }
        }
    }

    public void update(PowerUp powerUp) {
        if (powerUp.isCollected()) {
            if (powerUp.getState(PowerUp.State.COLLECTED).isFinished()) {
                powerUp.setToDraw(false);
                powerUp.removeToUpdate();
            }
        }
        else {
            if (chamber.getHitDirectionPlayer(powerUp) != null) {
                if (powerUp.changeState(PowerUp.State.SELECTED))
                    hudController.PowerUpText(powerUp);
            }
            else if (powerUp.checkAndChangeState(PowerUp.State.DESELECTED)) {
                hudController.hidePowerUpText();
            }
            powerUp.checkAndChangeState(PowerUp.State.IDLE);
        }
    }
}
