package chevy.view.hud;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

import chevy.utils.Fontt;
import component.NoCaret;

public class TextPowerUp extends JPanel {
    private static final String FONT_PATH = "/fonts/";
    private Color colorBg = new Color(0, 0, 0, 180);
    private String title = "Title\n";
    private int titleFontSize = 42;
    private Color colorTitle = new Color(255, 255, 255, 255);
    private String description = "Description";
    private int descriptionFontSize = 32;
    private Color colorDescription = new Color(255, 255, 255, 255);;
    private Font font;
    private JTextPane textPane = new JTextPane();

    public TextPowerUp() {
        setOpaque(true);
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        setBackground(colorBg);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setMaximumSize(d);
        setPreferredSize(d);
        setMaximumSize(d);

        textPane.setOpaque(false);
        textPane.setEditable(false);
        textPane.setCaret(new NoCaret());
        StyledDocument doc = textPane.getStyledDocument();

        setFont(FONT_PATH + "PixelatedPusab.ttf");

        // Creazione di stili per il titolo e la descrizione
        Style titleStyle = doc.addStyle("TitleStyle", null);
        StyleConstants.setAlignment(titleStyle, StyleConstants.ALIGN_CENTER);
        StyleConstants.setForeground(titleStyle, colorTitle); // Colore del testo
        StyleConstants.setFontSize(titleStyle, titleFontSize); // Dimensione del font
        StyleConstants.setBold(titleStyle, true); // Grassetto

        Style descriptionStyle = doc.addStyle("DescriptionStyle", null);
        StyleConstants.setAlignment(descriptionStyle, StyleConstants.ALIGN_CENTER);
        StyleConstants.setForeground(descriptionStyle, colorDescription);
        StyleConstants.setFontFamily(descriptionStyle, font.getFamily());
        StyleConstants.setFontSize(descriptionStyle, descriptionFontSize);

        try {
            doc.insertString(doc.getLength(), title, titleStyle);
            doc.setParagraphAttributes(doc.getLength() - title.length(), title.length(), titleStyle, false);
            doc.insertString(doc.getLength(), description, descriptionStyle);
            doc.setParagraphAttributes(doc.getLength() - description.length(), description.length(), descriptionStyle, false);
        }
        catch (BadLocationException e) {
            System.out.println("ERRORE");
        }

        springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, textPane, 0, SpringLayout.HORIZONTAL_CENTER, this);
        springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, textPane, 0, SpringLayout.VERTICAL_CENTER, this);

        add(textPane);

        revalidate();
        setVisible(true);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String title) {
        this.description = description;
    }

    public void setColorBg(Color colorBg) {
        this.colorBg = colorBg;
    }

    public void setTitleFontSize(int titleFontSize) {
        this.titleFontSize = titleFontSize;
    }

    public void setColorTitle(Color colorTitle) {
        this.colorTitle = colorTitle;
    }

    public void setDescriptionFontSize(int descriptionFontSize) {
        this.descriptionFontSize = descriptionFontSize;
    }

    public void setColorDescription(Color colorDescription) {
        this.colorDescription = colorDescription;
    }

    public void setFont(String path) {
        this.font = Fontt.load(path);
        textPane.setFont(font);
    }
}
