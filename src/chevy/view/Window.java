package chevy.view;

import chevy.control.KeyboardListener;
import chevy.settings.GameSettings;
import chevy.settings.WindowSettings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Objects;

public class Window extends JFrame {
    public static final Dimension size = new Dimension(WindowSettings.WINDOW_WIDTH, WindowSettings.WINDOW_HEIGHT);

    public Window() {
        setTitle("Chevy");
        setResizable(false);
        setSize(size);
        setLocationRelativeTo(null);
        setBackground(Color.DARK_GRAY);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        WindowSettings.SIZE_TOP_BAR = getInsets().top;
        requestFocus();

        System.out.println(size);


        // Rende la finestra responsive
//        this.addComponentListener(new ComponentAdapter() {
//            public void componentResized(ComponentEvent componentEvent) {
//                WindowSettings.WINDOW_HEIGHT = getHeight();
//                WindowSettings.WINDOW_WIDTH = getWidth();
//
//                GameSettings.SCALE_H = (float) (WindowSettings.WINDOW_HEIGHT - WindowSettings.SIZE_TOP_BAR) / GameSettings.nTileH;
//                GameSettings.SCALE_W = (float) WindowSettings.WINDOW_WIDTH / GameSettings.nTileW;
//
//                GameSettings.SCALE = (int) Math.min(GameSettings.SCALE_H, GameSettings.SCALE_W);
//            }
//        });
    }
}