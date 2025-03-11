package com.xww.projects.game02.content.Boss;

import com.xww.Engine.core.StateManager.StateMachine;

public class BossStateMachine extends StateMachine {
    private final Boss owner;
    public BossStateMachine(Boss boss) {
        super(null);
        this.owner = boss;
    }

    @Override
    public void forceSwitch(String id) {
        owner.updateBossDirection();
        super.forceSwitch(id);
    }
}
