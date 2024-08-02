package chevy.view.entityView.entityViewAnimated.trap;

import chevy.model.entity.stateMachine.CommonEnumStates;
import chevy.model.entity.staticEntity.environment.traps.Sludge;
import chevy.utils.Vector2;
import chevy.view.animation.AnimatedSprite;
import chevy.view.entityView.entityViewAnimated.EntityViewAnimated;

import java.awt.image.BufferedImage;

public class SludgeView extends EntityViewAnimated {
        private static final String SLUDGE_FLOOR_PATH = "/assets/traps/sludge/";
        private final Sludge sludge;


        public SludgeView(Sludge sludge) {
            this.sludge = sludge;
            this.currentViewPosition = new Vector2<>(
                    (double) sludge.getCol(),
                    (double) sludge.getRow()
            );

            initAnimation();
        }


        private void initAnimation() {
            createAnimation(Sludge.EnumState.SLUDGE_BUBBLES, 0,
                    4, sludge.getState(Sludge.EnumState.SLUDGE_BUBBLES).getDuration(),
                    SLUDGE_FLOOR_PATH, ".png");
        }

        public Vector2<Integer> getOffset() {
            CommonEnumStates currentState = sludge.getCurrentEumState();
            AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentState, 0);
            return currentAnimatedSprite.getOffset();
        }

        @Override
        public BufferedImage getCurrentFrame() {
            CommonEnumStates currentEnumState = sludge.getCurrentEumState();
            AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentEnumState, 0);

            if (currentAnimatedSprite != null) {
                if (!currentAnimatedSprite.isRunning()) {
                    currentAnimatedSprite.restart();
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
            super.deleteAnimations();
        }
    }
