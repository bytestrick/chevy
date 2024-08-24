package chevy.view;

import chevy.control.KeyboardListener;
import chevy.control.WindowController;
import chevy.settings.WindowSettings;
import chevy.utils.Load;
import chevy.utils.Log;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.util.SystemInfo;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Window extends JFrame {
    public static final Dimension size = new Dimension(WindowSettings.WINDOW_WIDTH, WindowSettings.WINDOW_HEIGHT);
    public static final Font handjet = Load.font("Handjet");
    private static final ImageIcon icon = new ImageIcon(Load.image("/assets/icons/Power.png").getScaledInstance(42,
            42, Image.SCALE_SMOOTH));
    private static final Color bg = new Color(24, 20, 37);
    public final GamePanel gamePanel = new GamePanel(this);
    public final Menu menu = new Menu(this);
    public final Options options = new Options(this);
    public final KeyboardListener keyboardListener = new KeyboardListener(this);
    private Scene scene;

    public Window(boolean resizable) {
        setSize(size);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowController(this));
        setScene(Scene.MENU);
        setVisible(true);
        requestFocus();

        getRootPane().setBackground(new Color(25, 21, 38)); // Sfondo della barra del titolo

        // assicura l'esecuzione del codice solamente dopo la creazione del componente JFrame
        SwingUtilities.invokeLater(() -> {
            WindowSettings.SIZE_TOP_BAR = getInsets().top;
            setResizable(resizable);
            if (resizable) {
                makeResponsive();
            }
        });

        Log.info(size.toString());
    }

    /**
     * Inizializza la FlatLaf e imposta personalizzazioni.
     * <a href="https://www.formdev.com/flatlaf/customizing/">Documentazione</a>.
     * Va chiamata prima di istanziare qualsiasi componente.
     */
    public static void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            Log.warn("Tentativo di inizializzare FlatLaf fallito.");
            return;
        }

        // Le decorazioni della finestra personalizzate sono già abilitate su Windows e non sono supportate su Mac.
        if (SystemInfo.isLinux) {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
        }

        UIManager.put("Button.font", handjet.deriveFont(25f));
        UIManager.put("ComboBox.font", handjet.deriveFont(40f));
        UIManager.put("Label.font", handjet.deriveFont(32f));
        UIManager.put("TitlePane.font", handjet.deriveFont(20f));
        UIManager.put("ToolTip.font", handjet.deriveFont(25f));

        UIManager.put("Component.background", bg);
        UIManager.put("Dialog.background", bg);
        UIManager.put("Component.arc", 10); // border-radius
        UIManager.put("Button.arc", 10);

        // Il colore di sfondo dei dialoghi è più chiaro, così è più facile accorgersi che un dialogo è attivo.
        // Lo sfondo della barra del titolo del JFrame è impostata nel costruttore, questo è per i JOptionPane
        final Color dialogBg = new Color(53, 20, 84);
        UIManager.put("RootPane.background", dialogBg);
        UIManager.put("OptionPane.background", dialogBg);

        final Color buttonBg = new Color(52, 39, 61);
        UIManager.put("Button.background", buttonBg);
        UIManager.put("ComboBox.background", buttonBg);
        UIManager.put("ComboBox.buttonBackground", buttonBg);

        final Color componentBorder = new Color(73, 81, 95);
        UIManager.put("Button.borderColor", componentBorder);
        UIManager.put("Component.borderColor", componentBorder);
        UIManager.put("Button.borderWidth", 2);
        UIManager.put("Component.borderWidth", 2);
        UIManager.put("Component.focusWidth", 2);

        UIManager.put("CheckBox.icon.disabledSelectedBackground", new Color(255, 255, 255));
        UIManager.put("CheckBox.icon.disabledCheckmarkColor", new Color(255, 255, 255));
        UIManager.put("CheckBox.icon.disabledSelectedBorderColor", new Color(255, 255, 255));
        UIManager.put("CheckBox.icon.disabledBorderColor", new Color(255, 255, 255));
        UIManager.put("CheckBox.icon.disabledBackground", bg);
    }

    private void makeResponsive() {
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                WindowSettings.updateValue(getHeight(), getWidth());
                gamePanel.windowResized();
            }
        });
    }

    /**
     * Unico punto di uscita (corretto) dall'app.
     * Il WindowController collega l'evento di chiusura del JFrame a questo metodo.
     *
     * @return true quando l'utente ha scelto di non uscire dall'app.
     */
    public boolean quitAction() {
        if (JOptionPane.showOptionDialog(this, "Uscire da Chevy?", "Conferma uscita", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, icon, new String[]{"Si", "No"}, "No") == 0) {
            Log.info("Salvataggio dei dati ...");
            // TODO: eventualmente salvare il progresso qui.

            Log.info("Chevy: TERMINAZIONE");
            System.exit(0);
        }
        return true;
    }

    public Scene getScene() { return scene; }

    public void setScene(Scene scene) {
        if (this.scene != scene) {
            Log.info("Cambio scena da " + this.scene + " a " + scene);
            final JPanel panel = switch (scene) {
                case MENU -> {
                    menu.startCharacterAnimation();
                    setTitle("Chevy");
                    yield menu.root;
                }
                case PLAYING -> {
                    setTitle("Chevy");
                    yield gamePanel;
                }
                case OPTIONS -> {
                    setTitle("Chevy - Opzioni");
                    yield options.root;
                }
            };
            options.setupReturnAction(this.scene);
            this.scene = scene;
            setContentPane(panel);
            panel.requestFocus();
            validate();
        }
    }

    public enum Scene {MENU, PLAYING, OPTIONS}
}