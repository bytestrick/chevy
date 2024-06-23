package chevy.view.chamber;

import chevy.model.chamber.drawOrder.Layer;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DynamicEntity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Enemy;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.settings.GameSettings;
import chevy.utilz.Vector2;
import chevy.view.entityView.EntityView;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class ChamberView {
    private List<Layer> drawOrderChamber;
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

                if (entity != null) {
                    if (entity instanceof DynamicEntity) {
                        g.setColor(bg);
                        g.fillRect(entity.getCol() * GameSettings.scale + GameSettings.offsetW,
                                entity.getRow() * GameSettings.scale + GameSettings.offsetH,
                                GameSettings.scale, GameSettings.scale);
                    }


                    EntityView entityViewSpecific = EntityToEntityView.getSpecific(entity);

                    if (entityViewSpecific != null) {
                        Vector2<Double> position = entityViewSpecific.getCurrentPosition();
                        g.drawImage(entityViewSpecific.getCurrentFrame(),
                                (int) (position.first * GameSettings.scale + GameSettings.offsetW),
                                (int) (position.second * GameSettings.scale + GameSettings.offsetH),
                                GameSettings.scale,
                                GameSettings.scale,
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

                    if (entity instanceof DynamicEntity) {
                        g.setColor(outLine);
                        g.drawRect(entity.getCol() * GameSettings.scale + GameSettings.offsetW,
                                entity.getRow() * GameSettings.scale + GameSettings.offsetH,
                                GameSettings.scale, GameSettings.scale);
                    }

                    if (!entity.isToDraw()) {
                        System.out.println("Rimosso dal ridisegno: " + entity);
                        it.remove();
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
