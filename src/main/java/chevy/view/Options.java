package chevy.view;

import chevy.service.Data;
import chevy.service.Sound;
import chevy.utils.Load;
import chevy.utils.Utils;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.util.LinkedHashMap;
import java.util.Map;

import static chevy.view.GamePanel.caution;

public final class Options {
    private static final Icon basket = Load.icon("Basket", 32, 32);
    private final Window window;
    /**
     * Contenitore radice dell'interfaccia, contiene tutti gli altri componenti ed è contenuto
     * in un {@link javax.swing.JScrollPane}
     */
    private JPanel root;
    private JButton back;
    private JButton restoreApp;
    /**
     * La lista che mostra le statistiche di gioco. È contenuta in un
     * {@link javax.swing.JScrollPane}
     */
    private JList<Statistic> statistics;
    private Window.Scene sceneToReturnTo;

    public Options(final Window window) {
        this.window = window;
        initializeComponents();
        attachListeners();
    }

    /**
     * Collega metodi Java agli eventi generati dall'utente sui componenti
     */
    private void attachListeners() {
        restoreApp.addActionListener(e -> restoreAppAction());

        back.addActionListener(e -> {
            Sound.play(Sound.Effect.BUTTON);
            if (sceneToReturnTo == Window.Scene.PLAYING) {
                window.setScene(sceneToReturnTo);
                window.getGamePanel().pauseDialog();
            } else {
                window.setScene(sceneToReturnTo);
            }
        });
    }

    private void restoreAppAction() {
        Sound.play(Sound.Effect.BUTTON);
        Sound.play(Sound.Effect.STOP);
        final String message = "L'app sarà riportata allo stato predefinito. Il progresso di " +
                "gioco e le impostazioni andranno perse. Continuare?";
        if (JOptionPane.showOptionDialog(window, message, null, JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE, caution, new String[]{"Si", "No"}, "No") == 0) {
            Data.createPristineFile();
            Data.read();
            window.refresh();
            window.setScene(Window.Scene.MENU);
        }
    }

    /**
     * Effettua operazioni che non si può (o non conviene) fare tramite il file xml
     * <code>Options.form</code> o il foglio di stile <code>FlatDarkLaf.properties</code>
     */
    private void initializeComponents() {
        statistics.setCellRenderer(new ItemRenderer());
        getStats(Data.get("stats"), null, -2);

        back.setIcon(Load.icon("arrow", 32, 32));

        // https://www.formdev.com/flatlaf/client-properties/#JComponent
        Color destructiveActionBg = UIManager.getColor("Chevy.color.destructiveActionBG");
        restoreApp.putClientProperty("FlatLaf.style", Map.of(
                "borderColor", destructiveActionBg.brighter().brighter(),
                "hoverBorderColor", destructiveActionBg.brighter().brighter(),
                "focusColor", destructiveActionBg.brighter().brighter().brighter(),
                "focusedBorderColor", destructiveActionBg.brighter().brighter().brighter()
        ));

        restoreApp.setBackground(UIManager.getColor("Chevy.color.destructiveActionBG"));
        restoreApp.setIcon(basket);
    }

    /**
     * Gestisce il passaggio da e a {@link Window.Scene#OPTIONS}.
     * È importante ricordare da quale scena si arriva, perché se si arriva da
     * {@link Window.Scene#PLAYING} all'uscita si
     * deve fare ripartire {@link chevy.service.GameLoop} e musica. Mentre se si arriva da
     * {@link Window.Scene#MENU} si deve fare ripartire
     * l'animazione del personaggio.
     */
    public void setupReturnAction(Window.Scene scene) {
        sceneToReturnTo = scene;
        String message = "Torna al ";
        if (scene == Window.Scene.PLAYING) {
            getStats(Data.get("stats"), null, -2);
            message += "gioco";
        } else {
            message += "menù";
        }
        back.setText(message);
    }

    /**
     * Visita ricorsivamente il sotto-ramo "stats" del file JSON.
     * Ogni chiamata ricostruisce il model di {@link #statistics}. Quindi è usabile per
     * inizializzare e aggiornare la visualizzazione delle statistiche.
     *
     * @param node        sotto nodo di cui fare il parsing
     * @param listModel   il model di {@link JList} a cui aggiungere i record {@link Statistic}
     *                    man mano che si creano
     * @param indentLevel livello di indentazione fisico di ciascuna statistica
     */
    private void getStats(LinkedHashMap<?, ?> node, DefaultListModel<Statistic> listModel,
                          int indentLevel) {
        if (listModel == null) {
            listModel = new DefaultListModel<>();
            statistics.setModel(listModel);
        }
        if (node.containsKey("count")) {
            listModel.addElement(new Statistic(node, indentLevel));
        } else {
            ++indentLevel;
            for (Map.Entry<?, ?> child : node.entrySet()) {
                getStats((LinkedHashMap<?, ?>) child.getValue(), listModel, indentLevel);
            }
            --indentLevel;
        }
    }

    public JPanel getRoot() {return root;}

    /**
     * L'elemento di {@link #statistics}
     */
    private record Statistic(Icon icon, String string, int count, int indent, int topMargin) {
        /**
         * Costruttore custom che scompone un nodo foglia dell'albero JSON
         *
         * @param data il nodo contenente "icon", "string" e "count"
         */
        Statistic(LinkedHashMap<?, ?> data, int indentLevel) {
            this(Load.icon((String) data.get("icon"), 48, 48), (String) data.get("string"),
                    (int) data.get("count"), indentLevel * 48, indentLevel == 0 ? 48 : 4);
        }
    }

    /**
     * Renderer personalizzato per visualizzare testo e icona in {@link JList}
     */
    private static class ItemRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index,
                    isSelected, cellHasFocus);
            Statistic item = (Statistic) value;
            final boolean timePlayedItem = item.string.startsWith("Tempo");
            label.setText(item.string + ": " + (timePlayedItem ? Utils.msToString(item.count) :
                    item.count));
            label.setIcon(item.icon);
            label.setIconTextGap(16);
            label.setBorder(new EmptyBorder(new Insets(timePlayedItem ? 10 : item.topMargin,
                    16 + item.indent, 4, 16)));
            return label;
        }
    }
}