package chevy.view.chamber;

import chevy.model.chamber.drawOrder.Layer;
import chevy.model.entity.Entity;
import chevy.model.entity.dynamicEntity.DynamicEntity;
import chevy.service.Render;
import chevy.service.RenderManager;
import chevy.utils.Load;
import chevy.utils.Log;
import chevy.utils.Vector2;
import chevy.view.Window;
import chevy.view.entities.EntityView;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

/**
 * Disegna la stanza
 */
public class ChamberView extends JPanel implements Render {
    /** Offset per centrare il contenuto in {@link Window} */
    public static final Dimension windowOffset = new Dimension();
    public static final boolean DRAW_COLLISIONS = true;
    /** La cella ha dimensione 16x16 pixel */
    private static final int TILE_SIZE = 16;
    private static final BufferedImage NULL_IMAGE = Load.image("/sprites/null.png");
    private static final Color collisionBG = new Color(31, 205, 242, 80);
    /** Il valore viene impostato non appena la finestra di gioco si apre */
    public static int topBarHeight = 0;
    /** Numero minimo di tile da visualizzare in larghezza e altezza in ogni momento */
    public static Dimension tiles = new Dimension();
    /** Lunghezza ottimale del lato di una cella, scalata rispetto all'altezza della finestra */
    public static int tileSide;
    private List<Layer> drawOrder;

    public ChamberView() {
        setOpaque(false);
        RenderManager.addToRender(this);
    }

    /**
     * Ricalcola le dimensioni cambiando il numero di celle
     */
    public static void updateSize(Dimension tiles) {
        ChamberView.tiles = tiles;
        updateSize();
    }

    /**
     * Ricalcola le dimensioni mantenendo il numero di celle invariato
     */
    public static void updateSize() {
        if (tiles.width > 0 && tiles.height > 0) {
            final int contentHeight = Window.size.height - topBarHeight;
            tileSide = Math.min(contentHeight / tiles.height, Window.size.width / tiles.width);
            windowOffset.height = Math.max(0, (contentHeight - tiles.height * tileSide) / 2);
            windowOffset.width = Math.max(0, (Window.size.width - tiles.width * tileSide) / 2);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (drawOrder == null) {
            return;
        }

        for (Layer layer : drawOrder) {
            Iterator<Entity> it = layer.getLayer().iterator();
            while (it.hasNext()) {
                Entity entity = it.next();
                if (entity != null) {
                    if (DRAW_COLLISIONS && entity instanceof DynamicEntity) {
                        // disegna lo sfondo della collisione
                        g.setColor(collisionBG);
                        int x = entity.getCol() * tileSide + windowOffset.width;
                        int y = entity.getRow() * tileSide + windowOffset.height;
                        g.fillRect(x, y, tileSide, tileSide);
                    }

                    EntityView entityView = EntityToEntityView.getSpecific(entity);
                    if (entityView == null) {
                        entityView = EntityToEntityView.getGeneric(entity);
                    }

                    if (entityView != null) {
                        BufferedImage image = entityView.getCurrentFrame();
                        if (image == null) {
                            image = NULL_IMAGE;
                        }

                        Vector2<Integer> offset = entityView.getOffset();
                        float offsetX = (float) offset.first / TILE_SIZE;
                        if (offset.first == 0) {
                            offsetX = 0;
                        }
                        float offsetY = (float) offset.second / TILE_SIZE;
                        if (offset.second == 0) {
                            offsetY = 0;
                        }

                        Vector2<Double> position = entityView.getCurrentViewPosition();
                        float scale = entityView.getScale();
                        int x = (int) ((position.first + offsetX) * tileSide * scale + windowOffset.width);
                        int y = (int) ((position.second + offsetY) * tileSide * scale + windowOffset.height);

                        int with = (int) (tileSide * scale * image.getWidth() / TILE_SIZE);
                        int height = (int) (tileSide * scale * image.getHeight() / TILE_SIZE);

                        g.drawImage(image, x, y, with, height, null);

                        if (!entity.toDraw()) {
                            it.remove();
                            Log.info("Entity rimossa dal ridisegno: " + entity.getSpecificType());
                            entityView.wasRemoved();
                        }
                    }
                }
            }
        }
    }

    public void setDrawOrder(List<Layer> drawOrder) {this.drawOrder = drawOrder;}

    @Override
    public void render(double delta) {repaint();}
}