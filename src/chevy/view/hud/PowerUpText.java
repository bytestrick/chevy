package chevy.view.hud;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

import chevy.model.entity.collectable.powerUp.PowerUp;
import chevy.settings.WindowSettings;
import chevy.utils.Fontt;
import chevy.view.component.NoCaret;

public class PowerUpText extends JPanel {
    private static final String FONT_PATH = "/fonts/PixelatedPusab.ttf";
    private static final int TITLE_FONT_SIZE = 24;
    private static final int DESCRIPTION_FONT_SIZE = 16;
    private static final Color COLOR_BG = new Color(0, 0, 0, 180);
    private static final Color COLOR_TITLE = new Color(255, 163, 0, 255);
    private static final Color COLOR_DESCRIPTION = new Color(255, 235, 151, 255);

    private String name = "Title\n";
    private String description = "Description";
    private Font font;
    private JTextPane textPane = new JTextPane();
    private final StyledDocument doc = textPane.getStyledDocument();
    private final Style titleStyle = doc.addStyle("TitleStyle", null);
    private final Style descriptionStyle = doc.addStyle("DescriptionStyle", null);

    public PowerUpText() {
        initializeLayout();
        initializeStyles();
        setFont(FONT_PATH);
        setVisible(true);
    }

    private void initializeLayout() {
        setOpaque(true);
        setBackground(COLOR_BG);
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        textPane.setOpaque(false);
        textPane.setEditable(false);
        textPane.setCaret(new NoCaret());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setMaximumSize(screenSize);
        setPreferredSize(screenSize);

        springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, textPane, 0, SpringLayout.HORIZONTAL_CENTER, this);
        springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, textPane, 0, SpringLayout.VERTICAL_CENTER, this);

        add(textPane);
    }

    private void initializeStyles() {
        StyleConstants.setAlignment(titleStyle, StyleConstants.ALIGN_CENTER);
        StyleConstants.setForeground(titleStyle, COLOR_TITLE);

        StyleConstants.setAlignment(descriptionStyle, StyleConstants.ALIGN_CENTER);
        StyleConstants.setForeground(descriptionStyle, COLOR_DESCRIPTION);
    }

    public void setFont(String path) {
        this.font = Fontt.load(path);
        textPane.setFont(font);
    }

    private void updateStyles() {
        int newTitleFontSize = Math.max(1, (int) (TITLE_FONT_SIZE * WindowSettings.scale));
        int newDescriptionFontSize = Math.max(1, (int) (DESCRIPTION_FONT_SIZE * WindowSettings.scale));

        StyleConstants.setFontSize(titleStyle, newTitleFontSize);
        StyleConstants.setFontSize(descriptionStyle, newDescriptionFontSize);
    }

    private void write() {
        try {
            doc.remove(0, doc.getLength());

            updateStyles();

            doc.insertString(0, name, titleStyle);
            doc.setParagraphAttributes(0, name.length(), titleStyle, false);

            doc.insertString(doc.getLength(), description, descriptionStyle);
            doc.setParagraphAttributes(doc.getLength() - description.length(), description.length(), descriptionStyle, false);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        revalidate();
        repaint();
    }

    public void show(PowerUp powerUp) {
        name = powerUp.getName();
        description = powerUp.getDescription();
        write();
        setVisible(true);
    }

    public void mHide() {
        setVisible(false);
        try {
            doc.remove(0, doc.getLength());
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    public void windowResized(float scale) {
        write();
    }
}
