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
import chevy.model.entity.staticEntity.environment.Environment;
import chevy.model.entity.staticEntity.environment.traps.IcyFloor;
import chevy.model.entity.staticEntity.environment.traps.SpikedFloor;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.service.Update;
import chevy.service.UpdateManager;

import java.awt.event.KeyEvent;

import static chevy.model.entity.staticEntity.environment.Environment.Type.TRAP;

/**
 * Gestisce le interazioni del giocatore con i nemici, i proiettili e le trappole.
 * Implementa l'interfaccia Update per aggiornare lo stato del giocatore a ogni ciclo di gioco.
 */
public class PlayerController implements Update {
    /**
     * Riferimento alla stanza di gioco.
     */
    private final Chamber chamber;
    private final Player player;
    private EnemyController enemyController;
    private TrapsController trapsController;
    private ProjectileController projectileController;
    private DirectionsModel direction;

    /**
     * @param chamber riferimento alla stanza di gioco
     */
    public PlayerController(Chamber chamber) {
        this.chamber = chamber;
        this.player = chamber.getPlayer();
        this.enemyController = null;
        this.trapsController = null;
        this.projectileController = null;
        this.direction = null;

        // Aggiunge il controller del giocatore all'UpdateManager.
        UpdateManager.addToUpdate(this);
    }

    /**
     * Gestisce gli eventi di pressione dei tasti, convertendo il codice del tasto in una direzione.
     *
     * @param keyEvent l'evento di pressione del tasto
     */
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

    /**
     * Gestisce le varie interazioni che il giocatore può subire.
     *
     * @param interaction il tipo di interazione
     * @param subject     l'oggetto con cui il giocatore interagisce
     */
    public synchronized void handleInteraction(InteractionTypes interaction, Object subject) {
        switch (interaction) {
            case KEYBOARD -> keyBoardInteraction((DirectionsModel) subject);
            case ENEMY -> enemyInteraction((Enemy) subject);
            case PROJECTILE -> projectileInteraction((Projectile) subject);
            case TRAP -> trapInteraction((Trap) subject);
        }
    }

    /**
     * Gestisce le interazioni del giocatore con i proiettili, applicando il danno al giocatore.
     *
     * @param projectile il proiettile con cui il giocatore interagisce
     */
    private void projectileInteraction(Projectile projectile) {
        hitPlayer(-1 * projectile.getDamage());
    }

    /**
     * Gestisce le interazioni delle trappole con il giocatore
     *
     * @param trap la trappola con cui il giocatore interagisce
     */
    private void trapInteraction(Trap trap) {
        switch (trap.getSpecificType()) {
            case Trap.Type.VOID -> {
                hitPlayer(-1 * trap.getDamage());
                if (player.isAlive() && chamber.canCross(player, direction.getOpposite())) {
                    chamber.moveDynamicEntity(player, direction.getOpposite());
                }
            }
            case Trap.Type.SPIKED_FLOOR -> {
                SpikedFloor spikedFloor = (SpikedFloor) trap;
                hitPlayer(-1 * spikedFloor.getDamage());
            }
            default -> { }
        }
    }

    /**
     * Gestisce le interazioni della tastiera con il giocatore, gestendo i movimenti del giocatore e
     * le interazioni con le entità nelle celle adiacenti.
     *
     * @param direction la direzione in cui il giocatore si muove
     */
    private void keyBoardInteraction(DirectionsModel direction) {
        Entity entityNextCell = chamber.getNearEntityOnTop(player, direction);
        Entity entityCurrentCell = chamber.getEntityBelowTheTop(player);

        // Player on
        if (entityCurrentCell != null) {
            switch (entityCurrentCell.getGenericType()) {
                case TRAP ->
                        trapsController.handleInteraction(InteractionTypes.PLAYER, player, (Trap) entityCurrentCell);
                default -> { }
            }
        }

        // Player in
        if (entityNextCell != null) switch (entityNextCell.getGenericType()) {
            case LiveEntity.Type.ENEMY -> {
                if (player.checkAndChangeState(Player.States.ATTACK)) {
                    enemyController.handleInteraction(InteractionTypes.PLAYER_IN, player, (Enemy) entityNextCell);
                }
                player.checkAndChangeState(Player.States.IDLE);
            }
            case TRAP -> {
                if (chamber.canCross(player, direction) && player.checkAndChangeState(Player.States.MOVE)) {
                    chamber.moveDynamicEntity(player, direction);
                    trapsController.handleInteraction(InteractionTypes.PLAYER_IN, player, entityNextCell);
                }
            }
            case DynamicEntity.Type.PROJECTILE -> {
                if (chamber.canCross(player, direction) && player.checkAndChangeState(Player.States.MOVE)) {
                    chamber.moveDynamicEntity(player, direction);
                    projectileController.handleInteraction(InteractionTypes.PLAYER_IN, player,
                            (Projectile) entityNextCell);
                }
            }
            default -> {
                if (chamber.canCross(player, direction) && player.checkAndChangeState(Player.States.MOVE)) {
                    chamber.moveDynamicEntity(player, direction);
                }
            }
        }

        // Player out
        if (entityCurrentCell != null) {
            switch (entityCurrentCell.getGenericType()) {
                case TRAP -> trapsController.handleInteraction(InteractionTypes.PLAYER_OUT, player, entityCurrentCell);
                default -> { }
            }
        }
    }

    /**
     * Gestisce le interazioni dei nemici con il giocatore, applicando il danno al giocatore.
     *
     * @param enemy il nemico con cui il giocatore interagisce
     */
    private void enemyInteraction(Enemy enemy) {
        hitPlayer(-1 * enemy.getDamage());
    }

    /**
     * Applica danno al giocatore e cambia il suo stato a "HIT" se possibile.
     *
     * @param damage la quantità di danno da inferire
     */
    private void hitPlayer(int damage) {
        if (player.changeState(Player.States.HIT)) {
            player.changeHealth(damage);
        }
    }

    /**
     * Imposta il controller dei nemici.
     *
     * @param enemyController il controller dei nemici
     */
    public void setEnemyController(EnemyController enemyController) {
        if (this.enemyController == null) {
            this.enemyController = enemyController;
        }
    }

    /**
     * Imposta il controller delle trappole.
     *
     * @param trapsController il controller delle trappole
     */
    public void setTrapController(TrapsController trapsController) {
        if (this.trapsController == null) {
            this.trapsController = trapsController;
        }
    }

    /**
     * Imposta il controller dei proiettili.
     *
     * @param projectileController il controller dei proiettili
     */
    public void setProjectileController(ProjectileController projectileController) {
        if (this.projectileController == null) this.projectileController = projectileController;
    }

    /**
     * Aggiorna lo stato del giocatore a ogni ciclo di gioco.
     *
     * @param delta il tempo trascorso dall'ultimo aggiornamento
     */
    @Override
    public void update(double delta) {
        // gestione della morte del player
        if (!player.isAlive()) {
            if (player.getState(Player.States.DEAD).isFinished()) {
                chamber.removeEntityOnTop(player);
                player.removeToUpdate();
                return;
            }
        } else if (player.getHealth() <= 0 && player.changeState(Player.States.DEAD)) {
            player.kill();
        }

        // gestione dello scivolamento del player
        if (player.getCurrentEumState() == Player.States.GLIDE && player.getState(player.getCurrentEumState()).isFinished() && chamber.canCross(player, direction) && chamber.getEntityBelowTheTop(player) instanceof IcyFloor) {
            Entity previousEntityBelowTheTop = chamber.getEntityBelowTheTop(player);
            chamber.moveDynamicEntity(player, direction);
            Entity nextEntityBelowTheTop = chamber.getEntityBelowTheTop(player);

            switch (previousEntityBelowTheTop.getGenericType()) {
                case Environment.Type.TRAP ->
                        trapsController.handleInteraction(InteractionTypes.PLAYER_OUT, player,
                                previousEntityBelowTheTop);
                case DynamicEntity.Type.PROJECTILE ->
                        projectileController.handleInteraction(InteractionTypes.PLAYER_IN, player,
                                (Projectile) previousEntityBelowTheTop);
                default -> { }
            }
            switch (nextEntityBelowTheTop.getGenericType()) {
                case Environment.Type.TRAP ->
                        trapsController.handleInteraction(InteractionTypes.PLAYER_IN, player, nextEntityBelowTheTop);
                case DynamicEntity.Type.PROJECTILE ->
                        projectileController.handleInteraction(InteractionTypes.PLAYER_IN, player,
                                (Projectile) nextEntityBelowTheTop);
                default -> { }
            }
        }

        // idle
        if (player.getCurrentEumState() != Player.States.SLUDGE) {
            player.checkAndChangeState(Player.States.IDLE);
        }
    }
}