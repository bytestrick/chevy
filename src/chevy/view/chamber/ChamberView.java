package chevy.view.chamber;

import chevy.model.chamber.drawOrder.Layer;
import chevy.model.entity.Entity;
import chevy.settings.GameSettings;
import chevy.utilz.Vector2;
import chevy.view.entityView.EntityView;
import chevy.view.entityView.entityViewAnimated.enemy.SlimeView;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class ChamberView {
    private List<Layer> drawOrderChamber;


    public ChamberView() {}


    public void draw(Graphics g) {
        if (drawOrderChamber == null)
            return;

        for (Layer layer : drawOrderChamber) {
            Iterator<Entity> it = layer.getLayer().iterator();
            while (it.hasNext()) {
                Entity entity = it.next();

                if (entity != null) {
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

                    if (!entity.isToDraw()) {
                        System.out.println("Remove to draw: " + entity);
                        it.remove();
                    }
                }
            }
        }
    }

    public void setDrawOrderChamber(List<Layer> drawOrderChamber) {
        this.drawOrderChamber = drawOrderChamber;
    }
}
