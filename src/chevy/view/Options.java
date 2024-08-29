package chevy.view;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Options {
    public JPanel root;
    private JButton quit;
    private Window.Scene sceneToReturnTo;

    public Options(final Window window) {
        quit.addActionListener(e -> {
            if (sceneToReturnTo == Window.Scene.PLAYING) {
                window.setScene(sceneToReturnTo);
                window.gamePanel.pauseDialog();
            } else {
                window.setScene(sceneToReturnTo);
            }
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