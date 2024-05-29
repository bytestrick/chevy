package chevy.view;

import chevy.settings.Settings;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Window extends JFrame {
    public static final Dimension size = new Dimension(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);
    public Font font;

    public Window() {
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().getResourceAsStream("/res/assets/Silver.ttf"))).deriveFont(48f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            setFont(font);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        setTitle("Chevy");
        setLocationRelativeTo(null);
        setResizable(true);
        setSize(size);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(true);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - size.width) / 2, (screen.height - size.height) / 2);
        setVisible(true);
        requestFocus();

        Settings.SIZE_TOP_BAR = getInsets().top;
        System.out.println(getSize());
    }
}