package chevy.view.animation;

import chevy.model.entity.dinamicEntity.stateMachine.EnumState;
import chevy.model.entity.dinamicEntity.stateMachine.SlimeStates;
import chevy.settings.GameSettings;
import chevy.view.Image;

import java.awt.image.BufferedImage;

public class AnimatedSprite {
    private final EnumState animationTypes;
    private final BufferedImage[] frames;
    private final int nFrame;
    private int tick;
    private final float secFrameDuration;
    private int currentIndexFrame;
    private final boolean loop;
    private boolean isEnd;
    private boolean isStart;


    public AnimatedSprite(EnumState animationTypes, int nFrame, float secFrameDuration) {
        this.animationTypes = animationTypes;
        this.nFrame = nFrame;
        this.secFrameDuration = secFrameDuration;
        this.currentIndexFrame = 0;
        this.tick = 0;
        this.loop = false;
        this.isEnd = false;
        this.isStart = false;

        frames = new BufferedImage[nFrame];
    }

    public AnimatedSprite(EnumState animationTypes, int nFrame, int fps) {
        this.animationTypes = animationTypes;
        this.nFrame = nFrame;
        this.secFrameDuration = (float) 1 / fps;
        this.currentIndexFrame = 0;
        this.tick = 0;
        this.loop = false;
        this.isEnd = false;
        this.isStart = false;

        frames = new BufferedImage[nFrame];
    }

    public AnimatedSprite(EnumState animationTypes, int nFrame, int fps, boolean loop) {
        this.animationTypes = animationTypes;
        this.nFrame = nFrame;
        this.secFrameDuration = (float) 1 / fps;
        this.currentIndexFrame = 0;
        this.tick = 0;
        this.loop = loop;
        this.isEnd = false;
        this.isStart = false;

        frames = new BufferedImage[nFrame];
    }

    public AnimatedSprite(EnumState animationTypes, int nFrame, float secFrameDuration, boolean loop) {
        this.animationTypes = animationTypes;
        this.nFrame = nFrame;
        this.secFrameDuration = secFrameDuration;
        this.currentIndexFrame = 0;
        this.tick = 0;
        this.loop = loop;
        this.isEnd = false;
        this.isStart = false;

        frames = new BufferedImage[nFrame];
    }


    public void addFrame(int index, BufferedImage frame) {
        if (index < nFrame)
            frames[index] = frame;
    }

    public BufferedImage getCurrentFrame() {
        if (isEnd)
            return frames[nFrame - 1];
        else
            updateAnimation();
        return frames[currentIndexFrame];
    }

    private void updateAnimation() {
        if (isEnd)
            return;

        if (tick >= secFrameDuration * GameSettings.FPS) {
            tick = 0;
            ++currentIndexFrame;

            if (loop)
                currentIndexFrame = currentIndexFrame % nFrame;
            else if (currentIndexFrame >= nFrame - 1) {
                stop();
            }
        }
        ++tick;
    }

    public void start() {
//        System.out.println("start " + animationTypes);
        tick = 0;
        currentIndexFrame = 0;
        isStart = true;
        isEnd = false;
    }

    public void stop() {
//        System.out.println("stop " + animationTypes);
        tick = 0;
        currentIndexFrame = 0;
        isEnd = true;
        isStart = false;
    }

    public int getNFrame() {
        return nFrame;
    }

    public boolean isStart() {
        return isStart;
    }

    public EnumState getAnimationTypes() {
        return animationTypes;
    }
}
