package chevy.view.component;



import chevy.utils.Image;

import javax.swing.*;
import javax.swing.plaf.PanelUI;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MyPanelUI extends PanelUI {
    public static final int CENTER = 0;
    public static final int CORNER_TL = 1;
    public static final int CORNER_TR = 2;
    public static final int CORNER_BL = 3;
    public static final int CORNER_BR = 4;
    public static final int BAR_T = 5;
    public static final int BAR_L = 6;
    public static final int BAR_B = 7;
    public static final int BAR_R = 8;

    private final BufferedImage[] texture = new BufferedImage[9];
    private final int[] textureWidth = new int[9];
    private final int[] textureHeight = new int[9];
    private final String[] texturePath;

    private float scale = 1f;
    

    public MyPanelUI(String[] texturePath) {
        this(texturePath, 1f);
    }

    public MyPanelUI(String[] texturePath, float scale) {
        this.texturePath = texturePath;
        this.scale = scale;

        if (texturePath != null) {
            loadTexture();
        }
    }

    private void loadTexture() {
        for (int i = 0; i < texturePath.length; ++i) {
            setTexture(i, texturePath[i]);
        }
    }

    public void setTexture(int i, String path) {
        if (i < 0 || i >= texture.length) {
            return;
        }
        if (path == null) {
            return;
        }

        texture[i] = Image.load(path);
        textureWidth[i] = (int) (texture[i].getWidth() * scale);
        textureHeight[i] = (int) (texture[i].getHeight() * scale);
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public int getWidth(int i) {
        return (int) (textureWidth[i] * scale);
    }

    public int getHeight(int i) {
        return (int) (textureHeight[i] * scale);
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        // Disegna il componente
        if (texture[CORNER_TL] != null) {
            g.drawImage(texture[CORNER_TL], 0, 0, getWidth(CORNER_TL), getHeight(CORNER_TL), null);
        }

        if (texture[BAR_T] != null) {
            int barTWidth = c.getWidth() - getWidth(CORNER_TL) - getWidth(CORNER_TR);
            g.drawImage(texture[BAR_T], getWidth(CORNER_TL), 0, barTWidth, getHeight(BAR_T), null);
        }
        if (texture[CORNER_TR] != null) {
            g.drawImage(texture[CORNER_TR], c.getWidth() - getWidth(CORNER_TR), 0, getWidth(CORNER_TR), getHeight(CORNER_TR), null);
        }

        if (texture[BAR_L] != null) {
            int barLHeight = c.getHeight() - getWidth(CORNER_TL) - getWidth(CORNER_BL);
            g.drawImage(texture[BAR_L], 0, getHeight(CORNER_TL), getWidth(BAR_L), barLHeight, null);
        }

        if (texture[CENTER] != null) {
            int centerWidth = c.getWidth() - getWidth(BAR_L) - getWidth(BAR_R);
            int centerHeight = c.getHeight() - getHeight(BAR_T) - getHeight(BAR_B);
            g.drawImage(texture[CENTER], getWidth(BAR_L), getHeight(BAR_T), centerWidth, centerHeight, null);
        }

        if (texture[BAR_R] != null) {
            int barRHeight = c.getHeight() - getWidth(CORNER_TR) - getWidth(CORNER_BR);
            g.drawImage(texture[BAR_R], c.getWidth() - getWidth(BAR_R), getHeight(CORNER_TR), getWidth(BAR_R), barRHeight, null);
        }

        if (texture[CORNER_BL] != null) {
            g.drawImage(texture[CORNER_BL], 0, c.getHeight() - getHeight(CORNER_TL), getWidth(CORNER_TL), getHeight(CORNER_TL), null);
        }

        if (texture[BAR_B] != null) {
            int barBWidth = c.getWidth() - getWidth(CORNER_BL) - getWidth(CORNER_BR);
            g.drawImage(texture[BAR_B], getWidth(CORNER_BL), c.getHeight() - getHeight(BAR_B), barBWidth, getHeight(BAR_B), null);
        }

        if (texture[CORNER_BR] != null) {
            g.drawImage(texture[CORNER_BR], c.getWidth() - getWidth(CORNER_BR), c.getHeight() - getHeight(CORNER_BR), getWidth(CORNER_BR), getHeight(CORNER_BR), null);
        }
    }
}
