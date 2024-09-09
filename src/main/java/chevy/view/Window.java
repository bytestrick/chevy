package chevy.view;

import chevy.control.WindowController;
import chevy.service.Data;
import chevy.service.GameLoop;
import chevy.service.Sound;
import chevy.utils.Load;
import chevy.utils.Log;
import chevy.view.chamber.ChamberView;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.util.SystemInfo;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Finestra principale
 */
public final class Window extends JFrame {
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Dimension ASPECT_RATIO = new Dimension(4, 3);
    private static final Icon POWER = Load.icon("Power", 42, 42);
    public static float scale = .8f;
    public static Dimension size = new Dimension();
    private static boolean quitDialogActive;

    static {
        final int maxSideLength =
                Math.round(Math.min(SCREEN_SIZE.width, SCREEN_SIZE.height) * scale);
        if (SCREEN_SIZE.width < SCREEN_SIZE.height) {
            size.height =
                    Math.round(1.f * maxSideLength * ASPECT_RATIO.height / ASPECT_RATIO.width);
        } else {
            size.height = maxSideLength;
        }
        if (SCREEN_SIZE.width > SCREEN_SIZE.height) {
            size.width = Math.round(1.f * maxSideLength * ASPECT_RATIO.width / ASPECT_RATIO.height);
        } else {
            size.width = maxSideLength;
        }
        scale = Math.min(size.width / SCREEN_SIZE.width, size.height / SCREEN_SIZE.height);
    }

    private final WindowController windowController;
    private GamePanel gamePanel = new GamePanel(this);
    private Options options = new Options(this);
    private Menu menu = new Menu(this);
    private Scene scene;

    private Window() {
        // Colore dello sfondo della finestra durante il caricamento
        setBackground(UIManager.getColor("Chevy.color.purpleDark"));
        windowController = new WindowController(this);
        setSize(size);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setScene(Scene.MENU);
        setVisible(true);
        requestFocus();
        SwingUtilities.invokeLater(() -> { // esecuzione asincrona
            ChamberView.topBarHeight = getInsets().top;
            setResizable(true);
            // visto che componentResized() non viene chiamata sempre all'avvio questo
            // assicura il corretto scale dei componenti
            updateSize(getSize());
            gamePanel.windowResized(scale);
        });
        Log.info("Window: creazione terminata [w: " + size.width + ", h: " + size.height + "]");
    }

    public static void updateSize(Dimension size) {
        Window.size = size;
        ChamberView.updateSize();
        float scaleX = 1.f * size.width / SCREEN_SIZE.width * ASPECT_RATIO.width;
        float scaleY = 1.f * size.height / SCREEN_SIZE.height * ASPECT_RATIO.height;
        scale = Math.min(scaleX, scaleY);
    }

    /**
     * Inizializza la FlatLaf e imposta personalizzazioni.
     * <a href="https://www.formdev.com/flatlaf/customizing/">Documentazione</a>.
     * Le modifica relative al LookAndFell vanno fatte prima di creare istanze di qualsiasi
     * componente.
     */
    public static void create() {
        FlatLaf.registerCustomDefaultsSource("style"); // collega il foglio di stile
        FlatDarkLaf.setup();
        // Le decorazioni della finestra personalizzate sono già abilitate su Windows e non sono
        // supportate su Mac.
        if (SystemInfo.isLinux) {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
        }
        UIManager.put("defaultFont", Load.font("VT323"));

        // L'attesa iniziale affinché il tooltip si attivi è più breve
        ToolTipManager.sharedInstance().setInitialDelay(50);
        // Il tooltip persiste per un'ora se il cursore vi giace sopra per tanto
        ToolTipManager.sharedInstance().setDismissDelay(3_600_000);

        new Window();
    }

    public static boolean isQuitDialogNotActive() {return !quitDialogActive;}

    static boolean isQuitDialogActive() {return quitDialogActive;}

    /**
     * Unico punto di uscita (corretto) dall'app.
     * Il {@link WindowController} collega l'evento di chiusura del {@code JFrame} a questo
     * metodo, perciò premere la 'X' della cornice della finestra passa il controllo qui.
     */
    public void quitAction() {
        quitDialogActive = true;
        if (scene == Scene.PLAYING) {
            GameLoop.stop();
            Sound.stopMusic();
        }
        Sound.play(Sound.Effect.STOP);
        final int ans = JOptionPane.showOptionDialog(this, "Uscire da Chevy?", null,
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, POWER, new String[]{"Si",
                        "No"}, "No");
        if (ans == 0) {
            Sound.play(Sound.Effect.BUTTON);
            Log.info("Salvataggio dei dati ...");
            Data.write();
            Log.info("Ciao");
            System.exit(0);
        } else if (ans == 1) {
            Sound.play(Sound.Effect.BUTTON);
        }
        quitDialogActive = false;
        if (scene == Scene.PLAYING) {
            gamePanel.pauseDialog();
        }
    }

    /**
     * Utile per portare l'app allo stato predefinito
     */
    void refresh() {
        menu = new Menu(this);
        gamePanel = new GamePanel(this);
        options = new Options(this);
        gamePanel.windowResized(Window.scale);
    }

    public Scene getScene() {return scene;}

    /**
     * Cambia la scena
     *
     * @param scene a cui si vuole passare
     */
    void setScene(Scene scene) {
        if (this.scene != scene) {
            Log.info("Cambio scena da " + this.scene + " a " + scene);
            final JPanel panel = switch (scene) {
                case MENU -> {
                    if (this.scene != Scene.OPTIONS) {
                        Sound.startLoop(Sound.Music.NEW_SONG);
                    }
                    getRootPane().setBackground(menu.getRoot().getBackground());
                    menu.startCharacterAnimation();
                    menu.updateComponents();
                    setTitle("Chevy");
                    yield menu.getRoot();
                }
                case PLAYING -> {
                    getRootPane().setBackground(gamePanel.getBackground());
                    setTitle("Chevy");
                    gamePanel.addComponents(false);
                    yield gamePanel;
                }
                case OPTIONS -> {
                    getRootPane().setBackground(UIManager.getColor("Chevy.color.purpleDark"));
                    setTitle("Chevy - Opzioni");
                    yield options.getRoot();
                }
                case TUTORIAL -> {
                    if (this.scene != Scene.MENU) {
                        Sound.startMusic(Sound.Music.SAME_SONG);
                    }
                    setTitle("Chevy - Tutorial");
                    gamePanel.addComponents(true);
                    getRootPane().setBackground(gamePanel.getBackground());
                    yield gamePanel;
                }
            };
            options.setupReturnAction(this.scene);
            this.scene = scene;
            setContentPane(panel);
            requestFocus(); // mantieni il focus su Window
            validate();
        }
    }

    public GamePanel getGamePanel() {return gamePanel;}

    public Menu getMenu() {return menu;}

    public Options getOptions() {return options;}

    public WindowController getWindowController() {return windowController;}

    public enum Scene {MENU, PLAYING, OPTIONS, TUTORIAL}
}