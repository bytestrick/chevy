package chevy.view.chamber;

import chevy.model.chamber.drawOrder.Layer;
import chevy.model.entity.Entity;
import chevy.settings.GameSettings;
import chevy.utils.Vector2;
import chevy.view.entityView.EntityView;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

/**
 * Gestisce il disegno a schermo della stanza
 */
public class ChamberView {
    private List<Layer> drawOrderChamber;

    // colori per la visualizzazione della collisione
    private static final Color bg = new Color(255, 255, 255, 80);
    private static final Color outLine = new Color(255, 255, 255, 255);


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
//                    if (entity instanceof DynamicEntity) {
//                        g.setColor(bg);
//                        g.fillRect(entity.getCol() * GameSettings.scale + GameSettings.offsetW,
//                                entity.getRow() * GameSettings.scale + GameSettings.offsetH,
//                                GameSettings.scale, GameSettings.scale);
//                    }

                    EntityView entityViewSpecific = EntityToEntityView.getSpecific(entity);

                    if (entityViewSpecific != null) {
                        Vector2<Integer> offset = entityViewSpecific.getOffset();
                        Vector2<Double> position = entityViewSpecific.getCurrentPosition();
                        int scale = Math.round(entityViewSpecific.getScale());

                        g.drawImage(entityViewSpecific.getCurrentFrame(),
                                (int) (position.first * GameSettings.scale + GameSettings.offsetW + (offset.first / GameSettings.SIZE_TILE * GameSettings.scale)),
                                (int) (position.second * GameSettings.scale + GameSettings.offsetH + (offset.second / GameSettings.SIZE_TILE * GameSettings.scale)),
                                GameSettings.scale * scale,
                                GameSettings.scale * scale,
                                null);
                    }
                    else {
                        EntityView entityViewGeneric = EntityToEntityView.getGeneric(entity);
                        if (entityViewGeneric != null) {
                            Vector2<Double> position = entityViewGeneric.getCurrentPosition();
                            g.drawImage(entityViewGeneric.getCurrentFrame(),
                                    (int) (position.first * GameSettings.scale + GameSettings.offsetW),
                                    (int) (position.second * GameSettings.scale + GameSettings.offsetH),
                                    GameSettings.scale,
                                    GameSettings.scale,
                                    null);
                        }
                    }

                    // disegna il margine della collisione
//                    if (entity instanceof DynamicEntity) {
//                        g.setColor(outLine);
//                        g.drawRect(entity.getCol() * GameSettings.scale + GameSettings.offsetW,
//                                entity.getRow() * GameSettings.scale + GameSettings.offsetH,
//                                GameSettings.scale, GameSettings.scale);
//                    }

                    if (!entity.isToDraw()) {
                        it.remove();
                        System.out.println("[-] Entity rimossa dal ridisegno: " + entity.getSpecificType());
                        if (entityViewSpecific instanceof EntityViewAnimated entityViewAnimated)
                            entityViewAnimated.wasRemoved();
                    }
                }
            }
        }
    }

    public void setDrawOrderChamber(List<Layer> drawOrderChamber) {
        this.drawOrderChamber = drawOrderChamber;
    }
}
