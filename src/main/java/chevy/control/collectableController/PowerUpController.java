package chevy.control.collectableController;

import chevy.control.HUDController;
import chevy.control.enemyController.EnemyUpdateController;
import chevy.control.projectileController.ProjectileUpdateController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.collectable.powerUp.*;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dynamicEntity.liveEntity.player.Knight;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.dynamicEntity.projectile.Arrow;
import chevy.service.Data;

public final class PowerUpController {
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
            if (player.acquirePowerUp((PowerUp.Type) powerUp.getSpecificType(), powerUp)) {
                hudController.addPowerUpIcon(powerUp);
                Data.increment("stats.collectable.powerUp.total.count");
                switch ((PowerUp.Type) powerUp.getSpecificType()) {
                    case HOLY_SHIELD -> Data.increment("stats.collectable.powerUp.specific.holyShield.count");
                    case VAMPIRE_FANGS -> Data.increment("stats.collectable.powerUp.specific.vampireFangs.count");
                    case ANGEL_RING -> Data.increment("stats.collectable.powerUp.specific.angelRing.count");
                    case LONG_SWORD -> Data.increment("stats.collectable.powerUp.specific.longSword.count");
                    case HOBNAIL_BOOTS -> Data.increment("stats.collectable.powerUp.specific.hobnailBoots.count");
                    case COIN_OF_GREED -> Data.increment("stats.collectable.powerUp.specific.coinOfGreed.count");
                    case HOT_HEART -> Data.increment("stats.collectable.powerUp.specific.hotHeart.count");
                    case COLD_HEART -> Data.increment("stats.collectable.powerUp.specific.coldHeart.count");
                    case STONE_BOOTS -> Data.increment("stats.collectable.powerUp.specific.stoneBoots.count");
                    case BROKEN_ARROWS -> Data.increment("stats.collectable.powerUp.specific.brokenArrows.count");
                    case AGILITY -> Data.increment("stats.collectable.powerUp.specific.agility.count");
                    case HEDGEHOG_SPINES -> Data.increment("stats.collectable.powerUp.specific.hedgehogSpines.count");
                    case SLIME_PIECE -> Data.increment("stats.collectable.powerUp.specific.slimePiece.count");
                    case GOLD_ARROW -> Data.increment("stats.collectable.powerUp.specific.goldArrow.count");
                    case HEALING_FLOOD -> Data.increment("stats.collectable.powerUp.specific.healingFlood.count");
                    case KEY_S_KEEPER -> Data.increment("stats.collectable.powerUp.specific.keySKeeper.count");
                }
            }

            switch (powerUp.getSpecificType()) {
                case PowerUp.Type.COIN_OF_GREED -> {
                    CoinOfGreed coinOfGreed = (CoinOfGreed) player.getOwnedPowerUp(PowerUp.Type.COIN_OF_GREED);
                    if (coinOfGreed != null && coinOfGreed.canUse()) {
                        int currentPercentage = Enemy.getDropPercentage(0);
                        int increaseValue = (int) (currentPercentage * coinOfGreed.getIncreaseDropPercentage());
                        Enemy.changeDropPercentage(0, currentPercentage + increaseValue);
                    }
                }
                case PowerUp.Type.COLD_HEART -> {
                    ColdHeart coldHeath = (ColdHeart) player.getOwnedPowerUp(PowerUp.Type.COLD_HEART);
                    if (coldHeath != null && coldHeath.canUse()) {
                        player.changeShield(player.getShield() + coldHeath.getIncreaseShield());
                        player.increaseCurrentShield(coldHeath.getIncreaseShield());
                        hudController.changeMaxShield(player.getCurrentShield(), player.getShield());
                    }
                }
                case PowerUp.Type.GOLD_ARROW -> {
                    GoldArrow goldArrow = (GoldArrow) player.getOwnedPowerUp(PowerUp.Type.GOLD_ARROW);
                    if (goldArrow != null && goldArrow.canUse()) {
                        Arrow.changeAddDamage(goldArrow.getIncreaseDamage());
                    }
                }
                case PowerUp.Type.HEALING_FLOOD -> {
                    HealingFlood healingFlood = (HealingFlood) player.getOwnedPowerUp(PowerUp.Type.HEALING_FLOOD);
                    if (healingFlood != null && healingFlood.canUse()) {
                        int currentPercentage = Enemy.getDropPercentage(2);
                        int increaseValue = (int) (currentPercentage * healingFlood.getIncreaseDropPercentage());
                        Enemy.changeDropPercentage(2, currentPercentage + increaseValue);
                    }

                }
                case PowerUp.Type.HOT_HEART -> {
                    HotHeart hotHeath = (HotHeart) player.getOwnedPowerUp(PowerUp.Type.HOT_HEART);
                    if (hotHeath != null && hotHeath.canUse()) {
                        player.changeHealth(player.getHealth() + hotHeath.getIncreaseHealth());
                        player.increaseCurrentHealth(hotHeath.getIncreaseHealth());
                        hudController.changeMaxHealth(player.getCurrentHealth(), player.getHealth());
                    }
                }
                case PowerUp.Type.KEY_S_KEEPER -> {
                    KeySKeeper keySKeeper = (KeySKeeper) player.getOwnedPowerUp(PowerUp.Type.KEY_S_KEEPER);
                    if (keySKeeper != null && keySKeeper.canUse()) {
                        int currentPercentage = Enemy.getDropPercentage(1);
                        int increaseValue = (int) (currentPercentage * keySKeeper.getIncreaseDropPercentage());
                        Enemy.changeDropPercentage(1, currentPercentage + increaseValue);
                    }
                }
                case PowerUp.Type.LONG_SWORD -> {
                    LongSword longSword = (LongSword) player.getOwnedPowerUp(PowerUp.Type.LONG_SWORD);
                    if (longSword != null && longSword.canUse() && player instanceof Knight) {
                        player.changeMinDamage(player.getMinDamage() + longSword.getIncreaseDamage());
                        player.changeMaxDamage(player.getMaxDamage() + longSword.getIncreaseDamage());
                        hudController.changeMaxAttack(player.getMaxDamage());
                        hudController.setTextAttackBar(player.getMinDamage() + " ~ " + player.getMaxDamage());
                    }
                }
                case PowerUp.Type.SLIME_PIECE -> {
                    SlimePiece slimePiece = (SlimePiece) player.getOwnedPowerUp(PowerUp.Type.SLIME_PIECE);
                    if (slimePiece != null && slimePiece.canUse()) {
                        player.changeMinDamage(player.getMinDamage() + slimePiece.getDamageIncrease());
                        player.changeMaxDamage(player.getMaxDamage() + slimePiece.getDamageIncrease());
                        hudController.changeMaxAttack(player.getMaxDamage());
                        hudController.setTextAttackBar(player.getMinDamage() + " ~ " + player.getMaxDamage());
                    }
                }
                default -> {}
            }
            EnemyUpdateController.runUpdate();
            ProjectileUpdateController.runUpdate();
        }
    }

    public void update(PowerUp powerUp) {
        if (powerUp.isCollected()) {
            if (powerUp.getState(PowerUp.State.COLLECTED).isFinished()) {
                powerUp.setToDraw(false);
                powerUp.removeFromUpdate();
            }
        } else {
            if (chamber.getDirectionToHitPlayer(powerUp) != null) {
                if (powerUp.changeState(PowerUp.State.SELECTED)) {
                    EnemyUpdateController.stopUpdate();
                    ProjectileUpdateController.stopUpdate();
                    hudController.PowerUpText(powerUp);
                }
            }
            else if (powerUp.checkAndChangeState(PowerUp.State.DESELECTED)) {
                hudController.hidePowerUpText();
                EnemyUpdateController.runUpdate();
                ProjectileUpdateController.runUpdate();
            }
            powerUp.checkAndChangeState(PowerUp.State.IDLE);
        }
    }
}
