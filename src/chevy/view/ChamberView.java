package chevy.view;

import chevy.model.chamber.drawOrder.Layer;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.EnemyTypes;
import chevy.model.entity.dinamicEntity.liveEntity.player.PlayerTypes;
import chevy.model.entity.dinamicEntity.projectile.ProjectileTypes;
import chevy.model.entity.staticEntity.environment.EnvironmentTypes;
import chevy.model.entity.staticEntity.environment.traps.TrapsTypes;
import chevy.service.Render;
import chevy.settings.GameSettings;

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
                    if (!entity.isToDraw()) {
                        it.remove();
                    }
                    boolean draw = true;
                    switch (entity.getSpecificType()) {
                        case PlayerTypes.KNIGHT -> g.setColor(Color.RED);
                        case EnemyTypes.BAT -> g.setColor(Color.MAGENTA);
                        case EnemyTypes.SLIME -> g.setColor(Color.GREEN);
                        case EnemyTypes.ZOMBIE -> g.setColor(Color.BLUE);
                        case EnemyTypes.BIG_SLIME -> g.setColor(Color.PINK);
                        case TrapsTypes.VOID -> g.setColor(Color.BLACK);
                        case TrapsTypes.ICY_FLOOR -> g.setColor(Color.CYAN);
                        case TrapsTypes.SLUDGE -> g.setColor(Color.BLUE);
                        case TrapsTypes.TRAPDOOR -> g.setColor(Color.LIGHT_GRAY);
                        case TrapsTypes.SPIKED_FLOOR -> g.setColor(Color.GRAY);
                        case TrapsTypes.TOTEM -> g.setColor(Color.ORANGE);
                        case ProjectileTypes.ARROW -> g.setColor(Color.WHITE);
                        case EnemyTypes.SKELETON -> g.setColor(Color.YELLOW);
                        case EnemyTypes.FROG -> g.setColor(Color.GREEN);
                        default -> draw = false;
                    }

                    if (draw) {
                        g.fillRect(entity.getCol() * GameSettings.SCALE, entity.getRow() * GameSettings.SCALE, GameSettings.SCALE, GameSettings.SCALE);
                    }
                    draw = true;

                    switch (entity.getGenericType()) {
                        case EnvironmentTypes.WALL -> g.setColor(Color.YELLOW);
                        case EnvironmentTypes.GROUND -> g.setColor(Color.DARK_GRAY);
                        default -> draw = false;
                    }

                    if (draw) {
                        g.fillRect(entity.getCol() * GameSettings.SCALE, entity.getRow() * GameSettings.SCALE, GameSettings.SCALE, GameSettings.SCALE);
                    }

                }
            }
        }
    }

    public void setDrawOrderChamber(List<Layer> drawOrderChamber) {
        this.drawOrderChamber = drawOrderChamber;
    }
}
