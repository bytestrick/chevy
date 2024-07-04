package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.stateMachine.CommonStates;
import chevy.model.entity.stateMachine.State;
import chevy.utils.Vector2;

public class Wraith extends Enemy {
    private final State idle = new State(States.IDLE, 0.8f);
    private final State move = new State(States.MOVE, 0.5f);
    private final State attack = new State(States.ATTACK, 0.5f);
    private final State hit = new State(States.HIT, 0.15f);
    private final State dead = new State(States.DEAD, 0.3f);

    public Wraith(Vector2<Integer> initPosition) {
        super(initPosition, Type.WRAITH);

        this.flying = true;
        this.health = 3;
        this.maxDamage = 2;
        this.minDamage = 1;

        initStateMachine();
    }

    private void initStateMachine() {
        stateMachine.setStateMachineName("Wraith");
        stateMachine.setInitialState(idle);

        idle.linkState(move);
        idle.linkState(attack);
        idle.linkState(hit);
        move.linkState(idle);
        move.linkState(hit);
        attack.linkState(idle);
        attack.linkState(hit);
        hit.linkState(idle);
        hit.linkState(dead);
    }

    @Override
    public synchronized State getState(CommonStates commonEnumStates) {
        States wraithState = (States) commonEnumStates;
        return switch (wraithState) {
            case MOVE -> move;
            case ATTACK -> attack;
            case HIT -> hit;
            case DEAD -> dead;
            case IDLE -> idle;
        };
    }

    public enum States implements CommonStates {
        MOVE, ATTACK, HIT, DEAD, IDLE
    }
}