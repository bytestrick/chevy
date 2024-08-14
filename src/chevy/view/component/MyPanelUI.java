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
    private String[] texturePath = new String[9];

    private float scale = 1f;
    

    public MyPanelUI(String[] texturePath) {
        this(texturePath, 1f);
    }

    public MyPanelUI(String[] texturePath, float scale) {
        this.texturePath = texturePath;
        this.scale = scale;

        if (texturePath != null)
            loadTexture();
    }

    private void loadTexture() {
        for (int i = 0; i < texturePath.length; ++i)
            setTexture(i, texturePath[i]);
    }

    public void setTexture(int i, String path) {
        if (i < 0 || i >= texture.length)
            return;
        if (path == null)
            return;

        texture[i] = Image.load(path);
        textureWidth[i] = (int) (texture[i].getWidth() * scale);
        textureHeight[i] = (int) (texture[i].getHeight() * scale);
    }

    public void setScale(float scale) {
        for (int i = 0; i < textureWidth.length; ++i)
            textureWidth[i] = (int) (textureWidth[i] / this.scale * scale);
        for (int i = 0; i < textureHeight.length; ++i)
            textureHeight[i] = (int) (textureHeight[i] / this.scale * scale);
        this.scale = scale;
    }

    public int getWidth(int i) {
        return textureWidth[i];
    }

    public int getHeight(int i) {
        return textureHeight[i];
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        // Disegna il componente
        if (texture[CORNER_TL] != null)
            g.drawImage(texture[CORNER_TL], 0, 0, textureWidth[CORNER_TL], textureHeight[CORNER_TL], null);

        if (texture[BAR_T] != null) {
            int barTWidth = c.getWidth() - textureWidth[CORNER_TL] - textureWidth[CORNER_TR];
            g.drawImage(texture[BAR_T], textureWidth[CORNER_TL], 0, barTWidth, textureHeight[BAR_T], null);
        }
        if (texture[CORNER_TR] != null)
            g.drawImage(texture[CORNER_TR], c.getWidth() - textureWidth[CORNER_TR], 0, textureWidth[CORNER_TR], textureHeight[CORNER_TR], null);

        if (texture[BAR_L] != null) {
            int barLHeight = c.getHeight() - textureWidth[CORNER_TL] - textureWidth[CORNER_BL];
            g.drawImage(texture[BAR_L], 0, textureHeight[CORNER_TL], textureWidth[BAR_L], barLHeight, null);
        }

        if (texture[CENTER] != null) {
            int centerWidth = c.getWidth() - textureWidth[BAR_L] - textureWidth[BAR_R];
            int centerHeight = c.getHeight() - textureHeight[BAR_T] - textureHeight[BAR_B];
            g.drawImage(texture[CENTER], textureWidth[BAR_L], textureHeight[BAR_T], centerWidth, centerHeight, null);
        }

        if (texture[BAR_R] != null) {
            int barRHeight = c.getHeight() - textureWidth[CORNER_TR] - textureWidth[CORNER_BR];
            g.drawImage(texture[BAR_R], c.getWidth() - textureWidth[BAR_R], textureHeight[CORNER_TR], textureWidth[BAR_R], barRHeight, null);
        }

        if (texture[CORNER_BL] != null)
            g.drawImage(texture[CORNER_BL], 0, c.getHeight() - textureHeight[CORNER_TL], textureWidth[CORNER_TL], textureHeight[CORNER_TL], null);

        if (texture[BAR_B] != null) {
            int barBWidth = c.getWidth() - textureWidth[CORNER_BL] - textureWidth[CORNER_BR];
            g.drawImage(texture[BAR_B], textureWidth[CORNER_BL], c.getHeight() - textureHeight[BAR_B], barBWidth, textureHeight[BAR_B], null);
        }

        if (texture[CORNER_BR] != null)
            g.drawImage(texture[CORNER_BR], c.getWidth() - textureWidth[CORNER_BR], c.getHeight() - textureHeight[CORNER_BR], textureWidth[CORNER_BR], textureHeight[CORNER_BR], null);
    }
}
