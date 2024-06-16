package chevy.control;

import chevy.control.enemyController.EnemyController;
import chevy.control.projectileController.ProjectileController;
import chevy.control.trapsController.TrapsController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.traps.IcyFloor;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.service.Update;
import chevy.service.UpdateManager;

import java.awt.event.KeyEvent;

import static chevy.model.entity.staticEntity.environment.Environment.Type.TRAP;

public class PlayerController implements Update {
    private Chamber chamber;
    private Player player;
    private EnemyController enemyController;
    private TrapsController trapsController;
    private ProjectileController projectileController;
    private DirectionsModel direction;


    public PlayerController(Chamber chamber) {
        this.chamber = chamber;
        this.player = chamber.getPlayer();
        this.enemyController = null;
        this.trapsController = null;
        this.projectileController = null;
        this.direction = null;

        UpdateManager.addToUpdate(this);
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
            handleInteraction(InteractionTypes.KEYBOARD, direction);
        }
    }

    public synchronized void handleInteraction(InteractionTypes interaction, Object subject) {
        switch (interaction) {
            case KEYBOARD -> keyBoardInteraction((DirectionsModel) subject);
            case ENEMY -> enemyInteraction((Enemy) subject);
            case PROJECTILE -> projectileInteraction((Projectile) subject);
            case TRAP -> trapInteraction((Trap) subject);
        }
    }

    private void projectileInteraction(Projectile projectile) {
        hitPlayer(-1 * projectile.getDamage());
    }

    private void trapInteraction(Trap trap) {
        switch (trap.getSpecificType()) {
            case Trap.Type.VOID -> {
                hitPlayer(-1 * trap.getDamage());
                if (player.isAlive() && chamber.canCross(player, direction.getOpposite()))
                    chamber.moveDynamicEntity(player, direction.getOpposite());
            }
            case Trap.Type.ICY_FLOOR -> {
                while (player.getCurrentEumState() == Player.EnumState.GLIDE &&
                        chamber.canCross(player, direction) &&
                        chamber.getEntityBelowTheTop(player) instanceof IcyFloor) {
                    chamber.moveDynamicEntity(player, direction);
                }

                player.checkAndChangeState(Player.EnumState.IDLE);
                if (chamber.getEntityBelowTheTop(player) instanceof Trap t)
                    trapsController.handleInteraction(InteractionTypes.PLAYER_IN, player, t);
                else if (chamber.getEntityBelowTheTop(player) instanceof Projectile p)
                    projectileController.handleInteraction(InteractionTypes.PLAYER_IN, player, p);
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
                case TRAP -> trapsController.handleInteraction(InteractionTypes.PLAYER, player, (Trap) entityCurrentCell);
                default -> {}
            }

        // Player in
        if (entityNextCell != null)
            switch (entityNextCell.getGenericType()) {
                case LiveEntity.Type.ENEMY -> {
                    if (player.checkAndChangeState(Player.EnumState.ATTACK))
                        enemyController.handleInteraction(InteractionTypes.PLAYER_IN, player, (Enemy) entityNextCell);
                    player.checkAndChangeState(Player.EnumState.IDLE);
                }
                case TRAP -> {
                    if (chamber.canCross(player, direction) && player.checkAndChangeState(Player.EnumState.MOVE)) {
                        chamber.moveDynamicEntity(player, direction);
                        trapsController.handleInteraction(InteractionTypes.PLAYER_IN, player, (Trap) entityNextCell);
                    }
                }
                case DynamicEntity.Type.PROJECTILE -> {
                    if (chamber.canCross(player, direction) && player.checkAndChangeState(Player.EnumState.MOVE)) {
                        chamber.moveDynamicEntity(player, direction);
                        projectileController.handleInteraction(InteractionTypes.PLAYER_IN, player, (Projectile) entityNextCell);
                    }
                }
                default -> {
                    if (chamber.canCross(player, direction) && player.checkAndChangeState(Player.EnumState.MOVE)) {
                        chamber.moveDynamicEntity(player, direction);
                    }
                }
            }


        // Player out
        if (entityCurrentCell != null)
            switch (entityCurrentCell.getGenericType()) {
                case TRAP -> trapsController.handleInteraction(InteractionTypes.PLAYER_OUT, player, (Trap) entityCurrentCell);
                default -> {}
            }
    }

    private void enemyInteraction(Enemy enemy) {
        hitPlayer(-1 * enemy.getDamage());
    }


    private void hitPlayer(int damage) {
        if (player.changeState(Player.EnumState.HIT))
            player.changeHealth(damage);
        if (!player.isAlive() && player.changeState(Player.EnumState.DEAD))
            chamber.removeEntityOnTop(player);
        else
            player.changeState(Player.EnumState.IDLE);
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

    @Override
    public void update(double delta) {
        player.checkAndChangeState(Player.EnumState.IDLE);
    }
}
