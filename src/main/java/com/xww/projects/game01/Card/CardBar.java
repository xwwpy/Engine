package com.xww.projects.game01.Card;

import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;

import java.awt.*;

public class CardBar extends FreeComponent {
    // 目前不考虑多线程的影响
    public static int CurrentSun = 100;
    private final int currentCanAccommodateCardsNum; // 当前可以容纳的卡片数量
    private int realAccommodateCardsNum; // 实际容纳的卡片数量

    private final BaseCard[] cards;



    private CardBar(Vector worldPosition, int currentCanAccommodateCardsNum) {
        super(worldPosition, GameFrame.PositionType.Screen, getCardBarSize(currentCanAccommodateCardsNum), AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), Integer.MAX_VALUE - 11, -1, -1, false, false);
        this.currentCanAccommodateCardsNum = currentCanAccommodateCardsNum;
        this.realAccommodateCardsNum = 0;
        cards = new BaseCard[this.currentCanAccommodateCardsNum];
    }

    /**
     * 根据卡槽可以容纳的最大数量计算实际的大小
     */
    private static Vector getCardBarSize(int currentCanAccommodateCardsNum){
        return Vector.build(100, 100).mul_to_self(currentCanAccommodateCardsNum);
    }

    public boolean addCard(BaseCard card) {
        if (realAccommodateCardsNum < currentCanAccommodateCardsNum) {
            cards[realAccommodateCardsNum++] = card;
            return true;
        } else return false;
    }

    @Override
    public void on_update(Graphics g) {
        for (int i = 0; i < realAccommodateCardsNum; i++) {
            // TODO 思考是否需要直接作为子类
            cards[i].on_update(g);
        }
        super.on_update(g);
    }
}
