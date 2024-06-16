package chevy.view.animation;

import chevy.model.entity.dinamicEntity.liveEntity.enemy.Slime;
import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.service.Render;
import chevy.service.RenderManager;
import chevy.settings.GameSettings;

import java.awt.image.BufferedImage;

public class AnimatedSprite implements Render {
    private final CommonEnumStates animationTypes;
    private final BufferedImage[] frames;
    private final int nFrame;
    private final float secFrameDuration;
    private int currentIndexFrame = 0;
    private final boolean loop;
    private boolean isRunning = false;
    private double time = 0d;


    public AnimatedSprite(CommonEnumStates animationTypes, int nFrame, float secFrameDuration) {
        this.animationTypes = animationTypes;
        this.nFrame = nFrame;
        this.secFrameDuration = secFrameDuration;
        this.loop = false;

        frames = new BufferedImage[nFrame];
    }

    public AnimatedSprite(CommonEnumStates animationTypes, int nFrame, float secFrameDuration, boolean loop) {
        this.animationTypes = animationTypes;
        this.nFrame = nFrame;
        this.secFrameDuration = secFrameDuration;
        this.loop = loop;

        frames = new BufferedImage[nFrame];
    }


    public void addFrame(int index, BufferedImage frame) {
        if (index < nFrame)
            frames[index] = frame;
    }

    public BufferedImage getCurrentFrame() {
        if (currentIndexFrame >= nFrame) {
            return frames[nFrame - 1];
        }
        return frames[currentIndexFrame];
    }

    public void start() {
//        System.out.println("Start animation: " + animationTypes);
        currentIndexFrame = 0;
        isRunning = true;
        time = 0d;

        RenderManager.addToRender(this);
    }

    public void stop() {
//        System.out.println("Stop animation: " + animationTypes);
        isRunning = false;
    }

    public int getNFrame() {
        return nFrame;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public CommonEnumStates getAnimationTypes() {
        return animationTypes;
    }


    @Override
    public void render(double delta) {
        if (time >= secFrameDuration) {
            time = 0d;
            ++currentIndexFrame;
            if (loop)
                currentIndexFrame = currentIndexFrame % nFrame;
            else if (currentIndexFrame >= nFrame - 1) {
                stop();
            }
        }
        else
            time += delta;
    }

    @Override
    public boolean renderIsEnd() {
        if (loop)
            return false;
        return !isRunning;
    }
}
