package chevy.control;

import chevy.control.enemyController.EnemyController;
import chevy.control.enemyController.InteractionType;
import chevy.control.trapsController.TrapsController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.LiveEntityTypes;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.stateMachine.PlayerStates;
import chevy.model.entity.staticEntity.environment.EnvironmentTypes;
import chevy.model.entity.staticEntity.environment.traps.IcyFloor;
import chevy.model.entity.staticEntity.environment.traps.Traps;

import java.awt.event.KeyEvent;

public class PlayerController {
    private Chamber chamber;
    private Player player;
    private EnemyController enemyController;
    private TrapsController trapsController;

    public PlayerController(Chamber chamber, EnemyController enemyController) {
        this.chamber = chamber;
        this.player = chamber.getPlayer();
        this.enemyController = enemyController;
    }


    public void keyPressed(KeyEvent keyEvent) {
        DirectionsModel direction = switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_W -> DirectionsModel.UP;
            case KeyEvent.VK_A -> DirectionsModel.LEFT;
            case KeyEvent.VK_S -> DirectionsModel.DOWN;
            case KeyEvent.VK_D -> DirectionsModel.RIGHT;
            default -> null;
        };

        if (direction != null) {
            keyBoardInteraction(direction);
        }
    }

    private void keyBoardInteraction(DirectionsModel direction) {
        Entity entityNextCell = chamber.getNearEntity(player, direction);
        Entity entityCurrentCell = chamber.getEntityBelowTheTop(player);

        // Player on
        if (entityCurrentCell != null)
            switch (entityCurrentCell.getGenericType()) {
                case EnvironmentTypes.TRAP -> trapsController.handleInteraction(InteractionType.PLAYER, player, (Traps) entityCurrentCell);
                default -> {}
            }

        // Player in
        if (entityNextCell != null)
            switch (entityNextCell.getGenericType()) {
                case LiveEntityTypes.ENEMY -> {
                    player.changeState(PlayerStates.ATTACK);
                    enemyController.handleInteraction(InteractionType.PLAYER_IN, player, (Enemy) entityNextCell);
                }
                case EnvironmentTypes.TRAP -> {
                    if (chamber.canCross(player, direction) && player.changeState(PlayerStates.MOVE)) {
                        chamber.moveDynamicEntity(player, direction);
                    }
                    trapsController.handleInteraction(InteractionType.PLAYER_IN, player, (Traps) entityNextCell);
                }
                default -> {}
            }

        // gestione dello scivolamento
        if (player.getCurrentEumState() == PlayerStates.GLIDE) {
            do {
                chamber.moveDynamicEntity(player, direction);
            } while (chamber.getNearEntity(player, direction) instanceof IcyFloor
                    && chamber.canCross(player, direction)
                    && player.changeState(PlayerStates.GLIDE));
            if (chamber.canCross(player, direction) && player.changeState(PlayerStates.GLIDE)) {
                chamber.moveDynamicEntity(player, direction);
            }
            player.changeState(PlayerStates.IDLE);
        }
        else if (chamber.canCross(player, direction) && player.changeState(PlayerStates.MOVE)) {
            chamber.moveDynamicEntity(player, direction);
            player.changeState(PlayerStates.IDLE);
        }

        if (player.getCurrentEumState() == PlayerStates.FALL) {
                chamber.moveDynamicEntity(player, direction.getOpposite());
                player.changeState(PlayerStates.IDLE);
        }

        player.changeState(PlayerStates.IDLE);


        // Player out
        if (entityCurrentCell != null)
            switch (entityCurrentCell.getGenericType()) {
                case EnvironmentTypes.TRAP -> trapsController.handleInteraction(InteractionType.PLAYER_OUT, player, (Traps) entityCurrentCell);
                default -> {}
            }
    }

    public void enemyInteraction(Enemy enemy) {
        if (player.changeState(PlayerStates.HIT))
            player.changeHealth(-1 * enemy.getDamage());
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
}
