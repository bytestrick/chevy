package chevy.model.chamber;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce l'insieme di stanze (Chamber) nel gioco.
 * Utilizza il pattern Singleton per garantire che esista una sola istanza di ChamberManager.
 */
public class ChamberManager {
    /**
     * L'istanza Singleton di ChamberManager.
     */
    private static ChamberManager instance = null;
    /**
     * Lista delle stanze nel gioco.
     */
    List<Chamber> chambers;
    /**
     * Indice della stanza corrente nel gioco.
     */
    private int currentNChamber;

    private ChamberManager() {
        this.chambers = new ArrayList<>();
        this.currentNChamber = 0;
    }

    /**
     * Restituisce l'istanza Singleton di ChamberManager. Se non esiste, la crea.
     * @return L'istanza Singleton di ChamberManager
     */
    public static ChamberManager getInstance() {
        if (instance == null) {
            instance = new ChamberManager();
        }
        return instance;
    }

    /**
     * Crea una nuova stanza se necessario e la aggiunge alla lista delle stanze.
     * @param n numero della stanza da creare (rappresenta il livello)
     * @return true se la stanza è stata creata e caricata correttamente, false altrimenti
     */
    private boolean createChamber(int n) {
        if (n < chambers.size()) {
            return true;
        }

        Chamber chamber = new Chamber();
        boolean loaded = ChamberLoader.loadChamber(n, chamber);
        chambers.add(chamber);
        return loaded;
    }

    /**
     * Restituisce la prossima stanza nel gioco.
     * @return La prossima stanza, o null se non è possibile creare la prossima stanza
     */
    public Chamber getNextChamber() {
        Chamber chamber = null;
        if (createChamber(currentNChamber + 1)) {
            ++currentNChamber;
            chamber = chambers.get(currentNChamber);
        }
        return chamber;
    }

    /**
     * Restituisce la stanza corrente nel gioco.
     * @return La stanza corrente, o null se non è possibile creare la stanza corrente
     */
    public Chamber getCurrentChamber() {
        Chamber chamber = null;
        if (createChamber(currentNChamber)) {
            chamber = chambers.get(currentNChamber);
        }
        return chamber;
    }
}