package chevy.control.enemyController;

import chevy.control.Interaction;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dynamicEntity.Direction;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Beetle;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.model.entity.dynamicEntity.projectile.Projectile;
import chevy.model.entity.dynamicEntity.projectile.SlimeShot;
import chevy.model.entity.staticEntity.environment.traps.Trap;
import chevy.service.Data;
import chevy.service.Sound;
import chevy.utils.Log;

/**
 * Gestisce il comportamento e le interazioni di un nemico di tipo {@link Beetle} all'interno del
 * gioco. Si occupa di come il Beetle reagisce alle azioni del giocatore, ai proiettili, e
 * gestisce i suoi aggiornamenti di stato e movimento.
 */
final class BeetleController {
    /**
     * Riferimento alla stanza di gioco in cui si trova il Beetle. Utilizzato per verificare le
     * posizioni,
     * aggiungere/rimuovere entità
     */
    private final Chamber chamber;
    /**
     * Riferimento al controller del giocatore, utilizzato per interagire con il giocatore.
     */
    private final PlayerController playerController;

    /**
     * @param chamber          la stanza di gioco
     * @param playerController il controller del giocatore
     */
    BeetleController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }

    /**
     * Gestisce le interazioni del Beetle con il giocatore.
     *
     * @param player il giocatore che interagisce con il Beetle
     * @param beetle il Beetle che subisce l'interazione
     */
    static void playerInInteraction(Player player, Beetle beetle) {
        // Se il giocatore è in stato di attacco, il Beetle viene danneggiato in base al danno
        // del giocatore.
        if (player.getState().equals(Player.State.ATTACK)) {
            beetle.setDirection(Direction.positionToDirection(player, beetle));
            hitBeetle(beetle, -1 * player.getDamage());
            Sound.play(Sound.Effect.ROBOTIC_INSECT);
        } else {
            Log.warn("Il BeetleController non gestisce questa azione: " + player.getState());
        }
    }

    /**
     * Gestisce le interazioni del Beetle con i proiettili.
     *
     * @param projectile il proiettile che colpisce il Beetle
     * @param beetle     il Beetle che subisce l'interazione
     */
    static void projectileInteraction(Projectile projectile, Beetle beetle) {
        beetle.setDirection(Direction.positionToDirection(projectile, beetle));
        hitBeetle(beetle, -1 * projectile.getDamage());
        Sound.play(Sound.Effect.ROBOTIC_INSECT);
    }

    /**
     * Gestisce le interazioni del Beetle con le trappole.
     *
     * @param trap   la trappola che interagisce con il beetle
     * @param beetle il Beetle che subisce l'interazione
     */
    static void trapInteraction(Trap trap, Beetle beetle) {
        if (trap.getType().equals(Trap.Type.SPIKED_FLOOR)) {
            hitBeetle(beetle, -1 * trap.getDamage());
        }
    }

    /**
     * Inferisce danno al Beetle e cambia il suo stato a "colpito" se possibile.
     *
     * @param beetle il Beetle che subisce il danno
     * @param damage la quantità di danno da applicare
     */
    private static void hitBeetle(Beetle beetle, int damage) {
        if (beetle.changeState(Beetle.State.HIT)) {
            beetle.decreaseHealthShield(damage);
        }
    }

    /**
     * Aggiorna lo stato del Beetle a ogni ciclo di gioco. Gestisce il cambiamento di stato, il
     * movimento e le azioni
     * del Beetle
     *
     * @param beetle il Beetle da aggiornare.
     */
    void update(Beetle beetle) {
        // Se il Beetle non è vivo e il suo stato "DEAD" è terminato, viene rimosso dalla stanza.
        if (beetle.isDead()) {
            if (beetle.getState(Beetle.State.DEAD).isFinished()) {
                chamber.removeEntityOnTop(beetle);
                beetle.removeFromUpdate();
                chamber.decreaseEnemyCounter();
                chamber.spawnCollectable(beetle);
                Data.increment("stats.kills.totalKills.count");
                Data.increment("stats.kills.enemies.beetle.count");
                return;
            }
        } else if (beetle.getCurrentHealth() <= 0 && beetle.checkAndChangeState(Beetle.State.DEAD)) {
            // Se la salute del Beetle è zero o inferiore, cambia lo stato del Beetle a "DEAD".
            chamber.spawnSlime(beetle); // power up
            Sound.play(Sound.Effect.BEETLE_DEATH);
            beetle.kill();
        }

        // Se il Beetle può cambiare lo stato a "MOVE", cerca il giocatore nelle vicinanze.
        if (beetle.canChange(Beetle.State.MOVE)) {
            Direction direction = chamber.getDirectionToHitPlayer(beetle, 3);
            // Se trova il giocatore, inizia l'inseguimento (chasing).
            if (direction == null) {
                if (chamber.chase(beetle)) {
                    beetle.setCanAttack(false);
                    beetle.changeState(Beetle.State.MOVE);
                }
            } else if (beetle.canChange(Beetle.State.ATTACK)) {
                // Se può cambiare lo stato a "ATTACK", cerca di attaccare il giocatore.
                for (int distance = 1; distance <= 3; ++distance) {
                    Entity entity = chamber.getEntityNearOnTop(beetle, direction, distance);
                    // muoviti in modo casuale, non sparare, se tra te è il player c'è un
                    // ostacolo che non può essere attraversato
                    if (!(entity instanceof Player) && !entity.isCrossable()) {
                        if (beetle.checkAndChangeState(Beetle.State.MOVE)) {
                            chamber.moveRandomPlus(beetle);
                        }
                        break;
                    }

                    if (distance > 1 && entity instanceof Player && beetle.changeState(Beetle.State.ATTACK)) {
                        Projectile slimeShot = new SlimeShot(beetle.getPosition(), direction);
                        chamber.addProjectile(slimeShot);
                        chamber.addEntityOnTop(slimeShot);
                        break;
                    } else if (distance == 1 && entity instanceof Player && beetle.changeState(Beetle.State.ATTACK)) {
                        Sound.play(Sound.Effect.BEETLE_ATTACK);
                        beetle.setCanAttack(true);
                    }
                }
            }
        }

        if (beetle.canAttack() && beetle.getState(Beetle.State.ATTACK).isFinished()) {
            Direction direction = chamber.getDirectionToHitPlayer(beetle);
            if (direction != null) {
                Entity entity = chamber.getEntityNearOnTop(beetle, direction);
                if (entity instanceof Player) {
                    playerController.handleInteraction(Interaction.ENEMY, beetle);
                    beetle.setCanAttack(false);
                }
            }
        }

        if (beetle.checkAndChangeState(Beetle.State.IDLE)) {
            beetle.setCanAttack(false);
        }
    }
}
