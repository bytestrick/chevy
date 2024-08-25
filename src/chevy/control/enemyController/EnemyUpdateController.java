package chevy.control.enemyController;

import chevy.control.InteractionType;
import chevy.model.entity.dynamicEntity.liveEntity.enemy.Enemy;
import chevy.service.Update;
import chevy.service.UpdateManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Gestisce gli aggiornamenti dei nemici nel gioco.
 * Implementa l'interfaccia Update per integrarsi con il ciclo di aggiornamento del gioco.
 * Gestisce l'aggiunta, l'aggiornamento e la rimozione dei nemici dall'aggiornamento.
 */
public class EnemyUpdateController implements Update {
    private static boolean STOP_UPDATE = false;
    private final EnemyController enemyController;
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Enemy> enemiesToAdd;
    private boolean updateFinished = false;

    /**
     * @param enemyController il controller dei nemici responsabile della gestione delle interazioni.
     * @param enemies         la lista dei nemici da aggiungere.
     */
    public EnemyUpdateController(EnemyController enemyController, List<Enemy> enemies) {
        this.enemyController = enemyController;
        this.enemiesToAdd = enemies;

        // Aggiunge questo controller al gestore degli aggiornamenti.
        UpdateManager.addToUpdate(this);
    }

    /**
     * Aggiunge i nuovi nemici alla lista degli aggiornamenti e svuota la lista temporanea.
     */
    private void addEnemies() {
        this.enemies.addAll(enemiesToAdd);
        enemiesToAdd.clear();
    }

    /**
     * Aggiorna lo stato di tutti i nemici a ogni ciclo di gioco.
     *
     * @param delta il tempo trascorso dall'ultimo aggiornamento.
     */
    @Override
    public void update(double delta) {
        if (STOP_UPDATE) return;

        addEnemies();

        // Itera attraverso la lista dei nemici per aggiornarli e rimuove quelli che devono essere rimossi.
        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy enemy = it.next();
            enemyController.handleInteraction(InteractionType.UPDATE, enemy, null);
            if (enemy.canRemoveToUpdate()) {
                it.remove();
            }
        }
    }

    public void updateTerminate() {
        updateFinished = true;
    }

    public static void stopUpdate() {
        STOP_UPDATE = true;
    }

    public static void runUpdate() {
        STOP_UPDATE = false;
    }

    /**
     * Verifica se l'aggiornamento è terminato, ovvero se non ci sono più nemici da aggiornare.
     *
     * @return true se la lista dei nemici è vuota, false altrimenti.
     */
    @Override
    public boolean updateFinished() {
        return enemies.isEmpty() || updateFinished;
    }
}