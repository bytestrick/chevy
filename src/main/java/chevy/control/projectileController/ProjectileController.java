package chevy.control.projectileController;

import chevy.control.Interaction;
import chevy.control.PlayerController;
import chevy.control.enemyController.EnemyController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.dynamicEntity.DynamicEntity;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.dynamicEntity.projectile.Arrow;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.model.entity.dynamicEntity.projectile.SlimeShot;

/**
 * Gestisce le interazioni e gli aggiornamenti dei proiettili nel gioco, delegando la gestione specifica a controller
 * più specifici come ArrowController e SlimeShotController.
 */
public final class ProjectileController {
    /**
     * Controller per la gestione delle frecce.
     */
    private final ArrowController arrowController;
    /**
     * Controller per la gestione degli slime shot.
     */
    private final SlimeShotController slimeShotController;

    /**
     * @param chamber          la stanza di gioco
     * @param playerController il controller del giocatore
     * @param enemyController  il controller dei nemici
     */
    public ProjectileController(Chamber chamber, PlayerController playerController, EnemyController enemyController) {
        arrowController = new ArrowController(chamber, playerController, enemyController);
        slimeShotController = new SlimeShotController(chamber, playerController, enemyController);
    }

    /**
     * Gestisce le interazioni dei proiettili in base al tipo di interazione e ai soggetti coinvolti.
     *
     * @param interaction il tipo di interazione da gestire
     * @param subject     l'entità che avvia l'interazione
     * @param object      l'entità che subisce l'interazione
     */
    public void handleInteraction(Interaction interaction, DynamicEntity subject, Projectile object) {
        switch (interaction) {
            case PLAYER_IN -> playerInInteraction((Player) subject, object);
            case UPDATE -> updateProjectile((Projectile) subject);
        }
    }

    /**
     * Gestisce l'interazione tra un giocatore e un proiettile, delegando il controllo al controller appropriato in
     * base al tipo di proiettile.
     *
     * @param player     giocatore che interagisce
     * @param projectile proiettile coinvolto nell'interazione
     */
    private void playerInInteraction(Player player, Projectile projectile) {
        switch (projectile.getType()) {
            case Projectile.Type.ARROW -> arrowController.playerInInteraction((Arrow) projectile);
            case Projectile.Type.SLIME_SHOT -> slimeShotController.playerInInteraction((SlimeShot) projectile);
            default -> { }
        }
    }

    /**
     * Aggiorna lo stato di un proiettile, delegando il controllo al controller appropriato in base al tipo di
     * proiettile.
     *
     * @param projectile proiettile da aggiornare
     */
    public synchronized void updateProjectile(Projectile projectile) {
        switch (projectile.getType()) {
            case Projectile.Type.ARROW -> arrowController.update((Arrow) projectile);
            case Projectile.Type.SLIME_SHOT -> slimeShotController.update((SlimeShot) projectile);
            default -> { }
        }
    }
}
