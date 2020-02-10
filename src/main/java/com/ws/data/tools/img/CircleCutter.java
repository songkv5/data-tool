package com.ws.data.tools.img;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

/**
 * @author willis
 * @chapter 运行图片裁剪
 * @section
 * @since 2020年02月07日 10:54
 */
public class CircleCutter implements Cutter{
    /**
     * 最后输出图片的圆的直径，也是图片的宽高
     */
    private int diameter;

    public CircleCutter(int diameter) {
        this.diameter = diameter;
    }

    @Override
    public BufferedImage cut(BufferedImage srcImage) {
        if (srcImage == null) {
            return null;
        }
        int diameter_ = diameter;
        if (diameter_ == 0) {
            int width = srcImage.getWidth(null);
            int height = srcImage.getHeight(null);
            if (width > height) {
                diameter_ = height;
            } else {
                diameter_ = width;
            }
        }


        // 根据需要是否使用 BufferedImage.TYPE_INT_ARGB
        BufferedImage result = new BufferedImage(diameter_, srcImage.getHeight(null), BufferedImage.TYPE_INT_BGR);
        // 圆形
        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, diameter_, diameter_);
        // 拿到画笔
        Graphics2D g2 = result.createGraphics();
//        result = g2.getDeviceConfiguration().createCompatibleImage(diameter_, diameter_, Transparency.TRANSLUCENT);
//        g2 = result.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        /**
         * 将背景设置为透明。如果注释该段代码，默认背景为白色.也可通过g2.setPaint(paint) 设置背景色
         */
        g2.setComposite(AlphaComposite.Clear);
//        g2.setPaint(Color.BLUE);
        g2.fill(new Rectangle(result.getWidth(), result.getHeight()));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1f));
        // 圈个圆出来
        g2.setClip(circle);
        g2.drawImage(srcImage, 0, 0, null);
        g2.dispose();
        return result;
    }
}
