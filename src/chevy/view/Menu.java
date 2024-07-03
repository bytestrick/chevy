package chevy.view;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;

public class Menu extends JPanel {
    public Menu(Window window) {
        setBackground(Color.RED);
        setAlignmentX(CENTER_ALIGNMENT);
        setAlignmentY(CENTER_ALIGNMENT);

        setSize(Window.size);

        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        JComponent characterSelector = new JPanel();
        add(characterSelector);

        // Selettore del tipo di giocatore, ...
        characterSelector.add(new JLabel("Archer"));
        characterSelector.add(new JButton(">"));
        characterSelector.add(new JButton("<"));

        JButton button = new JButton("Gioca");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setAlignmentY(Component.CENTER_ALIGNMENT);
        add(button);
    }
}