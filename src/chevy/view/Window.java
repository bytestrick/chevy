package chevy.view;

import chevy.settings.WindowSettings;
import chevy.utils.Log;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Window extends JFrame {
    public static final Dimension size = new Dimension(WindowSettings.WINDOW_WIDTH, WindowSettings.WINDOW_HEIGHT);
    private final GamePanel gamePanel;

    public Window(boolean resizable) {
        setTitle("Chevy");

        setResizable(resizable);
        if (resizable) makeResponsive();

        setSize(size);
        setLocationRelativeTo(null);
        setBackground(Color.DARK_GRAY);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel = new GamePanel();
        add(gamePanel);

        setVisible(true);
        WindowSettings.SIZE_TOP_BAR = getInsets().top;
        requestFocus();

        Log.info(size.toString());
    }

    private void makeResponsive() {
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                WindowSettings.updateValue(getHeight(), getWidth());
            }
        });
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}