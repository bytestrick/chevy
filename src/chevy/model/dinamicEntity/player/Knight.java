package chevy.model.dinamicEntity.player;

import chevy.model.dinamicEntity.Directions;
import chevy.model.stateMachine.State;
import chevy.settings.Settings;
import chevy.utilz.Vector2;

import java.util.Set;

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
                velocity.x() + direction.x() * Settings.SIZE_CELL,
                velocity.y() + direction.y() * Settings.SIZE_CELL
        ));

        stateMachine.changeState(idle);
    }
}
