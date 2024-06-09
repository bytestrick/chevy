package chevy.control;

import chevy.control.enemyController.EnemyController;
import chevy.control.projectileController.ProjectileController;
import chevy.control.trapsController.TrapsController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.DynamicEntityTypes;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntityTypes;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;
import chevy.model.entity.staticEntity.environment.traps.IcyFloor;
import chevy.model.entity.staticEntity.environment.traps.Traps;
import chevy.model.entity.staticEntity.environment.traps.TrapsTypes;
import chevy.model.entity.staticEntity.environment.traps.Void;

import java.awt.event.KeyEvent;

import static chevy.model.entity.staticEntity.environment.EnvironmentTypes.TRAP;

public class PlayerController {
    private Chamber chamber;
    private Player player;
    private EnemyController enemyController;
    private TrapsController trapsController;
    private ProjectileController projectileController;

    DirectionsModel direction;

    public PlayerController(Chamber chamber) {
        this.chamber = chamber;
        this.player = chamber.getPlayer();
        this.enemyController = null;
        this.trapsController = null;
        this.projectileController = null;
        this.direction = null;
    }


    public void keyPressed(KeyEvent keyEvent) {
        direction = switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_W -> DirectionsModel.UP;
            case KeyEvent.VK_A -> DirectionsModel.LEFT;
            case KeyEvent.VK_S -> DirectionsModel.DOWN;
            case KeyEvent.VK_D -> DirectionsModel.RIGHT;
            default -> null;
        };

        if (direction != null) {
            handleInteraction(InteractionType.KEYBOARD, direction);
        }
    }

    public synchronized void handleInteraction(InteractionType interaction, Object subject) {
        switch (interaction) {
            case KEYBOARD -> keyBoardInteraction((DirectionsModel) subject);
            case ENEMY -> enemyInteraction((Enemy) subject);
            case PROJECTILE -> projectileInteraction((Projectile) subject);
            case TRAP -> trapInteraction((Traps) subject);
        }
    }


    private void projectileInteraction(Projectile projectile) {
        hitPlayer(-1 * projectile.getDamage());
    }

    private void trapInteraction(Traps trap) {
        switch (trap.getSpecificType()) {
            case TrapsTypes.VOID -> {
                hitPlayer(-1 * trap.getDamage());
                if (player.isAlive() && chamber.canCross(player, direction.getOpposite()))
                    chamber.moveDynamicEntity(player, direction.getOpposite());
            }
            case TrapsTypes.ICY_FLOOR -> {
                while (player.getCurrentEumState() == PlayerStates.GLIDE &&
                        chamber.canCross(player, direction) &&
                        chamber.getEntityBelowTheTop(player) instanceof IcyFloor) {
                    chamber.moveDynamicEntity(player, direction);
                }

                player.changeState(PlayerStates.IDLE);
                if (chamber.getEntityBelowTheTop(player) instanceof Traps t)
                    trapsController.handleInteraction(InteractionType.PLAYER_IN, player, t);
                else if (chamber.getEntityBelowTheTop(player) instanceof Projectile p)
                    projectileController.handleInteraction(InteractionType.PLAYER_IN, player, p);
            }
            default -> {}
        }
    }

    private void keyBoardInteraction(DirectionsModel direction) {
        Entity entityNextCell = chamber.getNearEntityOnTop(player, direction);
        Entity entityCurrentCell = chamber.getEntityBelowTheTop(player);

        // Player on
        if (entityCurrentCell != null)
            switch (entityCurrentCell.getGenericType()) {
                case TRAP -> trapsController.handleInteraction(InteractionType.PLAYER, player, (Traps) entityCurrentCell);
                default -> {}
            }

        // Player in
        if (entityNextCell != null)
            switch (entityNextCell.getGenericType()) {
                case LiveEntityTypes.ENEMY -> {
                    if (player.changeState(PlayerStates.ATTACK))
                        enemyController.handleInteraction(InteractionType.PLAYER_IN, player, (Enemy) entityNextCell);
                    player.changeState(PlayerStates.IDLE);
                }
                case TRAP -> {
                    if (chamber.canCross(player, direction) && player.changeState(PlayerStates.MOVE)) {
                        chamber.moveDynamicEntity(player, direction);
                        trapsController.handleInteraction(InteractionType.PLAYER_IN, player, (Traps) entityNextCell);
                    }
                }
                case DynamicEntityTypes.PROJECTILE -> {
                    if (chamber.canCross(player, direction) && player.changeState(PlayerStates.MOVE)) {
                        chamber.moveDynamicEntity(player, direction);
                        projectileController.handleInteraction(InteractionType.PLAYER_IN, player, (Projectile) entityNextCell);
                    }
                }
                default -> {
                    if (chamber.canCross(player, direction) && player.changeState(PlayerStates.MOVE)) {
                        chamber.moveDynamicEntity(player, direction);
                        player.changeState(PlayerStates.IDLE);
                    }
                }
            }


        // Player out
        if (entityCurrentCell != null)
            switch (entityCurrentCell.getGenericType()) {
                case TRAP -> trapsController.handleInteraction(InteractionType.PLAYER_OUT, player, (Traps) entityCurrentCell);
                default -> {}
            }
    }

    private void enemyInteraction(Enemy enemy) {
        hitPlayer(-1 * enemy.getDamage());
    }


    private void hitPlayer(int damage) {
        if (player.changeState(PlayerStates.HIT))
            player.changeHealth(damage);
        if (!player.isAlive() && player.changeState(PlayerStates.DEAD))
            chamber.removeEntityOnTop(player);
        else
            player.changeState(PlayerStates.IDLE);
    }


    public void setEnemyController(EnemyController enemyController) {
        if (this.enemyController == null)
            this.enemyController = enemyController;
    }

    public void setTrapController(TrapsController trapsController) {
        if (this.trapsController == null)
            this.trapsController = trapsController;
    }

    public void setProjectileController(ProjectileController projectileController) {
        if (this.projectileController == null)
            this.projectileController = projectileController;
    }
}
