package chevy.control;

import chevy.control.collectableController.CollectableController;
import chevy.control.enemyController.EnemyController;
import chevy.control.environmentController.EnvironmentController;
import chevy.control.projectileController.ProjectileController;
import chevy.control.trapsController.TrapsController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.collectable.Collectable;
import chevy.model.entity.collectable.Health;
import chevy.model.entity.collectable.powerUp.HedgehogSpines;
import chevy.model.entity.collectable.powerUp.HolyShield;
import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.model.entity.collectable.powerUp.VampireFangs;
import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.DynamicEntity;
import chevy.model.entity.dynamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.dynamicEntity.projectile.Arrow;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.model.entity.stateMachine.EntityState;
import chevy.model.entity.staticEntity.environment.Environment;
import chevy.model.entity.staticEntity.environment.traps.IcyFloor;
import chevy.model.entity.staticEntity.environment.traps.SpikedFloor;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.service.Data;
import chevy.service.Sound;
import chevy.service.Updatable;
import chevy.service.UpdateManager;
import chevy.utils.Log;
import chevy.view.GamePanel;
import chevy.view.Window;
import chevy.view.chamber.ChamberView;

import java.awt.*;
import java.awt.event.KeyEvent;

import static chevy.model.entity.staticEntity.environment.Environment.Type.TRAP;

/**
 * Manages the interactions of the player with enemies, projectiles, and traps.
 */
public final class PlayerController implements Updatable {
    private static final double invSqrtTwo = 1 / Math.sqrt(2);
    private static final Point playerViewOffset = new Point(
            ChamberView.tileSide / 2 + ChamberView.windowOffset.width,
            ChamberView.tileSide / 2 + ChamberView.windowOffset.height
    );
    private final Chamber chamber;
    private final Player player;
    private final GamePanel gamePanel;
    private EnemyController enemyController;
    private TrapsController trapsController;
    private ProjectileController projectileController;
    private CollectableController collectableController;
    private EnvironmentController environmentController;
    private HUDController hudController;
    private boolean updateFinished;
    private int trap_damage;

    /**
     * @param chamber reference to the game room
     */
    public PlayerController(Chamber chamber, GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.chamber = chamber;
        player = chamber.getPlayer();

        // Adds the player controller to the UpdateManager.
        UpdateManager.register(this);
    }

    private static void playerMoveSound(Player player) {
        Sound.play(switch (player.getType()) {
            case ARCHER -> Sound.Effect.ARCHER_FOOTSTEPS;
            case NINJA -> Sound.Effect.NINJA_FOOTSTEPS;
            case KNIGHT -> Sound.Effect.KNIGHT_FOOTSTEPS;
        });
    }

    /**
     * Manages key press events, converting the key code into a direction.
     */
    void keyPressed(final KeyEvent keyEvent) {
        final EntityState currentPlayerState = player.getState();
        final int key = keyEvent.getKeyCode();
        if (key == KeyEvent.VK_ESCAPE) {
            gamePanel.pauseDialog();
        } else if (currentPlayerState != Player.State.DEAD
                && currentPlayerState != Player.State.GLIDE) {
            final Direction direction = switch (key) {
                case KeyEvent.VK_W, KeyEvent.VK_UP, KeyEvent.VK_I -> Direction.UP;
                case KeyEvent.VK_A, KeyEvent.VK_LEFT, KeyEvent.VK_J -> Direction.LEFT;
                case KeyEvent.VK_S, KeyEvent.VK_DOWN, KeyEvent.VK_K -> Direction.DOWN;
                case KeyEvent.VK_D, KeyEvent.VK_RIGHT, KeyEvent.VK_L -> Direction.RIGHT;
                default -> null;
            };
            if (direction != null && player.getState(Player.State.SLUDGE).isFinished()) {
                if (key >= KeyEvent.VK_I && key <= KeyEvent.VK_L) {
                    freeAttack(direction);
                } else {
                    keyBoardInteraction(direction);
                }
            }
        }
    }

    /**
     * Translates a mouse click into a free attack in the corresponding direction.
     *
     * @param click position of the mouse click
     */
    void mousePressed(final Point click) {
        if (gamePanel.getWindow().getScene() == Window.Scene.PLAYING) {
            final Point playerPos = player.getPosition();
            playerPos.setLocation(
                    ChamberView.tileSide * playerPos.x + playerViewOffset.x,
                    ChamberView.tileSide * playerPos.y + playerViewOffset.y
                            + gamePanel.getWindow().getHeight() - gamePanel.getHeight()
            );

            final double adj = playerPos.x - click.x, opp = playerPos.y - click.y;
            final double hyp = Math.sqrt(adj * adj + opp * opp);
            final double cosTheta = adj / hyp, sinTheta = opp / hyp;

            Direction direction = null;
            if (cosTheta > -invSqrtTwo && cosTheta < invSqrtTwo && sinTheta >= invSqrtTwo) {
                direction = Direction.UP;
            } else if (sinTheta > -invSqrtTwo && sinTheta < invSqrtTwo && cosTheta <= -invSqrtTwo) {
                direction = Direction.RIGHT;
            } else if (cosTheta > -invSqrtTwo && cosTheta < invSqrtTwo && sinTheta <= -invSqrtTwo) {
                direction = Direction.DOWN;
            } else if (sinTheta > -invSqrtTwo && sinTheta < invSqrtTwo && cosTheta >= invSqrtTwo) {
                direction = Direction.LEFT;
            }
            freeAttack(direction);
        }
    }

    /**
     * Handle the various interactions the player can undergo.
     *
     * @param interaction the type of interaction
     * @param subject     the subject of the interaction
     */
    public synchronized void handleInteraction(Interaction interaction, Object subject) {
        switch (interaction) {
            case ENEMY -> enemyInteraction((Enemy) subject);
            case PROJECTILE -> projectileInteraction((Projectile) subject);
            case TRAP -> trapInteraction((Trap) subject);
            case COLLECTABLE -> collectableInteraction((Collectable) subject);
        }
    }

    /**
     * Handles the interactions of the player with collectable objects.
     *
     * @param collectable collectable object with which the player interacts
     */
    private void collectableInteraction(Collectable collectable) {
        Collectable.Type collectableType = (Collectable.Type) collectable.getType();
        if (collectableType == Collectable.Type.HEALTH) {
            Health health = (Health) collectable;
            player.increaseCurrentHealth(health.getRecoverHealth());
        }
    }

    /**
     * Handle the interactions of the player with projectiles, applying damage to the player.
     *
     * @param projectile the projectile with which the player interacts
     */
    private void projectileInteraction(Projectile projectile) {
        boolean canHit = true;
        if (projectile instanceof Arrow) {
            PowerUp brokenArrow = player.getOwnedPowerUp(PowerUp.Type.BROKEN_ARROWS);
            if (brokenArrow != null) {
                canHit = !brokenArrow.canUse();
            }
        }
        if (canHit) {
            hitPlayer(-1 * projectile.getDamage());
        }
    }

    /**
     * Handle the interactions of the player with traps.
     *
     * @param trap the trap with which the player interacts
     */
    private void trapInteraction(Trap trap) {
        switch ((Trap.Type) trap.getType()) {
            case VOID, TRAPDOOR -> {
                player.changeState(Player.State.FALL);
                trap_damage = trap.getDamage();
            }
            case SPIKED_FLOOR -> {
                SpikedFloor spikedFloor = (SpikedFloor) trap;
                hitPlayer(-1 * spikedFloor.getDamage());
            }
            default -> {
            }
        }
    }

    /**
     * Il {@link Player} make a free attack in the specified direction.
     *
     * @param direction the direction in which the player attacks
     */
    private void freeAttack(final Direction direction) {
        final Entity entityNextCell = chamber.getEntityNearOnTop(player, direction);
        if (player.checkAndChangeState(Player.State.ATTACK)) {
            if (player.getDirection() != direction) {
                player.setDirection(direction);
            }
            switch (player.getType()) {
                case Player.Type.KNIGHT, Player.Type.NINJA -> {
                    if (player.getType() == Player.Type.NINJA) {
                        Sound.play(Sound.Effect.PUNCH);
                    } else {
                        Sound.play(Sound.Effect.BLADE_SLASH);
                    }
                    if (entityNextCell.getGenericType() == LiveEntity.Type.ENEMY) {
                        enemyController.handleInteraction(Interaction.PLAYER_IN, player,
                                (Enemy) entityNextCell);
                    }
                }
                case ARCHER -> { // Throw an arrow
                    final Arrow arrow = new Arrow(player.getPosition(), player.getDirection());

                    arrow.setDamage(player.getMinDamage(), player.getMaxDamage());
                    Sound.play(Sound.Effect.ARROW_SWOOSH);

                    chamber.addProjectile(arrow);
                    chamber.addEntityOnTop(arrow);
                }
            }

            final VampireFangs vampireFangs =
                    (VampireFangs) player.getOwnedPowerUp(PowerUp.Type.VAMPIRE_FANGS);
            if (vampireFangs != null && vampireFangs.canUse()) {
                player.increaseCurrentHealth(VampireFangs.getHealthBoost());
                hudController.changeHealth(player.getCurrentHealth());
            }
        }
    }

    /**
     * Handles the interactions of the keyboard with the player, managing the player's movements and the interactions with the entities in the adjacent cells.
     *
     * @param direction the direction in which the player moves
     */
    private void keyBoardInteraction(Direction direction) {
        Entity entityCurrentCell = chamber.getEntityBelowTheTop(player);
        assert entityCurrentCell != null : "entityCurrentCell == null";

        // Player on
        if (entityCurrentCell.getGenericType().equals(TRAP)) {
            trapsController.handleInteraction(Interaction.PLAYER, player, (Trap) entityCurrentCell);
        }

        Entity entityNextCell = chamber.getEntityNearOnTop(player, direction);
        // Player in
        switch (entityNextCell.getGenericType()) {
            case LiveEntity.Type.ENEMY -> {
                // Free attack automatically launched by walking against an enemy.
                if (player.checkAndChangeState(Player.State.ATTACK)) {
                    if (player.getDirection() != direction) {
                        player.setDirection(direction);
                    }
                    switch (player.getType()) {
                        case Player.Type.NINJA -> Sound.play(Sound.Effect.PUNCH);
                        case Player.Type.ARCHER -> Sound.play(Sound.Effect.ARROW_SWOOSH);
                        case Player.Type.KNIGHT -> Sound.play(Sound.Effect.BLADE_SLASH);
                    }
                    enemyController.handleInteraction(Interaction.PLAYER_IN, player, (Enemy) entityNextCell);
                }
            }
            case Environment.Type.TRAP -> {
                if (chamber.canCross(player, direction) && player.checkAndChangeState(Player.State.MOVE)) {
                    playerMoveSound(player);
                    chamber.moveDynamicEntity(player, direction);
                    trapsController.handleInteraction(Interaction.PLAYER_IN, player, (Trap) entityNextCell);
                }
            }
            case DynamicEntity.Type.PROJECTILE -> {
                if (chamber.canCross(player, direction) && player.checkAndChangeState(Player.State.MOVE)) {
                    playerMoveSound(player);
                    chamber.moveDynamicEntity(player, direction);
                    projectileController.handleInteraction(Interaction.PLAYER_IN, player, (Projectile) entityNextCell);
                }
            }
            case Entity.Type.COLLECTABLE, Collectable.Type.POWER_UP -> {
                if (chamber.canCross(player, direction) && player.checkAndChangeState(Player.State.MOVE)) {
                    playerMoveSound(player);
                    chamber.moveDynamicEntity(player, direction);
                    collectableController.handleInteraction(Interaction.PLAYER_IN, player, (Collectable) entityNextCell);
                }
            }
            case Entity.Type.ENVIRONMENT -> {
                if (chamber.canCross(player, direction) && player.checkAndChangeState(Player.State.MOVE)) {
                    playerMoveSound(player);
                    chamber.moveDynamicEntity(player, direction);
                    environmentController.handleInteraction(Interaction.PLAYER_IN, player, entityNextCell);
                }
            }
            default -> {
                if (chamber.canCross(player, direction) && player.checkAndChangeState(Player.State.MOVE)) {
                    playerMoveSound(player);
                    chamber.moveDynamicEntity(player, direction);
                }
            }
        }

        // Player out
        if (entityCurrentCell.getGenericType().equals(TRAP)) {
            trapsController.handleInteraction(Interaction.PLAYER_OUT, player, (Trap) entityCurrentCell);
        }
    }

    /**
     * Handle the interactions of the enemies with the player, applying damage to the player.
     *
     * @param enemy the enemy with which the player interacts
     */
    private void enemyInteraction(Enemy enemy) {
        HedgehogSpines hedgehogSpines =
                (HedgehogSpines) player.getOwnedPowerUp(PowerUp.Type.HEDGEHOG_SPINES);
        int partialDamage = 0;
        int damage = enemy.getDamage();
        if (hedgehogSpines != null && hedgehogSpines.canUse()) {
            partialDamage = damage - (int) (damage * HedgehogSpines.getDamageMultiplier());

            // change the player's damage so that only the partial damage is inflicted
            int minDamage = player.getMinDamage();
            int maxDamage = player.getMaxDamage();
            player.setDamage(partialDamage, partialDamage);

            player.changeState(Player.State.ATTACK);
            enemyController.handleInteraction(Interaction.PLAYER_IN, player, enemy);

            player.setDamage(minDamage, maxDamage);
        }

        hitPlayer(-1 * (damage - partialDamage));
    }

    /**
     * Update the player's state at each game cycle.
     */
    @Override
    public void update(double delta) {
        // handling of player death (DEAD state)
        if (player.isDead()) {
            if (player.getState(Player.State.DEAD).isFinished()) {
                chamber.findAndRemoveEntity(player, false);
                player.removeFromUpdate();
                updateFinished = true;

                Data.increment("stats.deaths.totalDeaths.count");
                switch (player.getType()) {
                    case KNIGHT -> Data.increment("stats.deaths.characters.knight.count");
                    case NINJA -> Data.increment("stats.deaths.characters.ninja.count");
                    case ARCHER -> Data.increment("stats.deaths.characters.archer.count");
                }

                gamePanel.playerDeathDialog();
                return;
            }
        } else if (player.getCurrentHealth() <= 0) {
            PowerUp angelRing = player.getOwnedPowerUp(PowerUp.Type.ANGEL_RING);
            boolean canKill = true;
            if (angelRing != null) {
                canKill = !angelRing.canUse(); // if you can use the Angel Ring, you can't die
            }

            if (canKill && player.changeState(Player.State.DEAD)) {
                Sound.play(Sound.Effect.HUMAN_DEATH);
                player.kill();
            } else {
                int health = player.getHealth();
                player.increaseCurrentHealth(health);
                hudController.changeHealth(health);
            }
        }

        // handling of player sliding (GLIDE state)
        if (player.getState() == Player.State.GLIDE
                && player.getState(player.getState()).isFinished()
                && chamber.canCross(player, player.getDirection())
                && chamber.getEntityBelowTheTop(player) instanceof IcyFloor) {
            Entity previousEntityBelowTheTop = chamber.getEntityBelowTheTop(player);
            assert previousEntityBelowTheTop != null;
            chamber.moveDynamicEntity(player, player.getDirection());
            Entity nextEntityBelowTheTop = chamber.getEntityBelowTheTop(player);
            assert nextEntityBelowTheTop != null;

            switch (previousEntityBelowTheTop.getGenericType()) {
                case TRAP ->
                        trapsController.handleInteraction(Interaction.PLAYER_OUT, player, (Trap) previousEntityBelowTheTop);
                case DynamicEntity.Type.PROJECTILE ->
                        projectileController.handleInteraction(Interaction.PLAYER_IN, player, (Projectile) previousEntityBelowTheTop);
                default -> {
                }
            }
            switch (nextEntityBelowTheTop.getGenericType()) {
                case TRAP ->
                        trapsController.handleInteraction(Interaction.PLAYER_IN, player, (Trap) nextEntityBelowTheTop);
                case DynamicEntity.Type.PROJECTILE ->
                        projectileController.handleInteraction(Interaction.PLAYER_IN, player, (Projectile) nextEntityBelowTheTop);
                case Entity.Type.COLLECTABLE, Collectable.Type.POWER_UP ->
                        collectableController.handleInteraction(Interaction.PLAYER_IN, player, (Collectable) nextEntityBelowTheTop);
                default -> {
                }
            }
        }

        // IDLE
        if (player.getState() == Player.State.FALL
                && player.getState(Player.State.FALL).isFinished()
                && chamber.canCross(player, player.getDirection().getOpposite())) {
            player.changeState(Player.State.IDLE);
            chamber.moveDynamicEntity(player, player.getDirection().getOpposite());
            hitPlayer(-trap_damage);
            trap_damage = 0;
        } else if (player.getState() != Player.State.SLUDGE) {
            player.checkAndChangeState(Player.State.IDLE);
        }
    }

    public boolean updateFinished() {
        return updateFinished;
    }

    /**
     * Apply damage to the player and change its state to "HIT" if possible.
     *
     * @param damage the amount of damage to apply to the player
     */
    private void hitPlayer(int damage) {
        Sound.play(Sound.Effect.DAMAGE);
        PowerUp agility = player.getOwnedPowerUp(PowerUp.Type.AGILITY);
        boolean dodged = false;
        if (agility != null) {
            dodged = agility.canUse();
            if (dodged) {
                Log.info(player + " dodged the attack");
            }
        }

        HolyShield holyShield = (HolyShield) player.getOwnedPowerUp(PowerUp.Type.HOLY_SHIELD);
        int reduceDamage = 0;
        if (holyShield != null && holyShield.canUse()) {
            reduceDamage = (int) (damage * HolyShield.getDamageReductionMultiplier());
        }

        if (!dodged && player.changeState(Player.State.HIT)) {
            player.decreaseHealthShield(damage - reduceDamage);
            hudController.changeHealth(player.getCurrentHealth());
            hudController.changeShield(player.getCurrentShield());
        }
    }

    public void setEnemyController(EnemyController enemyController) {
        if (this.enemyController == null) {
            this.enemyController = enemyController;
        }
    }

    void setTrapController(TrapsController trapsController) {
        if (this.trapsController == null) {
            this.trapsController = trapsController;
        }
    }

    void setProjectileController(ProjectileController projectileController) {
        if (this.projectileController == null) {
            this.projectileController = projectileController;
        }
    }

    void setCollectableController(CollectableController collectableController) {
        if (this.collectableController == null) {
            this.collectableController = collectableController;
        }
    }

    void setEnvironmentController(EnvironmentController environmentController) {
        if (this.environmentController == null) {
            this.environmentController = environmentController;
        }
    }

    public void setHUDController(HUDController hudController) {
        if (this.hudController == null) {
            this.hudController = hudController;
            hudController.changeMaxHealth(player.getCurrentHealth(), player.getHealth());
            hudController.changeHealth(player.getCurrentHealth());
            hudController.changeMaxShield(player.getCurrentShield(), player.getShield());
            hudController.changeShield(player.getCurrentShield());
            hudController.changeMaxAttack(player.getMaxDamage());
            hudController.setTextAttackBar(player.getMinDamage() + " ~ " + player.getMaxDamage());
        }
    }
}
