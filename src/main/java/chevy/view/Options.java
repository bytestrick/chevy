package chevy.view;

import chevy.service.Data;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;

public class Options {
    public JPanel root;
    private JButton quit;
    private JButton restoreApp;
    private Window.Scene sceneToReturnTo;
    private final Window window;
    private static final Color destructiveActionBG = new Color(103, 40, 40);

    public Options(final Window window) {
        this.window = window;
        quit.addActionListener(e -> {
            if (sceneToReturnTo == Window.Scene.PLAYING) {
                window.setScene(sceneToReturnTo);
                window.gamePanel.pauseDialog();
            } else {
                window.setScene(sceneToReturnTo);
            }
        });

        restoreApp.setBackground(destructiveActionBG);

        restoreApp.addActionListener(e -> {
            // TODO: dialogo di conferma che avvisa della perdita del progresso
            Data.createPristineFile();
            Data.read();
            window.refresh();
        });
    }

    /**
     * Gestisce il contesto del passaggio da e a OPTIONS.
     * È importante tracciare da quale scena si passa a OPTIONS, perché se si arriva da PLAYING all'uscita si
     * deve fare ripartire GameLoop e musica. Mentre se si arriva da MENU si deve fare ripartire
     * l'animazione del personaggio.
     */
    public void setupReturnAction(Window.Scene scene) {
        sceneToReturnTo = scene;
        quit.setText(scene == Window.Scene.PLAYING ? "Ritorna al gioco" : "Ritorna al menù");
    }
}