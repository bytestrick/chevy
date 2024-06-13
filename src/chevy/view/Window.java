package chevy.view;

import chevy.settings.GameSettings;
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

        setResizable(resizable);
        if (resizable)
            makeResponsive();

        setSize(size);
        setLocationRelativeTo(null);
        setBackground(Color.DARK_GRAY);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel = new GamePanel();
        add(gamePanel);

        setVisible(true);
        WindowSettings.SIZE_TOP_BAR = getInsets().top;
        requestFocus();

        System.out.println(size);
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