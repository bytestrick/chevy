package chevy.model.entity.dynamicEntity.liveEntity.enemy;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
import chevy.utils.Vector2;

public class Wraith extends Enemy {
    private final GlobalState idle = new GlobalState(State.IDLE, 0.8f);
    private final GlobalState move = new GlobalState(State.MOVE, 0.5f);
    private final GlobalState attack = new GlobalState(State.ATTACK, 0.5f);
    private final GlobalState hit = new GlobalState(State.HIT, 0.15f);
    private final GlobalState dead = new GlobalState(State.DEAD, 0.3f);

    public Wraith(Vector2<Integer> initPosition) {
        super(initPosition, Type.WRAITH);

        this.flying = true;
        this.health = 3;
        this.currentHealth = health;
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
    public synchronized GlobalState getState(CommonState commonEnumStates) {
        State wraithState = (State) commonEnumStates;
        return switch (wraithState) {
            case MOVE -> move;
            case ATTACK -> attack;
            case HIT -> hit;
            case DEAD -> dead;
            case IDLE -> idle;
        };
    }

    public enum State implements CommonState {
        MOVE, ATTACK, HIT, DEAD, IDLE
    }
}