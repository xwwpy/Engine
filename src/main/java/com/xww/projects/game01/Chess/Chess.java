package com.xww.projects.game01.Chess;

import com.xww.Engine.Utils.ImgUtils;
import com.xww.Engine.core.Anchor.AnchorMode;
import com.xww.Engine.core.Collision.CollisionDefaultConstValue;
import com.xww.Engine.core.Component.FreeComponent;
import com.xww.Engine.core.Vector.Vector;
import com.xww.Engine.gui.GameFrame;
import com.xww.Engine.setting.DebugSetting;
import com.xww.projects.game01.object.Plant.BasePlant;

import java.awt.*;


/**
 * 棋盘
 */
public class Chess extends FreeComponent {
    private final int row; // 行数 从1开始计数
    private final int col; // 列数 从1开始计数

    private final Image background;

    private final BasePlant[] plants;

    private final Vector start_position; // 开始种植的起始位置

    private final Vector district_size; // 种植区域大小

    private final Vector latticeSize; // 格子大小

    private Chess(int row, int col, Vector size, Vector start_position, Vector latticeSize, String path){
        super(Vector.Zero(), GameFrame.PositionType.World, size, AnchorMode.LeftTop, Vector.Zero(), Vector.Zero(), -2, CollisionDefaultConstValue.noCollisionChecking, CollisionDefaultConstValue.noCollisionChecking, -1, true, false);
        this.row = row;
        this.col = col;
        this.start_position = start_position;
        this.latticeSize = latticeSize;
        this.district_size = new Vector(col * latticeSize.getX(), row * latticeSize.getY());
        plants = new BasePlant[row * col];
        background = ImgUtils.getScaledImage(ImgUtils.loadImage(path), size.getX(), size.getY());
    }

    public static Chess createChess(int row, int col, Vector size, Vector latticeSize, Vector start_position, String path){
        return new Chess(row, col, size, start_position, latticeSize, path);
    }

    public Vector getRowAndColumn(Vector position){
        Vector start_position = this.start_position;
        Vector latticeSize = this.latticeSize;
        Vector position_in_chess = position.sub(start_position);
        int row = (position_in_chess.getY() / latticeSize.getY()) + 1;
        int col = (position_in_chess.getX() / latticeSize.getX()) + 1;
        return new Vector(row, col);
    }

    public PlantType addPlant(BasePlant plant, Vector position){
        Vector rowAndColumn = getRowAndColumn(position);
        int row = rowAndColumn.getX();
        int col = rowAndColumn.getY();
        if(!checkValid(row, col)){
            return PlantType.OUTBOUND;
        }
        BasePlant plant1 = this.getPlant(row, col);
        if (plant1 != null){
            BasePlant plant_after_fusion = plant.tryFusion(plant1);
            if (plant_after_fusion != null){
                plants[(row - 1) * col + col] = plant_after_fusion;
                return PlantType.FUSION;
            } else {
                return PlantType.NONE;
            }
        } else {
            plants[(row - 1) * col + col] = plant;
            return PlantType.PLANT;
        }
    }
//    public PlantType addPlant(BasePlant plant, int row, int col){
//        if(!checkValid(row, col)){
//            return PlantType.OUTBOUND;
//        }
//        BasePlant plant1 = this.getPlant(row, col);
//        if (plant1 != null){
//            BasePlant plant_after_fusion = plant.tryFusion(plant1);
//            if (plant_after_fusion != null){
//                plants[(row - 1) * col + col] = plant_after_fusion;
//                return PlantType.FUSION;
//            } else {
//                return PlantType.NONE;
//            }
//        } else {
//            plants[(row - 1) * col + col] = plant;
//            return PlantType.PLANT;
//        }
//    }

    private boolean checkValid(int row, int col) {
        return row > 0 && row <= this.row && col > 0 && col <= this.col;
    }

    public BasePlant getPlant(int row, int col){
        if (checkValid(row, col)) {
            return plants[(row - 1) * col + col];
        }
        throw new RuntimeException("Invalid row or col");
    }

    @Override
    public void on_update(Graphics g) {
        g.drawImage(background, this.getDrawPosition().getX(), this.getDrawPosition().getY(), null);
        super.on_update(g);
    }

    @Override
    protected void showDebugInfo(Graphics g) {
        Vector drawPosition = this.getDrawPosition();
        g.setColor(DebugSetting.DebugInfoColor);
        Vector startPosInScreen = drawPosition.add(this.start_position); // 起始位置相对于屏幕的坐标
        for (int i = 0; i <= col; i++) {
            g.drawLine(startPosInScreen.getX() + i * latticeSize.getX(), startPosInScreen.getY(), startPosInScreen.getX() + i * latticeSize.getX(), startPosInScreen.getY() + district_size.getY());
            for (int j = 0; j <= row; j++) {
                g.drawLine(startPosInScreen.getX(), startPosInScreen.getY() + j * latticeSize.getY(), startPosInScreen.getX() + district_size.getX(), startPosInScreen.getY() + j * latticeSize.getY());
                g.drawString("" + i, startPosInScreen.getX() + i * latticeSize.getX(), startPosInScreen.getY() + 20);
                g.drawString("" + j, startPosInScreen.getX(), startPosInScreen.getY() + j * latticeSize.getY());
            }
        }
    }
}
