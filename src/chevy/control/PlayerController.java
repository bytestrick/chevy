package chevy.control;

import chevy.control.collectableController.CollectableController;
import chevy.control.enemyController.EnemyController;
import chevy.control.projectileController.ProjectileController;
import chevy.control.trapsController.TrapsController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.CommonEntityType;
import chevy.model.entity.Entity;
import chevy.model.entity.collectable.Collectable;
import chevy.model.entity.collectable.Health;
import chevy.model.entity.collectable.powerUp.PowerUp;
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
import chevy.model.entity.staticEntity.environment.traps.Trapdoor;
import chevy.service.Update;
import chevy.service.UpdateManager;
import chevy.utils.Log;

import java.awt.event.KeyEvent;

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
    private CollectableController collectableController;

    /**
     * @param chamber riferimento alla stanza di gioco
     */
    public PlayerController(Chamber chamber) {
        this.chamber = chamber;
        this.player = chamber.getPlayer();
        this.enemyController = null;
        this.trapsController = null;
        this.projectileController = null;

        // Aggiunge il controller del giocatore all'UpdateManager.
        UpdateManager.addToUpdate(this);
    }

    /**
     * Gestisce gli eventi di pressione dei tasti, convertendo il codice del tasto in una direzione.
     *
     * @param keyEvent l'evento di pressione del tasto
     */
    public void keyPressed(KeyEvent keyEvent) {
        DirectionsModel direction = switch (keyEvent.getKeyCode()) {
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

    /**
     * Gestisce le varie interazioni che il giocatore può subire.
     *
     * @param interaction il tipo di interazione
     * @param subject     l'oggetto con cui il giocatore interagisce
     */
    public synchronized void handleInteraction(InteractionType interaction, Object subject) {
        switch (interaction) {
            case KEYBOARD -> keyBoardInteraction((DirectionsModel) subject);
            case ENEMY -> enemyInteraction((Enemy) subject);
            case PROJECTILE -> projectileInteraction((Projectile) subject);
            case TRAP -> trapInteraction((Trap) subject);
            case COLLECTABLE -> collectableInteraction((Collectable) subject);
        }
    }

    /**
     * Gestisce le interazioni del giocatore con gli oggetti collezionabili.
     *
     * @param collectable collezionabile con cui il giocatore interagisce
     */
    private void collectableInteraction(Collectable collectable) {
        Collectable.Type collectableType = (Collectable.Type) collectable.getSpecificType();
        switch (collectableType) {
            case HEALTH -> {
                Health health = (Health) collectable;
                player.changeHealth(health.getRecoverHealth());
            }
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
        Trap.Type trapType = (Trap.Type) trap.getSpecificType();
        switch (trapType) {
            case VOID -> {
                hitPlayer(-1 * trap.getDamage());
            }
            case SPIKED_FLOOR -> {
                SpikedFloor spikedFloor = (SpikedFloor) trap;
                hitPlayer(-1 * spikedFloor.getDamage());
            }
            case TRAPDOOR -> {
                Trapdoor trapdoor = (Trapdoor) trap;
                hitPlayer(-1 * trapdoor.getDamage());

                player.changeState(Player.State.FALL);
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
        CommonEntityType entityNextCellEnum = entityNextCell.getGenericType();
        if (entityNextCellEnum == null) entityNextCellEnum = entityNextCell.getSpecificType();
        CommonEntityType entityCurrentCellEnum = entityCurrentCell.getGenericType();
        if (entityCurrentCellEnum == null) entityCurrentCellEnum = entityCurrentCell.getSpecificType();

        // Player on
        if (entityNextCellEnum != null) {
            switch (entityNextCellEnum) {
                case Environment.Type.TRAP ->
                        trapsController.handleInteraction(InteractionType.PLAYER, player, (Trap) entityCurrentCell);
                default -> { }
            }
        }

        // Player in
        if (entityNextCellEnum != null) {
            switch (entityNextCellEnum) {
                case LiveEntity.Type.ENEMY -> {
                    if (player.checkAndChangeState(Player.State.ATTACK))
                        enemyController.handleInteraction(InteractionType.PLAYER_IN, player, (Enemy) entityNextCell);
                    player.checkAndChangeState(Player.State.IDLE);
                }
                case Environment.Type.TRAP -> {
                    if (chamber.canCross(player, direction) && player.checkAndChangeState(Player.State.MOVE)) {
                        chamber.moveDynamicEntity(player, direction);
                        trapsController.handleInteraction(InteractionType.PLAYER_IN, player, (Trap) entityNextCell);
                    }
                }
                case DynamicEntity.Type.PROJECTILE -> {
                    if (chamber.canCross(player, direction) && player.checkAndChangeState(Player.State.MOVE)) {
                        chamber.moveDynamicEntity(player, direction);
                        projectileController.handleInteraction(InteractionType.PLAYER_IN, player,
                                (Projectile) entityNextCell);
                    }
                }
                case Entity.Type.COLLECTABLE, Collectable.Type.POWER_UP -> {
                    if (chamber.canCross(player, direction) && player.checkAndChangeState(Player.State.MOVE)) {
                        chamber.moveDynamicEntity(player, direction);
                        collectableController.handleInteraction(InteractionType.PLAYER_IN, player,
                                (Collectable) entityNextCell);
                    }
                }
                default -> {
                    if (chamber.canCross(player, direction) && player.checkAndChangeState(Player.State.MOVE)) {
                        chamber.moveDynamicEntity(player, direction);
                    }
                }
            }
        }

        // Player out
        if (entityCurrentCellEnum != null) switch (entityCurrentCellEnum) {
            case Environment.Type.TRAP ->
                    trapsController.handleInteraction(InteractionType.PLAYER_OUT, player, (Trap) entityCurrentCell);
            default -> { }
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
     * Aggiorna lo stato del giocatore a ogni ciclo di gioco.
     *
     * @param delta il tempo trascorso dall'ultimo aggiornamento
     */
    @Override
    public void update(double delta) {
        // gestione della morte del player (stato DEAD)
        if (player.isDead()) {
            if (player.getState(Player.State.DEAD).isFinished()) {
                chamber.findAndRemoveEntity(player);
                player.removeToUpdate();
                return;
            }
        } else if (player.getHealth() <= 0 && player.changeState(Player.State.DEAD)) {
            player.kill();
        }

        // gestione dello scivolamento del player (stato GLIDE)
        if (player.getCurrentState() == Player.State.GLIDE && player.getState(player.getCurrentState()).isFinished() && chamber.canCross(player, player.getDirection()) && chamber.getEntityBelowTheTop(player) instanceof IcyFloor) {

            Entity previousEntityBelowTheTop = chamber.getEntityBelowTheTop(player);
            chamber.moveDynamicEntity(player, player.getDirection());
            Entity nextEntityBelowTheTop = chamber.getEntityBelowTheTop(player);

            switch (previousEntityBelowTheTop.getGenericType()) {
                case Environment.Type.TRAP ->
                        trapsController.handleInteraction(InteractionType.PLAYER_OUT, player,
                                (Trap) previousEntityBelowTheTop);
                case DynamicEntity.Type.PROJECTILE ->
                        projectileController.handleInteraction(InteractionType.PLAYER_IN, player,
                                (Projectile) previousEntityBelowTheTop);
                default -> { }
            }
            switch (nextEntityBelowTheTop.getGenericType()) {
                case Environment.Type.TRAP ->
                        trapsController.handleInteraction(InteractionType.PLAYER_IN, player,
                                (Trap) nextEntityBelowTheTop);
                case DynamicEntity.Type.PROJECTILE ->
                        projectileController.handleInteraction(InteractionType.PLAYER_IN, player,
                                (Projectile) nextEntityBelowTheTop);
                default -> { }
            }
        }

        // IDLE
        if (player.getCurrentState() == Player.State.FALL && player.getState(Player.State.FALL).isFinished() && chamber.canCross(player, player.getDirection().getOpposite())) {
            chamber.moveDynamicEntity(player, player.getDirection().getOpposite());
            player.checkAndChangeState(Player.State.IDLE);
        } else if (player.getCurrentState() != Player.State.SLUDGE) {
            player.checkAndChangeState(Player.State.IDLE);
        }
    }

    /**
     * Applica danno al giocatore e cambia il suo stato a "HIT" se possibile.
     *
     * @param damage la quantità di danno da applicare
     */
    private void hitPlayer(int damage) {
        PowerUp agility = player.getOwnedPowerUp(PowerUp.Type.AGILITY);
        boolean dodged = false;
        if (agility != null) {
            dodged = agility.isOccurring();
            if (dodged) {
                Log.info(player + " ha schivato l'attacco");
            }
        }

        if (!dodged && player.changeState(Player.State.HIT)) {
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
        if (this.projectileController == null) {
            this.projectileController = projectileController;
        }
    }

    public void setCollectableController(CollectableController collectableController) {
        if (this.collectableController == null) {
            this.collectableController = collectableController;
        }
    }
}