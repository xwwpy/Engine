package com.xww.projects.game01.Card;

import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;

public class CardBar extends FreeComponent {
    // TODO 目前不考虑多线程的影响
    public static int CurrentSun = 100;
    private int currentCanAccommodateCardsNum; // 当前可以容纳的卡片数量
    private int realAccommodateCardsNum; // 实际容纳的卡片数量



    private CardBar(Vector worldPosition, GameFrame.PositionType positionType, Vector size, AnchorMode anchorMode, Vector velocity, Vector acceleration, int order, int CollisionRegion, int mass, boolean whetherShowDebugInfo, boolean is_drag_on) {
        super(worldPosition, GameFrame.PositionType.Screen, size, AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), Integer.MAX_VALUE - 11, -1, -1, false, false);
    }
}
