package chevy.control.collectableController;

import chevy.control.HUDController;
import chevy.control.enemyController.EnemyUpdateController;
import chevy.control.projectileController.ProjectileUpdateController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.collectable.powerUp.CoinOfGreed;
import chevy.model.entity.collectable.powerUp.ColdHeart;
import chevy.model.entity.collectable.powerUp.GoldArrow;
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

final class PowerUpController {
    private static final String STATS_PREFIX = "stats.collectable.powerUp.specific.";
    private final Chamber chamber;
    private final HUDController hudController;

    PowerUpController(Chamber chamber, HUDController hudController) {
        this.chamber = chamber;
        this.hudController = hudController;
    }

    void playerInInteraction(Player player, PowerUp powerUp) {
        if (powerUp.changeState(PowerUp.State.COLLECTED)) {
            powerUp.collect();
            chamber.findAndRemoveEntity(powerUp);
            hudController.hidePowerUpText();
            if (player.acquirePowerUp((PowerUp.Type) powerUp.getType(), powerUp)) {
                hudController.addPowerUpIcon(powerUp);
                Data.increment("stats.collectable.powerUp.total.count");

                switch ((PowerUp.Type) powerUp.getType()) {
                    case HOLY_SHIELD -> Data.increment("holyShield.count");
                    case VAMPIRE_FANGS -> Data.increment(STATS_PREFIX + "vampireFangs.count");
                    case ANGEL_RING -> Data.increment(STATS_PREFIX + "angelRing.count");
                    case LONG_SWORD -> Data.increment(STATS_PREFIX + "longSword.count");
                    case HOBNAIL_BOOTS -> Data.increment(STATS_PREFIX + "hobnailBoots.count");
                    case COIN_OF_GREED -> Data.increment(STATS_PREFIX + "coinOfGreed.count");
                    case HOT_HEART -> Data.increment(STATS_PREFIX + "hotHeart.count");
                    case COLD_HEART -> Data.increment(STATS_PREFIX + "coldHeart.count");
                    case STONE_BOOTS -> Data.increment(STATS_PREFIX + "stoneBoots.count");
                    case BROKEN_ARROWS -> Data.increment(STATS_PREFIX + "brokenArrows.count");
                    case AGILITY -> Data.increment(STATS_PREFIX + "agility.count");
                    case HEDGEHOG_SPINES -> Data.increment(STATS_PREFIX + "hedgehogSpines.count");
                    case SLIME_PIECE -> Data.increment(STATS_PREFIX + "slimePiece.count");
                    case GOLD_ARROW -> Data.increment(STATS_PREFIX + "goldArrow.count");
                    case HEALING_FLOOD -> Data.increment(STATS_PREFIX + "healingFlood.count");
                    case KEY_S_KEEPER -> Data.increment(STATS_PREFIX + "keySKeeper.count");
                }
            }

            switch (powerUp.getType()) {
                case PowerUp.Type.COIN_OF_GREED -> {
                    CoinOfGreed coinOfGreed =
                            (CoinOfGreed) player.getOwnedPowerUp(PowerUp.Type.COIN_OF_GREED);
                    if (coinOfGreed != null && coinOfGreed.canUse()) {
                        int currentPercentage = Enemy.getDropPercentage(0);
                        int increaseValue =
                                (int) (currentPercentage * CoinOfGreed.getIncreasedDropProbability());
                        Enemy.changeDropPercentage(0, currentPercentage + increaseValue);
                    }
                }
                case PowerUp.Type.COLD_HEART -> {
                    ColdHeart coldHeath =
                            (ColdHeart) player.getOwnedPowerUp(PowerUp.Type.COLD_HEART);
                    if (coldHeath != null && coldHeath.canUse()) {
                        player.changeShield(player.getShield() + ColdHeart.getShieldBoost());
                        player.increaseCurrentShield(ColdHeart.getShieldBoost());
                        hudController.changeMaxShield(player.getCurrentShield(),
                                player.getShield());
                    }
                }
                case PowerUp.Type.GOLD_ARROW -> {
                    GoldArrow goldArrow =
                            (GoldArrow) player.getOwnedPowerUp(PowerUp.Type.GOLD_ARROW);
                    if (goldArrow != null && goldArrow.canUse()) {
                        Arrow.changeAddDamage(GoldArrow.getDamageBoost());
                    }
                }
                case PowerUp.Type.HEALING_FLOOD -> {
                    HealingFlood healingFlood =
                            (HealingFlood) player.getOwnedPowerUp(PowerUp.Type.HEALING_FLOOD);
                    if (healingFlood != null && healingFlood.canUse()) {
                        int currentPercentage = Enemy.getDropPercentage(2);
                        int increaseValue =
                                (int) (currentPercentage * HealingFlood.getIncreasedDropProbability());
                        Enemy.changeDropPercentage(2, currentPercentage + increaseValue);
                    }
                }
                case PowerUp.Type.HOT_HEART -> {
                    HotHeart hotHeath = (HotHeart) player.getOwnedPowerUp(PowerUp.Type.HOT_HEART);
                    if (hotHeath != null && hotHeath.canUse()) {
                        player.changeHealth(player.getHealth() + HotHeart.getHealthBoost());
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
                        Enemy.changeDropPercentage(1, currentPercentage + increaseValue);
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
                    EnemyUpdateController.stopUpdate();
                    ProjectileUpdateController.stopUpdate();
                    hudController.PowerUpText(powerUp);
                }
            } else if (powerUp.checkAndChangeState(PowerUp.State.DESELECTED)) {
                hudController.hidePowerUpText();
            }
            powerUp.checkAndChangeState(PowerUp.State.IDLE);
        }
    }
}
