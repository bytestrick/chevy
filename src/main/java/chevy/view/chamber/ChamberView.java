package chevy.view.chamber;

import chevy.model.chamber.drawOrder.Layer;
import chevy.model.entity.Entity;
import chevy.model.entity.dynamicEntity.DynamicEntity;
import chevy.service.Data;
import chevy.service.RenderManager;
import chevy.service.Renderable;
import chevy.utils.Load;
import chevy.utils.Log;
import chevy.view.Window;
import chevy.view.entities.EntityView;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

/**
 * Renders the chamber
 */
public final class ChamberView extends JPanel implements Renderable {
    /**
     * Offset to center the content in {@link Window}
     */
    public static final Dimension windowOffset = new Dimension();
    /**
     * The cell is 16x16 pixels
     */
    private static final int TILE_SIZE = 16;
    private static final BufferedImage NULL_IMAGE = Load.image("/sprites/null.png");
    private static final Color collisionBG = new Color(31, 205, 242, 80);
    public static boolean drawCollision = Data.get("options.drawHitBoxes");
    /**
     * The value is set as soon as the game window opens
     */
    public static int topBarHeight;
    /**
     * Optimal length of the side of a tile, scaled relative to the height of the window
     */
    public static int tileSide;
    /**
     * Least number of tiles to display in width and height at any time
     */
    private static Dimension tiles = new Dimension();
    private final Object mutex = new Object();
    private List<Layer> drawOrder;

    public ChamberView() {
        setOpaque(false);
        RenderManager.register(this);
    }

    /**
     * Recompute the size changing the number of tiles
     */
    public static void updateSize(Dimension tiles) {
        ChamberView.tiles = tiles;
        updateSize();
    }

    /**
     * Recompute the size keeping the number of tiles constant
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

        synchronized (mutex) {
            for (Layer layer : drawOrder) {
                Iterator<Entity> it = layer.getLayer().iterator();
                while (it.hasNext()) {
                    Entity entity = it.next();
                    if (entity != null) {
                        if (drawCollision && entity instanceof DynamicEntity) {
                            // draw the collision background
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
                            BufferedImage image = entityView.getFrame();
                            if (image == null) {
                                image = NULL_IMAGE;
                            }

                            Point o = entityView.getOffset();
                            Point2D.Double offset = new Point2D.Double(1d * o.x / TILE_SIZE,
                                    1d * o.y / TILE_SIZE);

                            Point2D.Double position = entityView.getViewPosition();
                            float scale = entityView.getScale();
                            int x = (int) ((position.x + offset.x) * tileSide * scale + windowOffset.width);
                            int y = (int) ((position.y + offset.y) * tileSide * scale + windowOffset.height);

                            int with = (int) (tileSide * scale * image.getWidth() / TILE_SIZE);
                            int height = (int) (tileSide * scale * image.getHeight() / TILE_SIZE);

                            g.drawImage(image, x, y, with, height, null);

                            if (entity.shouldNotDraw()) {
                                it.remove();
                                Log.info("Entity removed from rendering: " + entity.getType());
                                entityView.remove();
                            }
                        }
                    }
                }
            }
        }
    }

    public void setDrawOrder(List<Layer> drawOrder) {
        this.drawOrder = drawOrder;
    }

    @Override
    public synchronized void render(double delta) {
        repaint();
    }
}
