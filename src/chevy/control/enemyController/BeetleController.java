package chevy.control.enemyController;

import chevy.control.InteractionTypes;
import chevy.control.PlayerController;
import chevy.model.chamber.Chamber;
import chevy.model.entity.Entity;
import chevy.model.entity.dinamicEntity.DirectionsModel;
import chevy.model.entity.dinamicEntity.liveEntity.enemy.Beetle;
import chevy.model.entity.dinamicEntity.liveEntity.player.Player;
import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.entity.dinamicEntity.projectile.SlimeShot;
import chevy.utilz.Vector2;

public class BeetleController {
    private final Chamber chamber;
    private final PlayerController playerController;


    public BeetleController(Chamber chamber, PlayerController playerController) {
        this.chamber = chamber;
        this.playerController = playerController;
    }

    public void playerInInteraction(Player player, Beetle beetle) {
        switch (player.getCurrentEumState()) {
            case Player.EnumState.ATTACK -> {
                beetle.setDirection(DirectionsModel.positionToDirection(player, beetle));
                hitFrog(beetle, -1 * player.getDamage());
            }
            default -> System.out.println("[!] Il WraithController non gestisce questa azione");
        }
    }

    public void projectileInteraction(Projectile projectile, Beetle beetle) {
        beetle.setDirection(DirectionsModel.positionToDirection(projectile, beetle));
        hitFrog(beetle, -1 * projectile.getDamage());
    }

    public void update(Beetle beetle) {
        if (!beetle.isAlive()) {
            if (beetle.getState(Beetle.EnumState.DEAD).isFinished()) {
                chamber.removeEntityOnTop(beetle);
                beetle.removeToUpdate();
                return;
            }
        }
        else if (beetle.getHealth() <= 0 && beetle.checkAndChangeState(Beetle.EnumState.DEAD)) {
            beetle.kill();
        }

        if (beetle.canChange(Beetle.EnumState.MOVE)) {
            DirectionsModel direction = chamber.getHitDirectionPlayer(beetle, 3);
            if (direction == null) {
                if (chamber.chase(beetle)) {
                    beetle.changeState(Beetle.EnumState.MOVE);
                }
            }
            else if (beetle.canChange(Beetle.EnumState.ATTACK)) {
                for (int i = 3; i > 0; --i) {
                    Entity entity = chamber.getNearEntityOnTop(beetle, direction, i);
                    if (entity instanceof Player && beetle.changeState(Beetle.EnumState.ATTACK)) {
                        Projectile slimeShot = new SlimeShot(new Vector2<>(beetle.getRow(), beetle.getCol()), direction, 1f);
                        chamber.addProjectile(slimeShot);
                        chamber.addEntityOnTop(slimeShot);
                        break;
                    }
                }
            }
        }
        beetle.checkAndChangeState(Beetle.EnumState.IDLE);
    }

    private void hitFrog(Beetle beetle, int damage) {
        if (beetle.changeState(Beetle.EnumState.HIT))
            beetle.changeHealth(damage);
    }
}
