package com.xww.projects.game03.Content;

import com.xww.Engine.core.Actor.Character;
import com.xww.Engine.core.Vector.Vector;

public class Player extends Character {
    public Player(Vector worldPosition,
                  Vector size,
                  Vector logicSize,
                  Vector relativePosition,
                  int mass,
                  int hp,
                  int atk_interval,
                  int invulnerableTime,
                  int rollCd,
                  double rollSpeed,
                  boolean whetherFacingLeft,
                  int jumpMaxCount,
                  int jumpSpeed,
                  int runSpeed,
                  int climbSpeedY,
                  int atkBackSwingTime,
                  CharacterType characterType) {
        super(worldPosition,
                size,
                logicSize,
                relativePosition,
                mass,
                hp,
                atk_interval,
                invulnerableTime,
                rollCd,
                rollSpeed,
                whetherFacingLeft,
                jumpMaxCount,
                jumpSpeed,
                runSpeed,
                climbSpeedY,
                atkBackSwingTime,
                characterType);
    }

    @Override
    protected boolean tryAtk() {
        return false;
    }

    @Override
    protected void onInvulnerableHit() {

    }

    @Override
    protected void onHit() {

    }
}
