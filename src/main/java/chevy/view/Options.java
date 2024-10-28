package chevy.view;

import chevy.service.Data;
import chevy.service.Sound;
import chevy.utils.Load;
import chevy.utils.Log;
import chevy.utils.Utils;
import chevy.view.chamber.ChamberView;
import com.formdev.flatlaf.icons.FlatCheckBoxIcon;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static chevy.view.GamePanel.caution;

public final class Options {
    static final MouseListener SELECTOR_CLICK_LISTENER = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            Sound.play(Sound.Effect.BUTTON);
        }
    };
    private static final Icon HOME = Load.icon("Home"), TRASH_BIN = Load.icon("TrashBin"),
            GAME_PAD = Load.icon("GamePad"), SPEAKER = Load.icon("SpeakerOn"), SPEAKER_MUTE =
            Load.icon("SpeakerMute"), NOTES = Load.icon("MusicNotes");
    private static final String[] LANGUAGES = {"en", "it"};
    public static ResourceBundle strings = ResourceBundle.getBundle("i18n.strings",
            Locale.of(Data.get(
            "options.language")));
    private final Window window;
    private final ChangeListener changeListener = this::volumeChanged;
    /**
     * Contenitore radice dell'interfaccia, contiene tutti gli altri componenti ed è contenuto
     * in un {@link javax.swing.JScrollPane}
     */
    private JPanel root;
    private JButton back, restoreApp;
    /**
     * La lista che mostra le statistiche di gioco. È contenuta in un
     * {@link javax.swing.JScrollPane}
     */
    private JList<Statistic> statistics;
    private JSlider effectsVolume, musicVolume;
    private JScrollPane scrollPane;
    private JLabel audioLabel, advancedLabel, statsLabel, effectsLabel, musicLabel, generalLabel,
            languageLabel, logLevelLabel, drawHitBoxesLabel, restoreAppLabel;
    private JCheckBox showHitBoxes;
    private JComboBox<String> logLevel, languageSelector;
    private Window.Scene sceneToReturnTo;
    private final ActionListener actionListener = this::actionPerformed;

    Options(Window window) {
        this.window = window;

        initUI();
        List.of(back, restoreApp, showHitBoxes).forEach(c -> c.addActionListener(actionListener));
        List.of(musicVolume, effectsVolume).forEach(c -> c.addChangeListener(changeListener));
        showHitBoxes.addItemListener(itemEvent -> {
            Sound.play(Sound.Effect.BUTTON);
            ChamberView.drawCollision = switch (itemEvent.getStateChange()) {
                case ItemEvent.SELECTED -> true;
                case ItemEvent.DESELECTED -> false;
                default -> throw new IllegalStateException("Unexpected value: "
                        + itemEvent.getStateChange());
            };
            Data.set("options.drawHitBoxes", ChamberView.drawCollision);
        });
        languageSelector.addActionListener(actionListener);
        languageSelector.addMouseListener(SELECTOR_CLICK_LISTENER);
        logLevel.addMouseListener(SELECTOR_CLICK_LISTENER);
    }

    private void actionPerformed(ActionEvent event) {
        switch (event.getActionCommand()) {
            case "back" -> {
                Sound.play(Sound.Effect.BUTTON);
                backAction();
            }
            case "restoreApp" -> restoreAppAction();
            case "logLevelChanged" -> {
                Sound.play(Sound.Effect.BUTTON);
                Log.logLevel = Log.Level.values()[logLevel.getSelectedIndex()];
                Data.set("options.logLevel", Log.logLevel);
            }
            case "languageChanged" -> {
                Sound.play(Sound.Effect.BUTTON);
                String language = LANGUAGES[languageSelector.getSelectedIndex()];
                Data.set("options.language", language);
                strings = ResourceBundle.getBundle("i18n.strings", Locale.of(language));
                setStrings();
                window.setTitle(Options.strings.getString("title.options"));
                window.getMenu().setStrings();
                window.getGamePanel().getTutorial().setStrings();
                window.revalidate();
                window.repaint();
                volumeChanged(new ChangeEvent(musicVolume));
                volumeChanged(new ChangeEvent(effectsVolume));
            }
        }
    }

    private void backAction() {
        if (sceneToReturnTo == Window.Scene.PLAYING) {
            window.setScene(sceneToReturnTo);
            window.getGamePanel().pauseDialog();
        } else {
            window.setScene(sceneToReturnTo);
        }
    }

    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            back.requestFocus();
            Sound.play(Sound.Effect.BUTTON);
            backAction();
            window.requestFocus();
        }
    }

    private void volumeChanged(ChangeEvent event) {
        final JSlider slider = (JSlider) event.getSource();
        final int value = slider.getValue();
        final String info = value == 0 ? strings.getString("options.mute") : value + "%";
        slider.setToolTipText(info);
        if (slider == musicVolume) {
            Sound.setMusicVolume(value / 100d);
            musicLabel.setIcon(value == 0 ? SPEAKER_MUTE : NOTES);
            musicLabel.setToolTipText(info);
        } else {
            Sound.setEffectsVolume(value / 100d);
            effectsLabel.setIcon(value == 0 ? SPEAKER_MUTE : SPEAKER);
            effectsLabel.setToolTipText(info);
        }
    }

    private void restoreAppAction() {
        Sound.play(Sound.Effect.BUTTON);
        Sound.play(Sound.Effect.STOP);
        final String[] opts = strings.getString("dialog.yesNo").split(",");
        final int ans = JOptionPane.showOptionDialog(window, strings.getString("dialog.restoreApp"
                ), null,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE, caution, opts, opts[opts.length - 1]);
        if (ans == 0) {
            Sound.play(Sound.Effect.BUTTON);
            Data.createPristineFile();
            Data.read();
            window.refresh();
            window.setScene(Window.Scene.MENU);
        } else if (ans == 1) {
            Sound.play(Sound.Effect.BUTTON);
        }
    }

    /**
     * Effettua operazioni che non si possono (o non conviene) fare tramite il file xml
     * <code>Options.form</code> o il foglio di stile <code>FlatDarkLaf.properties</code>
     */
    private void initUI() {
        setStrings();
        showHitBoxes.setIcon(new SizedCheckBoxIcon());

        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(6);

        statistics.setCellRenderer(new ItemRenderer());
        getStats(Data.get("stats"), "", null, -2);

        final int effectsVolumeValue = (int) ((double) Data.get("options.effectsVolume") * 100);
        effectsVolume.setValue(effectsVolumeValue);
        volumeChanged(new ChangeEvent(effectsVolume));
        final int musicVolumeValue = (int) ((double) Data.get("options.musicVolume") * 100);
        musicVolume.setValue(musicVolumeValue);
        volumeChanged(new ChangeEvent(musicVolume));

        logLevel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        languageSelector.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        showHitBoxes.setSelected(Data.get("options.drawHitBoxes"));

        // https://www.formdev.com/flatlaf/client-properties/
        List.of(audioLabel, advancedLabel, statsLabel, generalLabel).forEach(label -> label.putClientProperty(
                "FlatLaf.style",
                Map.of("font", UIManager.getFont("defaultFont").deriveFont(45f),
                        "foreground", UIManager.getColor("Chevy.color.blueBright").brighter())));
        final Color destructiveActionBg = UIManager.getColor("Chevy.color.destructiveActionBG");
        restoreApp.putClientProperty("FlatLaf.style", Map.of(
                "background", destructiveActionBg,
                "borderColor", destructiveActionBg.brighter().brighter(),
                "hoverBorderColor", destructiveActionBg.brighter().brighter(),
                "focusColor", destructiveActionBg.brighter().brighter().brighter(),
                "focusedBorderColor", destructiveActionBg.brighter().brighter().brighter()
        ));
        restoreApp.setIcon(TRASH_BIN);
    }

    private void setStrings() {
        getStats(Data.get("stats"), "", null, -2);
        setupReturnAction(sceneToReturnTo);
        languageSelector.removeAllItems();
        for (String lang : LANGUAGES) {
            languageSelector.addItem(strings.getString("lang." + lang));
        }
        languageSelector.setSelectedIndex(List.of(LANGUAGES).indexOf((String) Data.get("options" +
                ".language")));

        logLevel.removeActionListener(actionListener);
        logLevel.removeAllItems();
        for (Log.Level level : Log.Level.values()) {
            logLevel.addItem(strings.getString("options.logLevel." + level));
        }
        final Log.Level lvl = Log.Level.valueOf(Data.get("options.logLevel"));
        Log.logLevel = lvl;
        logLevel.setSelectedIndex(lvl.ordinal());
        logLevel.addActionListener(actionListener);

        audioLabel.setText(strings.getString("options.audio"));
        generalLabel.setText(strings.getString("options.general"));
        advancedLabel.setText(strings.getString("options.advanced"));
        statsLabel.setText(strings.getString("options.stats"));
        effectsLabel.setText(strings.getString("options.effects"));
        musicLabel.setText(strings.getString("options.music"));
        languageLabel.setText(strings.getString("options.language"));
        logLevelLabel.setText(strings.getString("options.logLevel"));
        drawHitBoxesLabel.setText(strings.getString("options.drawHitBoxes"));
        restoreAppLabel.setText(strings.getString("options.restoreApp"));
    }

    /**
     * Gestisce il passaggio da e a {@link chevy.view.Window.Scene#OPTIONS}.
     * È importante ricordare da quale scena si arriva, perché se si arriva da
     * {@link chevy.view.Window.Scene#PLAYING} all'uscita si
     * deve fare ripartire {@link chevy.service.GameLoop} e musica. Mentre se si arriva da
     * {@link chevy.view.Window.Scene#MENU} si deve fare ripartire
     * l'animazione del personaggio.
     */
    void setupReturnAction(Window.Scene scene) {
        sceneToReturnTo = scene;
        String message = "";
        if (scene == Window.Scene.PLAYING) {
            back.setIcon(GAME_PAD);
            getStats(Data.get("stats"), "", null, -2);
            message = strings.getString("options.backToGame");
        } else if (scene == Window.Scene.MENU){
            back.setIcon(HOME);
            message = strings.getString("options.backToHome");
        }
        back.setText(message);
    }

    /**
     * Visita ricorsivamente il sotto-ramo "stats" del file JSON.
     * Ogni chiamata ricostruisce il model di {@link #statistics}. Quindi è usabile per
     * inizializzare e aggiornare la visualizzazione delle statistiche.
     *
     * @param node        sotto nodo di cui fare il parsing
     * @param listModel   il model di {@link javax.swing.JList} a cui aggiungere i record
     *                    {@link chevy.view.Options.Statistic}
     *                    man mano che si creano
     * @param indentLevel livello di indentazione fisico di ciascuna statistica
     */
    private void getStats(LinkedHashMap<?, ?> node, String nodeName,
                          DefaultListModel<Statistic> listModel, int indentLevel) {
        if (listModel == null) {
            listModel = new DefaultListModel<>();
            statistics.setModel(listModel);
        }
        if (node.containsKey("count")) {
            listModel.addElement(new Statistic(node, nodeName, indentLevel));
        } else {
            ++indentLevel;
            for (Map.Entry<?, ?> child : node.entrySet()) {
                getStats((LinkedHashMap<?, ?>) child.getValue(), child.getKey().toString(),
                        listModel, indentLevel);
            }
            --indentLevel;
        }
    }

    JPanel getRoot() {return root;}

    /**
     * L'elemento di {@link #statistics}
     */
    private record Statistic(Icon icon, String string, int count, int indent, int topMargin, String nodeName) {
        private static final int SPACING = 48;

        /**
         * Costruttore custom che scompone un nodo foglia dell'albero JSON
         *
         * @param data il nodo contenente "icon", "string" e "count"
         */
        private Statistic(LinkedHashMap<?, ?> data, String nodeName, int indentLevel) {
            this(Load.icon((String) data.get("icon"), SPACING, SPACING), strings.getString("stats" +
                            "." + nodeName), (int) data.get("count"), indentLevel * SPACING,
                    indentLevel == 0 ? SPACING : 4, nodeName);
        }
    }

    /**
     * Renderer personalizzato per visualizzare testo e icona in {@link javax.swing.JList}
     */
    private static class ItemRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index,
                    isSelected, cellHasFocus);
            Statistic item = (Statistic) value;
            final boolean timePlayedItem = item.nodeName.equals("totalPlayed");
            label.setText(item.string + ": " + (timePlayedItem ? Utils.msToString(item.count) :
                    item.count));
            label.setIcon(item.icon);
            final int spacing = 16;
            label.setIconTextGap(spacing);
            label.setBorder(new EmptyBorder(new Insets(timePlayedItem ? 10 : item.topMargin,
                    spacing + item.indent, 4, spacing)));
            return label;
        }
    }

    /**
     * Espediente per avere {@link javax.swing.JCheckBox} di una dimensione personalizzata
     * <p>
     * <a href="https://github.com/JFormDesigner/FlatLaf/issues/413#issuecomment-959545260">
     * Fonte</a>
     */
    private static class SizedCheckBoxIcon extends FlatCheckBoxIcon {
        private final float scaleFactor;

        SizedCheckBoxIcon() {
            assert super.getIconHeight() == super.getIconWidth() : "assunzione errata";
            scaleFactor = 1f * 50 / super.getIconHeight();
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.translate(x, y);
                g2.scale(scaleFactor, scaleFactor);
                super.paintIcon(c, g2, 0, 0);
            } finally {
                g2.dispose();
            }
        }

        @Override
        public int getIconWidth() {return Math.round(super.getIconWidth() * scaleFactor);}

        @Override
        public int getIconHeight() {return Math.round(super.getIconHeight() * scaleFactor);}
    }
}
