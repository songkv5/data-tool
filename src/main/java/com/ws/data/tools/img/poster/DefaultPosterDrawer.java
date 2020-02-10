package com.ws.data.tools.img.poster;

import com.ws.data.tools.img.drawer.AbstractDrawerBaseOnTemplate;

import java.io.File;
import java.net.URL;

/**
 * @author willis
 * @chapter 海报制作
 * @section
 * @since 2020年02月05日 16:17
 */
public class DefaultPosterDrawer extends AbstractDrawerBaseOnTemplate {

    private String outputPath;
    @Override
    public String getOutputPath() {
        return outputPath;
    }

    public DefaultPosterDrawer(File templateFile, String outputPath) {
        super(templateFile);
        this.outputPath = outputPath;
    }

    /**
     * 网络图片
     * @param templateURL
     * @param outputPath
     */
    public DefaultPosterDrawer(URL templateURL, String outputPath) {
        super(templateURL);
        this.outputPath = outputPath;
    }
}
