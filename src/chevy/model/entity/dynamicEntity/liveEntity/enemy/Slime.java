package chevy.model.entity.dynamicEntity.liveEntity.enemy;

import chevy.model.entity.stateMachine.CommonState;
import chevy.model.entity.stateMachine.GlobalState;
import chevy.utils.Vector2;

public class Slime extends Enemy {
    private final GlobalState idle = new GlobalState(State.IDLE, 1.f, true);
    private final GlobalState move = new GlobalState(State.MOVE, 0.5f);
    private final GlobalState attack = new GlobalState(State.ATTACK, 0.5f);
    private final GlobalState hit = new GlobalState(State.HIT, 0.15f);
    private final GlobalState dead = new GlobalState(State.DEAD, 0.3f);

    public Slime(Vector2<Integer> initPosition) {
        super(initPosition, Type.SLIME);
        this.health = 3;
        this.currentHealth = health;
        this.maxDamage = 2;
        this.minDamage = 1;

        initStateMachine();
    }

    private void initStateMachine() {
        this.stateMachine.setStateMachineName("Slime");
        this.stateMachine.setInitialState(idle);

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
        State slimeState = (State) commonEnumStates;
        return switch (slimeState) {
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