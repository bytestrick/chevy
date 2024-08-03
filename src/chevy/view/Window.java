package chevy.view;

import chevy.settings.WindowSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Window extends JFrame {
    public static final Dimension size = new Dimension(WindowSettings.WINDOW_WIDTH, WindowSettings.WINDOW_HEIGHT);
    private final GamePanel gamePanel;


    public Window(boolean resizable) {
        setTitle("Chevy");

        setSize(size);
        setLocationRelativeTo(null);
        setBackground(Color.DARK_GRAY);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel = new GamePanel();
        add(gamePanel, BorderLayout.CENTER);

        setVisible(true);
        requestFocus();

        // assicura l'esecuzione del codice solamente dopo la creazione del componente JFrame
        SwingUtilities.invokeLater(() -> {
            WindowSettings.SIZE_TOP_BAR = getInsets().top;
            setResizable(resizable);
            if (resizable)
                makeResponsive();
            System.out.println("Start window size: " + size);
        });
    }

    private void makeResponsive() {
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                WindowSettings.updateValue(getHeight(), getWidth());
                gamePanel.windowResized();
            }
        });
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}