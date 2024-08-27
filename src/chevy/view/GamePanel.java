package chevy.view;

import chevy.service.GameLoop;
import chevy.service.Sound;
import chevy.settings.WindowSettings;
import chevy.utils.Load;
import chevy.view.chamber.ChamberView;
import chevy.view.hud.HUDView;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import java.awt.Image;

public class GamePanel extends JPanel {
    private final static ImageIcon playPause =
            new ImageIcon(Load.image("/assets/icons/PlayPause.png").getScaledInstance(48, 48, Image.SCALE_SMOOTH));
    private final static ImageIcon caution =
            new ImageIcon(Load.image("/assets/icons/caution.png").getScaledInstance(48, 48, Image.SCALE_SMOOTH));
    private static final ChamberView chamberView = new ChamberView();
    private static boolean pauseDialogActive = false;
    private final HUDView hudView = new HUDView(3.0f);
    private final Window window;

    public GamePanel(Window window) {
        this.window = window;

        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        springLayout.putConstraint(SpringLayout.NORTH, chamberView, 0, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, chamberView, 0, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.WEST, chamberView, 0, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, chamberView, 0, SpringLayout.SOUTH, this);

        springLayout.putConstraint(SpringLayout.NORTH, hudView, 0, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, hudView, 0, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.WEST, hudView, 0, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, hudView, 0, SpringLayout.SOUTH, this);

        add(hudView);
        add(chamberView);
        setBackground(Window.bg);
    }

    /**
     * Dialogo di pausa del gioco. È innescato dalla pressione di ESC.
     */
    public void pauseDialog() {
        if (!pauseDialogActive) {
            window.setTitle("Chevy - Pausa");
            GameLoop.getInstance().pause();
            Sound.getInstance().pauseMusic();
            pauseDialogActive = true;
            switch (JOptionPane.showOptionDialog(window, "Chevy è in pausa, scegli cosa fare.", "Chevy - Pausa " +
                    "(dialogo)", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, playPause, new String[]{"Esci"
                    , "Opzioni", "Torna al menù", "Riprendi"}, "Riprendi")) {
                case 0 -> {
                    pauseDialogActive = false;
                    if (window.quitAction()) {
                        pauseDialog(); // Ricorsione ☺️
                    }
                }
                case 1 -> {
                    GameLoop.getInstance().pause();
                    Sound.getInstance().pauseMusic();
                    window.setScene(Window.Scene.OPTIONS);
                }
                case 2 -> {
                    if (JOptionPane.showOptionDialog(window, "Se torni al menù perderai il progresso. Continuare?",
                            "Chevy - " + "Conferma (dialogo)", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                            caution, new String[]{"Si", "No"}, "No") == 0) {
                        window.setScene(Window.Scene.MENU);
                        GameLoop.getInstance().stop();
                        Sound.getInstance().stopMusic();
                    } else {
                        pauseDialogActive = false;
                        pauseDialog(); // Ricorsione ☺️
                    }
                }
                default -> { // e case 3
                    // Considera anche il caso in cui l'utente chiude la finestra di dialogo.
                    GameLoop.getInstance().resume();
                    Sound.getInstance().resumeMusic();
                    window.setTitle("Chevy");
                }
            }
            pauseDialogActive = false;
        }
    }

    public void windowResized() {
        float scale = Math.min(WindowSettings.scaleX, WindowSettings.scaleY);
        hudView.windowResized(scale);
    }

    public ChamberView getChamberView() { return chamberView; }

    public HUDView getHudView() { return hudView; }
}
