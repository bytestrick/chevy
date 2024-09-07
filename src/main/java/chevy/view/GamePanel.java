package chevy.view;

import chevy.control.ChamberController;
import chevy.model.chamber.ChamberManager;
import chevy.service.GameLoop;
import chevy.service.Sound;
import chevy.utils.Load;
import chevy.utils.Utils;
import chevy.view.chamber.ChamberView;
import chevy.view.hud.HUDView;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public final class GamePanel extends JPanel {
    public static final Icon restart = Load.icon("Restart", 48, 48);
    static final Icon caution = Load.icon("caution", 48, 48);
    private static final Icon playPause = Load.icon("PlayPause", 48, 48);
    private static final Icon skull = Load.icon("Skull", 48, 48);
    private static final Icon trophy = Load.icon("Trophy", 48, 48);
    // @formatter:off
    private static final String[] deathMessages = new String[]{
            "Complimenti, hai vinto un biglietto per l'aldilà! Prossima fermata: riprova!",
            "Sembra che il tuo personaggio abbia deciso di prendersi una pausa... dalla vita.",
            "Ecco un esempio perfetto di cosa NON fare. Riprovaci!",
            "Il tuo personaggio ha appena scoperto il modo più veloce per tornare al menu!",
            "Se ti consola, anche i bot rideranno di questa mossa!"};
    private static final String[] winMessages = new String[] {
            "Grandioso! Ogni mossa è stata perfetta, livello conquistato!",
            "Impressionante! Non c'è nulla che possa fermarti, grande performance!",
            "Superbo! Questo livello non aveva alcuna chance contro di te!",
            "Complimenti! La tua strategia ha fatto la differenza, livello completato!",
            "Complimenti! Hai dimostrato ancora una volta la tua abilità!"};
    // @formatter:on
    private static final ChamberView chamberView = new ChamberView();
    private static boolean pauseDialogActive;
    private static boolean playerDeathDialogActive;
    private static boolean winDialogActive;
    private final HUDView hudView = new HUDView(1.3f);
    private final Window window;

    GamePanel(Window window) {
        this.window = window;
        ChamberController.setGamePanel(this);
        setLayout();
        add(hudView);
        add(chamberView);
        setBackground(Window.bg);
    }

    public static boolean isPauseDialogNotActive() {return !pauseDialogActive;}

    public static ChamberView getChamberView() {return chamberView;}

    private void setLayout() {
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
    }

    /**
     * Dialogo di pausa del gioco. È innescato dalla pressione di ESC.
     */
    public void pauseDialog() {
        if (!(pauseDialogActive || playerDeathDialogActive || winDialogActive
                || Window.isQuitDialogActive())) {
            pauseDialogActive = true;
            window.setTitle("Chevy - Pausa");
            GameLoop.stop();
            Sound.stopMusic();
            final String[] options = new String[]{"Esci", "Opzioni", "Rigioca", "Torna" +
                    " al menù", "Riprendi"};
            String message = "Chevy è in pausa, scegli cosa fare.";
            final int ans = JOptionPane.showOptionDialog(window, message, null,
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, playPause,
                    options,
                    options[options.length - 1]);
            switch (ans) {
                case 0 -> {
                    Sound.play(Sound.Effect.BUTTON);
                    window.quitAction();
                    pauseDialogActive = false;
                    pauseDialog();
                }
                case 1 -> {
                    Sound.play(Sound.Effect.BUTTON);
                    window.setScene(Window.Scene.OPTIONS);
                }
                case 2 -> {
                    Sound.play(Sound.Effect.BUTTON);
                    Sound.play(Sound.Effect.STOP);
                    final String msg = "Ricominciando il livello perderai il progresso. " +
                            "Continuare?";
                    final int subAns = JOptionPane.showOptionDialog(window, msg, null,
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, restart,
                            new String[]{"Si", "No"}, "No");
                    if (subAns == 0) {
                        Sound.play(Sound.Effect.BUTTON);
                        ChamberManager.enterChamber(ChamberManager.getCurrentChamberIndex());
                    } else {
                        if (subAns == 1) {
                            Sound.play(Sound.Effect.BUTTON);
                        }
                        pauseDialogActive = false;
                        pauseDialog();
                    }
                }
                case 3 -> {
                    Sound.play(Sound.Effect.STOP);
                    Sound.play(Sound.Effect.BUTTON);
                    final String msg = "Se torni al menù perderai il progresso. Continuare?";
                    final int subAns = JOptionPane.showOptionDialog(window, msg, null,
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE, caution, new String[]{"Si", "No"}, "No");
                    if (subAns == 0) {
                        Sound.play(Sound.Effect.BUTTON);
                        window.setScene(Window.Scene.MENU);
                        GameLoop.stop();
                    } else {
                        if (subAns == 1) {
                            Sound.play(Sound.Effect.BUTTON);
                        }
                        pauseDialogActive = false;
                        pauseDialog();
                    }
                }
                default -> { // e case 3
                    if (ans == 4) {
                        Sound.play(Sound.Effect.BUTTON);
                    }
                    // Considera anche il caso in cui l'utente chiude la finestra di dialogo.
                    GameLoop.start();
                    Sound.startMusic(Sound.Music.SAME_SONG);
                    window.setTitle("Chevy");
                }
            }
            pauseDialogActive = false;
        }
    }

    /**
     * Dialogo innescato dalla morte del giocatore
     */
    public void playerDeathDialog() {
        playerDeathDialogActive = true;
        GameLoop.stop();
        Sound.stopMusic();
        window.setTitle("Chevy - Morte");
        final String[] options = new String[]{"Esci", "Torna al menù", "Rigioca"};
        switch (JOptionPane.showOptionDialog(window,
                deathMessages[Utils.random.nextInt(deathMessages.length)],
                null, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, skull,
                options, options[options.length - 1])) {
            case 0 -> {
                Sound.play(Sound.Effect.BUTTON);
                window.quitAction();
                playerDeathDialogActive = false;
                playerDeathDialog();
            }
            case 1 -> {
                Sound.play(Sound.Effect.BUTTON);
                window.setScene(Window.Scene.MENU);
            }
            case 2 -> {
                Sound.play(Sound.Effect.BUTTON);
                ChamberManager.enterChamber(ChamberManager.getCurrentChamberIndex());
            }
            default -> playerDeathDialog(); // Questo dialogo non può essere ignorato
        }
        window.setTitle("Chevy - Morte");
        playerDeathDialogActive = false;
    }

    /**
     * Dialogo innescato quando l'utente fa entrare il Player nella botola
     */
    public void winDialog() {
        winDialogActive = true;
        Sound.stopMusic();
        window.setTitle("Chevy - Vittoria");
        GameLoop.stop();
        String[] option = new String[]{"Esci", "Rigioca", "Torna al menù", "Prossimo livello"};
        int defaultOption = 3;
        if (ChamberManager.isLastChamber()) {
            option = new String[]{"Esci", "Rigioca", "Torna al menù"};
            defaultOption = 2;
        }
        switch (JOptionPane.showOptionDialog(window,
                winMessages[Utils.random.nextInt(winMessages.length)], null,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, trophy, option,
                option[defaultOption])) {
            case 0 -> {
                Sound.play(Sound.Effect.BUTTON);
                window.quitAction();
                winDialogActive = false;
                winDialog();
            }
            case 1 -> {
                Sound.play(Sound.Effect.BUTTON);
                ChamberManager.enterChamber(ChamberManager.getCurrentChamberIndex());
            }
            case 2 -> {
                Sound.play(Sound.Effect.BUTTON);
                window.setScene(Window.Scene.MENU);
            }
            case 3 -> {
                Sound.play(Sound.Effect.BUTTON);
                ChamberManager.nextChamber();
            }
            default -> winDialog(); // Questo dialogo non può essere ignorato
        }
        window.setTitle("Chevy");
        winDialogActive = false;
    }

    public void windowResized(float scale) {hudView.windowResized(scale);}

    public HUDView getHudView() {return hudView;}

    public Window getWindow() {return window;}
}