package chevy.view.animation;

import chevy.model.entity.dinamicEntity.projectile.Projectile;
import chevy.model.entity.dinamicEntity.projectile.SlimeShot;
import chevy.model.entity.dinamicEntity.stateMachine.CommonEnumStates;
import chevy.service.Render;
import chevy.service.RenderManager;
import chevy.utilz.Pair;
import chevy.utilz.Vector2;

import java.awt.image.BufferedImage;

public class AnimatedSprite implements Render {
    private final Pair<CommonEnumStates, Integer> animationTypes;
    private final BufferedImage[] frames;
    private final int nFrame;
    private final float secFrameDuration;
    private int currentIndexFrame = 0;
    private final boolean loop;
    private boolean isRunning = false;
    private boolean delete = true;
    private double time = 0d;
    private final float scale;
    private final Vector2<Integer> offset;


    public AnimatedSprite(Pair<CommonEnumStates, Integer> animationTypes, int nFrame, float secFrameDuration, boolean loop) {
        this.animationTypes = animationTypes;
        this.nFrame = nFrame;
        this.secFrameDuration = secFrameDuration;
        this.loop = loop;

        this.scale = 1;
        this.offset = new Vector2<>(0, 0);

        frames = new BufferedImage[nFrame];
    }

    public AnimatedSprite(Pair<CommonEnumStates, Integer> animationTypes, int nFrame, float secFrameDuration, boolean loop, Vector2<Integer> offset, float scale) {
        this.animationTypes = animationTypes;
        this.nFrame = nFrame;
        this.secFrameDuration = secFrameDuration;
        this.loop = loop;

        this.scale = scale;
        this.offset = offset;

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

    public Vector2<Integer> getOffset() {
        return offset;
    }

    public float getScale() {
        return scale;
    }

    public void start() {
//        System.out.println("Inizio animazione: " + animationTypes);
        isRunning = true;

        if (delete) {
            delete = false;
            RenderManager.addToRender(this);
        }
    }

    public void restart() {
        currentIndexFrame = 0;
        time = 0d;
        if (delete) {
            start();
            return;
        }
        isRunning = true;
    }

    public void stop() {
//        System.out.println("Fine animazione: " + animationTypes);
        isRunning = false;
    }

    public int getNFrame() {
        return nFrame;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Pair<CommonEnumStates, Integer> getAnimationTypes() {
        return animationTypes;
    }

    public void delete() {
        stop();
        delete = true;
    }


    @Override
    public void render(double delta) {
        if (isRunning) {
            if (time >= secFrameDuration) {
                time = 0d;
                ++currentIndexFrame;
                if (loop) {
                    currentIndexFrame = currentIndexFrame % nFrame;
                }
                else if (currentIndexFrame >= nFrame) {
                    delete();
                }
            }
            else
                time += delta;
        }
    }

    @Override
    public boolean renderIsEnd() {
        return delete;
    }
}
