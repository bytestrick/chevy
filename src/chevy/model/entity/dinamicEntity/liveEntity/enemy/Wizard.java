package chevy.model.entity.dinamicEntity.liveEntity.enemy;

import chevy.model.entity.dinamicEntity.stateMachine.State;
import chevy.model.entity.dinamicEntity.stateMachine.WizardStates;
import chevy.utilz.Vector2;

public class Wizard extends Enemy {
    private final State idle = new State(WizardStates.IDLE);
    private final State move = new State(WizardStates.MOVE);
    private final State shot = new State(WizardStates.SHOT);
    private final State hit = new State(WizardStates.HIT);
    private final State dead = new State(WizardStates.DEAD);


    public Wizard(Vector2<Integer> initPosition) {
        super(initPosition, EnemyTypes.WIZARD);

        this.health = 8;

        stateMachine.setStateMachineName("Wizard");
        stateMachine.setInitialState(idle);
        initStateMachine();
    }


    private void initStateMachine() {
        idle.linkState(move);
        idle.linkState(shot);
        idle.linkState(hit);
        move.linkState(idle);
        shot.linkState(idle);
        hit.linkState(idle);
        hit.linkState(dead);
    }
}
