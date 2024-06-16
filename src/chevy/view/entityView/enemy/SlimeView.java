package chevy.view.entityView.enemy;

import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.view.animation.AnimatedSprite;
import chevy.view.entityView.EntityView;

import java.awt.image.BufferedImage;

public class SlimeView extends EntityView {
    private static final String SLIME_RESOURCES = "/assets/enemy/slime/";
    private final Slime slime;
    private CommonEnumStates previousEnumState = null;


    public SlimeView(Slime slime) {
        super();
        this.slime = slime;

        int nFrame = 6;
        int times = 4; // ripete l'animazione times volte
        float DurationFrame = slime.getState(Slime.EnumState.IDLE).getDuration() / (nFrame * times);
        AnimatedSprite idle = new AnimatedSprite(
                Slime.EnumState.IDLE,
                nFrame,
                DurationFrame,
                true
        );
        initAnimation(idle, SLIME_RESOURCES + "idle", ".png");

        // -----
        nFrame = 6;
        times = 1;
        DurationFrame = slime.getState(Slime.EnumState.MOVE).getDuration() / (nFrame * times);
        AnimatedSprite move = new AnimatedSprite(
                Slime.EnumState.MOVE,
                nFrame,
                DurationFrame
        );
        initAnimation(move, SLIME_RESOURCES + "move", ".png");

        // -----
        nFrame = 5;
        times = 1;
        DurationFrame = slime.getState(Slime.EnumState.ATTACK).getDuration() / (nFrame * times);
        AnimatedSprite attackUp = new AnimatedSprite(
                Slime.EnumState.ATTACK,
                nFrame,
                DurationFrame
        );
        initAnimation(attackUp, SLIME_RESOURCES + "attack/up", ".png");
    }



    @Override
    public BufferedImage getCurrentFrame() {
        CommonEnumStates currentEnumStates = slime.getCurrentEumState();
        AnimatedSprite currentAnimatedSprite = this.getAnimatedSprite(currentEnumStates);
        if (currentAnimatedSprite != null) {
            if (currentEnumStates != previousEnumState && !currentAnimatedSprite.isRunning())
                currentAnimatedSprite.start();
            previousEnumState = currentEnumStates;
            return currentAnimatedSprite.getCurrentFrame();
        }
        return null;
    }
}
