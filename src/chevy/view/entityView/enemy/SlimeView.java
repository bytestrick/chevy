package chevy.view.entityView.enemy;

import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.stateMachine.SlimeStates;
import chevy.view.animation.AnimatedSprite;
import chevy.view.entityView.EntityView;

import java.awt.image.BufferedImage;

public class SlimeView extends EntityView {
    private static final String SLIME_RESOURCES = "/assets/enemy/slime/";
    private final int FPS_ANIMATION = 12;

    private final Slime slime;


    public SlimeView(Slime slime) {
        super();
        this.slime = slime;

        AnimatedSprite idle = new AnimatedSprite(SlimeStates.IDLE, 6, FPS_ANIMATION, true);
        initAnimation(idle, SLIME_RESOURCES + "idle", ".png");

        AnimatedSprite move = new AnimatedSprite(SlimeStates.MOVE, 6, FPS_ANIMATION);
        initAnimation(move, SLIME_RESOURCES + "move", ".png");

        AnimatedSprite attackUp = new AnimatedSprite(SlimeStates.ATTACK, 5, FPS_ANIMATION);
        initAnimation(attackUp, SLIME_RESOURCES + "attack/up", ".png");
    }



    @Override
    public BufferedImage getCurrentFrame() {
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(slime.getCurrentEumState());
        if (currentAnimatedSprite != null) {
            if (!currentAnimatedSprite.isStart())
                currentAnimatedSprite.start();
            return currentAnimatedSprite.getCurrentFrame();
        }
        return null;
    }
}
