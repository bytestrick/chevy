package chevy.view;

import chevy.control.ChamberController;
import chevy.model.chamber.ChamberManager;
import chevy.service.GameLoop;
import chevy.service.Sound;
import chevy.utils.Load;
import chevy.utils.Utils;
import chevy.view.chamber.ChamberView;
import chevy.view.hud.HUDView;

import javax.swing.*;

public final class GamePanel extends JPanel {
    public static final Icon caution = Load.icon("caution", 48, 48);
    private static final Icon playPause = Load.icon("PlayPause", 48, 48);
    private static final Icon skull = Load.icon("Skull", 48, 48);
    private static final Icon trophy = Load.icon("Trophy", 48, 48);
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
    private static boolean pauseDialogActive;
    private static boolean playerDeathDialogActive;
    private static boolean winDialogActive;
    private final HUDView hudView = new HUDView(1.3f);
    private final Tutorial tutorial;
    private final Window window;

    public GamePanel(Window window) {
        this.window = window;
        ChamberController.setGamePanel(this);
        tutorial = new Tutorial(window);

        setLayout();
        setBackground(Window.bg);
    }

    public void addComponents(boolean tutorialBool) {
        if (tutorialBool) {
            remove(hudView);
            remove(chamberView);
            add(tutorial);
        }
        else {
            remove(tutorial);
            add(hudView);
            add(chamberView);
        }

        revalidate();
        repaint();
        setLayout();
    }

    public static boolean isPauseDialogNotActive() {return !pauseDialogActive;}

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


        springLayout.putConstraint(SpringLayout.NORTH, tutorial, 0, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, tutorial, 0, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.WEST, tutorial, 0, SpringLayout.WEST, this);
        springLayout.putConstraint(SpringLayout.SOUTH, tutorial, 0, SpringLayout.SOUTH, this);
    }

    /**
     * Dialogo di pausa del gioco. È innescato dalla pressione di ESC.
     */
    public void pauseDialog() {
        if (!(pauseDialogActive || playerDeathDialogActive || winDialogActive || window.isQuitDialogActive())) {
            pauseDialogActive = true;
            window.setTitle("Chevy - Pausa");
            GameLoop.stop();
            Sound.stopMusic();
            String message = "Chevy è in pausa, scegli cosa fare.";
            final int ans = JOptionPane.showOptionDialog(window, message, null,
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, playPause,
                    new String[]{"Esci", "Opzioni", "Torna " + "al menù", "Riprendi"},
                    "Riprendi");
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
                    int subAns = JOptionPane.showOptionDialog(window, "Se torni al menù perderai " +
                                    "il progresso. Continuare?", null, JOptionPane.YES_NO_OPTION,
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
                    if (ans == 3) {
                        Sound.play(Sound.Effect.BUTTON);
                    }
                    // Considera anche il caso in cui l'utente chiude la finestra di dialogo.
                    GameLoop.start();
                    Sound.startMusic(false);
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
        switch (JOptionPane.showOptionDialog(window, deathMessages[Utils.random.nextInt(deathMessages.length)],
                null, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, skull,
                new String[]{"Esci",
                        "Torna al menù", "Rigioca livello"}, "Rigioca livello")) {
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
        String[] option = new String[]{"Esci", "Rigioca livello", "Torna al menù", "Prossimo livello"};
        int defaultOption = 3;
        if (ChamberManager.isLastChamber()) {
            option = new String[]{"Esci", "Rigioca livello", "Torna al menù"};
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

    public void windowResized(float scale) {
        hudView.windowResized(scale);
    }

    public ChamberView getChamberView() {return chamberView;}

    public HUDView getHudView() {return hudView;}

    public Window getWindow() {return window;}

    public Tutorial getTutorial() { return tutorial; }
}