package chevy.view;

import chevy.control.ChamberController;
import chevy.model.chamber.ChamberManager;
import chevy.service.GameLoop;
import chevy.service.Sound;
import chevy.utils.Load;
import chevy.utils.Utils;
import chevy.view.chamber.ChamberView;
import chevy.view.hud.HUDView;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import java.awt.Image;

public class GamePanel extends JPanel {
    private static final ImageIcon playPause =
            new ImageIcon(Load.image("/assets/icons/PlayPause.png").getScaledInstance(48, 48, Image.SCALE_SMOOTH));
    private static final ImageIcon caution =
            new ImageIcon(Load.image("/assets/icons/caution.png").getScaledInstance(48, 48, Image.SCALE_SMOOTH));
    private static final ImageIcon skull = new ImageIcon(Load.image("/assets/icons/Skull.png").getScaledInstance(48,
            48, Image.SCALE_SMOOTH));

    private static final String[] deathMessages = new String[]{"Complimenti, hai vinto un biglietto per l'aldilà! " +
            "Prossima fermata: riprova!", "Sembra che il tuo personaggio abbia deciso di prendersi una pausa... dalla" +
            " vita.", "Ecco un esempio perfetto di cosa NON fare. Riprovaci!", "Il tuo personaggio ha appena scoperto" +
            " il modo più veloce per tornare al menu principale!", "Se ti consola, anche i bot rideranno di questa " +
            "mossa!", "Complimenti, hai vinto un biglietto per l'aldilà! Prossima fermata: riprova!"};
    private static final ChamberView chamberView = new ChamberView();
    private static boolean pauseDialogActive = false;
    private static boolean playerDeathDialogActive = false;
    private final HUDView hudView = new HUDView(1.3f);
    private final Window window;

    public GamePanel(Window window) {
        this.window = window;
        ChamberController.setGamePanel(this);

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
        if (!(pauseDialogActive || playerDeathDialogActive)) {
            window.setTitle("Chevy - Pausa");
            GameLoop.getInstance().stop();
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
                    GameLoop.getInstance().stop();
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
                    GameLoop.getInstance().start();
                    Sound.getInstance().resumeMusic();
                    window.setTitle("Chevy");
                }
            }
            pauseDialogActive = false;
        }
    }

    /**
     * Dialogo innescato dalla morte del giocatore.
     */
    public void playerDeathDialog() {
        playerDeathDialogActive = true;
        GameLoop.getInstance().stop();
        Sound.getInstance().stopMusic();
        switch (JOptionPane.showOptionDialog(window, deathMessages[Utils.random.nextInt(deathMessages.length)],
                "Chevy - Morte", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, skull, new String[]{"Esci",
                        "Torna al menù", "Rigioca livello"}, "Rigioca livello")) {
            case 0 -> {
                if (window.quitAction()) {
                    playerDeathDialog();
                }
            }
            case 1 -> window.setScene(Window.Scene.MENU);
            case 2 -> ChamberManager.enterChamber(ChamberManager.getCurrentChamberIndex());
            default -> playerDeathDialog(); // Questo dialogo non può essere ignorato
        }
        playerDeathDialogActive = false;
    }

    public void windowResized(float scale) { hudView.windowResized(scale); }

    public ChamberView getChamberView() { return chamberView; }

    public HUDView getHudView() { return hudView; }
}
