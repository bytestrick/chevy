package chevy.control.enemyController;

import chevy.control.InteractionType;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Beetle;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.BigSlime;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Skeleton;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Wraith;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Zombie;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.traps.Trap;

/**
 * Gestisce il comportamento e le interazioni di vari tipi di nemici nel gioco.
 * Coordina i sotto controller specifici per ogni tipo di nemico (Wraith, Zombie, Slime, BigSlime, Skeleton, Beetle)
 * e gestisce le interazioni tra giocatore, proiettili e nemici.
 */
public class EnemyController {
    private final WraithController wraithController;
    private final ZombieController zombieController;
    private final SlimeController slimeController;
    private final BigSlimeController bigSlimeController;
    private final SkeletonController skeletonController;
    private final BeetleController beetleController;

    /**
     * @param chamber          la stanza di gioco contenente i nemici
     * @param playerController il controller del giocatore
     */
    public EnemyController(Chamber chamber, PlayerController playerController) {
        this.wraithController = new WraithController(chamber, playerController);
        this.zombieController = new ZombieController(chamber, playerController);
        this.slimeController = new SlimeController(chamber, playerController);
        this.bigSlimeController = new BigSlimeController(chamber, playerController);
        this.skeletonController = new SkeletonController(chamber, playerController);
        this.beetleController = new BeetleController(chamber, playerController);
    }

    /**
     * Gestisce l'interazione tra entità dinamiche.
     *
     * @param interaction il tipo di interazione da gestire
     * @param subject     l'entità che avvia l'interazione
     * @param object      l'entità che subisce l'interazione
     */
    public synchronized void handleInteraction(InteractionType interaction, Entity subject, Enemy object) {
        switch (interaction) {
            case PLAYER_IN -> playerInInteraction((Player) subject, object);
            case UPDATE -> updateEnemy((Enemy) subject);
            case PROJECTILE -> projectileInteraction((Projectile) subject, object);
            case TRAP -> trapInteraction((Trap) subject, object);
            default -> {}
        }
    }

    /**
     * Gestisce l'interazione di una trappoòa con un nemico.
     *
     * @param trap  la trappola che interagisce con il nemico
     * @param enemy il nemico colpito dal proiettile
     */
    private void trapInteraction(Trap trap, Enemy enemy) {
        switch (enemy.getSpecificType()) {
            case Enemy.Type.WRAITH -> wraithController.trapInteraction(trap, (Wraith) enemy);
            case Enemy.Type.SLIME -> slimeController.trapInteraction(trap, (Slime) enemy);
            case Enemy.Type.BIG_SLIME -> bigSlimeController.trapInteraction(trap, (BigSlime) enemy);
            case Enemy.Type.ZOMBIE -> zombieController.trapInteraction(trap, (Zombie) enemy);
            case Enemy.Type.SKELETON -> skeletonController.trapInteraction(trap, (Skeleton) enemy);
            case Enemy.Type.BEETLE -> beetleController.trapInteraction(trap, (Beetle) enemy);
            default -> { }
        }
    }

    /**
     * Gestisce l'interazione di un proiettile con un nemico.
     *
     * @param projectile il proiettile che colpisce il nemico
     * @param enemy      il nemico colpito dal proiettile
     */
    private void projectileInteraction(Projectile projectile, Enemy enemy) {
        switch (enemy.getSpecificType()) {
            case Enemy.Type.WRAITH -> wraithController.projectileInteraction(projectile, (Wraith) enemy);
            case Enemy.Type.SLIME -> slimeController.projectileInteraction(projectile, (Slime) enemy);
            case Enemy.Type.BIG_SLIME -> bigSlimeController.projectileInteraction(projectile, (BigSlime) enemy);
            case Enemy.Type.ZOMBIE -> zombieController.projectileInteraction(projectile, (Zombie) enemy);
            case Enemy.Type.SKELETON -> skeletonController.projectileInteraction(projectile, (Skeleton) enemy);
            case Enemy.Type.BEETLE -> beetleController.projectileInteraction(projectile, (Beetle) enemy);
            default -> { }
        }
    }

    /**
     * Gestisce l'interazione di un giocatore con un nemico.
     *
     * @param player il giocatore che interagisce con il nemico
     * @param enemy  il nemico che subisce l'interazione
     */
    private void playerInInteraction(Player player, Enemy enemy) {
        switch (enemy.getSpecificType()) {
            case Enemy.Type.WRAITH -> wraithController.playerInInteraction(player, (Wraith) enemy);
            case Enemy.Type.SLIME -> slimeController.playerInInteraction(player, (Slime) enemy);
            case Enemy.Type.BIG_SLIME -> bigSlimeController.playerInInteraction(player, (BigSlime) enemy);
            case Enemy.Type.ZOMBIE -> zombieController.playerInInteraction(player, (Zombie) enemy);
            case Enemy.Type.SKELETON -> skeletonController.playerInInteraction(player, (Skeleton) enemy);
            case Enemy.Type.BEETLE -> beetleController.playerInInteraction(player, (Beetle) enemy);
            default -> { }
        }
    }

    /**
     * Aggiorna lo stato di un nemico a ogni ciclo di gioco.
     *
     * @param enemy il nemico da aggiornare
     */
    private void updateEnemy(Enemy enemy) {
        switch (enemy.getSpecificType()) {
            case Enemy.Type.WRAITH -> wraithController.update((Wraith) enemy);
            case Enemy.Type.SLIME -> slimeController.update((Slime) enemy);
            case Enemy.Type.ZOMBIE -> zombieController.update((Zombie) enemy);
            case Enemy.Type.BIG_SLIME -> bigSlimeController.update((BigSlime) enemy);
            case Enemy.Type.SKELETON -> skeletonController.update((Skeleton) enemy);
            case Enemy.Type.BEETLE -> beetleController.update((Beetle) enemy);
            default -> { }
        }
    }
}
