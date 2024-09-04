package chevy.view;

import chevy.model.Statistics;
import chevy.service.Data;
import chevy.utils.Load;
import chevy.utils.Log;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Options {
    private static final ImageIcon basket = Load.icon("Basket", 32, 32);
    public JPanel root;
    private JButton quit;
    private JButton restoreApp;
    private JPanel panelList;
    JList<ListItem> statistics = new JList<>();
    private Window.Scene sceneToReturnTo;

    public Options(final Window window) {
        quit.addActionListener(e -> {
            if (sceneToReturnTo == Window.Scene.PLAYING) {
                window.setScene(sceneToReturnTo);
                window.getGamePanel().pauseDialog();
            } else {
                window.setScene(sceneToReturnTo);
            }
        });

        restoreApp.setBackground(UIManager.getColor("Chevy.color.destructiveActionBG"));
        restoreApp.setIcon(basket);

        createList(48);

        restoreApp.addActionListener(e -> {
            // TODO: dialogo di conferma che avvisa della perdita del progresso
            Statistics.clearAll();
            Data.createPristineFile();
            Data.read();
            updateStatistics();
            window.refresh();
        });
    }

    /**
     * Gestisce il contesto del passaggio da e a OPTIONS.
     * È importante tracciare da quale scena si passa a OPTIONS, perché se si arriva da PLAYING
     * all'uscita si
     * deve fare ripartire GameLoop e musica. Mentre se si arriva da MENU si deve fare ripartire
     * l'animazione del personaggio.
     */
    public void setupReturnAction(Window.Scene scene) {
        sceneToReturnTo = scene;
        quit.setText("Torna al " + (scene == Window.Scene.PLAYING ? "gioco" : "menù"));
    }

    /**
     * Crea la lista che conterrà le varie statistiche
     */
    private void createList(int size) {
        DefaultListModel<ListItem> model = new DefaultListModel<>();
        statistics.setModel(model);

        statistics.setFont(UIManager.getFont("defaultFont").deriveFont(35f));
        statistics.setCellRenderer(new ItemRenderer());
        statistics.setBackground(UIManager.getColor("Chevy.color.purpleDark"));

        JScrollPane scrollPane = new JScrollPane(statistics);
        scrollPane.setOpaque(false);

        panelList.setOpaque(false);
        panelList.setBorder(new EmptyBorder(new Insets(16, 16, 16, 16)));
        panelList.setLayout(new BoxLayout(panelList, BoxLayout.X_AXIS));
        panelList.add(scrollPane);

        // Carica le statistiche (potrebbe richiedere tempo)
        Statistics.Statistic[] statisticsModel = Statistics.getStatistic();

        boolean skipFirst = false;
        for (int i = 0; i < statisticsModel.length; ++i) {
            Statistics.Statistic stat = statisticsModel[i];
            int leftSpace = size * stat.getSubStatistic();
            int topSpace = stat.getSubStatistic() == 0 && skipFirst ? size : 4;
            ListItem listItem = new ListItem(stat.getName() + ": " + Statistics.getInt(i),
                    Load.icon(stat.getIconPath(), size, size), leftSpace, topSpace);
            model.addElement(listItem);
            skipFirst = true;
        }
    }


    /**
     *  Aggiorna i valori delle statistiche
     */
    public void updateStatistics() {
        Statistics.saveAll();

        ListModel<ListItem> model = statistics.getModel();
        Statistics.Statistic[] statisticsModel = Statistics.getStatistic();

        for (int i = 0; i < model.getSize(); ++i) {
            Statistics.Statistic stat = statisticsModel[i];
            ListItem listItem = model.getElementAt(i);
            listItem.setText(stat.getName() + ": " + Statistics.getInt(i));
        }

        statistics.repaint();
        panelList.revalidate();
        panelList.repaint();

        Log.info("Statistiche aggiornate con successo");
    }

    /**
     * Questa classe rappresenta una singola statistica
     */
    private static class ListItem {
        private String text;
        private Icon icon;
        private int leftSpace;
        private int topSpace;

        public ListItem(String text, Icon icon, int leftSpace, int topSpace) {
            this.icon = icon;
            this.text = text;
            this.leftSpace = leftSpace;
            this.topSpace = topSpace;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public Icon getIcon() {
            return icon;
        }

        public int getLeftSpace() {
            return leftSpace;
        }

        public int getTopSpace() {
            return topSpace;
        }

        @Override
        public String toString() {
            return text; // Necessario per visualizzare il testo nel renderer
        }
    }

    // Renderer personalizzato per visualizzare testo e icona
    private static class ItemRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            ListItem item = (ListItem) value;
            label.setText(item.getText());
            label.setIcon(item.getIcon());
            label.setIconTextGap(16);
            label.setBorder(new EmptyBorder(new Insets(item.getTopSpace(), 16 + item.getLeftSpace(),4,16)));
            return label;
        }
    }
}