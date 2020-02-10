package com.ws.data.tools.img.drawer;

import java.awt.*;

/**
 * @author willis
 * @chapter 文本画笔
 * @section
 * @since 2020年02月07日 13:15
 */
public interface TextDrawer extends Drawer {
    /**
     * 文本画笔
     * @param text
     * @param x
     * @param y
     * @param color
     * @param font
     * @return
     */
    TextDrawer writeText(String text, int x, int y, Color color, Font font);
}
