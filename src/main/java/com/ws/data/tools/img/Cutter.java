package com.ws.data.tools.img;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 图片裁剪
 */
public interface Cutter {
    /**
     * @param src 源图
     * 执行裁剪
     * @return 裁剪后的图片对象
     */
    BufferedImage cut(BufferedImage src);
}
