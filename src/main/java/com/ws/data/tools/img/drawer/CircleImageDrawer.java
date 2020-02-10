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
     * @param image
     * @param x
     * @param y
     * @param radius
     * @return
     */
    ImageDrawer dramCircleImage(Image image, int x, int y, int radius);
}
