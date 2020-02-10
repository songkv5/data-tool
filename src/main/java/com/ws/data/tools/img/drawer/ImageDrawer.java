package com.ws.data.tools.img.drawer;

import java.awt.*;

/**
 * @author willis
 * @chapter 普通图片绘制
 * @section
 * @since 2020年02月07日 16:54
 */
public interface ImageDrawer extends Drawer {
    /**
     * 绘制图片
     * @param image
     * @param x 被画图片在承载图片上的位置 -x坐标
     * @param y 被画图片在承载图片上的位置 -y坐标
     * @param width 图片宽度
     * @param height 图片高度
     * @return
     */
    ImageDrawer dramImage(Image image, int x, int y, int width, int height);
}
