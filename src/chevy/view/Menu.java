package chevy.view;

import chevy.control.MenuController;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class Menu {
    public JPanel root;
    private JButton play;
    private JButton playerCycleNext;
    private JButton playerCyclePrev;
    private JPanel playerImage;
    private JLabel character;

    public Menu(Window window) {
        JButton[] buttons = new JButton[]{play, playerCycleNext, playerCyclePrev};

        character.setFont(Window.font.deriveFont(30f));
        play.setFont(Window.font.deriveFont(Font.BOLD, 50f));

        new MenuController(window, play, playerCycleNext, playerCyclePrev);
    }
}