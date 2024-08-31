package chevy.view;

import chevy.control.WindowController;
import chevy.service.Data;
import chevy.service.Sound;
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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Window extends JFrame {
    public static final Dimension size = new Dimension(WindowSettings.WINDOW_WIDTH,
            WindowSettings.WINDOW_HEIGHT);
    public static final Font handjet = Load.font("Handjet");
    public static final Color bg = new Color(24, 20, 37);
    private static final ImageIcon icon = Load.icon("Power", 42, 42);
    public GamePanel gamePanel = new GamePanel(this);
    public Options options = new Options(this);
    public Menu menu = new Menu(this);
    private Scene scene;

    public Window(boolean resizable) {
        new WindowController(this);
        setSize(size);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setScene(Scene.MENU);
        setVisible(true);
        requestFocus();
        // assicura l'esecuzione del codice solamente dopo la creazione del componente JFrame
        SwingUtilities.invokeLater(() -> {
            WindowSettings.SIZE_TOP_BAR = getInsets().top;
            setResizable(resizable);
            if (resizable) {
                // visto che componentResized() non viene chiamata sempre all'avvio questo
                // assicura il corretto scale
                // dei componenti
                WindowSettings.updateValue(getHeight(), getWidth());
                gamePanel.windowResized(WindowSettings.scale);
                // ---
                makeResponsive();
            }
        });
        Sound.getInstance(); // inizializza il Sound

        Log.info("Window: creazione terminata [w: " + size.width + ", h: " + size.height + "]");
    }

    /**
     * Inizializza la FlatLaf e imposta personalizzazioni.
     * <a href="https://www.formdev.com/flatlaf/customizing/">Documentazione</a>.
     * Le modifica relative al LookAndFell vanno fatte prima di creare istanze di qualsiasi
     * componente.
     */
    public static void create() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            Log.warn("Tentativo di inizializzare FlatLaf fallito.");
        }

        // Le decorazioni della finestra personalizzate sono già abilitate su Windows e non sono
        // supportate su Mac.
        if (SystemInfo.isLinux) {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
        }

        UIManager.put("Button.font", handjet.deriveFont(25f));
        UIManager.put("ComboBox.font", handjet.deriveFont(40f));
        UIManager.put("Label.font", handjet.deriveFont(32f));
        UIManager.put("TitlePane.font", handjet.deriveFont(20f));
        UIManager.put("ToolTip.font", handjet.deriveFont(25f));

        UIManager.put("Component.arc", 10); // border-radius
        UIManager.put("Button.arc", 10);
        UIManager.put("ProgressBar.arc", 10);

        // Il colore di sfondo dei dialoghi è più chiaro, così è più facile accorgersi che un
        // dialogo è attivo.
        // Lo sfondo della barra del titolo del JFrame è impostata nel costruttore, questo è per
        // i JOptionPane
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

        new Window(true);
    }

    private void makeResponsive() {
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                WindowSettings.updateValue(getHeight(), getWidth());
                System.out.println("update:  " + WindowSettings.scale);
                gamePanel.windowResized(WindowSettings.scale);
            }
        });
    }

    /**
     * Unico punto di uscita (corretto) dall'app.
     * Il WindowController collega l'evento di chiusura del JFrame a questo metodo, perciò
     * premere la 'X' della
     * cornice della finestra passa il controllo qui.
     *
     * @return true quando l'utente ha scelto di non uscire dall'app, altrimenti non ritorna
     * affatto.
     */
    public boolean quitAction() {
        if (JOptionPane.showOptionDialog(this, "Uscire da Chevy?", "Conferma uscita",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon, new String[]{"Si",
                        "No"}, "No") == 0) {
            Log.info("Salvataggio dei dati ...");
            Data.write();
            Log.info("Chevy: TERMINAZIONE");
            System.exit(0);
        }
        return true;
    }

    /**
     * Utile per portare l'app allo stato predefinito.
     */
    public void refresh() {
        menu = new Menu(this);
        gamePanel = new GamePanel(this);
        options = new Options(this);
    }

    public Scene getScene() {return scene;}

    /**
     * Cambia la scena
     *
     * @param scene a cui si vuole passare
     */
    public void setScene(Scene scene) {
        if (this.scene != scene) {
            Log.info("Cambio scena da " + this.scene + " a " + scene);
            final JPanel panel = switch (scene) {
                case MENU -> {
                    Sound.startMenuMusic();
                    getRootPane().setBackground(new Color(25, 21, 38)); // Sfondo della barra del
                    // titolo
                    menu.startCharacterAnimation();
                    setTitle("Chevy");
                    yield menu.root;
                }
                case PLAYING -> {
                    getRootPane().setBackground(bg);
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
            requestFocus(); // mantieni il focus su Window
            validate();
        }
    }

    public enum Scene {MENU, PLAYING, OPTIONS}
}