package chevy.model.entity.dynamicEntity.liveEntity.enemy;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
import chevy.utils.Vector2;

public class Beetle extends Enemy {
    private final GlobalState idle = new GlobalState(State.IDLE, 1.5f);
    private final GlobalState move = new GlobalState(State.MOVE, 0.5f);
    private final GlobalState attack = new GlobalState(State.ATTACK, 0.5f);
    private final GlobalState hit = new GlobalState(State.HIT, 0.15f);
    private final GlobalState dead = new GlobalState(State.DEAD, 0.3f);

    public Beetle(Vector2<Integer> initPosition) {
        super(initPosition, Type.BEETLE);
        this.health = 10;
        this.currentHealth = health;
        this.maxDamage = 5;
        this.minDamage = 3;
        this.drawLayer = 3;

        initStateMachine();
    }

    private void initStateMachine() {
        stateMachine.setStateMachineName("Beetle");
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
        State beetleState = (State) commonEnumStates;
        return switch (beetleState) {
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