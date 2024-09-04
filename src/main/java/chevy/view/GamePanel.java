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
import java.awt.*;

public final class GamePanel extends JPanel {
    private static final ImageIcon playPause = Load.icon("PlayPause", 48, 48);
    private static final ImageIcon caution = Load.icon("caution", 48, 48);
    private static final ImageIcon skull = Load.icon("Skull", 48, 48);
    private static final ImageIcon trophy = Load.icon("Trophy.png", 48, 48);
    // @formatter:off
    private static final String[] deathMessages = new String[]{
            "Complimenti, hai vinto un biglietto per l'aldilà! Prossima fermata: riprova!",
            "Sembra che il tuo personaggio abbia deciso di prendersi una pausa... dalla vita.",
            "Ecco un esempio perfetto di cosa NON fare. Riprovaci!",
            "Il tuo personaggio ha appena scoperto il modo più veloce per tornare al menu principale!",
            "Se ti consola, anche i bot rideranno di questa mossa!"};
    private static final String[] winMessages = new String[] {
            "Grandioso! Ogni mossa è stata perfetta, livello conquistato!",
            "Impressionante! Non c'è nulla che possa fermarti, grande performance!",
            "Superbo! Questo livello non aveva alcuna chance contro di te!",
            "Complimenti! La tua strategia ha fatto la differenza, livello completato!",
            "Complimenti! Hai dimostrato ancora una volta la tua abilità!"};
    // @formatter:on
    private static final ChamberView chamberView = new ChamberView();
    private static boolean pauseDialogActive = false;
    private static boolean playerDeathDialogActive = false;
    private static boolean winDialogActive = false;
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
        if (!(pauseDialogActive || playerDeathDialogActive || winDialogActive)) {
            window.setTitle("Chevy - Pausa");
            GameLoop.stop();
            boolean pauseMusic = true;
            pauseDialogActive = true;
            switch (JOptionPane.showOptionDialog(window, "Chevy è in pausa, scegli cosa " + "fare" +
                    ".", null, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, playPause,
                    new String[]{"Esci", "Opzioni", "Torna " + "al menù", "Riprendi"},
                    "Riprendi")) {
                case 0 -> {
                    pauseDialogActive = false;
                    window.quitAction();
                    pauseDialog();
                }
                case 1 -> {
                    GameLoop.stop();
                    pauseMusic = false;
                    window.setScene(Window.Scene.OPTIONS);
                }
                case 2 -> {
                    if (JOptionPane.showOptionDialog(window, "Se torni al menù perderai il " +
                            "progresso. Continuare?", null, JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE, caution, new String[]{"Si", "No"}, "No") == 0) {
                        window.setScene(Window.Scene.MENU);
                        GameLoop.stop();
                    } else {
                        pauseDialogActive = false;
                        pauseDialog();
                    }
                }
                default -> { // e case 3
                    // Considera anche il caso in cui l'utente chiude la
                    // finestra di dialogo.
                    GameLoop.start();
                    pauseMusic = false;
                    Sound.resumeMusic();
                    window.setTitle("Chevy");
                }
            }
            if (pauseMusic)
                Sound.pauseMusic();
            pauseDialogActive = false;
        }
    }

    /**
     * Dialogo innescato dalla morte del giocatore.
     */
    public void playerDeathDialog() {
        playerDeathDialogActive = true;
        GameLoop.stop();
        boolean pauseMusic = true;

        switch (JOptionPane.showOptionDialog(window, deathMessages[Utils.random.nextInt(deathMessages.length)],
                "Chevy - Morte", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, skull, new String[]{"Esci",
                        "Torna al menù", "Rigioca livello"}, "Rigioca livello")) {
            case 0 -> {
                window.quitAction();
                playerDeathDialog();
            }
            case 1 -> window.setScene(Window.Scene.MENU);
            case 2 ->  {
                pauseMusic = false;
                ChamberManager.enterChamber(ChamberManager.getCurrentChamberIndex());
            }
            default -> playerDeathDialog(); // Questo dialogo non può essere ignorato
        }

        if (pauseMusic)
            Sound.pauseMusic();
        playerDeathDialogActive = false;
    }

    public void winDialog() {
        winDialogActive = true;
        GameLoop.stop();
        boolean pauseMusic = true;
        String[] option = new String[]{"Esci", "Torna al menù", "Rigioca livello", "Continua"};
        int defaultOption = 3;
        if (ChamberManager.isLastChamber()) {
            option = new String[]{"Esci", "Torna al menù", "Rigioca livello"};
            defaultOption = 2;
        }

        switch (JOptionPane.showOptionDialog(window,
                winMessages[Utils.random.nextInt(winMessages.length)],
                "Chevy - Vittoria",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                trophy,
                option, option[defaultOption])) {
            case 0 -> window.quitAction();
            case 1 -> window.setScene(Window.Scene.MENU);
            case 2 -> {
                pauseMusic = false;
                ChamberManager.enterChamber(ChamberManager.getCurrentChamberIndex());
            }
            case 3 -> {
                pauseMusic = false;
                ChamberManager.nextChamber();
            }
            default -> winDialog(); // Questo dialogo non può essere ignorato
        }

        if (pauseMusic)
            Sound.pauseMusic();
        winDialogActive = false;
    }

    public void windowResized(float scale) { hudView.windowResized(scale); }

    public ChamberView getChamberView() {return chamberView;}

    public HUDView getHudView() {return hudView;}

    public Window getWindow() {return window;}
}
