package chevy.view.chamber;

import chevy.model.chamber.drawOrder.Layer;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.settings.GameSettings;
import chevy.utils.Vector2;
import chevy.view.Image;
import chevy.view.entityView.EntityView;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

/**
 * Gestisce il disegno a schermo della stanza
 */
public class ChamberView {
    private static final boolean DRAW_COLLISIONS = false;
    private static final BufferedImage NULL_IMAGE = Image.load("/assets/null.png");
    // colori per la visualizzazione della collisione
    private static final Color bg = new Color(255, 255, 255, 80);
    private static final Color outLine = new Color(255, 255, 255, 255);

    private List<Layer> drawOrderChamber;
    

    public ChamberView() {}


    public void draw(Graphics g) {
        if (drawOrderChamber == null)
            return;

        for (Layer layer : drawOrderChamber) {
            Iterator<Entity> it = layer.getLayer().iterator();
            while (it.hasNext()) {
                Entity entity = it.next();

                // disegna lo sfondo della collisione
                if (entity != null) {
                    if (DRAW_COLLISIONS && entity instanceof DynamicEntity) {
                        g.setColor(bg);
                        g.fillRect(entity.getCol() * GameSettings.optimalCellSize + GameSettings.offsetW,
                                entity.getRow() * GameSettings.optimalCellSize + GameSettings.offsetH,
                                GameSettings.optimalCellSize, GameSettings.optimalCellSize);
                    }

                    EntityView entityViewSpecific = EntityToEntityView.getSpecific(entity);

                    if (entityViewSpecific != null) {
                        Vector2<Double> entityViewPosition = entityViewSpecific.getCurrentViewPosition();
                        Vector2<Integer> offset = entityViewSpecific.getOffset();
                        float scale = entityViewSpecific.getScale();
                        BufferedImage image = entityViewSpecific.getCurrentFrame();
                        if (image == null)
                            image = NULL_IMAGE;

                        float offsetX = (float) offset.first / GameSettings.SIZE_TILE;
                        if (offset.first == 0)
                            offsetX = 0;
                        int positionX = (int) ((entityViewPosition.first + offsetX) * GameSettings.optimalCellSize * scale + GameSettings.offsetW);

                        float offsetY = (float) offset.second / GameSettings.SIZE_TILE;
                        if (offset.second == 0)
                            offsetY = 0;
                        int positionY = (int) ((entityViewPosition.second + offsetY) * GameSettings.optimalCellSize * scale + GameSettings.offsetH);

                        int with = (int) (GameSettings.optimalCellSize * scale * image.getWidth() / GameSettings.SIZE_TILE);
                        int height = (int) (GameSettings.optimalCellSize * scale * image.getHeight() / GameSettings.SIZE_TILE);

                        g.drawImage(image, positionX, positionY, with, height, null);
                    }
                    else {
                        EntityView entityViewGeneric = EntityToEntityView.getGeneric(entity);
                        if (entityViewGeneric != null) {
                            Vector2<Double> position = entityViewGeneric.getCurrentViewPosition();
                            g.drawImage(entityViewGeneric.getCurrentFrame(),
                                    (int) (position.first * GameSettings.optimalCellSize + GameSettings.offsetW),
                                    (int) (position.second * GameSettings.optimalCellSize + GameSettings.offsetH),
                                    GameSettings.optimalCellSize,
                                    GameSettings.optimalCellSize,
                                    null);
                        }
                    }

                    // disegna il margine della collisione
                    if (DRAW_COLLISIONS && entity instanceof DynamicEntity) {
                        g.setColor(outLine);
                        g.drawRect(entity.getCol() * GameSettings.optimalCellSize + GameSettings.offsetW,
                                entity.getRow() * GameSettings.optimalCellSize + GameSettings.offsetH,
                                GameSettings.optimalCellSize, GameSettings.optimalCellSize);
                    }

                    if (!entity.isToDraw()) {
                        it.remove();
                        System.out.println("[-] Entity rimossa dal ridisegno: " + entity.getSpecificType());
                        if (entityViewSpecific != null)
                            entityViewSpecific.wasRemoved();
                    }
                }
            }
        }
    }

    public void setDrawOrderChamber(List<Layer> drawOrderChamber) {
        this.drawOrderChamber = drawOrderChamber;
    }
}
