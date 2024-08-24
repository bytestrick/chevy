package chevy.view;

import javax.swing.JButton;
import javax.swing.JPanel;

public class Options {
    public JPanel root;
    private JButton quit;
    private Window.Scene sceneToReturnTo;

    // È importante tracciare da quale scena si passa a OPTIONS, perché se si passa da PLAYING a OPTION all'uscita si
    // deve fare ripartire il GameLoop e Sound. Mentre se si passa da OPTIONS a MENU si deve fare ripartire
    // l'animazione del personaggio Mentre se si passa da OPTIONS a MENU si deve fare ripartire l'animazione del
    // personaggio.

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

    public void setupReturnAction(Window.Scene scene) {
        sceneToReturnTo = scene;
        quit.setText(scene == Window.Scene.PLAYING ? "Ritorna al gioco" : "Ritorna al menù");
    }
}