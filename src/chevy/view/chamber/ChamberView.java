package chevy.view.chamber;

import chevy.model.chamber.drawOrder.Layer;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.settings.GameSettings;
import chevy.utils.Log;
import chevy.utils.Vector2;
import chevy.view.entities.EntityView;
import chevy.view.entities.animated.AnimatedEntityView;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

/**
 * Gestisce il disegno a schermo della stanza
 */
public class ChamberView {
    // colori per la visualizzazione della collisione
    private static final Color bg = new Color(255, 255, 255, 80);
    private static final Color outLine = new Color(255, 255, 255, 255);
    private List<Layer> drawOrder;

    public ChamberView() { }

    public void draw(Graphics g) {
        if (drawOrder == null) return;

        for (Layer layer : drawOrder) {
            Iterator<Entity> it = layer.getLayer().iterator();
            while (it.hasNext()) {
                Entity entity = it.next();

                // disegna lo sfondo della collisione
                if (entity != null) {
                    if (entity instanceof DynamicEntity) {
                        g.setColor(bg);
                        g.fillRect(entity.getCol() * GameSettings.optimalCellSize + GameSettings.offsetW,
                                entity.getRow() * GameSettings.optimalCellSize + GameSettings.offsetH,
                                GameSettings.optimalCellSize, GameSettings.optimalCellSize);
                    }

                    EntityView entityViewSpecific = EntityToEntityView.getSpecific(entity);

                    if (entityViewSpecific != null) {
                        Vector2<Double> entityViewPosition = entityViewSpecific.getCurrentPosition();
                        Vector2<Integer> offset = entityViewSpecific.getOffset();
                        float scale = entityViewSpecific.getScale();
                        BufferedImage image = entityViewSpecific.getCurrentFrame();

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
                    } else {
                        EntityView entityViewGeneric = EntityToEntityView.getGeneric(entity);
                        if (entityViewGeneric != null) {
                            Vector2<Double> position = entityViewGeneric.getCurrentPosition();
                            g.drawImage(entityViewGeneric.getCurrentFrame(),
                                    (int) (position.first * GameSettings.optimalCellSize + GameSettings.offsetW),
                                    (int) (position.second * GameSettings.optimalCellSize + GameSettings.offsetH),
                                    GameSettings.optimalCellSize, GameSettings.optimalCellSize, null);
                        }
                    }

                    // disegna il margine della collisione
                    if (entity instanceof DynamicEntity) {
                        g.setColor(outLine);
                        g.drawRect(entity.getCol() * GameSettings.optimalCellSize + GameSettings.offsetW,
                                entity.getRow() * GameSettings.optimalCellSize + GameSettings.offsetH,
                                GameSettings.optimalCellSize, GameSettings.optimalCellSize);
                    }

                    if (!entity.isToDraw()) {
                        it.remove();
                        Log.info("Entity rimossa dal ridisegno: " + entity.getSpecificType());
                        if (entityViewSpecific instanceof AnimatedEntityView entityViewAnimated) {
                            entityViewAnimated.remove();
                        }
                    }
                }
            }
        }
    }

    public void setDrawOrder(List<Layer> drawOrder) {
        this.drawOrder = drawOrder;
    }
}