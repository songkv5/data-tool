package com.ws.data.tools.img.drawer;

import java.awt.*;

/**
 * @author willis
 * @chapter
 * @section
 * @since 2020年02月10日 12:04
 */
public interface CircleImageDrawer extends Drawer{
    /**
     * 绘制图片
     * @param image 图片。将这个图片切成圆形画到源图片上
     * @param x 画圆的位置 - x坐标
     * @param y 画图的位置 - y坐标
     * @param radius 圆的半径
     * @return
     */
    ImageDrawer drawCircleImage(Image image, int x, int y, int radius);
}
