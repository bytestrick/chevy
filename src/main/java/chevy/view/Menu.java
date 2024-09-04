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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
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
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.stream.Stream;

public final class Menu {
    public static final ImageIcon ex = Load.icon("x", 48, 48);
    // @formatter:off
    private static final String[][] quotes = new String[][]{
            {"“Ho giurato di proteggere il regno, difendere i deboli e… sì, anche\n" +
                "cercare di capire come si monta questa maledetta armatura!”",
             "“Il mio cavallo è coraggioso, la mia spada è affilata, e io… beh,\n" +
                "io mi sono già perso due volte cercando di arrivare qui.”"},
            {"“Precisione millimetrica, occhio di falco, respiro controllato...\n" +
                "ma se c’è una zanzara che mi gira intorno, non garantisco nulla.”",
             "“Posso colpire una moneta a 100 passi, ma non\n" +
                "chiedermi di trovare le chiavi quando ho fretta.”"},
            {"“Muovermi nell’ombra, sparire nel nulla, essere silenzioso\n" +
                "come il vento... e poi inciampo su una foglia secca.”",
             "“Mi alleno per anni a diventare un maestro del silenzio...\n" +
                "e poi la mia pancia decide di brontolare nel momento più critico.”"}};
    // @formatter:on
    private static final Dimension spriteSize = new Dimension(200, 200);
    private static final Color[] barsColors = new Color[]{new Color(153, 255, 153), new Color(189
            , 189, 189), new Color(255, 80, 80), new Color(255, 255, 102)};
    private static final Color progressBarDimmedForeground = new Color(144, 144, 144);
    private static final int archerCost = 500, ninjaCost = 1000;
    private static final ImageIcon coin = Load.icon("Coin", 32, 32);
    private static final ImageIcon[] statsIcons = new ImageIcon[]{Load.icon("heart", 32, 32),
            Load.icon("shield", 36, 36), Load.icon("sword", 32, 32), Load.icon("boot", 28, 28)};
    private static final ImageIcon[] statsIconsGreyScale = new ImageIcon[]{Load.icon(
            "heart_greyscale", 32, 32), Load.icon("shield_greyscale", 36, 36), Load.icon(
            "sword_greyscale", 32, 32), Load.icon("boot_greyscale", 28, 28)};
    private static final String[] statsTooltipPrefixes = new String[]{"Salute: ", "Scudo: ",
            "Danno: ", "Velocità: "};
    public static Player.Type playerType = Player.Type.valueOf(Data.get("state.menu.playerType"));
    private static int level = Data.get("state.menu.level");
    private static boolean currentPlayerLocked;
    final int[][] playerStats = new int[][]{new Knight(null).getStats(),
            new Archer(null).getStats(), new Ninja(null).getStats()};
    private final Image[][] sprites = new Image[3][4];
    private final Object updateAnimation = new Object();
    private final Window window;
    public JPanel root;
    private JComboBox<String> levelSelector;
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
    private final JRadioButton[] indicators = new JRadioButton[]{knightIndicator, archerIndicator,
            ninjaIndicator};
    private JPanel characterAnimation;
    private JLabel characterName;
    private JProgressBar healthBar;
    private JProgressBar damageBar;
    private JProgressBar speedBar;
    private JProgressBar shieldBar;
    private final JProgressBar[] bars = new JProgressBar[]{healthBar, shieldBar, damageBar,
            speedBar};
    private JLabel healthLabel;
    private JLabel shieldLabel;
    private JLabel damageLabel;
    private JLabel speedLabel;
    private final JLabel[] statsLabels = new JLabel[]{healthLabel, shieldLabel, damageLabel,
            speedLabel};
    private JButton unlock;
    private JLabel cost;
    private boolean alternateAnimation = true;
    private boolean animationRunning;

    public Menu(final Window window) {
        this.window = window;
        attachListeners();
        initializeComponents();
        loadCharactersSprites();
        setPlayerType(playerType);
    }

    public static void incrementLevel() {
        ++level;
        Data.set("progress.lastUnlockedLevel", level);
        LevelSelectorRenderer.setEnabledInterval(0, level);
    }

    /**
     * Impostare l'elemento attivo per JComboBox richiede che il componente sia visibile
     */
    public void updateLevel() {levelSelector.setSelectedIndex(level);}

    /**
     * Procedura del costruttore: costruzione dell'interfaccia
     */
    private void initializeComponents() {
        root.setBackground(new Color(33, 6, 47));
        coins.setText(Data.get("progress.coins").toString());
        keys.setText(Data.get("progress.keys").toString());
        levelSelector.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        levelSelector.setRenderer(new LevelSelectorRenderer());
        LevelSelectorRenderer.setEnabledInterval(0, Data.get("progress.lastUnlockedLevel"));
        for (int i = 1; i < ChamberManager.NUMBER_OF_CHAMBERS + 1; ++i) {
            levelSelector.addItem("Livello " + i);
        }
        final Font menuFont = UIManager.getFont("defaultFont").deriveFont(35f);
        Stream.of(quit, play, keys, cost, coins, unlock).forEach(c -> c.setFont(menuFont));
        playerCycleNext.setIcon(Load.icon("right-chevron", 32, 32));
        playerCyclePrev.setIcon(Load.icon("left-chevron", 32, 32));
        play.setIcon(Load.icon("Play", 32, 32));
        quit.setIcon(Load.icon("Exit", 32, 32));
        options.setIcon(Load.icon("Gear", 32, 32));
        coins.setIcon(Load.icon("Coin2", 32, 32));
        keys.setIcon(Load.icon("Key", 32, 32));
        cost.setIcon(coin);
        characterName.setFont(UIManager.getFont("defaultFont").deriveFont(Font.BOLD, 50f));
        unlock.setIcon(Load.icon("Unlocked", 30, 30));
    }

    /**
     * Procedura del costruttore: inizializza i listener dei componenti
     * dell'interfaccia
     */
    private void attachListeners() {
        options.addActionListener(e -> {
            Sound.play(Sound.Effect.BUTTON);
            window.setScene(Window.Scene.OPTIONS);
            stopCharacterAnimation();
        });
        play.addActionListener(actionEvent -> {
            Sound.play(Sound.Effect.PLAY_BUTTON);
            playAction();
        });
        quit.addActionListener(e -> {
            Sound.play(Sound.Effect.BUTTON);
            window.quitAction();
        });
        playerCycleNext.addActionListener(e -> {
            Sound.play(Sound.Effect.BUTTON);
            playerCycleNextAction();
        });
        playerCyclePrev.addActionListener(e -> {
            Sound.play(Sound.Effect.BUTTON);
            playerCyclePrevAction();
        });
        levelSelector.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {}

            @Override
            public void mousePressed(MouseEvent mouseEvent) {Sound.play(Sound.Effect.BUTTON);}

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {}

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {}

            @Override
            public void mouseExited(MouseEvent mouseEvent) {}
        });
        levelSelector.addActionListener(actionEvent -> changeLevelAction());
        unlock.addActionListener(e -> {
            Sound.play(Sound.Effect.BUTTON);
            unlockPlayerAction();
        });

        Stream<JComponent> components = Stream.of(playerCycleNext, playerCyclePrev,
                characterAnimation, coins, keys, options, healthBar, shieldBar, damageBar,
                speedBar, levelSelector, healthLabel, shieldLabel, damageLabel, speedLabel, play,
                characterName);
        components.forEach(component -> component.addMouseListener(new TooltipMouseAdapter()));
    }

    private void changeLevelAction() {
        final int i = levelSelector.getSelectedIndex();
        if (LevelSelectorRenderer.isInsideInterval(i)) {
            level = i;
            Data.set("state.menu.level", level);
            Log.info("Cambiato livello: " + level);
        } else {
            levelSelector.hidePopup();
            levelSelector.setSelectedIndex(level);
            Sound.play(Sound.Effect.STOP);
            JOptionPane.showMessageDialog(window, "Il livello " + (i + 1) + " è bloccato.", null,
                    JOptionPane.WARNING_MESSAGE, ex);
            SwingUtilities.invokeLater(levelSelector::showPopup);
        }
    }

    private void unlockPlayerAction() {
        int actualCost = playerType == Player.Type.ARCHER ? archerCost : ninjaCost;
        if (JOptionPane.showOptionDialog(window,
                "Sbloccare " + playerType + " per " + actualCost + " monete?", null,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, Load.icon("Unlocked", 48
                        , 48), new String[]{"Si", "No"}, "No") == 0) {
            if (actualCost > (int) Data.get("progress.coins")) {
                Sound.play(Sound.Effect.STOP);
                JOptionPane.showMessageDialog(window, "Monete insufficienti", null,
                        JOptionPane.WARNING_MESSAGE, ex);
                return;
            }
            Sound.play(Sound.Effect.UNLOCK_CHARACTER);
            currentPlayerLocked = false;
            Data.set("progress.player." + playerType.toString().toLowerCase() + ".locked", false);
            int coinsLeft = (int) Data.get("progress.coins") - actualCost;
            Data.set("progress.coins", coinsLeft);
            coins.setText(Integer.toString(coinsLeft));
            setPlayerType(playerType);
        }
    }

    /**
     * Risponde agli eventi passati da Window
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
        Data.set("state.menu.playerType", playerType.toString());
        Data.set("state.menu.level", level);
        final int p = playerType.ordinal();
        characterName.setText(playerType.toString());
        indicators[p].setSelected(true);
        for (int i = 0; i < bars.length; ++i) {
            bars[i].setValue(playerStats[p][i]);
            final String statTooltip = statsTooltipPrefixes[i] + playerStats[p][i] / 10;
            bars[i].setToolTipText(statTooltip);
            statsLabels[i].setToolTipText(statTooltip);
        }
        synchronized (updateAnimation) {
            updateAnimation.notify();
        }
        currentPlayerLocked =
                Data.get("progress.player." + playerType.toString().toLowerCase() + ".locked");
        if (currentPlayerLocked) {
            characterName.setToolTipText("Bloccato");
            characterAnimation.setToolTipText(null);
            play.setEnabled(false);
            play.setToolTipText(playerType + " è bloccato");
            unlock.setVisible(true);
            cost.setText(String.valueOf(playerType == Player.Type.ARCHER ? archerCost : ninjaCost));
            cost.setVisible(true);
            for (int i = 0; i < bars.length; ++i) {
                bars[i].setForeground(progressBarDimmedForeground);
                statsLabels[i].setIcon(statsIconsGreyScale[i]);
            }
        } else {
            characterAnimation.setToolTipText(quotes[p][Utils.random.nextInt(quotes[p].length)]);
            characterName.setToolTipText(null);
            play.setEnabled(true);
            play.setToolTipText(null);
            unlock.setVisible(false);
            cost.setVisible(false);
            for (int i = 0; i < bars.length; ++i) {
                bars[i].setForeground(barsColors[i]);
                statsLabels[i].setIcon(statsIcons[i]);
            }
        }
    }

    /**
     * Carica le sprite usate per mostrare il personaggio
     */
    private void loadCharactersSprites() {
        for (Player.Type t : Player.Type.values()) {
            String substring = "";
            int nSprites = 2;
            if (t == Player.Type.KNIGHT) {
                substring = "_16x16";
            } else {
                nSprites = 4;
            }
            for (int f = 0; f < nSprites; ++f) { // 2 frame
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
                // .notify(), in quel caso  ridisegna subito e questo evento è innescato dal
                // cambiamento del personaggio da parte dell'utente.
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
        final Color purple = UIManager.getColor("Chevy.color.purpleDark");
        final Color pink = UIManager.getColor("Chevy.color.pinkDark");
        root = new JPanel() {
            private static final float[] fractions = new float[]{0f, .6f, 1f};
            private final Color[] colors = new Color[]{purple, purple, pink};

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setPaint(new LinearGradientPaint(0, 0, getWidth() * .1f, getHeight(),
                        fractions, colors));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        characterAnimation = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int i = 0;
                if (alternateAnimation) {
                    i = 1;
                }
                if (playerType != Player.Type.KNIGHT && currentPlayerLocked) {
                    i += 2;
                }
                g.drawImage(sprites[playerType.ordinal()][i], 0, 0, spriteSize.width,
                        spriteSize.height, null);
            }

            @Override
            public Point getToolTipLocation(MouseEvent event) {return getMousePosition();}
        };
    }

    /**
     * Rende possibile avere elementi non selezionabili in JComboBox (
     * <a href="https://stackoverflow.com/a/23724201">StackOverflow</a>).
     * <p>
     * Funziona in combinazione con l'{@link java.awt.event.ActionListener} di
     * {@link #levelSelector}
     */
    private static class LevelSelectorRenderer extends BasicComboBoxRenderer {
        private static final ListSelectionModel model = new DefaultListSelectionModel();
        private static int enabledFirst;
        private static int enabledLast;

        /**
         * Imposta l'intervallo di opzioni selezionabile in JComboBox
         *
         * @param first inizio dell'intervallo
         * @param last  fine dell'intervallo
         */
        public static void setEnabledInterval(int first, int last) {
            enabledFirst = first;
            level = enabledLast = last;
            model.setSelectionInterval(first, last);
        }

        /**
         * @param x indice di un elemento del JComboBox
         * @return true se l'indice è compreso nell'intervallo degli elementi attivi, false
         * altrimenti
         */
        private static boolean isInsideInterval(final int x) {return x >= enabledFirst && x <= enabledLast;}

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected,
                    cellHasFocus);
            if (!model.isSelectedIndex(index)) { // not enabled
                if (isSelected) {
                    c.setBackground(UIManager.getColor("ComboBox.background"));
                } else {
                    c.setBackground(super.getBackground());
                }
                c.setForeground(UIManager.getColor("PopupMenu.foreground").darker());
            } else {
                c.setBackground(super.getBackground());
                c.setForeground(super.getForeground());
            }
            return c;
        }
    }

    private static class TooltipMouseAdapter extends MouseAdapter {
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
    }
}