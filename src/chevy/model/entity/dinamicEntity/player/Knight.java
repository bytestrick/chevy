package chevy.model.entity.dinamicEntity.player;

import chevy.model.entity.dinamicEntity.Directions;
import chevy.model.entity.dinamicEntity.stateMachine.State;
import chevy.settings.Settings;
import chevy.utilz.Vector2;

public class Knight extends Player {
    private final State idle = new State("idle");
    private final State run = new State("run");
    private final State attack = new State("attack");
    private final State hit = new State("hit");


    public Knight(Vector2<Integer> initVelocity) {
        super(initVelocity, PlayerTypes.KNIGHT);
        stateMachine.setInitialState(idle);
        initStateMachine();
    }


    private void initStateMachine() {
        idle.linkState(run);
        idle.linkState(attack);
        idle.linkState(hit);
        run.linkState(hit);
        attack.linkState(hit);
        run.linkState(idle);
        attack.linkState(idle);
        hit.linkState(idle);
    }


    @Override
    public void move(Directions direction) {
        if (!stateMachine.changeState(run))
            return;

        changeVelocity(new Vector2<>(
                velocity.x() + direction.x() * Settings.TILE_SIZE,
                velocity.y() + direction.y() * Settings.TILE_SIZE
        ));

        stateMachine.changeState(idle);
    }
}
