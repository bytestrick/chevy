package chevy.view.entityView.enemy;

import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.view.animation.AnimatedSprite;
import chevy.view.entityView.EntityView;

import java.awt.image.BufferedImage;

public class SlimeView extends EntityView {
    private static final String SLIME_RESOURCES = "/assets/enemy/slime/";
    private final Slime slime;


    public SlimeView(Slime slime) {
        super();
        this.slime = slime;

        int idleFrame = 6;
        int times = 4; // ripete l'animazione times volte
        float idleDuration = slime.getState(Slime.EnumState.IDLE).getDuration() / (idleFrame * times);
        AnimatedSprite idle = new AnimatedSprite(
                Slime.EnumState.IDLE,
                idleFrame,
                idleDuration,
                true
        );
        initAnimation(idle, SLIME_RESOURCES + "idle", ".png");

        // -----

        AnimatedSprite move = new AnimatedSprite(
                Slime.EnumState.MOVE,
                6,
                slime.getState(Slime.EnumState.MOVE).getDuration() / 6f
        );
        initAnimation(move, SLIME_RESOURCES + "move", ".png");

        // -----

        AnimatedSprite attackUp = new AnimatedSprite(
                Slime.EnumState.ATTACK,
                5,
                slime.getState(Slime.EnumState.IDLE).getDuration() / 5f
        );
        initAnimation(attackUp, SLIME_RESOURCES + "attack/up", ".png");
    }



    @Override
    public BufferedImage getCurrentFrame() {
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(slime.getCurrentEumState());
        if (currentAnimatedSprite != null) {
            if (!currentAnimatedSprite.isRunning())
                currentAnimatedSprite.start();
            return currentAnimatedSprite.getCurrentFrame();
        }
        return null;
    }
}
