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
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public final class GamePanel extends JPanel {
    static final Icon caution = Load.icon("caution", 48, 48);
    private static final Icon restart = Load.icon("Restart", 48, 48);
    private static final Icon playPause = Load.icon("PlayPause", 48, 48);
    private static final Icon skull = Load.icon("Skull", 48, 48);
    private static final Icon trophy = Load.icon("Trophy", 48, 48);
    private static final ChamberView chamberView = new ChamberView();
    private static boolean pauseDialogActive;
    private static boolean playerDeathDialogActive;
    private static boolean winDialogActive;
    private final HUDView hudView = new HUDView(1.3f);
    private final Tutorial tutorial;
    private final Window window;

    GamePanel(Window window) {
        this.window = window;
        ChamberController.setGamePanel(this);
        tutorial = new Tutorial(window);

        setLayout();
        setBackground(new Color(24, 20, 37));
    }

    public static boolean isPauseDialogNotActive() {
        return !pauseDialogActive;
    }

    public static ChamberView getChamberView() {
        return chamberView;
    }

    void addComponents(boolean tutorialBool) {
        if (tutorialBool) {
            remove(hudView);
            remove(chamberView);
            add(tutorial);
        } else {
            remove(tutorial);
            add(hudView);
            add(chamberView);
        }

        revalidate();
        repaint();
        setLayout();
    }

    private void setLayout() {
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);
        for (JComponent component : List.of(chamberView, hudView, tutorial)) {
            for (String dir : List.of(SpringLayout.NORTH, SpringLayout.EAST, SpringLayout.WEST, SpringLayout.SOUTH)) {
                springLayout.putConstraint(dir, component, 0, dir, this);
            }
        }
    }

    /**
     * Game paused dialog. It is triggered by pressing ESC.
     */
    public void pauseDialog() {
        if (!(pauseDialogActive || playerDeathDialogActive || winDialogActive || Window.isQuitDialogActive())) {
            pauseDialogActive = true;
            window.setTitle(Options.strings.getString("title.pause"));
            GameLoop.stop();
            Sound.stopMusic();
            final String[] options = Options.strings.getString("dialog.pauseActions").split(", ");
            String message = Options.strings.getString("dialog.pause");
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
                    final String[] opts = Options.strings.getString("dialog.yesNo").split(",");
                    final int subAns = JOptionPane.showOptionDialog(window,
                            Options.strings.getString("dialog.pausePlayAgain"), null,
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, restart,
                            opts, opts[opts.length - 1]);
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
                    final String[] opts = Options.strings.getString("dialog.yesNo").split(",");
                    final int subAns = JOptionPane.showOptionDialog(window,
                            Options.strings.getString("dialog.pauseBackToMenu"), null,
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE, caution, opts, opts[opts.length - 1]);
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
                default -> {
                    if (ans == 4) {
                        Sound.play(Sound.Effect.BUTTON);
                    }
                    // Consider also the case where the user closes the dialog window.
                    GameLoop.start();
                    Sound.startMusic(Sound.Music.SAME_SONG);
                    setWindowTitle();
                }
            }
            pauseDialogActive = false;
        }
    }

    public void setWindowTitle() {
        window.setTitle(String.format(Options.strings.getString("title.level"),
                ChamberManager.getCurrentChamberIndex()));
    }

    /**
     * Dialog triggered by player death
     */
    public void playerDeathDialog() {
        playerDeathDialogActive = true;
        GameLoop.stop();
        Sound.stopMusic();
        window.setTitle(Options.strings.getString("title.death"));
        final String[] options = Options.strings.getString("dialog.deathActions").split(", ");
        final String[] deathMessages = Options.strings.getString("dialog.deathMessages").split(";");
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
            default -> playerDeathDialog(); // this dialog cannot be ignored
        }
        playerDeathDialogActive = false;
    }

    /**
     * Dialog triggered when the user makes the Player enter the trapdoor
     */
    public void winDialog() {
        winDialogActive = true;
        Sound.stopMusic();
        window.setTitle(Options.strings.getString("title.levelCompleted"));
        GameLoop.stop();
        String[] options = Options.strings.getString("dialog.levelCompletedActions").split(", ");
        int defaultOption = 3;
        if (ChamberManager.isLastChamber()) {
            options = Arrays.copyOf(options, options.length - 1);
            defaultOption = 2;
        }
        final String[] winMessages = Options.strings.getString("dialog.winMessages").split(";");
        switch (JOptionPane.showOptionDialog(window,
                winMessages[Utils.random.nextInt(winMessages.length)], null,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, trophy, options,
                options[defaultOption])) {
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
            default -> winDialog(); // this dialog cannot be ignored
        }
        winDialogActive = false;
    }

    public void windowResized(float scale) {
        hudView.windowResized(scale);
    }

    public HUDView getHudView() {
        return hudView;
    }

    public Window getWindow() {
        return window;
    }

    public Tutorial getTutorial() {
        return tutorial;
    }
}
