package chevy.control.enemyController;

import chevy.control.InteractionTypes;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.service.Update;
import chevy.service.UpdateManager;
import chevy.settings.GameSettings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * La classe EnemyUpdateController è responsabile della gestione degli aggiornamenti
 * dei nemici nel gioco. Implementa l'interfaccia Update per integrarsi con il ciclo di
 * aggiornamento del gioco. Gestisce l'aggiunta, l'aggiornamento e la rimozione dei nemici
 * dall'aggiornamento.
 */
public class EnemyUpdateController implements Update {
    private final EnemyController enemyController;
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Enemy> enemiesToAdd;

    /**
     * Inizializza il controller degli aggiornamenti dei nemici con il controller dei nemici e una lista di nemici.
     * @param enemyController il controller dei nemici responsabile della gestione delle interazioni.
     * @param enemies la lista dei nemici da aggiungere.
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
     * @param delta il tempo trascorso dall'ultimo aggiornamento.
     */
    @Override
    public void update(double delta) {
        addEnemies();

        // Itera attraverso la lista dei nemici per aggiornarli e rimuove quelli che devono essere rimossi.
        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy enemy = it.next();
            enemyController.handleInteraction(InteractionTypes.UPDATE, enemy, null);
            if (enemy.canRemoveToUpdate())
                it.remove();
        }
    }

    /**
     * Verifica se l'aggiornamento è terminato, ovvero se non ci sono più nemici da aggiornare.
     * @return true se la lista dei nemici è vuota, false altrimenti.
     */
    @Override
    public boolean updateIsEnd() {
        return enemies.isEmpty();
    }
}
