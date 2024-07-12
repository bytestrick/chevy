package chevy.view.entityView.entityViewAnimated.trap;

import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.staticEntity.environment.traps.Trapdoor;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.image.BufferedImage;

public class TrapdoorView extends EntityViewAnimated {

    private static final String TRAPDOOR_PATH = "/assets/traps/trapdoor/";
    private final Trapdoor trapdoor;


    public TrapdoorView(Trapdoor trapdoor) {
        this.trapdoor = trapdoor;
        this.currentViewPosition = new Vector2<>(
                (double) trapdoor.getCol(),
                (double) trapdoor.getRow()
        );

        initAnimation();
    }


    private void initAnimation() {
        createAnimation(Trapdoor.EnumState.IDLE, 0,
                1, trapdoor.getState(Trapdoor.EnumState.IDLE).getDuration(),
                TRAPDOOR_PATH + "idle", ".png");

        createAnimation(Trapdoor.EnumState.OPEN, 0,
                3, trapdoor.getState(Trapdoor.EnumState.OPEN).getDuration(),
                TRAPDOOR_PATH + "open", ".png");

        createAnimation(Trapdoor.EnumState.DAMAGE, 0,
                1, trapdoor.getState(Trapdoor.EnumState.DAMAGE).getDuration(),
                TRAPDOOR_PATH + "damage", ".png");
    }


    @Override
    public BufferedImage getCurrentFrame() {
        CommonEnumStates currentEnumState = trapdoor.getCurrentEumState();
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentEnumState, 0);

        if (currentAnimatedSprite != null) {
            if (!currentAnimatedSprite.isRunning()) {
                currentAnimatedSprite.start();
            }
            return currentAnimatedSprite.getCurrentFrame();
        }
        return null;
    }

    @Override
    public Vector2<Double> getCurrentViewPosition() {
        return currentViewPosition;
    }

    @Override
    public void wasRemoved() {
        deleteAnimations();
    }
}
