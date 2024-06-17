package chevy.view.chamber;

import chevy.model.chamber.drawOrder.Layer;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.settings.GameSettings;

import java.awt.*;
import java.awt.image.BufferedImage;
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
                    BufferedImage image = EntityToImage.get(entity);
                    if (image != null) {
                        g.drawImage(image,
                                entity.getCol() * GameSettings.scale + GameSettings.offsetW,
                                entity.getRow() * GameSettings.scale + GameSettings.offsetH,
                                GameSettings.scale,
                                GameSettings.scale,
                                null);

                        if (!entity.isToDraw()) {
                            System.out.println("Remove to draw: " + entity);
                            it.remove();
                        }
                    }
                }
            }
        }
    }

    public void setDrawOrderChamber(List<Layer> drawOrderChamber) {
        this.drawOrderChamber = drawOrderChamber;
    }
}
