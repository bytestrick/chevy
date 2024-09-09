package chevy.control.enemyController;

import chevy.control.Interaction;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Beetle;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.BigSlime;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy.Type;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Skeleton;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Wraith;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Zombie;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.model.entity.staticEntity.environment.traps.Trap;

/**
 * Gestisce il comportamento e le interazioni di vari tipi di nemici nel gioco.
 * Coordina i sotto controller specifici per ogni tipo di nemico
 * ({@link Wraith}, {@link Zombie}, {@link Slime}, {@link BigSlime}, {@link Skeleton},
 * {@link Beetle})
 * e gestisce le interazioni tra giocatore, proiettili e nemici.
 */
public final class EnemyController {
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
        wraithController = new WraithController(chamber, playerController);
        zombieController = new ZombieController(chamber, playerController);
        slimeController = new SlimeController(chamber, playerController);
        bigSlimeController = new BigSlimeController(chamber, playerController);
        skeletonController = new SkeletonController(chamber, playerController);
        beetleController = new BeetleController(chamber, playerController);
    }

    /**
     * Gestisce l'interazione di una trappola con un nemico
     *
     * @param trap  la trappola che interagisce con il nemico
     * @param enemy il nemico colpito dal proiettile
     */
    private static void trapInteraction(Trap trap, Enemy enemy) {
        switch (enemy.getType()) {
            case Type.WRAITH -> WraithController.trapInteraction(trap, (Wraith) enemy);
            case Type.SLIME -> SlimeController.trapInteraction(trap, (Slime) enemy);
            case Type.BIG_SLIME -> BigSlimeController.trapInteraction(trap, (BigSlime) enemy);
            case Type.ZOMBIE -> ZombieController.trapInteraction(trap, (Zombie) enemy);
            case Type.SKELETON -> SkeletonController.trapInteraction(trap, (Skeleton) enemy);
            case Type.BEETLE -> BeetleController.trapInteraction(trap, (Beetle) enemy);
            default -> {}
        }
    }

    /**
     * Gestisce l'interazione di un proiettile con un nemico
     *
     * @param projectile il proiettile che colpisce il nemico
     * @param enemy      il nemico colpito dal proiettile
     */
    private static void projectileInteraction(Projectile projectile, Enemy enemy) {
        switch (enemy.getType()) {
            case Type.WRAITH -> WraithController.projectileInteraction(projectile, (Wraith) enemy);
            case Type.SLIME -> SlimeController.projectileInteraction(projectile, (Slime) enemy);
            case Type.BIG_SLIME ->
                    BigSlimeController.projectileInteraction(projectile, (BigSlime) enemy);
            case Type.ZOMBIE -> ZombieController.projectileInteraction(projectile, (Zombie) enemy);
            case Type.SKELETON ->
                    SkeletonController.projectileInteraction(projectile, (Skeleton) enemy);
            case Type.BEETLE -> BeetleController.projectileInteraction(projectile, (Beetle) enemy);
            default -> {}
        }
    }

    /**
     * Gestisce l'interazione di un giocatore con un nemico
     *
     * @param player il giocatore che interagisce con il nemico
     * @param enemy  il nemico che subisce l'interazione
     */
    private static void playerInInteraction(Player player, Enemy enemy) {
        switch (enemy.getType()) {
            case Type.WRAITH -> WraithController.playerInInteraction(player, (Wraith) enemy);
            case Type.SLIME -> SlimeController.playerInInteraction(player, (Slime) enemy);
            case Type.BIG_SLIME -> BigSlimeController.playerInInteraction(player, (BigSlime) enemy);
            case Type.ZOMBIE -> ZombieController.playerInInteraction(player, (Zombie) enemy);
            case Type.SKELETON -> SkeletonController.playerInInteraction(player, (Skeleton) enemy);
            case Type.BEETLE -> BeetleController.playerInInteraction(player, (Beetle) enemy);
            default -> {}
        }
    }

    /**
     * Gestisce l'interazione tra entità dinamiche
     *
     * @param interaction il tipo di interazione da gestire
     * @param subject     l'entità che avvia l'interazione
     * @param object      l'entità che subisce l'interazione
     */
    public synchronized void handleInteraction(Interaction interaction, Entity subject,
                                               Enemy object) {
        switch (interaction) {
            case PLAYER_IN -> playerInInteraction((Player) subject, object);
            case UPDATE -> updateEnemy((Enemy) subject);
            case PROJECTILE -> projectileInteraction((Projectile) subject, object);
            case TRAP -> trapInteraction((Trap) subject, object);
            default -> {}
        }
    }

    /**
     * Aggiorna lo stato di un nemico a ogni ciclo di gioco
     *
     * @param enemy il nemico da aggiornare
     */
    private void updateEnemy(Enemy enemy) {
        switch (enemy.getType()) {
            case Type.WRAITH -> wraithController.update((Wraith) enemy);
            case Type.SLIME -> slimeController.update((Slime) enemy);
            case Type.ZOMBIE -> zombieController.update((Zombie) enemy);
            case Type.BIG_SLIME -> bigSlimeController.update((BigSlime) enemy);
            case Type.SKELETON -> skeletonController.update((Skeleton) enemy);
            case Type.BEETLE -> beetleController.update((Beetle) enemy);
            default -> {}
        }
    }
}