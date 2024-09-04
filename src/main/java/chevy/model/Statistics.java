package chevy.model;

import chevy.service.Data;
import chevy.utils.Log;
import chevy.utils.Pair;

public class Statistics {
    private record InfoStat(String path, String name, String iconPath) {}
    public static class Statistic {
        private final Pair<InfoStat, Integer> stat;
        private int subStatistic = 0;   // indica il livello di una sotto statistica
                                        /*
                                         * Statistica a                 (Livello 0)
                                         * Statistica b                 (Livello 0)
                                         * - Statistica b.a             (Livello 1)
                                         * - Statistica b.b             (Livello 1)
                                         * Statistica c                 (Livello 0)
                                         * - Statistica c.a             (Livello 1)
                                         * - - Statistica c.a.a         (Livello 2)
                                         * - - Statistica c.a.b         (Livello 2)
                                         */

        public Statistic(String path, String name, String iconPath, Integer value) {
           this(path, name, iconPath, value, 0);
        }

        public Statistic(String path, String name, String iconPath, Integer value, int subStatistic) {
            this.stat = new Pair<>(
                    new InfoStat(path, name, iconPath),
                    value
            );
            this.subStatistic = subStatistic;
        }

        public int getSubStatistic() { return subStatistic; }
        public Integer getValue() { return stat.second; }
        public void setValue(int value) { stat.second = value; }
        public String getPath() { return stat.first.path(); }
        public String getName() { return stat.first.name(); }
        public String getIconPath() {  return stat.first.iconPath(); }
    }

    private static final String COMMON_PATH = "state.options.statistics.";
    private static final String COMMON_KILLS_PATH = COMMON_PATH + "enemyKills.";
    private static final String COMMON_DEATH_PATH = COMMON_PATH + "playerDeath.";
    private static final String COMMON_COLLECTABLES_PATH = COMMON_PATH + "collectibleCollected.";
    private static final String COMMON_POWER_UP_PATH = COMMON_COLLECTABLES_PATH + "powerUpCollected.";

    public static final int PLAYER_DEATH = 0;
    public static final int ARCHER_DEATH = 1;
    public static final int KNIGHT_DEATH = 2;
    public static final int NINJA_DEATH = 3;

    public static final int KILLED_ENEMY = 4;
    public static final int KILLED_BEETLE = 5;
    public static final int KILLED_BIG_SLIME = 6;
    public static final int KILLED_SKELETON = 7;
    public static final int KILLED_SLIME = 8;
    public static final int KILLED_WRAITH = 9;
    public static final int KILLED_ZOMBIE = 10;

    public static final int COLLECTED_COLLECTABLE = 11;
    public static final int COLLECTED_COIN = 12;
    public static final int COLLECTED_KEY = 13;
    public static final int COLLECTED_HEALTH_POTION = 14;
    public static final int COLLECTED_POWER_UP = 15;
    public static final int COLLECTED_AGILITY = 16;
    public static final int COLLECTED_ANGEL_RING = 17;
    public static final int COLLECTED_BROKEN_ARROWS = 18;
    public static final int COLLECTED_COIN_OF_GREED = 19;
    public static final int COLLECTED_COLD_HEART = 20;
    public static final int COLLECTED_GOLD_ARROWS = 21;
    public static final int COLLECTED_HEALING_FLOOD = 22;
    public static final int COLLECTED_HEDGEHOG_SPINES = 23;
    public static final int COLLECTED_HOBNAIL_BOOTS = 24;
    public static final int COLLECTED_HOLY_SHIELD = 25;
    public static final int COLLECTED_HOT_HEART = 26;
    public static final int COLLECTED_KEY_S_KEEPER = 27;
    public static final int COLLECTED_LONG_SWORD = 28;
    public static final int COLLECTED_SLIME_PIECE = 29;
    public static final int COLLECTED_STONE_BOOTS = 30;
    public static final int COLLECTED_VAMPIRE_FANGS = 31;

    private final static Statistic[] statistics = new Statistic[]{
            new Statistic(COMMON_DEATH_PATH + "total", "Morti totali", "Skull", 0),
            new Statistic(COMMON_DEATH_PATH + "archer", "Morti come arciere", "deathArcher", 0, 1),
            new Statistic(COMMON_DEATH_PATH + "knight", "Morti come cavaliere", "deathKnight", 0, 1),
            new Statistic(COMMON_DEATH_PATH + "ninja", "Morti come ninja", "deathNinja", 0, 1),

            new Statistic(COMMON_KILLS_PATH + "total", "Uccisioni totali", "sword", 0),
            new Statistic(COMMON_KILLS_PATH + "beetle", "Scarafaggi uccisi", "beetle", 0, 1),
            new Statistic(COMMON_KILLS_PATH + "bigSlime", "Slime grandi uccisi", "bigSlime", 0, 1),
            new Statistic(COMMON_KILLS_PATH + "skeleton", "Scheletri uccisi", "skeleton", 0, 1),
            new Statistic(COMMON_KILLS_PATH + "slime", "Slime uccisi", "slime", 0, 1),
            new Statistic(COMMON_KILLS_PATH + "wraith", "Fantasmi uccisi", "wraith", 0, 1),
            new Statistic(COMMON_KILLS_PATH + "zombie", "Zombie uccisi", "zombie", 0, 1),

            new Statistic(COMMON_COLLECTABLES_PATH + "total", "Collezionabili raccolti totali", "Backpack", 0),
            new Statistic(COMMON_COLLECTABLES_PATH + "coins", "Monete raccolte", "coin3", 0, 1),
            new Statistic(COMMON_COLLECTABLES_PATH + "keys", "Chiavi raccolte", "key2", 0, 1),
            new Statistic(COMMON_COLLECTABLES_PATH + "healthPotions", "Pozioni di cura raccolte", "health", 0, 1),

            new Statistic(COMMON_POWER_UP_PATH + "total", "Potenziamenti raccolti totali", "powerUp", 0, 1),
            new Statistic(COMMON_POWER_UP_PATH + "agility", "Potenziamenti agilità raccolti", "agility", 0, 2),
            new Statistic(COMMON_POWER_UP_PATH + "angelRing", "Anelli dell'angelo raccolti", "angelRing", 0, 2),
            new Statistic(COMMON_POWER_UP_PATH + "brokenArrows", "Frecce rotte raccolte", "brokenArrows", 0, 2),
            new Statistic(COMMON_POWER_UP_PATH + "coinOfGreed", "Monete dell'avidità raccolte", "coinOfGreed", 0, 2),
            new Statistic(COMMON_POWER_UP_PATH + "coldHeart", "Cuori freddi raccolti", "coldHeart", 0, 2),
            new Statistic(COMMON_POWER_UP_PATH + "goldenArrows", "Frecce d'oro raccolte", "goldenArrows", 0, 2),
            new Statistic(COMMON_POWER_UP_PATH + "healingFlood", "Inondazioni curative raccolte", "healingFlood", 0, 2),
            new Statistic(COMMON_POWER_UP_PATH + "hedgehogSpines", "Spine di riccio raccolte", "hedgehogSpines", 0, 2),
            new Statistic(COMMON_POWER_UP_PATH + "hobnailBoots", "Stivali chiodati raccolti", "hobnailBoots", 0, 2),
            new Statistic(COMMON_POWER_UP_PATH + "holyShield", "Scudi sacri raccolti", "holyShield", 0, 2),
            new Statistic(COMMON_POWER_UP_PATH + "hotHeart", "Cuori caldi raccolti", "hotHeart", 0, 2),
            new Statistic(COMMON_POWER_UP_PATH + "keySKeeper", "Chiavi del guardiano raccolte", "keySKeeper", 0, 2),
            new Statistic(COMMON_POWER_UP_PATH + "longSword", "Spade lunghe raccolte", "longSword", 0, 2),
            new Statistic(COMMON_POWER_UP_PATH + "slimePiece", "Pezzi di slime raccolti", "slimePiece", 0, 2),
            new Statistic(COMMON_POWER_UP_PATH + "stoneBoots", "Stivali di pietra raccolti", "stoneBoots", 0, 2),
            new Statistic(COMMON_POWER_UP_PATH + "vampireFangs", "Zanne di vampiro raccolte", "vampireFangs", 0, 2)
    };


    static public void increase(int stat, int value) {
        statistics[stat].setValue(statistics[stat].getValue() + value);
    }

    /**
     * Ritorna il valore salvato della statistica
     * @param stat indice della statistica
     */
    public static int getInt(int stat) {
        return Data.get(statistics[stat].getPath());
    }

    public static void save(int stat) {
        Object value = Data.get(statistics[stat].getPath());
        if (value instanceof Integer intValue) {
            Data.set(statistics[stat].getPath(), intValue + statistics[stat].getValue());
            statistics[stat].setValue(0);
        }
        else {
            Log.error("Statistica id:" + stat + " NON salvata");
            return;
        }
        Log.info("Statistica id:" + stat + " salvata");
    }

    public static void saveAll() {
        for (int i = 0; i < statistics.length; ++i) {
            save(i);
        }
    }

    public static void clear(int stat) {
        statistics[stat].setValue(0);
        Log.warn("Statistica id:" + stat + " resettata");
    }

    public static void clearAll() {
        for (int i = 0; i < statistics.length; ++i) {
            clear(i);
        }
    }

    public static Statistic[] getStatistic() {
        return statistics;
    }
}
