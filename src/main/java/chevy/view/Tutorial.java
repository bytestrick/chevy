package chevy.view;

import chevy.service.Sound;
import chevy.utils.Load;
import chevy.view.component.NoCaret;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import static chevy.view.Options.home;

public final class Tutorial extends JPanel {
    private static final String FONT_NAME = "VT323";
    private static final float FONT_SIZE = 32f;
    private static final int TITLE_SIZE = 64;
    private static final int TEXT_SIZE = 38;
    private static final Color TITLE_COLOR = UIManager.getColor("Chevy.color.purpleBright");
    private static final Color TEXT_COLOR = new Color(188, 188, 188, 255);

    // @formatter:off
    private static final String[] title = new String[] {
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

    private static final String[] texts = new String[] {
            "Utilizza i tasti “W”, “A”, “S”, “D” oppure le frecce direzionali per muoverti nel gioco.",
            "Premi i tasti “J”, “I”, “K”, “L” per attaccare i nemici. Ricorda: l'attacco è la tua migliore difesa!",
            "Durante la tua avventura, incontrerai diversi oggetti utili. Inizia raccogliendo le monete; accumulandone abbastanza, potrai sbloccare nuovi personaggi.",
            "Le pozioni di cura sono fondamentali per ripristinare la tua vita durante le battaglie. A volte, puoi ottenerle dopo aver eliminato i nemici.",
            "I potenziamenti sono oggetti speciali che ti conferiscono abilità straordinarie, rendendoti più potente nel corso del gioco. Usali saggiamente!",
            "Le chiavi servono per aprire forzieri, che possono contenere monete, pozioni, potenziamenti o ulteriori chiavi.",
            "Fai attenzione agli spuntoni, in quanto possono infliggere danni se li tocchi. Tieni gli occhi aperti!",
            "Le superfici ghiacciate sono scivolose; se le calpesti, continuerai a scivolare per tutta la durata del ghiaccio.",
            "Il pavimento vischioso blocca il tuo movimento e una volta che ti sei liberato scompare, quindi cerca di evitarlo.",
            "La botola si apre una volta che ci sei passato sopra. Attento a non caderci dentro.",
            "I totem lanciano frecce appuntite a intervalli regolari. Impara a schivarle per evitare danni. Sii veloce!",
            "La scala è l'unica via d'uscita dalla stanza. Tuttavia, si aprirà solo dopo che avrai eliminato tutti i nemici. Preparati alla battaglia!",
            "Il gioco può essere messo in pausa premendo il tasto “Esc”. Prenditi una pausa se hai bisogno di riflettere!",
            "Hai completato il tutorial. Ora sei pronto ad affrontare il resto del gioco.\n\nBuona fortuna!"
    };
    // @formatter:on

    private static final String COMMO_GIF_PATH = "sprites/tutorial/";

    private final JTextPane textPane = new JTextPane();
    private final StyledDocument doc = textPane.getStyledDocument();
    private final Style titleStyle = doc.addStyle("TitleStyle", null);
    private final Style textStyle = doc.addStyle("TextStyle", null);
    private final JLabel progress = new JLabel();
    private final JLabel gif = new JLabel();
    private final JButton buttonLeft = new JButton();
    private final JButton buttonRight = new JButton();
    private final JButton menu = new JButton();
    private final Window window;
    private int step;

    Tutorial(Window window) {
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
        add(menu);
        setOpaque(false);
        textPane.setOpaque(false);
        textPane.setEditable(false);
        textPane.setCaret(new NoCaret());
        textPane.setBorder(new EmptyBorder(new Insets(offset, offset, offset, offset)));
        initializeStyles();
        progress.setBorder(new EmptyBorder(new Insets(offset, offset, offset, offset)));
        progress.setFont(Load.font(FONT_NAME).deriveFont(FONT_SIZE));
        gif.setBorder(new EmptyBorder(new Insets(offset, offset, offset, offset)));
        buttonLeft.setIcon(Load.icon("left-chevron", 64, 64));
        buttonRight.setIcon(Load.icon("right-chevron", 64, 64));
        menu.setFont(Load.font(FONT_NAME).deriveFont(FONT_SIZE));
        menu.setIcon(home);
        menu.setText("Torna al menù");
        menu.setVisible(false);
    }

    private void setConstraints(int offset) {
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        springLayout.putConstraint(SpringLayout.NORTH, textPane, 0, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, textPane, 0, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.WEST, textPane, 0, SpringLayout.WEST, this);

        springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, gif, 0,
                SpringLayout.HORIZONTAL_CENTER, this);
        springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, gif, 0,
                SpringLayout.VERTICAL_CENTER, this);

        springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, buttonLeft, 0,
                SpringLayout.VERTICAL_CENTER, this);
        springLayout.putConstraint(SpringLayout.WEST, buttonLeft, offset * 2, SpringLayout.WEST,
                this);

        springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, buttonRight, 0,
                SpringLayout.VERTICAL_CENTER, this);
        springLayout.putConstraint(SpringLayout.EAST, buttonRight, -offset * 2, SpringLayout.EAST
                , this);

        springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, progress, 0,
                SpringLayout.HORIZONTAL_CENTER, this);
        springLayout.putConstraint(SpringLayout.SOUTH, progress, 0, SpringLayout.SOUTH, this);

        springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, menu, 0,
                SpringLayout.HORIZONTAL_CENTER, this);
        springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, menu, 0,
                SpringLayout.VERTICAL_CENTER, this);
    }

    private void attachListeners() {
        buttonLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sound.play(Sound.Effect.BUTTON);
                stepBack();
                window.requestFocus(); // altrimenti il focus rimane sul pulsante e non si può
                // più premere 'esc'
            }
        });

        buttonRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sound.play(Sound.Effect.BUTTON);
                stepAhead();
                window.requestFocus(); // altrimenti il focus rimane sul pulsante e non si può
                // più premere 'esc'
            }
        });

        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sound.play(Sound.Effect.BUTTON);
                Sound.stopMusic();
                Menu.incrementLevel();
                window.setScene(Window.Scene.MENU);
                Sound.startLoop(Sound.Music.SAME_SONG);
                window.requestFocus(); // altrimenti il focus rimane sul pulsante e non si può
                // più premere 'esc'
            }
        });
    }

    private void stepAhead() {
        if (step < texts.length - 1) {
            ++step;
            updateDraw(step);
        }
    }

    private void stepBack() {
        if (step > 0) {
            --step;
            updateDraw(step);
        }
    }

    private void advanceProgress(int step) {progress.setText(step + 1 + "/" + texts.length);}

    private void setGif(int step) {
        if (step < texts.length - 1) {
            gif.setIcon(Load.gif(COMMO_GIF_PATH + step));
        }
    }

    private void initializeStyles() {
        Font font = Load.font(FONT_NAME);

        StyleConstants.setAlignment(titleStyle, StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(titleStyle, TITLE_COLOR);
        StyleConstants.setFontFamily(titleStyle, font.getFamily());
        StyleConstants.setBold(titleStyle, true);
        StyleConstants.setFontSize(titleStyle, TITLE_SIZE);

        StyleConstants.setAlignment(textStyle, StyleConstants.ALIGN_LEFT);
        StyleConstants.setFontFamily(textStyle, font.getFamily());
        StyleConstants.setForeground(textStyle, TEXT_COLOR);
        StyleConstants.setFontSize(textStyle, TEXT_SIZE);

        textPane.setFont(font);
    }

    private void write(int step) {
        if (step < texts.length) {
            try {
                doc.remove(0, doc.getLength());

                doc.insertString(doc.getLength(), title[step], titleStyle);
                doc.setParagraphAttributes(doc.getLength(), title[step].length(), titleStyle,
                        false);

                doc.insertString(doc.getLength(), texts[step], textStyle);
                doc.setParagraphAttributes(doc.getLength(), texts[step].length(), textStyle, false);
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    void updateDraw(int step) {
        this.step = step;

        write(step);
        setGif(step);
        advanceProgress(step);

        boolean isLastStep = step == texts.length - 1;
        boolean isFirstStep = step == 0;

        // Gestione visibilità del menu e della gif
        menu.setVisible(isLastStep);
        gif.setVisible(!isLastStep);

        // Gestione visibilità dei pulsanti
        buttonRight.setVisible(!isLastStep);
        buttonLeft.setVisible(!isFirstStep);
    }

    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_ESCAPE -> {
                final int ans = JOptionPane.showOptionDialog(window, "Abbandonare il tutorial?", null,
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, Load.icon("Home"
                                , 48, 48), new String[]{"Si", "No"}, "No");
                if (ans == 0) {
                    Sound.play(Sound.Effect.BUTTON);
                    Sound.stopMusic();
                    window.setScene(Window.Scene.MENU);
                } else if (ans == 1) {
                    Sound.play(Sound.Effect.BUTTON);
                }
            }
            case KeyEvent.VK_RIGHT -> stepAhead();
            case KeyEvent.VK_LEFT -> stepBack();
        }
    }
}
