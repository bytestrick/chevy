package chevy.view.chamber;

import chevy.model.chamber.drawOrder.Layer;
import chevy.model.entity.Entity;
import chevy.service.Render;
import chevy.service.RenderManager;
import chevy.settings.GameSettings;
import chevy.utils.Image;
import chevy.utils.Log;
import chevy.utils.Vector2;
import chevy.view.entities.EntityView;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

/**
 * Gestisce il disegno a schermo della stanza
 */
public class ChamberView extends JPanel implements Render {
    private static final boolean DRAW_COLLISIONS = false;
    private static final BufferedImage NULL_IMAGE = Image.load("/assets/null.png");
    // colori per la visualizzazione della collisione
    private static final Color bg = new Color(31, 205, 242, 80);
    private List<Layer> drawOrder;


    public ChamberView() {
        setOpaque(false);
        RenderManager.addToRender(this);
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (drawOrder == null) return;

        for (Layer layer : drawOrder) {
            Iterator<Entity> it = layer.getLayer().iterator();
            while (it.hasNext()) {
                Entity entity = it.next();

                // disegna lo sfondo della collisione
                if (entity != null) {
                    if (DRAW_COLLISIONS) {
                        g.setColor(bg);
                        g.fillRect(entity.getCol() * GameSettings.optimalCellSize + GameSettings.offsetW,
                                entity.getRow() * GameSettings.optimalCellSize + GameSettings.offsetH,
                                GameSettings.optimalCellSize, GameSettings.optimalCellSize);
                    }

                    EntityView entityView = EntityToEntityView.getSpecific(entity);
                    if (entityView == null) {
                        entityView = EntityToEntityView.getGeneric(entity);
                    }

                    if (entityView != null) {
                        Vector2<Double> entityViewPosition = entityView.getCurrentViewPosition();
                        Vector2<Integer> offset = entityView.getOffset();
                        float scale = entityView.getScale();
                        BufferedImage image = entityView.getCurrentFrame();
                        if (image == null) image = NULL_IMAGE;

                        float offsetX = (float) offset.first / GameSettings.SIZE_TILE;
                        if (offset.first == 0) offsetX = 0;
                        int positionX =
                                (int) ((entityViewPosition.first + offsetX) * GameSettings.optimalCellSize * scale + GameSettings.offsetW);

                        float offsetY = (float) offset.second / GameSettings.SIZE_TILE;
                        if (offset.second == 0) offsetY = 0;
                        int positionY =
                                (int) ((entityViewPosition.second + offsetY) * GameSettings.optimalCellSize * scale + GameSettings.offsetH);

                        int with =
                                (int) (GameSettings.optimalCellSize * scale * image.getWidth() / GameSettings.SIZE_TILE);
                        int height =
                                (int) (GameSettings.optimalCellSize * scale * image.getHeight() / GameSettings.SIZE_TILE);

                        g.drawImage(image, positionX, positionY, with, height, null);

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

    public void setDrawOrder(List<Layer> drawOrder) {
        this.drawOrder = drawOrder;
    }


    @Override
    public void render(double delta) {
        repaint();
    }
}