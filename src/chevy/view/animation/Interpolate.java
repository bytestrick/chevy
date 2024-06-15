package chevy.view.animation;

import chevy.service.Render;

public class Interpolate implements Render {
    private double time = 0d;


    @Override
    public void render(double delta) {
        time += delta;
    }

    @Override
    public boolean renderIsEnd() {
        return Render.super.renderIsEnd();
    }
}
