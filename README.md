数据处理工具

### 图片工具使用
#### 海报制作

```java
// 模板图片
File srcFile = new File("E:\\image2020-1-17_17-53-21.png");
// 头像
BufferedImage portraitImage = ImageIO.read(new File("E:\\DSC_0183.JPG"));
// 二维码
BufferedImage srImg = ImageIO.read(new File("E:\\sr.jpg"));

// 参数1=模板图片；参数2=最终制作的图片的输出位置
DefaultPosterDrawer drawer = new DefaultPosterDrawer(srcFile, "E:\\result.png");
Color c = new Color(100, 100, 100);
Font f = new Font("微软雅黑", Font.PLAIN, 25);

int hSpace = 50;
int x = 120;
int y = 450;
drawer
	// 写文字到图片上
    .writeText("xxxxxxxxxx", x, y += hSpace, c, f)
    .writeText("xxxxxxxxxx", x, y += hSpace, c, f)
    .writeText("xxxx", x, y += hSpace, c, f)
    .writeText("xxxx", x, y += hSpace, c, f)
    // 头像
    // 画个圆图到图片上，如果传的图片是方的，会切成圆形
    .drawCircleImage(portraitImage, 205, 120, 86)
    // 画个图片到图片上
    .dramImage(srImg, 79, 800, 131, 131)
    .dramImage(srImg, 172, 268, 200, 40)
    .writeText("武汉加油", 172, 323, Color.RED, f)// 调用这个方法会输出图片
    // 调用这个方法会输出图片
    .make();
```