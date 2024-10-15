package chevy.control.collectableController;

import chevy.control.HUDController;
import chevy.control.enemyController.EnemyUpdateController;
import chevy.control.projectileController.ProjectileUpdateController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.collectable.powerUp.CoinOfGreed;
import chevy.model.entity.collectable.powerUp.ColdHeart;
import chevy.model.entity.collectable.powerUp.GoldArrows;
import chevy.model.entity.collectable.powerUp.HealingFlood;
import chevy.model.entity.collectable.powerUp.HotHeart;
import chevy.model.entity.collectable.powerUp.KeySKeeper;
import chevy.model.entity.collectable.powerUp.LongSword;
import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.model.entity.collectable.powerUp.SlimePiece;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dynamicEntity.liveEntity.player.Knight;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.dynamicEntity.projectile.Arrow;
import chevy.service.Data;
import chevy.service.Sound;

final class PowerUpController {
    private final Chamber chamber;
    private final HUDController hudController;
    private final EnemyUpdateController enemyUpdateController;
    private final ProjectileUpdateController projectileUpdateController;

    PowerUpController(Chamber chamber, HUDController hudController, EnemyUpdateController enemyUpdateController, ProjectileUpdateController projectileUpdateController) {
        this.chamber = chamber;
        this.hudController = hudController;
        this.enemyUpdateController = enemyUpdateController;
        this.projectileUpdateController = projectileUpdateController;
    }

    void playerInInteraction(Player player, PowerUp powerUp) {
        if (powerUp.changeState(PowerUp.State.COLLECTED)) {
            Sound.play(Sound.Effect.POWER_UP_EQUIPPED);
            powerUp.collect();
            chamber.findAndRemoveEntity(powerUp);
            hudController.hidePowerUpText();
            if (player.acquirePowerUp((PowerUp.Type) powerUp.getType(), powerUp)) {
                hudController.addPowerUpIcon(powerUp);
                Data.increment("stats.collectable.powerUp.totalPowerUps.count");
                Data.increment("stats.collectable.powerUp.specific." + powerUp.getType() +
                        ".count");
            }

            switch (powerUp.getType()) {
                case PowerUp.Type.COIN_OF_GREED -> {
                    CoinOfGreed coinOfGreed =
                            (CoinOfGreed) player.getOwnedPowerUp(PowerUp.Type.COIN_OF_GREED);
                    if (coinOfGreed != null && coinOfGreed.canUse()) {
                        int currentPercentage = Enemy.getDropPercentage(0);
                        int increaseValue =
                                (int) (currentPercentage * CoinOfGreed.getIncreasedDropProbability());
                        Enemy.setDropPercentage(0, currentPercentage + increaseValue);
                    }
                }
                case PowerUp.Type.COLD_HEART -> {
                    ColdHeart coldHeath =
                            (ColdHeart) player.getOwnedPowerUp(PowerUp.Type.COLD_HEART);
                    if (coldHeath != null && coldHeath.canUse()) {
                        player.setShield(player.getShield() + ColdHeart.getShieldBoost());
                        player.increaseCurrentShield(ColdHeart.getShieldBoost());
                        hudController.changeMaxShield(player.getCurrentShield(),
                                player.getShield());
                    }
                }
                case PowerUp.Type.GOLD_ARROWS -> {
                    GoldArrows goldArrows =
                            (GoldArrows) player.getOwnedPowerUp(PowerUp.Type.GOLD_ARROWS);
                    if (goldArrows != null && goldArrows.canUse()) {
                        Arrow.setDamageBoost(GoldArrows.getDamageBoost());
                    }
                }
                case PowerUp.Type.HEALING_FLOOD -> {
                    HealingFlood healingFlood =
                            (HealingFlood) player.getOwnedPowerUp(PowerUp.Type.HEALING_FLOOD);
                    if (healingFlood != null && healingFlood.canUse()) {
                        int currentPercentage = Enemy.getDropPercentage(2);
                        int increaseValue =
                                (int) (currentPercentage * HealingFlood.getIncreasedDropProbability());
                        Enemy.setDropPercentage(2, currentPercentage + increaseValue);
                    }
                }
                case PowerUp.Type.HOT_HEART -> {
                    HotHeart hotHeath = (HotHeart) player.getOwnedPowerUp(PowerUp.Type.HOT_HEART);
                    if (hotHeath != null && hotHeath.canUse()) {
                        player.setHealth(player.getHealth() + HotHeart.getHealthBoost());
                        player.increaseCurrentHealth(HotHeart.getHealthBoost());
                        hudController.changeMaxHealth(player.getCurrentHealth(),
                                player.getHealth());
                    }
                }
                case PowerUp.Type.KEY_S_KEEPER -> {
                    KeySKeeper keySKeeper =
                            (KeySKeeper) player.getOwnedPowerUp(PowerUp.Type.KEY_S_KEEPER);
                    if (keySKeeper != null && keySKeeper.canUse()) {
                        int currentPercentage = Enemy.getDropPercentage(1);
                        int increaseValue =
                                (int) (currentPercentage * KeySKeeper.getIncreasedDropProbability());
                        Enemy.setDropPercentage(1, currentPercentage + increaseValue);
                    }
                }
                case PowerUp.Type.LONG_SWORD -> {
                    LongSword longSword =
                            (LongSword) player.getOwnedPowerUp(PowerUp.Type.LONG_SWORD);
                    if (longSword != null && longSword.canUse() && player instanceof Knight) {
                        player.setDamage(player.getMinDamage() + LongSword.getDamageBoost(),
                                player.getMaxDamage() + LongSword.getDamageBoost());
                        hudController.changeMaxAttack(player.getMaxDamage());
                        hudController.setTextAttackBar(player.getMinDamage() + " ~ " + player.getMaxDamage());
                    }
                }
                case PowerUp.Type.SLIME_PIECE -> {
                    SlimePiece slimePiece =
                            (SlimePiece) player.getOwnedPowerUp(PowerUp.Type.SLIME_PIECE);
                    if (slimePiece != null && slimePiece.canUse()) {
                        player.setDamage(player.getMinDamage() + SlimePiece.getDamageBoost(),
                                player.getMaxDamage() + SlimePiece.getDamageBoost());
                        hudController.changeMaxAttack(player.getMaxDamage());
                        hudController.setTextAttackBar(player.getMinDamage() + " ~ " + player.getMaxDamage());
                    }
                }
                default -> {}
            }
            enemyUpdateController.setPaused(false);
            projectileUpdateController.setPaused(false);
        }
    }

    void update(PowerUp powerUp) {
        if (powerUp.isCollected()) {
            if (powerUp.getState(PowerUp.State.COLLECTED).isFinished()) {
                powerUp.setShouldDraw(false);
                powerUp.removeFromUpdate();
            }
        } else {
            if (chamber.getDirectionToHitPlayer(powerUp) != null) {
                if (powerUp.changeState(PowerUp.State.SELECTED)) {
                    enemyUpdateController.setPaused(true);
                    projectileUpdateController.setPaused(true);
                    hudController.PowerUpText(powerUp);
                }
            } else if (powerUp.checkAndChangeState(PowerUp.State.DESELECTED)) {
                hudController.hidePowerUpText();
                enemyUpdateController.setPaused(false);
                projectileUpdateController.setPaused(false);
            }
            powerUp.checkAndChangeState(PowerUp.State.IDLE);
        }
    }
}
