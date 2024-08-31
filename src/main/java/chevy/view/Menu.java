package chevy.view;

import chevy.model.chamber.ChamberManager;
import chevy.model.entity.dynamicEntity.liveEntity.player.Archer;
import chevy.model.entity.dynamicEntity.liveEntity.player.Knight;
import chevy.model.entity.dynamicEntity.liveEntity.player.Ninja;
import chevy.model.entity.dynamicEntity.liveEntity.player.Player;
import chevy.service.Data;
import chevy.service.Sound;
import chevy.utils.Load;
import chevy.utils.Log;
import chevy.utils.Utils;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.ListSelectionModel;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.stream.Stream;

public class Menu {
    private static final String[][] quotes = new String[][]{{"Ho giurato di proteggere il regno, "
            + "difendere i deboli e… sì, anche\ncercare di capire come si monta questa maledetta "
            + "armatura!",
            "Il mio cavallo è coraggioso, la mia spada è affilata, e io… beh,\n " + "io" + " " +
                    "mi sono già perso due volte cercando di arrivare qui."}, {"Precisione " +
            "millimetrica, " + "occhio di falco, respiro controllato...\nma se " + "c’è una " +
            "zanzara " + "che mi gira intorno, " + "non garantisco nulla.",
            "Posso colpire una " + "moneta a 100 " + "passi, ma non\nchiedermi di " + "trovare" + " le chiavi quando ho " + "fretta."}, {"Muovermi " + "nell’ombra, sparire " + "nel nulla, " + "essere " + "silenzioso\ncome il vento... e poi " + "inciampo su una " + "foglia secca.", "Mi " + "alleno" + " per anni a diventare un maestro del " + "silenzio.." + ".\ne poi la mia " + "pancia decide di " + "brontolare nel momento più critico."}};
    private static final Dimension spriteSize = new Dimension(200, 200);
    public static Player.Type playerType = Player.Type.valueOf(Data.get("$.state.menu.playerType"));
    final int[][] playerStats = new int[][]{new Knight(null).getStats(),
            new Archer(null).getStats(), new Ninja(null).getStats()};
    private final Image[][] sprites = new Image[3][2];
    private final Object updateAnimation = new Object();
    private final Window window;
    public JPanel root;
    private JComboBox<String> levelSelector;
    private final LevelSelectorRenderer levelSelectorRenderer =
            new LevelSelectorRenderer(levelSelector);
    private JButton play;
    private JButton playerCycleNext;
    private JButton playerCyclePrev;
    private JButton quit;
    private JButton options;
    private JLabel coins;
    private JLabel keys;
    private JRadioButton knightIndicator;
    private JRadioButton archerIndicator;
    private JRadioButton ninjaIndicator;
    final JRadioButton[] indicators = new JRadioButton[]{knightIndicator, archerIndicator,
            ninjaIndicator};
    private JPanel characterAnimation;
    private JLabel characterName;
    private JProgressBar healthBar;
    private JProgressBar damageBar;
    private JProgressBar speedBar;
    private JProgressBar shieldBar;
    private JLabel healthLabel;
    private JLabel shieldLabel;
    private JLabel damageLabel;
    private JLabel speedLabel;
    private int level = Data.get("$.state.menu.level");
    private boolean alternateAnimation = true;
    private boolean animationRunning = false;

    public Menu(final Window window) {
        this.window = window;
        applyProperties();
        loadCharacterSprites();
        setPlayerType(playerType);
        initListeners();
    }

    /**
     * Procedura che fa parte del costruttore: costruzione dell'interfaccia
     */
    private void applyProperties() {
        coins.setText(Data.get("$.progress.coins").toString());
        keys.setText(Data.get("$.progress.keys").toString());
        for (int i = 1; i < ChamberManager.NUMBER_OF_CHAMBERS + 1; ++i) {
            levelSelector.addItem("Livello " + i);
        }
        LevelSelectorRenderer.setEnabledInterval(0, Data.get("$.progress.lastUnlockedLevel"));
        final Font buttonFont = Window.handjet.deriveFont(40f);
        quit.setFont(buttonFont);
        play.setFont(buttonFont);
        playerCycleNext.setIcon(Load.icon("right-chevron", 32, 32));
        playerCyclePrev.setIcon(Load.icon("left-chevron", 32, 32));
        play.setIcon(Load.icon("Play", 32, 32));
        quit.setIcon(Load.icon("Exit", 32, 32));
        options.setIcon(Load.icon("Gear", 32, 32));
        coins.setIcon(Load.icon("Coin", 32, 32));
        keys.setIcon(Load.icon("Key", 32, 32));
        healthLabel.setIcon(Load.icon("heart", 32, 32));
        shieldLabel.setIcon(Load.icon("shield", 36, 36));
        damageLabel.setIcon(Load.icon("sword", 32, 32));
        speedLabel.setIcon(Load.icon("boot", 28, 28));
        characterName.setFont(Window.handjet.deriveFont(50f));
        healthBar.setForeground(new Color(153, 255, 153));
        shieldBar.setForeground(new Color(189, 189, 189));
        damageBar.setForeground(new Color(255, 80, 80));
        speedBar.setForeground(new Color(255, 255, 102));
    }

    /**
     * Procedura che fa parte del costruttore: inizializza i listener dei componenti
     * dell'interfaccia
     */
    private void initListeners() {
        options.addActionListener(e -> {
            window.setScene(Window.Scene.OPTIONS);
            stopCharacterAnimation();
        });

        play.addActionListener(actionEvent -> playAction());
        quit.addActionListener(e -> window.quitAction());
        playerCycleNext.addActionListener(e -> playerCycleNextAction());
        playerCyclePrev.addActionListener(e -> playerCyclePrevAction());

        levelSelector.addActionListener(actionEvent -> {
            final int i = levelSelector.getSelectedIndex();
            if (levelSelectorRenderer.isInsideInterval(i)) {
                level = i;
            } else {
                levelSelector.setSelectedIndex(level);
                levelSelector.showPopup();
            }
            Log.info("Cambiato livello: " + level);
        });

        Stream.of(playerCycleNext, playerCyclePrev, characterAnimation, coins, keys, options,
                healthBar, damageBar, speedBar, levelSelector, healthLabel, shieldLabel,
                damageLabel, speedLabel).forEach(jComponent -> jComponent.addMouseListener(new MouseAdapter() {
            /**
             * L'attesa iniziale affinché il tooltip si attivi è più breve.
             * Il tooltip persiste per un'ora se il cursore vi giace sopra per tanto.
             */
            public void mouseEntered(MouseEvent ignored) {
                ToolTipManager.sharedInstance().setInitialDelay(100);
                ToolTipManager.sharedInstance().setDismissDelay(3_600_000);
            }

            /**
             * Il tooltip viene nascosto non appena il cursore abbandona il componente.
             */
            public void mouseExited(MouseEvent ignored) {
                ToolTipManager.sharedInstance().setDismissDelay(0);
            }
        }));
    }

    /**
     * Risponde agli eventi passati dalla Window
     */
    public void handleKeyPress(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_ESCAPE -> window.quitAction();
            case KeyEvent.VK_ENTER -> playAction();
            case KeyEvent.VK_RIGHT -> playerCycleNextAction();
            case KeyEvent.VK_LEFT -> playerCyclePrevAction();
        }
    }

    /**
     * L'azione di passare alla scena PLAYING e avviare il gioco. È innescata dal JButton play e
     * dal tasto 'invio'
     * sulla tastiera.
     */
    private void playAction() {
        ChamberManager.enterChamber(level);
        stopCharacterAnimation();
        Sound.stopMenuMusic();
        window.setScene(Window.Scene.PLAYING);
    }

    /**
     * L'azione di passaggio al personaggio successivo, innescata dall'apposito JButton e dalla
     * freccia a destra
     * sulla tastiera.
     */
    private void playerCycleNextAction() {
        final Player.Type[] v = Player.Type.values();
        setPlayerType(v[(playerType.ordinal() + 1) % v.length]);
    }

    /**
     * L'azione di passaggio al personaggio precedente, innescata dall'apposito JButton e dalla
     * freccia a sinistra
     * sulla tastiera.
     */
    private void playerCyclePrevAction() {
        final Player.Type[] v = Player.Type.values();
        setPlayerType(playerType.ordinal() == 0 ? v[v.length - 1] : v[(playerType.ordinal() - 1)]);
    }

    /**
     * Controlla il selettore del personaggio
     *
     * @param type il tipo di giocatore tra KNIGHT, ARHCER e NINJA
     */
    private void setPlayerType(final Player.Type type) {
        indicators[playerType.ordinal()].setSelected(false);
        playerType = type;
        Data.set("$.state.menu.playerType", playerType.toString());
        Data.set("$.state.menu.level", level);
        final int p = playerType.ordinal();
        characterAnimation.setToolTipText(quotes[p][Utils.random.nextInt(quotes[p].length)]);
        characterName.setText(playerType.toString());
        indicators[p].setSelected(true);
        healthBar.setValue(playerStats[p][0]);
        shieldBar.setValue(playerStats[p][1]);
        damageBar.setValue(playerStats[p][2]);
        speedBar.setValue(playerStats[p][3]);
        String healthInfo = "Salute: " + playerStats[p][0] / 10;
        healthBar.setToolTipText(healthInfo);
        healthLabel.setToolTipText(healthInfo);
        String shieldInfo = "Scudo: " + playerStats[p][1] / 10;
        shieldBar.setToolTipText(shieldInfo);
        shieldLabel.setToolTipText(shieldInfo);
        String damageInfo = "Danno: " + playerStats[p][2] / 10;
        damageBar.setToolTipText(damageInfo);
        damageLabel.setToolTipText(damageInfo);
        String speedInfo = "Velocità: " + playerStats[p][3] / 10;
        speedBar.setToolTipText(speedInfo);
        speedLabel.setToolTipText(speedInfo);
        synchronized (updateAnimation) {
            updateAnimation.notify();
        }
    }

    /**
     * Carica le sprite usate per mostrare il personaggio
     */
    private void loadCharacterSprites() {
        for (Player.Type t : Player.Type.values()) {
            for (int f = 0; f < 2; ++f) { // 2 frame
                final String substring = t == Player.Type.KNIGHT ? "_16x16" : "";
                final String path = "/sprites/player/" + t.toString().toLowerCase() + "/idle/down"
                        + "/" + f + substring + ".png";
                final BufferedImage img = Load.image(path);
                sprites[t.ordinal()][f] = img.getScaledInstance(spriteSize.width,
                        spriteSize.height, Image.SCALE_SMOOTH);
            }
        }
    }

    /**
     * Crea un thread the anima il personaggio nel menu
     * Da usare quando si entra nella scena MENU.
     */
    public void startCharacterAnimation() {
        if (animationRunning) {
            Log.warn("Thread per l'animazione del personaggio già in esecuzione");
            return;
        }
        animationRunning = true;
        Thread.ofPlatform().start(() -> {
            Log.info("Thread per l'animazione del personaggio nel menù avviato");
            while (animationRunning) {
                characterAnimation.repaint();
                alternateAnimation = !alternateAnimation;

                // Ridisegna dopo un certo intervallo, tranne quando riceve updateAnimation
                // .notify(), in quel caso
                // ridisegna subito e questo evento è innescato dal cambiamento del personaggio
                // da parte dell'utente.
                synchronized (updateAnimation) {
                    try {
                        updateAnimation.wait(386);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            Log.info("Thread per l'animazione del personaggio nel menù terminato");
        });
    }

    /**
     * Cancella il thread che si occupa dell'animazione del personaggio
     * Da usare quando si lascia la scena MENU.
     */
    public void stopCharacterAnimation() {
        if (animationRunning) {
            synchronized (updateAnimation) {
                animationRunning = false;
                updateAnimation.notify(); // termina subito l'attesa
            }
        }
    }

    /**
     * Chiamato dal form dell'interfaccia per la creazione personalizzata dei componenti
     */
    private void createUIComponents() {
        root = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getBackground();
                g2d.setPaint(new LinearGradientPaint(0, 0, 0, getHeight(), new float[]{.1f, .8f,
                        1}, new Color[]{bg, new Color(70, 0, 94), new Color(122, 9, 161)}));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        characterAnimation = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (alternateAnimation) {
                    g.drawImage(sprites[playerType.ordinal()][0], 0, 0, spriteSize.width,
                            spriteSize.height, null);
                } else {
                    g.drawImage(sprites[playerType.ordinal()][1], 0, 0, spriteSize.width,
                            spriteSize.height, null);
                }
            }

            @Override
            public Point getToolTipLocation(MouseEvent event) {return getMousePosition();}
        };
    }

    /**
     * Rende possibile avere elementi non selezionabili in JComboBox.
     * <a href="https://stackoverflow.com/a/23724201">StackOverflow</a>.
     * Funziona in combinazione con l'ActionListener di Menu.levelSelector
     */
    public static class LevelSelectorRenderer extends BasicComboBoxRenderer {
        private static final ListSelectionModel model = new DefaultListSelectionModel();
        private static int enabledFirst = 0;
        private static int enabledLast = 0;

        private LevelSelectorRenderer(JComboBox<?> comboBox) {comboBox.setRenderer(this);}

        /**
         * Imposta l'intervallo di opzioni selezionabile in JComboBox
         *
         * @param first inizio dell'intervallo
         * @param last  fine dell'intervallo
         */
        public static void setEnabledInterval(int first, int last) {
            Data.set("$.progress.lastUnlockedLevel", last);
            enabledFirst = first;
            enabledLast = last;
            model.setSelectionInterval(first, last);
        }

        /**
         * @param x indice di un elemento del JComboBox
         * @return true se l'indice è compreso nell'intervallo degli elementi attivi, false
         * altrimenti
         */
        private boolean isInsideInterval(final int x) {return x >= enabledFirst && x <= enabledLast;}

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected,
                    cellHasFocus);
            if (!model.isSelectedIndex(index)) { // not enabled
                if (isSelected) {
                    c.setBackground(UIManager.getColor("ComboBox.background"));
                } else {
                    c.setBackground(super.getBackground());
                }
                c.setForeground(new Color(86, 86, 86));
            } else {
                c.setBackground(super.getBackground());
                c.setForeground(super.getForeground());
            }
            return c;
        }
    }
}