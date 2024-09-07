package chevy.view;

import chevy.service.Sound;
import chevy.utils.Load;
import chevy.view.component.NoCaret;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public final class Tutorial extends JPanel {
    private static final int TITLE_SIZE = 48;
    private static final int TEXT_SIZE = 32;
    private static final Color TEXT_COLOR = new Color(202, 202, 202, 255);


    private String[] title = new String[] {
            "Benvenuto in Chevy!\n",
            "Ora, passiamo agli attacchi!\n",
            "Oggetti da raccogliere\n",
            "Pozioni di cura\n",
            "Potenziamenti\n",
            "Le chiavi\n",
            "Pericoli e trappole\n",
            "Pericoli e trappole\n",
            "Pericoli e trappole\n",
            "Pericoli e trappole\n",
            "Pericoli e trappole\n",
            "La via di uscita\n",
            "Pausa\n",
            "Fine del tutorial\n"
    };

    private String[] texts = new String[] {
            "\nUtilizza i tasti \"W\", \"A\", \"S\", \"D\" oppure le frecce direzionali per muoverti nel gioco.",

            "\nPremi i tasti \"J\", \"I\", \"K\", \"L\" per attaccare i nemici.",

            "\nDurante la tua avventura, incontrerai diversi oggetti utili. Inizia raccogliendo le monete; accumulandone abbastanza, potrai sbloccare nuovi personaggi.",

            "\nLe pozioni di cura sono fondamentali per ripristinare la tua vita durante le battaglie. A volte, puoi ottenerle dopo aver eliminato i nemici.",

            "\nI potenziamenti sono oggetti speciali che ti conferiscono abilità straordinarie, rendendoti più potente nel corso del gioco.",

            "\nLe chiavi servono per aprire forzieri, che possono contenere monete, pozioni, potenziamenti o ulteriori chiavi.",

            "\nFai attenzione agli spuntoni, in quanto possono infliggere danni se li tocchi.",

            "\nLe superfici ghiacciate sono scivolose; se le calpesti, continuerai a scivolare per tutta la durata del ghiaccio.",

            "\nIl pavimento vischioso blocca temporaneamente il tuo movimento, quindi cerca di evitarlo.",

            "\nLa botola si apre una volta che ci sei passato spora.",

            "\nI totem lanciano frecce appuntite a intervalli regolari. Impara a schivarle per evitare danni.",

            "\nLa scala è l'unica via d'uscita dalla stanza. Tuttavia, si aprirà solo dopo che avrai eliminato tutti i nemici.",

            "\nIl gioco può essere messo in pausa premendo il tasto \"Esc\"",

            "\nCongratulazioni! Hai completato il tutorial. Ora sei pronto ad affrontare il resto del gioco. Buona fortuna!"
    };

    private static final String COMMO_GIF_PATH = "sprites/tutorial/";

    private final JTextPane textPane = new JTextPane();
    private final StyledDocument doc = textPane.getStyledDocument();
    private final Style titleStyle = doc.addStyle("TitleStile", null);
    private final Style textStyle = doc.addStyle("TextStyle", null);
    private final JLabel progress = new JLabel();
    private final JLabel gif = new JLabel();
    private final JButton buttonLeft = new JButton();
    private final JButton buttonRight = new JButton();
    private final JButton menu = new JButton();
    private final Window window;
    private int step = 0;

    public Tutorial(Window window) {
        this.window = window;
        int offset = 16;

        initializedComponents(offset);
        setConstraints(offset);
        attachListeners();
    }

    private void initializedComponents(int offset) {
        add(buttonLeft);
        add(buttonRight);
        add(textPane);
        add(progress);
        add(gif);

        menu.setVisible(false);
        add(menu);

        setOpaque(false);
        textPane.setOpaque(false);
        textPane.setEditable(false);
        textPane.setCaret(new NoCaret());
        textPane.setBorder(new EmptyBorder(new Insets(offset, offset, offset, offset)));
        initializeStyles();

        progress.setBorder(new EmptyBorder(new Insets(offset, offset, offset, offset)));
        gif.setBorder(new EmptyBorder(new Insets(offset, offset, offset, offset)));

        buttonLeft.setIcon(Load.icon("left-chevron", 64, 64));
        buttonRight.setIcon(Load.icon("right-chevron", 64, 64));
        menu.setText("Torna al menù");
    }

    private void setConstraints(int offset) {
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        springLayout.putConstraint(SpringLayout.NORTH, textPane, 0, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, textPane, 0, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.WEST, textPane, 0, SpringLayout.WEST, this);

        springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, gif, 0, SpringLayout.HORIZONTAL_CENTER, this);
        springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, gif, 0, SpringLayout.VERTICAL_CENTER, this);

        springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, buttonLeft, 0, SpringLayout.VERTICAL_CENTER, this);
        springLayout.putConstraint(SpringLayout.WEST, buttonLeft, offset, SpringLayout.WEST, this);

        springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, buttonRight, 0, SpringLayout.VERTICAL_CENTER, this);
        springLayout.putConstraint(SpringLayout.EAST, buttonRight, -offset, SpringLayout.EAST, this);


        springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, progress, 0, SpringLayout.HORIZONTAL_CENTER, this);
        springLayout.putConstraint(SpringLayout.SOUTH, progress, 0, SpringLayout.SOUTH, this);

        springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, menu, 0, SpringLayout.HORIZONTAL_CENTER, this);
        springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, menu, 0, SpringLayout.VERTICAL_CENTER, this);

    }

    private void attachListeners() {
        buttonLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sound.play(Sound.Effect.BUTTON);
                if (step > 0) {
                    --step;
                    updateDraw(step);
                }

                window.requestFocus(); // altrimenti il focus rimane sul pulsante e non si può più premere 'esc'
            }
        });

        buttonRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sound.play(Sound.Effect.BUTTON);
                if (step < texts.length - 1) {
                    ++step;
                    updateDraw(step);
                }

                window.requestFocus(); // altrimenti il focus rimane sul pulsante e non si può più premere 'esc'
            }
        });

        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sound.play(Sound.Effect.BUTTON);
                Menu.incrementLevel();
                window.setScene(Window.Scene.MENU);
                window.requestFocus(); // altrimenti il focus rimane sul pulsante e non si può più premere 'esc'
            }
        });
    }

    public void advanceProgress(int step) {
        progress.setText(step + 1 + "/" + texts.length);
    }

    public void setGif(int step) {
        if (step < texts.length - 1)
            gif.setIcon(Load.gif(COMMO_GIF_PATH + step));
    }

    private void initializeStyles() {
        textPane.setFont(Load.font("superstar_2/superstarorig_memesbruh03"));

        StyleConstants.setAlignment(titleStyle, StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(titleStyle, TEXT_COLOR);
        StyleConstants.setFontSize(titleStyle, TITLE_SIZE);

        StyleConstants.setAlignment(textStyle, StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(textStyle, TEXT_COLOR);
        StyleConstants.setFontSize(textStyle, TEXT_SIZE);
    }

    public void write(int step) {
        if (step < texts.length) {
            try {
                doc.remove(0, doc.getLength());

                doc.insertString(doc.getLength(), title[step], titleStyle);
                doc.setParagraphAttributes(doc.getLength(), title[step].length(), titleStyle, false);

                doc.insertString(doc.getLength(), texts[step], textStyle);
                doc.setParagraphAttributes(doc.getLength(), texts[step].length(), textStyle, false);

            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void updateDraw(int step) {
        this.step = step;

        write(step);
        setGif(step);
        advanceProgress(step);

        if (step == texts.length - 1) {
            menu.setVisible(true);
            gif.setVisible(false);
            buttonRight.setVisible(false);
            buttonLeft.setVisible(true);
        }
        else if (step == 0) {
            buttonRight.setVisible(true);
            buttonLeft.setVisible(false);
        }
        else {
            menu.setVisible(false);
            gif.setVisible(true);
            buttonRight.setVisible(true);
            buttonLeft.setVisible(true);
        }
    }

    public void keyPressed(KeyEvent keyEvent) {
        final int key = keyEvent.getKeyCode();
        if (key == KeyEvent.VK_ESCAPE)
            window.getGamePanel().pauseDialog();
    }
}
