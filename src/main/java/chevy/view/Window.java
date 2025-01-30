package chevy.view;

import chevy.control.WindowController;
import chevy.model.chamber.ChamberManager;
import chevy.service.Data;
import chevy.service.GameLoop;
import chevy.service.Sound;
import chevy.utils.Load;
import chevy.utils.Log;
import chevy.view.chamber.ChamberView;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.util.SystemInfo;

import javax.swing.*;
import java.awt.*;

/**
 * Main window
 */
public final class Window extends JFrame {
    static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Dimension ASPECT_RATIO = new Dimension(4, 3);
    private static final Icon POWER = Load.icon("Power", 42, 42);
    public static float scale = .8f;
    public static Dimension size = new Dimension();
    private static boolean quitDialogActive;

    static {
        int maxSideLength = Math.round(Math.min(SCREEN_SIZE.width, SCREEN_SIZE.height) * scale);
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
        // Background color of the window during loading
        setBackground(UIManager.getColor("Chevy.color.purpleDark"));
        windowController = new WindowController(this);
        setSize(size);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setScene(Scene.MENU);
        setVisible(true);
        setMinimumSize(new Dimension(550, 650));
        requestFocus();
        SwingUtilities.invokeLater(() -> {
            ChamberView.topBarHeight = getInsets().top;
            setResizable(true);
            // componentResized() is not always called at startup, this ensures that all components are scaled correctly
            updateSize(getSize());
            gamePanel.windowResized(scale);
        });
        ChamberManager.setWindow(this);
        Log.info("Window size: " + size.width + "x" + size.height);
    }

    public static void updateSize(Dimension size) {
        Window.size = size;
        ChamberView.updateSize();
        float scaleX = 1.f * size.width / SCREEN_SIZE.width * ASPECT_RATIO.width;
        float scaleY = 1.f * size.height / SCREEN_SIZE.height * ASPECT_RATIO.height;
        scale = Math.min(scaleX, scaleY);
    }

    /**
     * Initialises FlatLaf and sets customisations.
     * <a href="https://www.formdev.com/flatlaf/customizing/">Documentation</a>.
     * Changes related to the LookAndFell must be done before creating instances of any component.
     */
    public static void create() {
        FlatLaf.registerCustomDefaultsSource("style"); // load the style sheet
        FlatDarkLaf.setup();
        // Window decorations are already enabled on Windows and are not supported on Mac.
        if (SystemInfo.isLinux) {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
        }
        UIManager.put("defaultFont", Load.font("VT323"));

        // The initial delay before the tooltip appears is shorter
        ToolTipManager.sharedInstance().setInitialDelay(50);
        // The tooltip persists for an hour if the cursor lies over it for that long
        ToolTipManager.sharedInstance().setDismissDelay(3_600_000);

        new Window();
    }

    public static boolean isQuitDialogNotActive() {
        return !quitDialogActive;
    }

    static boolean isQuitDialogActive() {
        return quitDialogActive;
    }

    /**
     * Only correct way to exit the app.
     * The {@link WindowController} links the closing event of the {@code JFrame} to this
     * method, so pressing the 'X' of the window frame passes control here.
     */
    public void quitAction() {
        quitDialogActive = true;
        if (scene == Scene.PLAYING) {
            GameLoop.stop();
            Sound.stopMusic();
        }
        Sound.play(Sound.Effect.STOP);
        final String[] opts = Options.strings.getString("dialog.yesNo").split(",");
        final int ans = JOptionPane.showOptionDialog(this, Options.strings.getString("dialog.confirmQuit"), null,
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, POWER, opts, opts[opts.length - 1]);
        if (ans == 0) {
            Sound.play(Sound.Effect.BUTTON);
            Log.info("Saving data ...");
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
     * Bring the app to the default state
     */
    void refresh() {
        menu = new Menu(this);
        gamePanel = new GamePanel(this);
        options = new Options(this);
        gamePanel.windowResized(Window.scale);
    }

    public Scene getScene() {
        return scene;
    }

    /**
     * Change the scene
     *
     * @param scene the new scene
     */
    void setScene(Scene scene) {
        if (this.scene != scene) {
            Log.info("Changing scene from " + this.scene + " to " + scene);
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
                    gamePanel.addComponents(false);
                    yield gamePanel;
                }
                case OPTIONS -> {
                    getRootPane().setBackground(UIManager.getColor("Chevy.color.purpleDark"));
                    setTitle(Options.strings.getString("title.options"));
                    yield options.getRoot();
                }
                case TUTORIAL -> {
                    if (this.scene != Scene.MENU) {
                        Sound.startMusic(Sound.Music.SAME_SONG);
                    }
                    setTitle(Options.strings.getString("title.tutorial"));
                    gamePanel.addComponents(true);
                    getRootPane().setBackground(gamePanel.getBackground());
                    yield gamePanel;
                }
            };
            options.setupReturnAction(this.scene);
            this.scene = scene;
            setContentPane(panel);
            requestFocus(); // maintain focus on the window
            validate();
        }
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public Menu getMenu() {
        return menu;
    }

    public Options getOptions() {
        return options;
    }

    public WindowController getWindowController() {
        return windowController;
    }

    public enum Scene {MENU, PLAYING, OPTIONS, TUTORIAL}
}
