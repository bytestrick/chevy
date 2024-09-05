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
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Finestra principale
 */
public final class Window extends JFrame {
    public final static Color bg = new Color(24, 20, 37);
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Dimension aspectRatio = new Dimension(4, 3);
    private static final Icon icon = Load.icon("Power", 42, 42);
    public static float scale = .8f; // TODO: questa non è la scala
    public static Dimension size = new Dimension();
    private static boolean quitDialogActive;

    static {
        final int maxSideLength = Math.round(Math.min(screenSize.width, screenSize.height) * scale);
        if (screenSize.width < screenSize.height) {
            size.height = Math.round(1.f * maxSideLength * aspectRatio.height / aspectRatio.width);
        } else {
            size.height = maxSideLength;
        }
        if (screenSize.width > screenSize.height) {
            size.width = Math.round(1.f * maxSideLength * aspectRatio.width / aspectRatio.height);
        } else {
            size.width = maxSideLength;
        }
        scale = Math.min(size.width / screenSize.width, size.height / screenSize.height);
    }

    private final WindowController windowController;
    private GamePanel gamePanel = new GamePanel(this);
    private Options options = new Options(this);
    private Menu menu = new Menu(this);
    private Scene scene;

    public Window(boolean resizable) {
        final Color bg = UIManager.getColor("Chevy.color.purpleDark");
        getRootPane().setBackground(bg);
        getContentPane().setBackground(bg);
        windowController = new WindowController(this);
        setSize(size);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setScene(Scene.MENU);
        setVisible(true);
        requestFocus();
        SwingUtilities.invokeLater(() -> { // esecuzione asincrona
            ChamberView.topBarHeight = getInsets().top;
            setResizable(resizable);
            if (resizable) {
                // visto che componentResized() non viene chiamata sempre all'avvio questo
                // assicura il corretto scale dei componenti
                updateSize(getSize());
                gamePanel.windowResized(scale);
            }
        });
        Log.info("Window: creazione terminata [w: " + size.width + ", h: " + size.height + "]");
    }

    public static void updateSize(Dimension size) {
        Window.size = size;
        ChamberView.updateSize();
        float scaleX = 1.f * size.width / screenSize.width * aspectRatio.width;
        float scaleY = 1.f * size.height / screenSize.height * aspectRatio.height;
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
        new Window(true);
    }

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
        if (JOptionPane.showOptionDialog(this, "Uscire da Chevy?", null,
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, icon, new String[]{"Si",
                        "No"}, "No") == 0) {
            Log.info("Salvataggio dei dati ...");
            Data.write();
            Log.info("Ciao");
            System.exit(0);
        }
        quitDialogActive = false;
        if (scene == Scene.PLAYING) {
            gamePanel.pauseDialog();
        }
    }

    /**
     * Utile per portare l'app allo stato predefinito
     */
    public void refresh() {
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
    public void setScene(Scene scene) {
        if (this.scene != scene) {
            Log.info("Cambio scena da " + this.scene + " a " + scene);
            final JPanel panel = switch (scene) {
                case MENU -> {
                    if (this.scene != Scene.OPTIONS) {
                        Sound.startLoop(true);
                    }
                    getRootPane().setBackground(menu.getRoot().getBackground());
                    menu.startCharacterAnimation();
                    menu.updateLevel();
                    setTitle("Chevy");
                    yield menu.getRoot();
                }
                case PLAYING -> {
                    getRootPane().setBackground(bg);
                    setTitle("Chevy");
                    yield gamePanel;
                }
                case OPTIONS -> {
                    setTitle("Chevy - Opzioni");
                    yield options.getRoot();
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

    public WindowController getWindowController() {return windowController;}

    public boolean isQuitDialogActive() {return quitDialogActive;}

    public static boolean isQuitDialogNotActive() {return !quitDialogActive;};

    public enum Scene {MENU, PLAYING, OPTIONS}
}