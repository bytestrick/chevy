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
    private static final Font font = UIManager.getFont("defaultFont");
    private static final float FONT_SIZE = 32f;
    private static final int TITLE_SIZE = 64;
    private static final int TEXT_SIZE = 38;
    private static final Color TITLE_COLOR = UIManager.getColor("Chevy.color.purpleBright");
    private static final Color TEXT_COLOR = new Color(188, 188, 188, 255);
    private static final Icon HOME = Load.icon("Home", 48, 48);
    private static final int MARGIN = 32;
    private static final String GIFS_PATH = "sprites/tutorial/";
    private final JTextPane textPane = new JTextPane();
    private final StyledDocument doc = textPane.getStyledDocument();
    private final Style titleStyle = doc.addStyle("TitleStyle", null);
    private final Style textStyle = doc.addStyle("TextStyle", null);
    private final JLabel progress = new JLabel();
    private final JLabel gif = new JLabel();
    private final JButton left = new JButton(Load.icon("left-chevron", 48, 48));
    private final JButton right = new JButton(Load.icon("right-chevron", 48, 48));
    private final JButton menu = new JButton("menu", HOME);
    private final Window window;
    private String[] titles, texts;
    private int step;
    private final ActionListener actionListener = this::actionPerformed;

    Tutorial(Window window) {
        this.window = window;
        initUI();
        setConstraints();
    }

    void setStrings() {
        titles = Options.strings.getString("tutorial.titles").split(";");
        texts = Options.strings.getString("tutorial.descriptions").split(";");
        menu.setText(Options.strings.getString("options.backToHome"));
    }

    private void initUI() {
        setStrings();
        add(left);
        add(right);
        add(textPane);
        add(progress);
        add(gif);
        add(menu);
        setOpaque(false);
        textPane.setOpaque(false);
        textPane.setEditable(false);
        textPane.setCaret(new NoCaret());
        textPane.setBorder(new EmptyBorder(new Insets(MARGIN, MARGIN, MARGIN, MARGIN)));
        initStyles();
        progress.setBorder(new EmptyBorder(new Insets(MARGIN, MARGIN, MARGIN, MARGIN)));
        progress.setFont(font.deriveFont(FONT_SIZE));
        gif.setBorder(new EmptyBorder(new Insets(MARGIN, MARGIN, MARGIN, MARGIN)));
        left.setActionCommand("left");
        left.addActionListener(actionListener);
        right.setActionCommand("right");
        right.addActionListener(actionListener);
        menu.setFont(font.deriveFont(FONT_SIZE));
        menu.setVisible(false);
        menu.setActionCommand("menu");
        menu.addActionListener(actionListener);
    }

    private void setConstraints() {
        SpringLayout springLayout = new SpringLayout();
        setLayout(springLayout);

        springLayout.putConstraint(SpringLayout.NORTH, textPane, 0, SpringLayout.NORTH, this);
        springLayout.putConstraint(SpringLayout.EAST, textPane, 0, SpringLayout.EAST, this);
        springLayout.putConstraint(SpringLayout.WEST, textPane, 0, SpringLayout.WEST, this);

        springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, gif, 0, SpringLayout.HORIZONTAL_CENTER, this);
        springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, gif, 0, SpringLayout.VERTICAL_CENTER, this);

        springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, left, 0, SpringLayout.VERTICAL_CENTER, this);
        springLayout.putConstraint(SpringLayout.WEST, left, MARGIN * 2, SpringLayout.WEST, this);

        springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, right, 0, SpringLayout.VERTICAL_CENTER, this);
        springLayout.putConstraint(SpringLayout.EAST, right, -MARGIN * 2, SpringLayout.EAST, this);

        springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, progress, 0, SpringLayout.HORIZONTAL_CENTER, this);
        springLayout.putConstraint(SpringLayout.SOUTH, progress, 0, SpringLayout.SOUTH, this);

        springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, menu, 0, SpringLayout.HORIZONTAL_CENTER, this);
        springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, menu, 0, SpringLayout.VERTICAL_CENTER, this);
    }

    private void actionPerformed(ActionEvent event) {
        Sound.play(Sound.Effect.BUTTON);
        switch (event.getActionCommand()) {
            case "right" -> stepAhead();
            case "left" -> stepBack();
            case "menu" -> {
                Sound.stopMusic();
                window.setScene(Window.Scene.MENU);
            }
        }
        // Without this the focus remains on the buttons and ESC can no longer be pressed
        window.requestFocus();
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

    private void advanceProgress(int step) {
        progress.setText(step + 1 + "/" + texts.length);
    }

    private void setGif(int step) {
        if (step < texts.length - 1) {
            gif.setIcon(Load.gif(GIFS_PATH + step));
        }
    }

    private void initStyles() {
        StyleConstants.setAlignment(titleStyle, StyleConstants.ALIGN_CENTER);
        StyleConstants.setForeground(titleStyle, TITLE_COLOR);
        StyleConstants.setFontFamily(titleStyle, font.getFamily());
        StyleConstants.setBold(titleStyle, true);
        StyleConstants.setFontSize(titleStyle, TITLE_SIZE);

        StyleConstants.setAlignment(textStyle, StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontFamily(textStyle, font.getFamily());
        StyleConstants.setForeground(textStyle, TEXT_COLOR);
        StyleConstants.setFontSize(textStyle, TEXT_SIZE);

        textPane.setFont(font);
    }

    private void write(int step) {
        if (step < texts.length) {
            try {
                doc.remove(0, doc.getLength());
                doc.setParagraphAttributes(0, titles[step].length(), titleStyle, false);
                doc.insertString(0, titles[step] + "\n\n", titleStyle);

                doc.setParagraphAttributes(doc.getLength(), texts[step].length(), textStyle, false);
                doc.insertString(doc.getLength(), texts[step], textStyle);
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

        // Handling of the visibility of the menu and gif
        menu.setVisible(isLastStep);
        gif.setVisible(!isLastStep);

        // Handling of the visibility of the buttons
        right.setVisible(!isLastStep);
        left.setVisible(!isFirstStep);
    }

    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_ESCAPE -> {
                Sound.play(Sound.Effect.STOP);
                final String[] opts = Options.strings.getString("dialog.yesNo").split(",");
                final int ans = JOptionPane.showOptionDialog(window, Options.strings.getString(
                                "dialog.quitTutorial"),
                        null, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, HOME,
                        opts, opts[opts.length - 1]);
                if (ans == 0) {
                    Sound.play(Sound.Effect.BUTTON);
                    Sound.stopMusic();
                    window.setScene(Window.Scene.MENU);
                } else if (ans == 1) {
                    Sound.play(Sound.Effect.BUTTON);
                }
            }
            case KeyEvent.VK_RIGHT -> {
                right.requestFocus();
                Sound.play(Sound.Effect.BUTTON);
                stepAhead();
            }
            case KeyEvent.VK_LEFT -> {
                left.requestFocus();
                Sound.play(Sound.Effect.BUTTON);
                stepBack();
            }
        }
        window.requestFocus();
    }
}
