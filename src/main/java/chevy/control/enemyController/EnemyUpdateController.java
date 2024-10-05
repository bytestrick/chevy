package chevy.control.enemyController;

import chevy.control.Interaction;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy;
import chevy.service.Updatable;
import chevy.service.UpdateManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Gestisce gli aggiornamenti dei nemici nel gioco.
 * Implementa l'interfaccia Updatable per integrarsi con il ciclo di aggiornamento del gioco.
 * Gestisce l'aggiunta, l'aggiornamento e la rimozione dei nemici dall'aggiornamento.
 */
public final class EnemyUpdateController implements Updatable {
    private boolean running, paused;
    private final EnemyController enemyController;
    private final Collection<Enemy> enemies = new ArrayList<>();
    private final List<Enemy> enemiesToAdd;

    /**
     * @param enemyController il controller dei nemici responsabile della gestione delle
     *                        interazioni.
     * @param enemies         la lista dei nemici da aggiungere.
     */
    public EnemyUpdateController(EnemyController enemyController, List<Enemy> enemies) {
        this.enemyController = enemyController;
        enemiesToAdd = enemies;
        running = true;
        paused = false;

        // Aggiunge questo controller al gestore degli aggiornamenti.
        UpdateManager.register(this);
    }

    public void stopUpdate() { running = false; }

    public void setPaused(boolean paused) { this.paused = paused; }

    /**
     * Aggiunge i nuovi nemici alla lista degli aggiornamenti e svuota la lista temporanea.
     */
    private void addEnemies() {
        enemies.addAll(enemiesToAdd);
        enemiesToAdd.clear();
    }

    /**
     * Aggiorna lo stato di tutti i nemici a ogni ciclo di gioco.
     */
    @Override
    public void update(double delta) {
        if (paused) {
            return;
        }
        addEnemies();

        // Itera attraverso la lista dei nemici per aggiornarli e rimuove quelli che devono
        // essere rimossi.
        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy enemy = it.next();
            enemyController.handleInteraction(Interaction.UPDATE, enemy, null);
            if (enemy.shouldNotUpdate()) {
                it.remove();
            }
        }
    }

    /**
     * Verifica se l'aggiornamento è terminato, ovvero se non ci sono più nemici da aggiornare.
     *
     * @return {@code true} se la lista dei nemici è vuota, {@code false} altrimenti.
     */
    @Override
    public boolean updateFinished() {return enemies.isEmpty() || !running;}
}