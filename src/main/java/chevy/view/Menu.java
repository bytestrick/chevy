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

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Stream;

import static chevy.view.Options.SELECTOR_CLICK_LISTENER;

public final class Menu {
    private static final Icon ex = Load.icon("x", 48, 48);
    private static final Dimension spriteSize = new Dimension(200, 200);
    private static final Color[] barsColors = {new Color(255, 0, 68), new Color(212
            , 212, 212), new Color(228, 166, 114), new Color(254, 231, 97)};
    private static final Color progressBarDimmedForeground = new Color(144, 144, 144);
    private static final int archerCost = 500, ninjaCost = 1000;
    private static final Icon coin = Load.icon("Coin");
    private static final Icon[] statsIcons = {Load.icon("heart_2"),
            Load.icon("shield_2", 36, 36), Load.icon("attack"), Load.icon("lightning", 28, 28)};
    private static final Icon[] statsIconsGreyScale = {Load.icon(
            "heart_2_greyscale"), Load.icon("shield_2_greyscale", 36, 36),
            Load.icon("attack_greyscale"), Load.icon("lightning_greyscale", 28, 28)};
    private static final String[] statsTooltipPrefixes = {"health", "shield", "damage", "speed"};
    public static Player.Type playerType = Player.Type.valueOf(Data.get("menu.playerType"));
    private static boolean currentPlayerLocked, animationPaused, alternateAnimation = true,
            animationRunning;
    private static Thread animationWorker;
    private final int[][] playerStats = {new Knight(null).getStats(),
            new Archer(null).getStats(), new Ninja(null).getStats()};
    private final Image[][] sprites = new Image[3][4];
    private final Object updateAnimation = new Object();
    private final Window window;
    private final JLabel[] statsLabels;
    private final JProgressBar[] bars;
    private JPanel root, characterAnimation;
    private JComboBox<String> levelSelector;
    private JButton play, playerCycleNext, playerCyclePrev, quit, options, unlock;
    private JLabel coins, keys, characterName, health, shield, damage, speed, cost;    private final ActionListener actionListener = this::actionPerformed;
    private JRadioButton knightIndicator, archerIndicator, ninjaIndicator;
    private final JRadioButton[] indicators = {knightIndicator, archerIndicator, ninjaIndicator};
    private JProgressBar healthBar, damageBar, speedBar, shieldBar;
    Menu(Window window) {
        this.window = window;
        statsLabels = new JLabel[]{health, shield, damage, speed};
        bars = new JProgressBar[]{healthBar, shieldBar, damageBar, speedBar};

        initUI();
        loadCharactersSprites();
        setPlayerType(playerType);

        animationPaused = animationRunning = false;
        animationWorker = null;
    }

    /**
     * Aggiorna il livello corrente nel menù quando si passa a un nuovo livello nel gioco
     */
    public static void incrementLevel() {
        if (!ChamberManager.isLastChamber()) {
            Data.increment("progress.lastUnlockedLevel");
            final int level = Data.get("progress.lastUnlockedLevel");
            Data.set("progress.lastUnlockedLevel", level);
            LevelSelectorRenderer.setLastEnabledItemIndex(level);
        }
    }

    private void actionPerformed(ActionEvent event) {
        final String actionCommand = event.getActionCommand();
        switch (actionCommand) {
            case "play" -> playAction();
            case "quit" -> {
                Sound.play(Sound.Effect.BUTTON);
                window.quitAction();
            }
            case "options" -> {
                Sound.play(Sound.Effect.BUTTON);
                window.setScene(Window.Scene.OPTIONS);
                stopCharacterAnimation();
            }
            case "prev" -> playerCyclePrevAction();
            case "next" -> playerCycleNextAction();
            case "levelChanged" -> changeLevelAction();
            case "unlock" -> unlockPlayerAction();
        }
        if (!actionCommand.equals("levelChanged")) {
            window.requestFocus();
        }
    }

    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_ENTER -> {
                if (!currentPlayerLocked) {
                    play.requestFocus();
                    playAction();
                } else {
                    Sound.play(Sound.Effect.STOP);
                }
            }
            case KeyEvent.VK_ESCAPE -> {
                Sound.play(Sound.Effect.BUTTON);
                quit.requestFocus();
                window.quitAction();
            }
            case KeyEvent.VK_RIGHT -> {
                playerCycleNext.requestFocus();
                playerCycleNextAction();
            }
            case KeyEvent.VK_LEFT -> {
                playerCyclePrev.requestFocus();
                playerCyclePrevAction();
            }
        }
        SwingUtilities.invokeLater(window::requestFocus);
    }

    /**
     * Aggiorna i componenti dopo il cambio scena.
     * Impostare l'elemento attivo per {@link JComboBox} richiede che il componente sia visibile.
     */
    void updateComponents() {
        coins.setText(Data.get("progress.coins").toString());
        keys.setText(Data.get("progress.keys").toString());
        levelSelector.setSelectedIndex(Data.get("menu.level"));
        if (levelSelector.getActionListeners().length == 0) {
            levelSelector.addActionListener(actionListener);
        }
    }

    /**
     * Procedura del costruttore: costruzione dell'interfaccia
     */
    private void initUI() {
        setStrings();
        List.of(play, quit, options, playerCycleNext, playerCyclePrev, unlock)
                .forEach(c -> c.addActionListener(actionListener));
        levelSelector.addMouseListener(SELECTOR_CLICK_LISTENER);

        root.setBackground(new Color(33, 6, 47));
        levelSelector.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        levelSelector.setRenderer(new LevelSelectorRenderer());
        LevelSelectorRenderer.setLastEnabledItemIndex(Data.get("progress.lastUnlockedLevel"));
        final Font menuFont = UIManager.getFont("defaultFont").deriveFont(35f);
        Stream.of(quit, play, keys, cost, coins, unlock).forEach(c -> c.setFont(menuFont));
        playerCycleNext.setIcon(Load.icon("right-chevron"));
        playerCyclePrev.setIcon(Load.icon("left-chevron"));
        play.setIcon(Load.icon("Play"));
        quit.setIcon(Load.icon("Exit"));
        options.setIcon(Load.icon("Gear"));
        coins.setIcon(Load.icon("Coin2"));
        keys.setIcon(Load.icon("Key"));
        cost.setIcon(coin);
        characterName.setFont(UIManager.getFont("defaultFont").deriveFont(Font.BOLD, 50f));
        unlock.setIcon(Load.icon("Unlocked", 30, 30));
    }

    void setStrings() {
        play.setText(Options.strings.getString("menu.play"));
        quit.setText(Options.strings.getString("menu.quit"));
        health.setText(Options.strings.getString("menu.health"));
        shield.setText(Options.strings.getString("menu.shield"));
        damage.setText(Options.strings.getString("menu.damage"));
        speed.setText(Options.strings.getString("menu.speed"));
        unlock.setText(Options.strings.getString("menu.unlock"));
        playerCycleNext.setToolTipText(Options.strings.getString("menu.nextCharacter"));
        playerCyclePrev.setToolTipText(Options.strings.getString("menu.prevCharacter"));
        options.setToolTipText(Options.strings.getString("menu.opts"));
        keys.setToolTipText(Options.strings.getString("menu.keys"));
        coins.setToolTipText(Options.strings.getString("menu.coins"));
        levelSelector.setToolTipText(Options.strings.getString("menu.selectLevel"));

        levelSelector.removeActionListener(actionListener);
        levelSelector.removeAllItems();
        levelSelector.addItem(Options.strings.getString("menu.tutorial"));
        for (int i = 1; i < ChamberManager.NUMBER_OF_CHAMBERS; ++i) {
            levelSelector.addItem(String.format(Options.strings.getString("menu.level"), i));
        }
        levelSelector.setSelectedIndex(Data.get("menu.level"));
        levelSelector.addActionListener(actionListener);
        setPlayerType(playerType);
    }

    private void changeLevelAction() {
        final int level = levelSelector.getSelectedIndex();
        if (LevelSelectorRenderer.isInsideInterval(level)) {
            Data.set("menu.level", level);
            Log.info("Cambiato livello: " + level);
        } else {
            levelSelector.hidePopup();
            levelSelector.setSelectedIndex(level);
            Sound.play(Sound.Effect.STOP);
            JOptionPane.showMessageDialog(window, String.format(Options.strings.getString("dialog" +
                            ".levelLocked"), level), null,
                    JOptionPane.WARNING_MESSAGE, ex);
            SwingUtilities.invokeLater(levelSelector::showPopup);
        }
    }

    private void unlockPlayerAction() {
        Sound.play(Sound.Effect.BUTTON);
        int actualCost = playerType == Player.Type.ARCHER ? archerCost : ninjaCost;
        final String[] opts = Options.strings.getString("dialog.yesNo").split(",");
        if (JOptionPane.showOptionDialog(window,
                String.format(Options.strings.getString("dialog.unlock"),
                        Options.strings.getString("menu." + playerType), actualCost), null,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, Load.icon("Unlocked", 48
                        , 48), opts, opts[opts.length - 1]) == 0) {
            if (actualCost > (int) Data.get("progress.coins")) {
                Sound.play(Sound.Effect.STOP);
                JOptionPane.showMessageDialog(window, Options.strings.getString("dialog" +
                                ".notEnoughCoins"), null,
                        JOptionPane.WARNING_MESSAGE, ex);
                return;
            }
            Sound.play(Sound.Effect.UNLOCK_CHARACTER);
            currentPlayerLocked = false;
            Data.set("progress.player." + playerType + ".locked", false);
            int coinsLeft = (int) Data.get("progress.coins") - actualCost;
            Data.set("progress.coins", coinsLeft);
            coins.setText(Integer.toString(coinsLeft));
            setPlayerType(playerType);
        }
    }

    /**
     * L'azione di passare a {@link Window.Scene#PLAYING} e avviare il gioco. È innescata dal
     * JButton play e
     * dal tasto 'invio'
     * sulla tastiera.
     */
    private void playAction() {
        Sound.play(Sound.Effect.PLAY_BUTTON);
        stopCharacterAnimation();
        Sound.stopLoop();
        final int level = Data.get("menu.level");
        if (level == 0) {
            window.getGamePanel().getTutorial().updateDraw(0);
            window.setScene(Window.Scene.TUTORIAL);
            Sound.startMusic(Sound.Music.SAME_SONG); // 🎵
        } else {
            ChamberManager.enterChamber(level);
            window.setScene(Window.Scene.PLAYING);
        }
    }

    /**
     * L'azione di passaggio al personaggio successivo, innescata dall'apposito JButton e dalla
     * freccia a destra
     * sulla tastiera.
     */
    private void playerCycleNextAction() {
        Sound.play(Sound.Effect.BUTTON);
        final Player.Type[] v = Player.Type.values();
        setPlayerType(v[(playerType.ordinal() + 1) % v.length]);
    }

    /**
     * L'azione di passaggio al personaggio precedente, innescata dall'apposito JButton e dalla
     * freccia a sinistra
     * sulla tastiera.
     */
    private void playerCyclePrevAction() {
        Sound.play(Sound.Effect.BUTTON);
        final Player.Type[] v = Player.Type.values();
        setPlayerType(playerType.ordinal() == 0 ? v[v.length - 1] : v[(playerType.ordinal() - 1)]);
    }

    /**
     * Controlla il selettore del personaggio
     *
     * @param type il tipo di giocatore in {@link Player.Type}
     */
    private void setPlayerType(final Player.Type type) {
        indicators[playerType.ordinal()].setSelected(false);
        playerType = type;
        Data.set("menu.playerType", playerType.toString());
        final int p = playerType.ordinal();
        characterName.setText(Options.strings.getString("menu." + playerType).toUpperCase());
        indicators[p].setSelected(true);
        for (int i = 0; i < bars.length; ++i) {
            bars[i].setValue(playerStats[p][i]);
            final String statTooltip =
                    Options.strings.getString("menu." + statsTooltipPrefixes[i]) + ": " + playerStats[p][i] / 10;
            bars[i].setToolTipText(statTooltip);
            statsLabels[i].setToolTipText(statTooltip);
        }
        synchronized (updateAnimation) {
            updateAnimation.notify();
        }
        currentPlayerLocked =
                Data.get("progress.player." + playerType + ".locked");
        if (currentPlayerLocked) {
            characterName.setToolTipText(Options.strings.getString("menu.locked"));
            characterAnimation.setToolTipText(null);
            play.setEnabled(false);
            play.setToolTipText(String.format(Options.strings.getString("menu.playerLocked"),
                    Options.strings.getString("menu." + playerType)));
            unlock.setVisible(true);
            cost.setText(String.valueOf(playerType == Player.Type.ARCHER ? archerCost : ninjaCost));
            cost.setVisible(true);
            for (int i = 0; i < bars.length; ++i) {
                bars[i].setForeground(progressBarDimmedForeground);
                statsLabels[i].setIcon(statsIconsGreyScale[i]);
            }
        } else {
            final String[] quotes = Options.strings.getString("menu.quotes." + playerType).split(
                    ";");
            characterAnimation.setToolTipText(quotes[Utils.random.nextInt(quotes.length)]);
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
    void startCharacterAnimation() {
        if (animationWorker == null) {
            animationWorker = Thread.ofPlatform().start(() -> {
                Log.info("Thread per l'animazione del personaggio creato");
                animationRunning = true;
                while (animationRunning) {
                    characterAnimation.repaint();
                    alternateAnimation = !alternateAnimation;

                    // Ridisegna dopo un certo intervallo, tranne quando riceve updateAnimation
                    // .notify(), in quel caso  ridisegna subito e questo evento è innescato dal
                    // cambiamento del personaggio da parte dell'utente.
                    synchronized (updateAnimation) {
                        try {
                            while (animationPaused) {
                                Log.info("Animazione personaggio in pausa");
                                updateAnimation.wait();
                            }
                            updateAnimation.wait(386);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                Log.warn("Thread per l'animazione del personaggio terminato");
            });
        } else if (animationPaused) {
            synchronized (updateAnimation) {
                animationPaused = false;
                updateAnimation.notify();
            }
        }
    }

    /**
     * Fa attendere il thread che si occupa dell'animazione del personaggio
     * Da usare quando si lascia la scena MENU.
     */
    private void stopCharacterAnimation() {
        if (!animationPaused) {
            synchronized (updateAnimation) {
                animationPaused = true;
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
            public Point getToolTipLocation(MouseEvent event) {
                return getMousePosition();
            }
        };
    }

    JPanel getRoot() {
        return root;
    }

    /**
     * Rende possibile avere elementi non selezionabili in {@link JComboBox}
     * <p>
     * <a href="https://stackoverflow.com/a/23724201">Fonte</a>
     * <p>
     * Funziona in combinazione con l'{@link java.awt.event.ActionListener} di
     * {@link #levelSelector}
     */
    private static class LevelSelectorRenderer extends BasicComboBoxRenderer {
        private static final ListSelectionModel model = new DefaultListSelectionModel();
        private static int enabledLast;

        /**
         * Imposta l'intervallo di opzioni selezionabile in {@link JComboBox}
         *
         * @param last fine dell'intervallo
         */
        static void setLastEnabledItemIndex(final int last) {
            enabledLast = last;
            model.setSelectionInterval(0, last);
        }

        /**
         * @param x indice di un elemento del {@link JComboBox}
         * @return {@code true} se l'indice è compreso nell'intervallo degli elementi attivi,
         * {@code false}
         * altrimenti
         */
        private static boolean isInsideInterval(final int x) {
            return x >= 0 && x <= enabledLast;
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected,
                    cellHasFocus);
            if (!model.isSelectedIndex(index)) { // not enabled
                if (isSelected) {
                    c.setBackground(UIManager.getColor("ComboBox.background"));
                } else {
                    c.setBackground(getBackground());
                }
                c.setForeground(UIManager.getColor("PopupMenu.foreground").darker());
            } else {
                c.setBackground(getBackground());
                c.setForeground(getForeground());
            }
            return c;
        }
    }
}
