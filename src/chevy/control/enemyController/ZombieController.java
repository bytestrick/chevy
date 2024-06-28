package chevy.control.enemyController;

import chevy.control.InteractionTypes;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Zombie;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;

/**
 * La classe ZombieController è responsabile della gestione del comportamento e delle interazioni
 * del nemico Zombie all'interno del gioco. Gestisce come lo Zombie risponde agli attacchi del giocatore,
 * ai colpi dei proiettili e coordina il suo stato e i suoi movimenti.
 */
public class ZombieController {
    /**
     * Riferimento alla stanza di gioco in cui si trova lo Zombie. Utilizzato per verificare le posizioni,
     * aggiungere/rimuovere entità.
     */
    private final Chamber chamber;
    /**
     * Riferimento al controller del giocatore, utilizzato per gestire le interazioni tra lo Zombie e il giocatore.
     */
    private final PlayerController playerController;

    /**
     * Inizializza il controller con i riferimenti alla stanza di gioco e al controller del giocatore.
     * @param chamber riferimento della stanza
     * @param playerController riferimento al controllo del giocatore
     */
    public ZombieController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }

    /**
     * Gestisce le interazioni dello Zombie con il giocatore.
     * @param player il giocatore che interagisce con lo Zombie.
     * @param zombie lo Zombie che subisce l'interazione.
     */
    public void playerInInteraction(Player player, Zombie zombie) {
        switch (player.getCurrentEumState()) {
            // Se il giocatore è in stato di attacco, lo Zombie viene danneggiato in base al danno del giocatore.
            case Player.EnumState.ATTACK -> {
                zombie.setDirection(DirectionsModel.positionToDirection(player, zombie));
                hitZombie(zombie, -1 * player.getDamage());
            }
            default -> System.out.println("[!] Lo ZombieController non gestisce questa azione: " + player.getCurrentEumState());
        }
    }

    /**
     * Aggiorna lo stato dello Zombie a ogni ciclo di gioco.
     * @param zombie lo Zombie da aggiornare.
     */
    public void update(Zombie zombie) {
        if (!zombie.isAlive()) {
            if (zombie.getState(Zombie.EnumState.DEAD).isFinished()) {
                chamber.removeEntityOnTop(zombie);
                zombie.removeToUpdate();
                return;
            }
        } else if (zombie.getHealth() <= 0 && zombie.checkAndChangeState(Zombie.EnumState.DEAD)) {
            zombie.kill();
        }

        if (zombie.canChange(Zombie.EnumState.MOVE)) {
            DirectionsModel direction = chamber.getHitDirectionPlayer(zombie);
            if (direction == null) {
                // Se non c'è un giocatore nelle vicinanze, lo Zombie vaga casualmente.
                if (chamber.wanderChase(zombie, 4)) {
                    zombie.changeState(Zombie.EnumState.MOVE);
                }
            } else if (zombie.canChange(Zombie.EnumState.ATTACK)) {
                Entity entity = chamber.getNearEntityOnTop(zombie, direction);
                if (entity instanceof Player && zombie.changeState(Zombie.EnumState.ATTACK)) {
                    playerController.handleInteraction(InteractionTypes.ENEMY, zombie);
                }
            }
        }
        zombie.checkAndChangeState(Zombie.EnumState.IDLE);
    }

    /**
     * Gestisce le interazioni dello Zombie con i proiettili.
     * @param projectile il proiettile che colpisce lo Zombie.
     * @param zombie lo Zombie che subisce l'interazione.
     */
    public void projectileInteraction(Projectile projectile, Zombie zombie) {
        zombie.setDirection(DirectionsModel.positionToDirection(projectile, zombie));
        hitZombie(zombie, -1 * projectile.getDamage());
    }

    /**
     * Applica danno allo Zombie e aggiorna il suo stato se possibile.
     * @param zombie lo Zombie che subisce il danno.
     * @param damage il danno da applicare.
     */
    private void hitZombie(Zombie zombie, int damage) {
        if (zombie.changeState(Zombie.EnumState.HIT))
            zombie.changeHealth(damage);
    }
}
