package com.ws.data.tools.img.drawer;

import com.ws.data.tools.exceptions.DTException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * @author willis
 * @chapter 基于模板的绘图类，用于在原有的模板上修改
 * @section
 * @since 2020年02月05日 15:54
 */
public abstract class AbstractDrawerBaseOnTemplate implements TextDrawer, ImageDrawer, CircleImageDrawer {
    /**
     * 源图片,即模板图片
     */
    private Image srcImg;

    /**
     * 海报对象
     */
    private BufferedImage tgtImg;
    /**
     * 画笔对象
     */
    private Graphics2D graphics2D;
    /**
     * 2D画笔是否准备好
     */
    private boolean graphics2DPrepared;
    /**
     * 构造方法
     *
     * @param srcImgFile 模板图片文件
     */
    public AbstractDrawerBaseOnTemplate(File srcImgFile) {
        try {
            this.srcImg = ImageIO.read(srcImgFile);
            this.tgtImg =
                new BufferedImage(this.srcImg.getWidth(null), this.srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
        } catch (Exception e) {
            throw new DTException(40001, "模板解析出错");
        }
    }

    /**
     * 构造方法
     *
     * @param url 模板图片文件地址
     */
    public AbstractDrawerBaseOnTemplate(URL url) {
        try {
            this.srcImg = ImageIO.read(url);
            this.tgtImg =
                new BufferedImage(this.srcImg.getWidth(null), this.srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
        } catch (Exception e) {
            throw new DTException(40001, "模板解析出错");
        }
    }

    /**
     * 构造方法
     *
     * @param srcImg
     */
    public AbstractDrawerBaseOnTemplate(Image srcImg) {
        this.srcImg = srcImg;
        this.tgtImg =
            new BufferedImage(this.srcImg.getWidth(null), this.srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public boolean make() {
        checkBeforeDraw();
        this.graphics2D.dispose();
        String outputPath = getOutputPath();
        if (outputPath == null || outputPath.trim() == "") {
            throw new DTException(40002, "未设置输出路径");
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outputPath);
            ImageIO.write(this.tgtImg, "JPG", fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                }
            }
        }
        return false;
    }

    /**
     * 书写文案到制定图片
     *
     * @param text  文本
     * @param x     文本横坐标起始位置
     * @param y     文本纵坐标起始位置
     * @param color 颜色
     * @param font  字体
     * @return
     */
    @Override
    public AbstractDrawerBaseOnTemplate writeText(String text, int x, int y, Color color, Font font) {
        checkBeforeDraw();
        this.graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);
        // 消除文本锯齿
        this.graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        this.graphics2D.setColor(color);
        this.graphics2D.setFont(font);
        this.graphics2D.drawString(text, x, y);
        return this;
    }

    @Override
    public AbstractDrawerBaseOnTemplate dramImage(Image image, int x, int y, int width, int height) {
        checkBeforeDraw();
        this.graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.graphics2D.drawImage(image, x, y, width, height, null);
        return this;
    }

    @Override
    public AbstractDrawerBaseOnTemplate dramCircleImage(Image image, int x, int y, int radius) {
        checkBeforeDraw();
        // 图片抗锯齿
        this.graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        /*
         * 把模板图片的哪里剪掉
         */
        Ellipse2D.Double shape = new Ellipse2D.Double(x, y, radius * 2, radius * 2);
        //要绘制的区域
        graphics2D.setClip(shape);
//        graphics2D.setComposite(AlphaComposite.SrcIn);
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1f));
        graphics2D.drawImage(image, x, y, radius * 2, radius * 2, null);
        graphics2D.setClip(null);
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        return this;
    }
    private boolean isGraphics2DPrepared() {
        return graphics2DPrepared;
    }
    private void checkBeforeDraw() {
        if (!isGraphics2DPrepared()) {
            this.graphics2D = this.tgtImg.createGraphics();
            // 结果图片准备
            this.graphics2D.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
            this.graphics2DPrepared = true;
        }
    }

    public abstract String getOutputPath();
}
